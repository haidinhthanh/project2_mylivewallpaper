package com.example.haidt.mylivewallpaper.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.haidt.mylivewallpaper.Adapter.ListViewAdapter;
import com.example.haidt.mylivewallpaper.Model.LiveImage;
import com.example.haidt.mylivewallpaper.R;
import com.example.haidt.mylivewallpaper.uril.CheckConnection;
import com.example.haidt.mylivewallpaper.uril.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StandOutPicture extends Fragment {
    private static StandOutPicture INSTANCE=null;
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    
    ArrayList<LiveImage> listLiveImage;
    ListViewAdapter listViewAdapter;
    public StandOutPicture() {
        // Required empty public constructor
    }
    public static StandOutPicture getINSTANCE() {
        if(INSTANCE==null)
            INSTANCE = new StandOutPicture();
        return INSTANCE;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_stand_out_picture,container,false);
        toolbar=view.findViewById(R.id.toolbar_standout_fragment);
        recyclerView=view.findViewById(R.id.listImage_recyclerView_standout_fragment);
        progressBar=view.findViewById(R.id.list_image_progressbar_standout_fragment);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        setListImage();
        return view;
    }

    private void setListImage() {
        listLiveImage= new ArrayList<>();
        listViewAdapter= new ListViewAdapter(getContext(),listLiveImage);
        recyclerView.setAdapter(listViewAdapter);
        if(CheckConnection.haveNetworkConnection(getActivity().getApplicationContext())){
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
            JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Server.GetStandOutPicture, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(response!=null){
                        for (int i=0; i<response.length();i++){
                            try {
                                JSONObject jsonObject= response.getJSONObject(i);
                                String liveImageID= jsonObject.getString("liveImageID");
                                String CatagoryID=jsonObject.getString("CatagoryID");
                                String nameLiveImage= jsonObject.getString("nameLiveImage");
                                String urlLinkImage=jsonObject.getString("urlLinkImage");
                                int numberOfSee=jsonObject.getInt("numberOfSee");
                                int numberOfDownload=jsonObject.getInt("numberOfDowload");
                                String urlGifImage=jsonObject.getString("UrlGIFImage");
                                String dayUpload=jsonObject.getString("DayUpdate");
                                listLiveImage.add(new LiveImage(CatagoryID,liveImageID,nameLiveImage,numberOfSee,numberOfDownload,urlLinkImage,urlGifImage,dayUpload));
                                listViewAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonArrayRequest);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else{
            CheckConnection.showToast(getContext(),"Bạn hãy kiểm tra kết nối mạng");
        }
    }
}
