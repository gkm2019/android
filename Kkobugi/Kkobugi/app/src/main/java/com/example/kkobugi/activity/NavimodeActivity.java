package com.example.kkobugi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.kkobugi.R;

import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;


public class NavimodeActivity extends RosActivity {

    private String location;

    private RosTextView<std_msgs.String> rosTextView;

    private ObjectAnimator progressAnimator;
    private ProgressBar progressBar;
    private TextView txt_navimode;

    //이미지 슬라이드 이미지 파일명
    int[] img = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};
    ViewFlipper vf;

    public NavimodeActivity() {
        super("Kkobugi", "Kkobugi",URI.create("http://172.20.10.8:11311"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navimode);

        init2();

        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.rostextview_complete);

        Intent intent = getIntent();
        location = intent.getStringExtra("loc");

        txt_navimode = (TextView)findViewById(R.id.text_navimode);
        txt_navimode.setText("길 안내를 시작합니다!\n"+location+"로 이동중...");

        vf=findViewById(R.id.image_slide);
        for(int img : img){
            flipperImages(img);
        }

        progressAnimator.start();

        rosTextView.setTopicName("complete");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(final std_msgs.String message) {
                if(message.getData().equals("1")) {
                    Intent intent = new Intent(NavimodeActivity.this, HomeActivity.class);
                    intent.putExtra("goal", location);
                    startActivity(intent);
                    finish();
                }
                return message.getData();
            }
        });


    }

    private void flipperImages(int img) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);

        vf.setFlipInterval(1000);
        vf.setAutoStart(true);

        vf.setInAnimation(this,android.R.anim.slide_in_left);
        vf.setOutAnimation(this,android.R.anim.slide_out_right);
    }


    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress().toString());
        nodeConfiguration.setMasterUri(getMasterUri());
        //nodeConfiguration.setNodeName("keywords");// <--this fixed it
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }

    protected void init2(){
        progressBar = findViewById(R.id.progressBar_hori);
        progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,100);
    }
}
