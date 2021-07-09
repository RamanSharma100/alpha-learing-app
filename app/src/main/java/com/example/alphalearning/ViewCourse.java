package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewCourse extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private List<Videos> videos = new ArrayList<>();
    private List<String> videoIds = new ArrayList<>();
    private RecyclerView videosListView;
    private VideoView videoView;
    private ProgressBar bufferingVideo;
    private String userId,courseId;
    private FirebaseFirestore firestore;
    private int stopPosition;
    private VideosListAdaptor adapter;
    private MaterialButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        progressDialog =  new ProgressDialog(ViewCourse.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();


        userId = getIntent().getExtras().getString("userId");
        courseId = getIntent().getExtras().getString("courseId");
        videoIds = HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId)).getVideos();

        firestore = FirebaseFirestore.getInstance();


        setContentView(R.layout.activity_view_course);

        videosListView = findViewById(R.id.videosList);
        backButton = findViewById(R.id.backBtnVideos);

        bufferingVideo = findViewById(R.id.bufferingVideo);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firestore.collection("videos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                    if(videoIds.indexOf(documentSnapshot.getId())  >= 0) {
                        Videos video = documentSnapshot.toObject(Videos.class);
                        videos.add(video);
                    }
                }
                videosListView.setLayoutManager(new LinearLayoutManager(ViewCourse.this, LinearLayoutManager.VERTICAL, false));
                adapter = new VideosListAdaptor(ViewCourse.this,videos,videoIds,userId);
                videosListView.setAdapter(adapter);

                videoView =findViewById(R.id.videoView);


                MediaController mediaController= new MediaController(ViewCourse.this);
                Uri uri = Uri.parse(videos.get(0).getVideoUrl());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();

                adapter.setPositon(0);

                progressDialog.dismiss();

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
        });






    }



    // calling in videosList adapter
    public void onClickCalled(View view, int position){
        Uri uri = Uri.parse(videos.get(position).getVideoUrl());
        videoView.stopPlayback();
        videoView.setVideoURI(uri);
        videoView.start();
        adapter.setPositon(position);
    }


    @Override
    public void onPause() {

        stopPosition = videoView.getCurrentPosition();
        videoView.pause();
        super.onPause();
    }
    @Override
    public void onResume() {

        if(stopPosition != 0){
            videoView.seekTo(stopPosition);
            videoView.start();
        }
        super.onResume();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


