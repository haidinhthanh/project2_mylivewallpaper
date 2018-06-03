package com.example.haidt.mylivewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.haidt.mylivewallpaper.Comon.Comon;
import com.example.haidt.mylivewallpaper.Interface.ItemClickListenner;
import com.example.haidt.mylivewallpaper.ListImage;
import com.example.haidt.mylivewallpaper.LoadImage.ImageLoader;
import com.example.haidt.mylivewallpaper.Model.CatagoryItem;
import com.example.haidt.mylivewallpaper.R;

import java.util.ArrayList;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.CatagoryViewHolder> {
    Context context;
    ArrayList<CatagoryItem> arrayListCatagoryItem;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;
    public CatagoryAdapter(Context context, ArrayList<CatagoryItem> arrayList){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.arrayListCatagoryItem=arrayList;
        this.imageLoader=new ImageLoader(context);
    }
    @NonNull
    @Override
    public CatagoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_item_layout,parent,false);
        return new CatagoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CatagoryViewHolder holder, int position) {
        holder.textView.setText(arrayListCatagoryItem.get(position).getNameCatagory());
        imageLoader.clearCache();
        ImageLoader.REQUIRED_SIZE=130;
        imageLoader.DisplayImage(arrayListCatagoryItem.get(position).getUrlImageCatagory(),holder.imageView);
        holder.setItemClickListenner(new ItemClickListenner() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent= new Intent(context, ListImage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Comon.CATEGORY_ID_KEY,arrayListCatagoryItem.get(position).getIdCatagory());
                intent.putExtra(Comon.CATAGORY_SELECT,arrayListCatagoryItem.get(position).getNameCatagory());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayListCatagoryItem.size();
    }
    public class CatagoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView imageView;
        public ItemClickListenner itemClickListenner;
        public CatagoryViewHolder(View itemView) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.catagory_item_image);
            this.textView=itemView.findViewById(R.id.catagory_item_name);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListenner(ItemClickListenner itemClickListenner) {
            this.itemClickListenner = itemClickListenner;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListenner.OnClick(v,getAdapterPosition());
        }
    }
}

