package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
                    Toast.makeText(CreateCourse.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                };





                startActivity(new Intent(CreateCourse.this,AddCourseImage.class));
                finish();
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
        if(name.isEmpty() || desc.isEmpty() || cate.isEmpty()){
            return true;
        }
        return false;
    }
}
