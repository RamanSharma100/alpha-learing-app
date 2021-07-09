package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddCourseImage extends AppCompatActivity {

    private String courseId,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getExtras().getString("userId");
        courseId = getIntent().getExtras().getString("courseId");

        setContentView(R.layout.activity_add_course_image);
    }
}
