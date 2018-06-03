package com.example.haidt.mylivewallpaper.Navigation;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.example.haidt.mylivewallpaper.LiveImageView;
import com.example.haidt.mylivewallpaper.Model.LiveImage;
import com.example.haidt.mylivewallpaper.R;
import com.example.haidt.mylivewallpaper.uril.CheckConnection;
import com.example.haidt.mylivewallpaper.uril.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Favorite_Image extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<LiveImage> listLiveImage;
    ListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite__image);
        setIDView();
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        setListFavoriteImage();
    }
    private void setListFavoriteImage() {
        listLiveImage= new ArrayList<>();
        listViewAdapter= new ListViewAdapter(getApplicationContext(),listLiveImage);
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            SharedPreferences preferences=getApplicationContext().getSharedPreferences(LiveImageView.MYPREF,MODE_PRIVATE);
            String mylist=preferences.getString(LiveImageView.LISTFAVORITE,"");
            final String[] mylist2=mylist.split("-");
            for (int j=0;j<mylist2.length;j++) {
                final String imageID=mylist2[j];
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST, Server.GetFavoriteImage, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONArray liveImageArray = new JSONArray(response);
                                for (int i = 0; i < liveImageArray.length(); i++) {
                                    JSONObject jsonObject = liveImageArray.getJSONObject(i);
                                    String liveImageID = jsonObject.getString("liveImageID");
                                    String CatagoryID = jsonObject.getString("CatagoryID");
                                    String nameLiveImage = jsonObject.getString("nameLiveImage");
                                    String urlLinkImage = jsonObject.getString("urlLinkImage");
                                    int numberOfSee = jsonObject.getInt("numberOfSee");
                                    int numberOfDownload = jsonObject.getInt("numberOfDowload");
                                    String urlGifImage = jsonObject.getString("UrlGIFImage");
                                    String dayUpload = jsonObject.getString("DayUpdate");
                                    listLiveImage.add(new LiveImage(CatagoryID, liveImageID, nameLiveImage, numberOfSee, numberOfDownload, urlLinkImage, urlGifImage, dayUpload));
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("liveImageID",imageID);
                        return hashMap;
                    }
                };
                requestQueue.add(request);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        else{
            CheckConnection.showToast(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối internet");
        }
        recyclerView.setAdapter(listViewAdapter);
    }
    private void setIDView() {
        progressBar=findViewById(R.id.list_image_progressbar_favorite_activity);
        recyclerView=findViewById(R.id.listImage_recyclerView_favorite_activity);
        toolbar=findViewById(R.id.toolbar_favorite_activity);
        toolbar.setTitle("Favorite Image");
    }
}
