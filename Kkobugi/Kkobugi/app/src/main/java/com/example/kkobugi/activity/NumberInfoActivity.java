package com.example.kkobugi.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kkobugi.DTO.NumberInfo;
import com.example.kkobugi.R;
import com.example.kkobugi.adapter.NumberAdapter;
import com.example.kkobugi.task.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NumberInfoActivity extends Activity {

    private static String TAG = "phptest";
    private String jsonString;

    //명령어
    private String tableName; //table이름
    private String title; //원자값

    //ArrayList
    private ArrayList<NumberInfo> numberArrayList;

    //Adapter
    private NumberAdapter numberAdapter;

    //DTO
    private NumberInfo numberInfo;

    //view
    private TextView text_noData;
    private ListView listview_number;
    private TextView text_numberDepartment, text_number, text_numberFax;
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
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(NumberInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listview_number = (ListView)findViewById(R.id.listview_introduction);
        numberArrayList = new ArrayList<>();
        numberAdapter = new NumberAdapter(NumberInfoActivity.this, numberArrayList);
        listview_number.setAdapter(numberAdapter);
        text_numberDepartment = (TextView)findViewById(R.id.text_numDepartment);
        text_number = (TextView)findViewById(R.id.text_number);
        text_numberFax = (TextView)findViewById(R.id.text_numberFax);

        numberArrayList.clear();
        numberAdapter.notifyDataSetChanged();


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
        String TAG_NAME = "depname";
        String TAG_NUMBER ="depnumber";
        String TAG_FAX="fax";


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String depname = item.getString(TAG_NAME);
                String depnumber = item.getString(TAG_NUMBER);
                String fax = item.getString(TAG_FAX);

                NumberInfo numberInfo = new NumberInfo();
                numberInfo.setDepartmentName(depname);
                numberInfo.setNumber(depnumber);
                numberInfo.setNumberFax(fax);

                numberArrayList.add(numberInfo);
                numberAdapter.notifyDataSetChanged();

            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
