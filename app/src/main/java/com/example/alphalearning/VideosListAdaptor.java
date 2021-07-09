package com.example.alphalearning;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VideosListAdaptor extends RecyclerView.Adapter<VideosListAdaptor.ViewHolder> {


    private Context context;
    private List<Videos> videosList = new ArrayList<>();
    private List<String> videoIds = new ArrayList<>();
    private  ViewGroup parent;
    private String userId;
    private int positionItem;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public VideosListAdaptor(){}


    public void setPositon(int position){
        this.positionItem = position;
    }

    public VideosListAdaptor(Context context,List<Videos> videosList, List<String> videoIds, String userId ) {
        this.videosList = videosList;
        this.context = context;
        this.videoIds = videoIds;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_list_view, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TextView numberCount, title;

        numberCount = holder.itemView.findViewById(R.id.titleCount);
        title = holder.itemView.findViewById(R.id.titleTile);

        title.setText(videosList.get(position).getVideoTitle());

        String positionString = (position+1+"");
        numberCount.setText(positionString);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewCourse) v.getContext()).onClickCalled(v,position);
            }
        });


        if(positionItem == position){
            ((ImageView) holder.itemView.findViewById(R.id.playCircleBtn) ).setImageResource(R.drawable.icons8_circle_24);
        }else{
            ((ImageView) holder.itemView.findViewById(R.id.playCircleBtn) ).setImageResource(R.drawable.icons8_circled_play_24);
        }



    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }




}
