package com.example.haidt.mylivewallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.haidt.mylivewallpaper.Adapter.ListViewAdapter;
import com.example.haidt.mylivewallpaper.Comon.Comon;
import com.example.haidt.mylivewallpaper.Model.LiveImage;
import com.example.haidt.mylivewallpaper.uril.CheckConnection;
import com.example.haidt.mylivewallpaper.uril.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListImage extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    String nameCatagory;
    String IDCatagory;

    ArrayList<LiveImage> listLiveImage;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image);

        Intent intent= getIntent();
        nameCatagory=intent.getStringExtra(Comon.CATAGORY_SELECT);
        IDCatagory=intent.getStringExtra(Comon.CATEGORY_ID_KEY);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(nameCatagory);
        progressBar=findViewById(R.id.list_image_progressbar);

        recyclerView=findViewById(R.id.listImage_recyclerView);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        setListImage();

    }

    private void setListImage() {
        listLiveImage= new ArrayList<>();
        listViewAdapter= new ListViewAdapter(getApplicationContext(),listLiveImage);
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            StringRequest request= new StringRequest(Request.Method.POST, Server.LiveImageListItemLink, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response!=null){
                        try {
                            JSONArray liveImageArray= new JSONArray(response);
                            for (int i=0;i<liveImageArray.length();i++){
                                JSONObject jsonObject= liveImageArray.getJSONObject(i);
                                String liveImageID= jsonObject.getString("liveImageID");
                                String CatagoryID=IDCatagory;
                                String nameLiveImage= jsonObject.getString("nameLiveImage");
                                String urlLinkImage=jsonObject.getString("urlLinkImage");
                                int numberOfSee=jsonObject.getInt("numberOfSee");
                                int numberOfDownload=jsonObject.getInt("numberOfDowload");
                                String urlGifImage=jsonObject.getString("UrlGIFImage");
                                String dayUpload=jsonObject.getString("DayUpdate");
                                listLiveImage.add(new LiveImage(CatagoryID,liveImageID,nameLiveImage,numberOfSee,numberOfDownload,urlLinkImage,urlGifImage,dayUpload));
                                listViewAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap= new HashMap<>();
                    hashMap.put("Catagory",IDCatagory);
                    return hashMap;
                }
            };
            requestQueue.add(request);
        }
        else{
            CheckConnection.showToast(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối internet");
        }
        recyclerView.setAdapter(listViewAdapter);
    }
}
