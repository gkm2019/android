package com.example.kkobugi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kkobugi.R;
import com.example.kkobugi.task.Talker;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

public class RosConnectActivity extends RosActivity {
    private Talker talker;

    private String str;

    private Button button;

    public RosConnectActivity() {
        super("Kkobugi", "Kkobugi", URI.create("http://172.20.10.8:11311"));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ros_connect);

        Intent intent = getIntent();
        str = intent.getStringExtra("str");
        Log.d("MyTag","ros"+str);

        button = (Button)findViewById(R.id.button_pub);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talker.publish(str);
                Intent intent1 = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent1);
                finish();
            }
        });


    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        Log.d("MyTag", "init2");

        talker = new Talker();

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(talker, nodeConfiguration);
        //nodeConfiguration.setNodeName("keywords");// <--this fixed it
    }
}
