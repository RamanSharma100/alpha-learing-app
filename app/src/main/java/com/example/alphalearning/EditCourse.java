package com.example.alphalearning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditCourse extends AppCompatActivity {


    private Course course;
    private String courseId;
    private String userId;
    private List<Videos> videosList = new ArrayList<>();
    private List<String> videoIds = new ArrayList<>();
    private ImageView imageView;
    private MaterialButton imgDelBtn, addVideoBtn;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private TextView textView;
    private EditCourseVideosListAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}
        setContentView(R.layout.activity_edit_course);



        firestore = FirebaseFirestore.getInstance();


        imageView = findViewById(R.id.editCourseImage);
        imgDelBtn = findViewById(R.id.editCourseImageUpdateBtn);
        recyclerView = findViewById(R.id.editCourseVideoLists);
        textView = findViewById(R.id.editCourseNoVideosText);
        addVideoBtn = findViewById(R.id.editCourseAddVideo);

        courseId = getIntent().getExtras().getString("courseId");
        userId = getIntent().getExtras().getString("userId");
        course = HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId));


        fetchData();



        imgDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCourse.this, AddCourseImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseId", courseId);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


        addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCourse.this, AddCourseVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseId", courseId);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });






    }

    private void fetchData(){
        final ProgressDialog progressDialog = new ProgressDialog(EditCourse.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();
        firestore.collection("videos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                videosList.clear();
                videoIds.clear();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                    Log.e("hello", course.getVideos().indexOf(documentSnapshot.getId())+"");
                    if(course.getVideos().indexOf(documentSnapshot.getId())  >= 0) {
                        Videos video = documentSnapshot.toObject(Videos.class);
                        videosList.add(video);
                        videoIds.add(documentSnapshot.getId());
                    }
                }

                if(videosList.isEmpty()){
                    textView.setText("No videos found");
                    recyclerView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(EditCourse.this, LinearLayoutManager.VERTICAL, false));
                    adapter = new EditCourseVideosListAdaptor(EditCourse.this,videosList,videoIds,courseId);
                    recyclerView.setAdapter(adapter);
                }

                if(course.getThumbnail().equals("")){
                    imageView.setImageResource(R.drawable.no_image_found);
                }else{
                    Bitmap imgBitmap = CourseDescription.getImageBitmap(course.getThumbnail());
                    imageView.setImageBitmap(imgBitmap);
                }



                progressDialog.dismiss();

            }
        });
    }


}
