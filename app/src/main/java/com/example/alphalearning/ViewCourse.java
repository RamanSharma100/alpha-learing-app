package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewCourse extends AppCompatActivity {

    ArrayList<Videos> videos;
    RecyclerView videosListView;
    VideoView videoView;
    ProgressBar bufferingVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}
        setContentView(R.layout.activity_view_course);

        videosListView = findViewById(R.id.videosList);

        bufferingVideo = findViewById(R.id.bufferingVideo);


        videosListView.setLayoutManager(new LinearLayoutManager(ViewCourse.this, LinearLayoutManager.VERTICAL, false));
        VideosListAdaptor adapter = new VideosListAdaptor(ViewCourse.this,videos);
        videosListView.setAdapter(adapter);

        videoView =findViewById(R.id.videoView);


        MediaController mediaController= new MediaController(this);
        Uri uri = Uri.parse(videos.get(0).getVideoUrl());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        bufferingVideo.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        bufferingVideo.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        bufferingVideo.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });
    }



    // calling in videosList adapter
    public void onClickCalled(View view, int position){
        Uri uri = Uri.parse(videos.get(position).getVideoUrl());
        videoView.stopPlayback();
        videoView.setVideoURI(uri);
        videoView.start();
        ImageView image = view.findViewById(R.id.playCircleBtn);
        image.setImageResource(R.drawable.icons8_circle_24);
    }


    @Override
    protected void onResume() {
        if (videoView != null)
            videoView.resume();  // <-- this will cause re-buffer.
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (videoView != null)
            videoView.suspend(); // <-- this will cause clear buffer.
        super.onPause();
    }

}


