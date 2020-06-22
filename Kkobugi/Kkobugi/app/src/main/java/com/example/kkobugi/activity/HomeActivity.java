package com.example.kkobugi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kkobugi.R;

public class HomeActivity extends AppCompatActivity {
    String goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        goal = intent.getStringExtra("goal");
        ImageButton btn_voice = (ImageButton)findViewById(R.id.btn_home);
        final TextView textView1 = (TextView)findViewById(R.id.text_home);
        textView1.setText(goal+" 도착!\n길 안내를 종료합니다.");

        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
