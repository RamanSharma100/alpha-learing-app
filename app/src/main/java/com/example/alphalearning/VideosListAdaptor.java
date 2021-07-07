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
    private ArrayList<Videos> videosList;
    private  ViewGroup parent;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public VideosListAdaptor(){}


    public VideosListAdaptor(Context context,ArrayList<Videos> videosList) {
        this.videosList = videosList;
        this.context = context;
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



    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }




}
