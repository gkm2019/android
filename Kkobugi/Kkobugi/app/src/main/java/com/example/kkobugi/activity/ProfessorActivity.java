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

import com.example.kkobugi.DTO.Professor;
import com.example.kkobugi.R;
import com.example.kkobugi.adapter.ProfessorAdapter;
import com.example.kkobugi.task.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorActivity extends Activity {

    private static String TAG = "phptest";
    private String jsonString;

    //명령어
    private String tableName; //table이름
    private String title; //원자값

    //ArrayList
    private ArrayList<Professor> professorArrayList;

    //Adapter
    private ProfessorAdapter professorAdapter;

    //DTO
    private Professor professor;

    //view
    private TextView text_noData;
    private ListView listview_professor;
    private TextView text_professorName, text_professorField, text_professorTell, text_professorAddr, text_professorEmail, text_professorDepartment;
    private ImageView image_professor;
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
                intent = new Intent(ProfessorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /***********/
        //department
        listview_professor = (ListView)findViewById(R.id.listview_introduction);
        professorArrayList = new ArrayList<>();
        professorAdapter = new ProfessorAdapter(ProfessorActivity.this, professorArrayList);
        listview_professor.setAdapter(professorAdapter);
        text_professorName = (TextView)findViewById(R.id.text_professorName);
        text_professorDepartment = (TextView)findViewById(R.id.text_professorDepartment);
        text_professorField = (TextView)findViewById(R.id.text_professorField);
        text_professorTell = (TextView)findViewById(R.id.text_professorTell);
        text_professorAddr = (TextView)findViewById(R.id.text_professorAddr);
        text_professorEmail = (TextView)findViewById(R.id.text_professorEmail);
        image_professor = (ImageView)findViewById(R.id.image_professor);

        professorArrayList.clear();
        professorAdapter.notifyDataSetChanged();
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
        String _profname = "profname";
        String _depname ="depname";
        String _proffield="proffield";
        String _profnum="profnum";
        String _profemail="profemail";
        String _profimage="profimage";
        String _profaddr="profaddr";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String profname = item.getString(_profname);
                String depname = item.getString(_depname);
                String proffield = item.getString(_proffield);
                String profnum = item.getString(_profnum);
                String profemail = item.getString(_profemail);
                String profimage = item.getString(_profimage);
                String profaddr = item.getString(_profaddr);

                Professor professor = new Professor();
                professor.setProfessorName(profname);
                professor.setDepartmentName(depname);
                professor.setProfessorField(proffield);
                professor.setProfessorTell(profnum);
                professor.setProfessorEmail(profemail);
                professor.setProfessorImage(profimage);
                professor.setProfessorAddr(profaddr);

                professorArrayList.add(professor);
                professorAdapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
