package com.example.kkobugi.activity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kkobugi.DTO.Location;
import com.example.kkobugi.task.RequestHttpURLConnection;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.kkobugi.R;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    //명령어 임시 변수
    private String introOrder;
    private String introOrder2;
    String loclat,loclon;
    private String jsonString;

    private Button btn_bottom;
    private TextView text_noData;

    private Location location;
    private GoogleMap mMap = null;
    private Geocoder coder = null;
    private GoogleApiClient gac=null;
    private Marker selectMarker;

    double lat=0,lon=0;

    public MapActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        introOrder = intent.getStringExtra("table");
        introOrder2 = intent.getStringExtra("title");

        btn_bottom = (Button) findViewById(R.id.btn_bottom);
        text_noData = (TextView)findViewById(R.id.text_noData);
        text_noData.setVisibility(View.GONE);

        String url = RequestHttpURLConnection.IP_URL;
        ContentValues cv = new ContentValues();
        cv.put("introOrder","location");
        cv.put("introOrder2",introOrder2);
        NetworkTask networkTask = new NetworkTask(url, cv);
        networkTask.execute();

        btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(loclat==null){
            LatLng pos = new LatLng(36.625431, 127.457861);
            MarkerOptions mo = new MarkerOptions();
            mo.position(pos);
            mo.title("현재 위치");

//마커 이미지설정
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
            Bitmap b = bitmapDrawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,70,100,false);
            mo.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(mo);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));


            Log.i("TAG","위도 경도 찾기 실패!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        else{
            Log.i("TAG","위도 경도 찾기 성공");
            LatLng pos2 = new LatLng(Double.parseDouble(loclat),Double.parseDouble(loclon));

            MarkerOptions mo = new MarkerOptions();
            mo.position(pos2);
            mo.title("현재 위치");

//마커 이미지설정
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
            Bitmap b = bitmapDrawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,70,100,false);
            mo.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(mo);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos2,15));
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
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapActivity.this);
        }
    }


    private void showResult(){

        String TAG_JSON="location";
        String NAME = "locname";
        String LAT = "loclat";
        String LON="loclon";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                loclat = item.getString(LAT);
                loclon = item.getString(LON);

                Location location = new Location();
                location.setLoclat(loclat);
                location.setLoclon(loclon);

                loclat=location.getLoclat();
                loclon=location.getLoclon();
            }



        } catch (JSONException e) {

            Log.d("TAG", "showResult : ", e);
        }
    }

}