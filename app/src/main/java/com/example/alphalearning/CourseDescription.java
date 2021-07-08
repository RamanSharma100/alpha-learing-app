package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        progressDialog = new ProgressDialog(CourseDescription.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();

        setContentView(R.layout.activity_course_description);

        courseId = getIntent().getExtras().getString("courseId");
        userId = getIntent().getExtras().getString("userId");

        course = HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId));



        courseImage = findViewById(R.id.courseImageDesc);
        courseName = findViewById(R.id.headingCourseDesc);
        courseDesc = findViewById(R.id.descCourseDesc);
        viewCourse = findViewById(R.id.viewCourseDesc);
        editCourse = findViewById(R.id.btnCourseDesc);
        goBack = findViewById(R.id.backBtnCourseDesc);

        Bitmap bitmapImg = getImageBitmap(course.getThumbnail());

        if(bitmapImg != null)
            courseImage.setImageBitmap(bitmapImg);

        progressDialog.dismiss();

        courseName.setText(course.getName());
        courseDesc.setText(course.getDescription());

        if(!userId.equals(course.getCreatedBy())){
            editCourse.setText("Un-Enroll Course");
        }



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });







    }

    private Bitmap getImageBitmap(String src) {
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
