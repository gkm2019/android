package com.example.kkobugi.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kkobugi.DTO.Naviloc;
import com.example.kkobugi.R;
import com.example.kkobugi.task.RequestHttpURLConnection;
import com.example.kkobugi.task.Talker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;
import java.util.Stack;

public class NavigationActivity extends RosActivity {


    private Talker talker;

    private Button btn_yes, btn_no;
    private TextView text_navi;
    private RosTextView<std_msgs.String> rosTextView;
    private ProgressBar progressBar;

    private String command;
    private String[] keywords;
    private String category;
    private String title;

    private Stack<String> stack;

    //navi db 변수
    private static String TAG = "phptest";
    private String jsonString;
    private String x,y,angle;


    public NavigationActivity() {
        super("Kkobugi", "Kkobugi", URI.create("http://172.20.10.8:11311"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Log.d("MyTag","1");

        Intent intent = getIntent();

        rosTextView = (RosTextView<std_msgs.String>)findViewById(R.id.rostextview);

        //rosTextView_sub = (RosTextView<std_msgs.String>) findViewById(R.id.rostextview_category);
        btn_yes = (Button)findViewById(R.id.btn_yes);
        btn_no = (Button)findViewById(R.id.btn_no);
        text_navi = (TextView)findViewById(R.id.text_navi);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        text_navi.setText("분석 중입니다. \n 잠시만 기다려주세요!");
        btn_yes.setVisibility(View.GONE);
        btn_no.setVisibility(View.GONE);

        rosTextView.setTopicName("keywords");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(final std_msgs.String message) {
                command = message.getData();
                keywords = command.split("/");
                if(keywords != null){
                    Log.d("MyTag",keywords.toString());
                    progressBar.setVisibility(View.GONE);
                    btn_no.setVisibility(View.VISIBLE);
                    btn_yes.setVisibility(View.VISIBLE);
                    setTextView();
                }
                return message.getData();
            }
        });


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void setTextView(){
        String mode;
        final String table;

        stack = new Stack<>();

        for(int i = 0; i<keywords.length; i++){
            stack.push(keywords[i]);
        }

        mode = stack.pop();

        if(mode.equals("0")){
            Log.d("MyTag","this line");
            category = stack.pop();
            table = setTableName(category);
            Log.d("MyTag", table + title);

            if(category.equals("전화번호")){
                text_navi.setText(title +" \n "+ category+ "을/를 소개할까요?");
            }
            else if(category.equals("충북대학교") || category.equals("충북대") || category.equals("")){
                text_navi.setText(category +"의 "+ title+ "에 대해 알려드릴까요?");
            }
            else if(category.equals("위치")){
                text_navi.setText(title + "의 위치를 보여드릴까요?");
            }
            else{
                text_navi.setText(title + "에 대해 소개할까요?");
            }
            btn_yes.setText("네, 소개해주세요.");

            if(table.equals("department")){
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(NavigationActivity.this, DepartmentActivity.class);
                        intent1.putExtra("table",table);
                        intent1.putExtra("title",title);
                        startActivity(intent1);
                        finish();
                    }
                });
            }
            else if(table.equals("location")){
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent4 = new Intent(NavigationActivity.this, MapActivity.class);
                        intent4.putExtra("table",table);
                        intent4.putExtra("title",title);
                        startActivity(intent4);
                        finish();
                    }
                });
            }
            else if(table.equals("numberinfo")){
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent4 = new Intent(NavigationActivity.this, NumberInfoActivity.class);
                        intent4.putExtra("table",table);
                        intent4.putExtra("title",title);
                        startActivity(intent4);
                        finish();
                    }
                });
            }
            else if(table.equals("professor")){
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent4 = new Intent(NavigationActivity.this, ProfessorActivity.class);
                        intent4.putExtra("table",table);
                        intent4.putExtra("title",title);
                        startActivity(intent4);
                        finish();
                    }
                });
            }
            else if(table.equals("introduction")){
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(NavigationActivity.this, IntroductionActivity.class);
                        intent2.putExtra("table",table);
                        intent2.putExtra("title",title);
                        startActivity(intent2);
                        finish();
                    }
                });
            }

        }
        else if(mode.equals("1")){
            category = stack.pop();
            text_navi.setText(category + "로 \n 길 안내 할까요?");
            btn_yes.setText("네, 길 안내해주세요.");

            String url = RequestHttpURLConnection.IP_URL;
            ContentValues cv = new ContentValues();
            cv.put("introOrder","naviloc");
            cv.put("introOrder2",category);
            NetworkTask networkTask = new NetworkTask(url, cv);
            networkTask.execute();

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = x +"/"+ y +"/"+ angle;
                    talker.publish("0.1/0.1/0");
                    Intent intent = new Intent(getApplicationContext(), NavimodeActivity.class);
                    intent.putExtra("loc", category);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            btn_yes.setVisibility(View.GONE);
            text_navi.setText("죄송합니다. \n 인식하지 못했습니다.");
        }
    }

    public String setTableName(String str){
        String strPop = stack.pop();

        if(str.equals("전화번호")){
            if(strPop.equals("학과") || strPop.equals("부") || strPop.equals("대학")){
                title = stack.pop() + strPop;
            }
            else{
                title = strPop;
            }
            return "numberinfo";
        }
        else if(str.equals("학과") || str.equals("부") || str.equals("대학")){
            title = strPop + str;
            return "department";
        }
        else if(str.equals("교수")){
            title = strPop;
            return "professor";
        }
        else if(str.equals("위치")){
            title = strPop;
            return "location";
        }
        else {
            title = str;
            category = strPop;
            return "introduction";
        }

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result=requestHttpURLConnection.request(url,values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackGround()에서 return 된 result의 s로 넘어온다.
            jsonString = s;
            showResult();
        }
    }

    private void showResult(){

        String TAG_JSON="naviloc";
        String X = "x";
        String Y = "y";
        String ANGLE="angle";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                x = item.getString(X);
                y = item.getString(Y);
                angle = item.getString(ANGLE);

                Naviloc naviloc = new Naviloc();
                naviloc.setX(x);
                naviloc.setY(y);
                naviloc.setAngle(angle);

                //여기에 변수 값 전달됨
                x=naviloc.getX();
                y=naviloc.getY();
                angle=naviloc.getAngle();

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        Log.d("MyTag","init");
        talker = new Talker("navimode");

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress().toString());
        nodeConfiguration.setMasterUri(getMasterUri());
        //nodeConfiguration.setNodeName("keywords");// <--this fixed it
        nodeMainExecutor.execute(talker, nodeConfiguration);
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);

    }
}
