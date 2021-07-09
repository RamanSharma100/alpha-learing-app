package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {

    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public static User userData;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final ProgressDialog progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setMessage("Fetching....");

        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        progressDialog.show();



        // home fragment


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.Screens, new HomeScreenFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();


        firestore  = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        floatingActionButton = findViewById(R.id.addCourseBtn);

        user = auth.getCurrentUser();

        firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userData = documentSnapshot.toObject(User.class);

                if(!userData.isInstructor()){
                    floatingActionButton.setVisibility(View.GONE);
                }

                progressDialog.dismiss();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, CreateCourse.class);
                startActivity(intent);

            }
        });



    }



}
