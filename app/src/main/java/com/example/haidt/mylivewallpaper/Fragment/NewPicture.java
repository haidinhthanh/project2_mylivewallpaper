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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.haidt.mylivewallpaper.Adapter.ListViewAdapter;
import com.example.haidt.mylivewallpaper.Model.LiveImage;
import com.example.haidt.mylivewallpaper.R;
import com.example.haidt.mylivewallpaper.uril.CheckConnection;
import com.example.haidt.mylivewallpaper.uril.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NewPicture extends Fragment {
    private static NewPicture INSTANCE=null;
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ArrayList<LiveImage> listLiveImage;
    ListViewAdapter listViewAdapter;
    public NewPicture() {
        // Required empty public constructor
    }
    public static NewPicture getINSTANCE() {
        if(INSTANCE==null)
            return INSTANCE= new NewPicture();
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
            View view=inflater.inflate(R.layout.fragment_new_picture,container,false);
            toolbar=view.findViewById(R.id.toolbar_new_image_fragment);
            progressBar=view.findViewById(R.id.list_image_progressbar_new_image_fragment);

            recyclerView=view.findViewById(R.id.listImage_recyclerView_new_image_fragment);
            GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            setListImage();
            return view;
        }
        private void setListImage() {
            listLiveImage= new ArrayList<>();
            listViewAdapter= new ListViewAdapter(getContext(),listLiveImage);
            if(CheckConnection.haveNetworkConnection(getContext())){
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                StringRequest request= new StringRequest(Request.Method.POST, Server.GetNewImage, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response!=null){
                            try {
                                JSONArray liveImageArray= new JSONArray(response);
                                for (int i=0;i<liveImageArray.length();i++){
                                    JSONObject jsonObject= liveImageArray.getJSONObject(i);
                                    String liveImageID= jsonObject.getString("liveImageID");
                                    String CatagoryID= jsonObject.getString("CatagoryID");
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
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap= new HashMap<>();
                        Date current_time= Calendar.getInstance().getTime();
                        String timenow= new SimpleDateFormat("yyyy-MM-dd").format(current_time);
                        hashMap.put("DayUpdate",timenow);
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
            else{
                CheckConnection.showToast(getContext(),"Bạn hãy kiểm tra lại kết nối internet");
            }
            recyclerView.setAdapter(listViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
