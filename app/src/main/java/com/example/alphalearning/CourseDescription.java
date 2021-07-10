package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CourseDescription extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String courseId, userId;
    private Course course;
    private ImageView courseImage;
    private TextView courseName, courseDesc;
    private MaterialButton viewCourse, editCourse, goBack ;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(CourseDescription.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();

        setContentView(R.layout.activity_course_description);

        courseId = getIntent().getExtras().getString("courseId");
        userId = getIntent().getExtras().getString("userId");
        courseImage = findViewById(R.id.courseImageDesc);
        courseName = findViewById(R.id.headingCourseDesc);
        courseDesc = findViewById(R.id.descCourseDesc);
        viewCourse = findViewById(R.id.viewCourseDesc);
        editCourse = findViewById(R.id.btnCourseDesc);
        goBack = findViewById(R.id.backBtnCourseDesc);


        firestore.collection("courses").document(courseId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if(documentSnapshot != null)
                    course = documentSnapshot.toObject(Course.class);

                Log.e("courseDes", documentSnapshot.getId()+" "+ courseId);

                if(course.getThumbnail() == null ||course.getThumbnail().equals("")){
                    courseImage.setImageResource(R.drawable.no_image_found);
                }else{
                    Bitmap bitmapImg = getImageBitmap(course.getThumbnail());

                    if(bitmapImg != null)
                        courseImage.setImageBitmap(bitmapImg);
                }



                progressDialog.dismiss();

                courseName.setText(course.getName());
                courseDesc.setText(course.getDescription());

                if(!userId.equals(course.getCreatedBy())){
                    editCourse.setText("Un-Enroll Course");
                }
                if(Dashboard.userData.getEnrolledCourses().indexOf(courseId) == -1){
                    editCourse.setVisibility(View.GONE);
                }

                if(Dashboard.userData.getEnrolledCourses().indexOf(courseId) == -1 && !userId.equals(course.getCreatedBy())){
                    viewCourse.setText("Enroll Course");
                }





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseDescription.this, "fail!", Toast.LENGTH_SHORT).show();
            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userId.equals(course.getCreatedBy())){
                    firestore.collection("courses").document(courseId).update("enrolledBy", FieldValue.arrayRemove(userId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            firestore.collection("users").document(userId).update("enrolledCourses", FieldValue.arrayRemove(courseId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CourseDescription.this, "Course Un enrolled Successfully!!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });

                        }
                    });
                }else{
                        Intent intent = new Intent(CourseDescription.this, EditCourse.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("courseId", courseId);
                        bundle.putString("userId", userId);
                        intent.putExtras(bundle);
                        startActivity(intent);


                }
            }
        });

        viewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Dashboard.userData.getEnrolledCourses().indexOf(courseId) == -1 && !userId.equals(course.getCreatedBy())){
                    firestore.collection("courses").document(courseId).update("enrolledBy", FieldValue.arrayUnion(userId), "students", course.getStudents() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firestore.collection("users").document(userId).update("enrolledCourses", FieldValue.arrayUnion(courseId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CourseDescription.this, "Thanks for enrolling the course!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CourseDescription.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CourseDescription.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    if(course.getVideos().size() > 0) {
                        Intent intent = new Intent(CourseDescription.this, ViewCourse.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", userId);
                        bundle.putString("courseId", courseId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(CourseDescription.this, "This course is not having any videos", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });









    }

    public static Bitmap getImageBitmap(String src) {
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(IOException e) {
            Log.e("course", e.getMessage());
            return null;
        }

    }
}
