package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView textView;
    private FirebaseFirestore firestore;

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

        textView = findViewById(R.id.textEditCourse);


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
                }else{
                    String text = ""+videosList.size();
                    textView.setText(text);
                }

                progressDialog.dismiss();
            }
        });









    }
}
