package com.example.kkobugi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kkobugi.DTO.Introduction;
import com.example.kkobugi.R;
import com.example.kkobugi.adapter.IntroductionAdapter;
import com.example.kkobugi.task.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IntroductionActivity extends Activity {

    private static String TAG = "phptest";
    private String jsonString;

    //명령어
    private String tableName; //table이름
    private String title; //원자값

    //ArrayList
    private ArrayList<Introduction> introductionArrayList;

    //Adapter
    private IntroductionAdapter introductionAdapter;

    //DTO
    private Introduction introduction;

    private ImageView imageView;

    //view
    private TextView text_noData;
    private ListView listview_introduction;
    private TextView text_introductionName, text_introductionContent;
    private Button btn_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);


        Intent intent = getIntent();
        tableName = intent.getStringExtra("table");
        title = intent.getStringExtra("title");

        text_noData = (TextView)findViewById(R.id.text_noData);
        text_noData.setVisibility(View.GONE);

        btn_bottom = (Button)findViewById(R.id.btn_bottom);
        btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(IntroductionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /***********/
        //introduction
        listview_introduction = (ListView)findViewById(R.id.listview_introduction);
        introductionArrayList = new ArrayList<>();
        introductionAdapter = new IntroductionAdapter(IntroductionActivity.this, introductionArrayList);
        listview_introduction.setAdapter(introductionAdapter);
        text_introductionName = (TextView)findViewById(R.id.text_introductionName);
        text_introductionContent = (TextView)findViewById(R.id.text_introductionContent);

        introductionArrayList.clear();
        introductionAdapter.notifyDataSetChanged();

        String url = RequestHttpURLConnection.IP_URL;
        ContentValues cv = new ContentValues();
        cv.put("introOrder",tableName);
        cv.put("introOrder2",title);
        NetworkTask networkTask = new NetworkTask(url, cv);
        networkTask.execute();
        /*************/
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

        String TAG_JSON=tableName;
        String TAG_TITLE = "title";
        String TAG_CONTENT ="content";
        String TAG_IMAGE="introimage";


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                String content = item.getString(TAG_CONTENT);
                content = content.replace("\\n","\n");
                String introimage = item.getString(TAG_IMAGE);

                Introduction introduction = new Introduction();
                introduction.setIntroductionName(title);
                introduction.setIntroductionContent(content);
                introduction.setIntroImage(introimage);

                introductionArrayList.add(introduction);
                introductionAdapter.notifyDataSetChanged();

            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
