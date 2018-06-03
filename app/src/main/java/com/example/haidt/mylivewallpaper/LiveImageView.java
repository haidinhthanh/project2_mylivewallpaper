package com.example.haidt.mylivewallpaper;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.haidt.mylivewallpaper.Comon.Comon;
import com.example.haidt.mylivewallpaper.uril.Server;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class LiveImageView extends AppCompatActivity {
    public static final String MYPREF="FavoriteImage";
    public static final String LISTFAVORITE="ListFavorite";
    public static final String URLIMAGELINK="UrlImageLink";
    public static final String NAMEGIFIMAGE="NameGifImage";

    public static boolean IS_SET=false;

    SharedPreferences preferences;
    String URLGifLiveImageLink;
    String LiveImageID;
    String LiveImageName;

    Button buttonSetLiveImage;
    ImageView numDownload;
    ImageView likeImage;
    GifImageView gifImageView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_image);
        GetViewID();
        Intent intent= getIntent();
        LiveImageName=intent.getStringExtra(Comon.LiveImageName);
        URLGifLiveImageLink= intent.getStringExtra(Comon.LiveImageLink);
        LiveImageID=intent.getStringExtra(Comon.LiveImageID);
        setView();
        updateStateFavorite();
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences=getApplicationContext().getSharedPreferences(MYPREF,MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                if(((BitmapDrawable)likeImage.getDrawable()).getBitmap().sameAs(((BitmapDrawable)getResources().getDrawable(R.drawable.dontlike)).getBitmap())){
                    likeImage.setImageResource(R.drawable.like);
                    String list=preferences.getString(LISTFAVORITE,"");
                    String[] listImageID=list.split("-");
                    StringBuilder stringListLiveImageID= new StringBuilder();
                    for(int i=0;i<listImageID.length;i++){
                        stringListLiveImageID.append(listImageID[i]).append("-");
                    }
                    stringListLiveImageID.append(LiveImageID).append("-");
                    editor.putString(LISTFAVORITE,stringListLiveImageID.toString());
                    editor.commit();
                }
                else {
                    likeImage.setImageResource(R.drawable.dontlike);
                    String list=preferences.getString(LISTFAVORITE,"");
                    String newList=list.replaceAll(LiveImageID+"-","");
                    editor.clear();
                    editor.putString(LISTFAVORITE,newList);
                    editor.commit();
                }
            }
        });
        numDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadLiveWallpaper();
            }
        });
        buttonSetLiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGifWallPaper();
            }
        });
        updateSeeLiveImage();
    }

    private void downLoadLiveWallpaper() {
        File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+  "/LiveWallPaper/"+LiveImageName+".gif");
        if(f.exists()){
            Toast.makeText(getApplicationContext(),"ImageHasDownLoad",Toast.LENGTH_SHORT).show();
        }
        else {
            new DownloadFile(LiveImageName, ".gif", getApplicationContext()).execute(URLGifLiveImageLink);
            updateDownloadLiveImage();
        }
    }

    private void updateDownloadLiveImage() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Server.UpdateDownLoadLiveImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap= new HashMap<>();
                hashMap.put("LiveImageID",LiveImageID);
                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void updateSeeLiveImage() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Server.UpdateSeeLiveImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap= new HashMap<>();
                hashMap.put("LiveImageID",LiveImageID);
                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setView() {
        new RetriveByteArray().execute(URLGifLiveImageLink);
        gifImageView.startAnimation();
    }
    private void updateStateFavorite(){
        preferences=getApplicationContext().getSharedPreferences(MYPREF,MODE_PRIVATE);
        String list=preferences.getString(LISTFAVORITE,"");
        if(list.contains(LiveImageID)){
            likeImage.setImageResource(R.drawable.like);
        }
    }

    private void GetViewID() {
        buttonSetLiveImage=findViewById(R.id.bt_set_bg);
        numDownload=findViewById(R.id.download_icon);
        likeImage=findViewById(R.id.like_icon);
        gifImageView=findViewById(R.id.gif_bg);
        progressBar=findViewById(R.id.id_progress_bar);
    }
    class RetriveByteArray extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            try{
                File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+  "/LiveWallPaper/"+LiveImageName+".gif");
                if(f.exists()) {
                    FileInputStream fileInputStream=new FileInputStream(f);
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    int nRead;
                    byte[] data= new byte[1094720];
                    while((nRead=fileInputStream.read(data,0,data.length))!=-1){
                        byteArrayOutputStream.write(data,0,nRead);
                    }
                    byteArrayOutputStream.flush();
                    return byteArrayOutputStream.toByteArray();
                }
                URL url= new URL(strings[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                if(httpURLConnection.getResponseCode()==200){
                    InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    int nRead;
                    byte[] data= new byte[1094720];
                    while((nRead=inputStream.read(data,0,data.length))!=-1){
                        byteArrayOutputStream.write(data,0,nRead);
                    }
                    byteArrayOutputStream.flush();
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(byte[] bytes) {
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(bytes);
            gifImageView.setBytes(bytes);
        }
    }
    public void setGifWallPaper(){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, GIFWallpaperService.class));
        preferences= getApplicationContext().getSharedPreferences(MYPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(NAMEGIFIMAGE,"");
        editor.putString(URLIMAGELINK,"");
        editor.commit();
        editor.putString(URLIMAGELINK,URLGifLiveImageLink);
        editor.putString(NAMEGIFIMAGE,LiveImageName);
        editor.commit();
        startActivity(intent);
    }

    class DownloadFile extends AsyncTask<String,Integer,Long> {
        String targetFileName;
        Context context;
        ProgressDialog progressDialog= new ProgressDialog(LiveImageView.this);
        private PowerManager.WakeLock mWakeLock;
        public DownloadFile(String filename,String extension,Context context){
            this.targetFileName=filename+extension;
            this.context=context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            progressDialog.show();
        }

        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection mconection = url.openConnection();
                mconection.connect();
                int lenghtOfFile = mconection.getContentLength();
                String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LiveWallPaper/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH + targetFileName);
                byte data[] = new byte[4096];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mWakeLock.release();
            progressDialog.dismiss();
            super.onPostExecute(aLong);
            Toast.makeText(getApplicationContext(),"DownLoad",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+  "/LiveWallPaper/"+LiveImageName+".gif");
        if(IS_SET==true){
            if(f.exists()){
                Toast.makeText(getApplicationContext(),"ImageHasDownLoad",Toast.LENGTH_SHORT).show();
            }
            else {
                new DownloadFile(LiveImageName, ".gif", getApplicationContext()).execute(URLGifLiveImageLink);
                updateDownloadLiveImage();
            }
        }
        IS_SET=false;
    }
}
