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

import com.example.kkobugi.DTO.Department;
import com.example.kkobugi.R;
import com.example.kkobugi.adapter.DepartmentAdapter;
import com.example.kkobugi.task.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DepartmentActivity extends Activity {

    private static String IP_ADDRESS="192.168.0.174";
    private static String TAG = "phptest";
    private String jsonString;

    private String tableName;
    private String title;

    private ArrayList<Department> departmentArrayList;

    private DepartmentAdapter departmentAdapter;

    private ListView listview_department;
    private TextView text_departmentName, text_departmentContent;

    private TextView text_noData;
    private Button btn_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Intent intent= getIntent();
        tableName = intent.getStringExtra("table");
        title = intent.getStringExtra("title");

        text_noData = (TextView)findViewById(R.id.text_noData);
        text_noData.setVisibility(View.GONE);

        btn_bottom = (Button)findViewById(R.id.btn_bottom);
        btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(DepartmentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /***********/
        //department
        listview_department = (ListView)findViewById(R.id.listview_introduction);
        departmentArrayList = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(DepartmentActivity.this, departmentArrayList);
        listview_department.setAdapter(departmentAdapter);
        text_departmentName = (TextView)findViewById(R.id.text_departmentName);
        text_departmentContent = (TextView)findViewById(R.id.text_departmentContent);

        departmentArrayList.clear();
        departmentAdapter.notifyDataSetChanged();
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
        String TAG_CONTENT ="content";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String depname = item.getString(TAG_NAME);
                String content = item.getString(TAG_CONTENT);
                content = content.replace("\\n","\n");

                Department department = new Department();
                department.setDepartmentName(depname);
                department.setDepartmentContent(content);

                departmentArrayList.add(department);
                departmentAdapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
