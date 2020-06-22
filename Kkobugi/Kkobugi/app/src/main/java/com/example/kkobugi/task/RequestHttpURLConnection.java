package com.example.kkobugi.task;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {
    //씨각: 192.168.0.174
    //집: 192.168.35.241
    //예원집: 192.168.0.25

    public static final String IP_URL="http://172.20.10.7/department.php";

    public String request(String url, ContentValues params) {
        HttpURLConnection urlConn = null;
        //url 뒤에 붙여보낼 파라미터
        StringBuffer sb = new StringBuffer();

        //StringBuffer에 파라미터 연결
        if (params == null) {
            sb.append("");
        }
        else {
            //파라미터 2개 이상이면 &으로 연결
            boolean isAnd = false;
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                //파라미터가 두개 이상일 때
                if (isAnd) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
                //파라미터가 2개 이상이면 isAnd true로 바꿔
                if (!isAnd) {
                    if (params.size() >= 2) {
                        isAnd=true;
                    }
                }
            }
        }

        //web의 데이터 가져옴
        try{
            URL _url = new URL(url);
            urlConn = (HttpURLConnection) _url.openConnection();

            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept-Charset","UTF-8");
            urlConn.setRequestProperty("Context_Type","application/x-www-form-urlencoded; charset=UTF-8");

            //parameter 전달 및 데이터 읽기
            String strParams = sb.toString(); //strParmas에 introOrder="dep"&introOrder2="소웨"저장
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8")); //출력스트림에 출력
            os.flush();
            os.close();

            //연결 확인
            //실패하면 null반환 후 함수 종료
            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            //읽은 결과 리턴
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            return builder.toString().trim();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConn !=null){
                urlConn.disconnect();
            }
        }
        return null;
    }
}
