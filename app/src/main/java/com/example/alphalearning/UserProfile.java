package com.example.alphalearning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {


    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private User userData;
    private MaterialButton button;
    private TextView nameView, emailView, instructorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}
        setContentView(R.layout.activity_user_profile);
        final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        nameView = findViewById(R.id.profileName);
        emailView = findViewById(R.id.profileEmail);
        instructorView = findViewById(R.id.profileJoinedAs);
        button = findViewById(R.id.profileLogoutBtn);

        userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userData = documentSnapshot.toObject(User.class);

                nameView.setText(userData.getName());
                emailView.setText(userData.getEmail());

                if(userData.isInstructor()){
                    instructorView.setText("Instructor");
                }else{
                    instructorView.setText("Student");
                }


                progressDialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfile.this, "You are Logged Out!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

}
