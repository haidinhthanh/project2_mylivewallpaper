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
import com.example.haidt.mylivewallpaper.Adapter.CatagoryAdapter;
import com.example.haidt.mylivewallpaper.Model.CatagoryItem;
import com.example.haidt.mylivewallpaper.R;
import com.example.haidt.mylivewallpaper.uril.CheckConnection;
import com.example.haidt.mylivewallpaper.uril.Server;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Catagory extends Fragment {
    private static Catagory INSTANCE=null;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    ArrayList<CatagoryItem> arrayListCatagoryItem;
    CatagoryAdapter catagoryAdapter;
    public Catagory() {
        // Required empty public constructor
    }
    public static Catagory getINSTANCE() {
        if(INSTANCE==null)
            INSTANCE= new Catagory();
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_catagory, container, false);
        progressBar=view.findViewById(R.id.fragment_catagory_progressbar);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_catagory_item);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        setUpCatagoryItem();

        return view;
    }

    public void setUpCatagoryItem(){
        arrayListCatagoryItem= new ArrayList<>();
        catagoryAdapter= new CatagoryAdapter(getActivity().getApplicationContext(),arrayListCatagoryItem);
        recyclerView.setAdapter(catagoryAdapter);
        if(CheckConnection.haveNetworkConnection(getActivity().getApplicationContext())){
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
            JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Server.CatagoryListItemLink, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(response!=null){
                        for (int i=0; i<response.length();i++){
                            try {
                                JSONObject jsonObject= response.getJSONObject(i);
                                String IdCatagory= jsonObject.getString("catagoryID");
                                String nameCatagory= jsonObject.getString("catagoryName");
                                String urlImageCatagory= jsonObject.getString("catagoryImageURL");
                                arrayListCatagoryItem.add( new CatagoryItem(IdCatagory,nameCatagory,urlImageCatagory));
                                catagoryAdapter.notifyDataSetChanged();
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
        }
        else{
            CheckConnection.showToast(getContext(),"Bạn hãy kiểm tra kết nối mạng");
        }

    }

}
