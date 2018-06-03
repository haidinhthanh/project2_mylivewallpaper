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
import com.example.haidt.mylivewallpaper.LiveImageView;
import com.example.haidt.mylivewallpaper.LoadImage.ImageLoader;
import com.example.haidt.mylivewallpaper.Model.LiveImage;
import com.example.haidt.mylivewallpaper.R;
import java.util.ArrayList;

public class ListViewAdapter  extends RecyclerView.Adapter<ListViewAdapter.LiveImageViewHolder>{
    Context context;
    ArrayList<LiveImage> listLiveImage;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;
    public ListViewAdapter(Context context,ArrayList<LiveImage> listLiveImage){
        this.context=context;
        this.listLiveImage=listLiveImage;
        this.layoutInflater=LayoutInflater.from(context);
        imageLoader= new ImageLoader(context);
    }
    @NonNull
    @Override
    public LiveImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.live_image_layout,parent,false);
        return new LiveImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveImageViewHolder holder, int position) {
        imageLoader.clearCache();
        ImageLoader.REQUIRED_SIZE=200;
        imageLoader.DisplayImage(listLiveImage.get(position).getUrlLinkImage(),holder.liveImgageBackground);
        holder.nameLiveImage.setText(listLiveImage.get(position).getNameLiveImage());
        holder.numOfSee.setText(String.valueOf(listLiveImage.get(position).getNumberOfSee()));
        holder.numOfDownload.setText(String.valueOf(listLiveImage.get(position).getNumberOfDowload()));
        holder.setItemClickListenner(new ItemClickListenner() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent= new Intent(context, LiveImageView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Comon.LiveImageLink,listLiveImage.get(position).getUrlGifImage());
                intent.putExtra(Comon.LiveImageID,listLiveImage.get(position).getLiveImageID());
                intent.putExtra(Comon.LiveImageName,listLiveImage.get(position).getNameLiveImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLiveImage.size();
    }

    public class LiveImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView liveImgageBackground;
        TextView nameLiveImage;
        TextView numOfSee;
        TextView numOfDownload;
        ItemClickListenner itemClickListenner;
        public LiveImageViewHolder(View itemView) {
            super(itemView);
            this.liveImgageBackground=itemView.findViewById(R.id.live_image_imageView);
            this.nameLiveImage=itemView.findViewById(R.id.name_image);
            this.numOfDownload=itemView.findViewById(R.id.num_Download);
            this.numOfSee=itemView.findViewById(R.id.num_See);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListenner(ItemClickListenner itemClickListenner) {
            this.itemClickListenner = itemClickListenner;
        }

        @Override
        public void onClick(View v) {
            itemClickListenner.OnClick(v,getAdapterPosition());
        }
    }
}
