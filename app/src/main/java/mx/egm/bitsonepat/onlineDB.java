package mx.egm.bitsonepat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class onlineDB extends AppCompatActivity {
    static String[] getJsonName, getJsonData, getKeys;
    static String geturl,data3;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id=this.getIntent().getExtras().getInt("id");
        switch(id){
            case R.id.notices:
                setTitle("Notices");
                getJsonName=this.getIntent().getExtras().getStringArray("jsonName");
                getJsonData=this.getIntent().getExtras().getStringArray("jsonData");
                getKeys=this.getIntent().getExtras().getStringArray("keyName");
                new dba(getJsonName,getJsonData,getKeys,this).execute();
                break;
            case R.id.resultreqbutton:
                setTitle("Result");
                geturl=this.getIntent().getExtras().getString("url");
                new dba(geturl,this).execute();
                break;
        }
    }

    public class dba extends AsyncTask<String[], String[], String[]> {
        public String[] data=null, name=null, keys, ret={null,null,null,null};
        public String geturl2;
        public Context ctx;
        DatabaseHandler dbh;
        String error="";
        int datatype;

        public dba(String[] jsonname, String[] jsondata, String[] respKeys, Context act){
            ctx=act;
            data=jsondata;
            name=jsonname;
            keys=respKeys;
            dbh=new DatabaseHandler(ctx);
            datatype=0;
        }//initialise variables fetched from main

        public dba(String url, Context act){
            ctx=act;
            geturl2=url;
            datatype=1;
        }//initialise variables fetched from main

        protected void onPreExecute(){}

        protected String[] doInBackground(String[]... arg0) {
            HttpPost httppost;
            JSONObject jsonobj;
            List<NameValuePair> nameValuePairs;
            switch (datatype) {
                case 0:
                    httppost = new HttpPost(data[0]);
                    jsonobj = new JSONObject();
                    nameValuePairs = new ArrayList<NameValuePair>();
                    try {
                        for (int i = 0; i <= name.length - 1; i++) jsonobj.put(name[i], data[i + 2]); //array starts from 0
                        nameValuePairs.add(new BasicNameValuePair(data[1], jsonobj.toString()));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//preparing json data
                        HttpResponse response = new DefaultHttpClient().execute(httppost);//executing json post request
                        ret[1] = (EntityUtils.toString(response.getEntity(), "UTF-8"));
                        error = "Updating done";
                    } catch (UnsupportedEncodingException e) {error = "Unsupported encoding found";}
                    catch (ClientProtocolException e) {error = "Client protocol error";}
                    catch (IOException e) {error = "Could not connect to server";}
                    catch (Exception e) {error = "Unknown error: " + e.getMessage();}

                    try {
                        JSONObject jsonRootObject = new JSONObject(ret[1]);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("notices");
                        String[] getKeys=new String[keys.length];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            for(int count=0; count < keys.length; count++) {
                                getKeys[count] = jsonObject.optString(keys[count]).toString();
                            }
                            if(!(jsonObject.optString(keys[7]).toString().equals("text"))){
                                int byteCount;
                                try {
                                    URL url = new URL(jsonObject.optString(keys[8]).toString());
                                    URLConnection conection = url.openConnection();
                                    conection.connect();
                                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                                    new File("/sdcard/.bits/notice/").mkdir();
                                    OutputStream output = new FileOutputStream("/sdcard/.bits/notice/"
                                            +jsonObject.optString(keys[7]).toString());
                                    byte data[] = new byte[1024];
                                    while ((byteCount = input.read(data)) != -1) {
                                        output.write(data, 0, byteCount);
                                    }
                                    output.flush();
                                    output.close();
                                    input.close();
                                } catch (Exception e) {
                                }
                            }
                            dbh.addRow("notices", keys, getKeys);
                        }
                    }catch(Exception e){}

                    break;
                case 1:
                    httppost = new HttpPost(geturl2);
                    try {
                        HttpResponse response = new DefaultHttpClient().execute(httppost);//executing json post request
                        ret[1] = (EntityUtils.toString(response.getEntity(), "UTF-8"));
                        data3=ret[1];
                    } catch (UnsupportedEncodingException e) {error = "Unsupported encoding found";}
                    catch (ClientProtocolException e) {error = "Client protocol error";}
                    catch (IOException e) {error = "Could not connect to server";}
                    catch (Exception e) {error = "Unknown error: " + e.getMessage();}
                    break;
            }
            return null;
        }
        protected void onPostExecute(String[] Void) {
            super.onPostExecute(Void);
            returner();
            Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
        }
    }

    public final void returner(){
        this.finish();
        switch(id){
            case R.id.notices:
                startActivity(new Intent(this, Notices.class));
                break;
            case R.id.resultreqbutton:
                startActivity(new Intent(this,result.class).putExtra("html",data3)
                        .putExtra("url",geturl));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}