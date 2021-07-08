package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button next;
    private String category = "Web Developement", courseName, courseDecription;
    private EditText editName, editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        getSupportActionBar().setTitle("CREATE YOUR COURSE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        next=findViewById(R.id.btnnext);

        editName = findViewById(R.id.courseName);
        editDescription = findViewById(R.id.courseDescription);

        Spinner spinner=findViewById(R.id.coursecategory);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.Categories, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName = editName.getText().toString();
                courseDecription = editDescription.getText().toString();

                if(isEmpty(courseName, courseDecription, category)){
                    Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_LONG).show();
                    Log.e("hello", "please fill in all fields!");
                    return;
                };

                // creating object for course

                final Course course = new Course(category,user.getUid(), courseName, "", Arrays.asList(), Arrays.asList(), 0, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), courseDecription);
                Log.e("hello", course.getCreatedAt().toString());

//                firestore.collection("courses").add().


//                startActivity(new Intent(CreateCourse.this,AddCourseImage.class));
//                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      category =parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isEmpty(String name, String desc, String cate){
        if(name.isEmpty() || desc.isEmpty() || cate.isEmpty() || name.equals("") || desc.equals("") || cate.equals("")){
            return true;
        }
        return false;
    }
}
