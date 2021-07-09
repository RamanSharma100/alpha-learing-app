package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private MaterialButton imgDelBtn;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        setContentView(R.layout.activity_edit_course);



        firestore = FirebaseFirestore.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(EditCourse.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();

        imageView = findViewById(R.id.editCourseImage);
        imgDelBtn = findViewById(R.id.editCourseImageUpdateBtn);
        recyclerView = findViewById(R.id.editCourseVideoLists);
        textView = findViewById(R.id.editCourseNoVideosText);

        courseId = getIntent().getExtras().getString("courseId");
        userId = getIntent().getExtras().getString("userId");
        course = HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId));


        firestore.collection("videos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                }

                Bitmap imgBitmap = CourseDescription.getImageBitmap(course.getThumbnail());
                imageView.setImageBitmap(imgBitmap);

                progressDialog.dismiss();
            }
        });


        imgDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCourse.this, AddCourseImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseId", courseId);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });









    }
}
