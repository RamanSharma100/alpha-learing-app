package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

    }

    public void onClick(View view){

        switch(view.getId()){

            case R.id.button:
                Intent intent = new Intent(this,Dashboard.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                Intent newIntent = new Intent(this,ViewCourse.class);
                startActivity(newIntent);
                finish();
                break;
            default:
                break;
        }


    }
}
