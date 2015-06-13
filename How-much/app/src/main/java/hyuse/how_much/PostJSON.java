package hyuse.how_much;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.*;
import android.os.Process;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * Created by hwang-gyojun on 2015. 5. 4..
 */
public class PostJSON {
    private String url = "http://bear.3dwise.com:8000/price/";
    private String result;
    private String type;
    private HttpAsyncTask task;

    /* Kyojun Hwang  code */
    public void setType(String type) {
        result = null;
        this.type = type;
    }

    public boolean send(String sub_id, String region_si) {
        if (type.equals("result")) {
            String data = "?sub_id=" + sub_id + "&region_si=" + region_si;
            String u = url + type + data;

            try {
                task = new HttpAsyncTask();
                if(task.execute(u, "get").get()) {
                    task.cancel(true);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean send() {
        String u = url + type;

        try {
            task = new HttpAsyncTask();
            if(task.execute(u, "get").get()) {
                task.cancel(true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean send(String _data) {
        String data = "";
        String u = "";
        try {
            switch (type) {
                case "main":
                    data = "?category_id=" + _data;
                    u = url + type + data;
                    task = new HttpAsyncTask();
                    if(task.execute(u, "get").get()) {
                        task.cancel(true);
                        return true;
                    }
                    break;
                case "sub":
                    data = "?main_id=" + _data;
                    u = url + type + data;
                    task = new HttpAsyncTask();
                    if(task.execute(u, "get").get()) {
                        task.cancel(true);
                        return true;
                    }
                    break;
                case "auto_complete":
                    data = "?data=" + _data.replace(" ", "");
                    u = url + type + data;
                    task = new HttpAsyncTask();
                    if(task.execute(u, "get").get()) {
                        task.cancel(true);
                        return true;
                    }
                    break;
                case "home":
                    task = new HttpAsyncTask();
                    if (task.execute(_data, "post").get()) {
                        task.cancel(true);
                        return true;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String returnResult() throws ExecutionException, InterruptedException {
        return result;
    }
    /* Kyojun Hwang  code end */

    /* haryeong code */
    public void GET(final String urlString){

        final int[] end = {1};
        final String[] responseString = new String[1];
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = null;
                try
                {
                    URI url = new URI(urlString);

                    HttpGet httpGet = new HttpGet(url);
                    httpResponse = httpClient.execute(httpGet);
                    responseString[0] = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                    end[0] = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        while(end[0] == 1){}

        result = responseString[0];
    }
    /* haryeong code end*/

    /* Kyojun Hwang  code */
    public void POST(String json){
        InputStream inputStream = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            StringEntity se = new StringEntity(json, HTTP.UTF_8);

            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream,"utf-8"));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... objs) {
            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpParams param = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(param, 2000);
                HttpConnectionParams.setSoTimeout(param, 2000);

                HttpGet httpGet = new HttpGet(url);
                httpClient.execute(httpGet);

                if (objs[1].equals("get"))
                    GET(objs[0]);
                else if (objs[1].equals("post"))
                    POST(objs[0]);
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    }
    /* Kyojun Hwang  code end */
}
