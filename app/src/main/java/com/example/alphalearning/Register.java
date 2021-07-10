package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import io.grpc.Server;

public class Register extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Button loginBtn, signUpBtn;
    private TextInputEditText edtName,edtEmail,edtPass;
    private String name, email,password, joinedAs = "Student";
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}
        setContentView(R.layout.activity_register);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.login_screen);
        signUpBtn = findViewById(R.id.registerSignUpBtn);
        edtName = findViewById(R.id.registerName);
        edtEmail = findViewById(R.id.registerEmail);
        edtPass = findViewById(R.id.registerPassword);
        spinner = findViewById(R.id.registerJoinedAs);

        ArrayList<String> roles = getRoles();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Register.this, android.R.layout.simple_spinner_item, roles);

        //Set adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPass.getText().toString();
                joinedAs = spinner.getSelectedItem().toString();

                if(isEmpty(email,password,name)){
                    Toast.makeText(Register.this, "Pease fill in all fields!", Toast.LENGTH_LONG).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = auth.getCurrentUser();
                        boolean instructor = true;
                        if(joinedAs.equals("Student")){
                            instructor = false;
                        }
                        final User newUser = new User(name,email,user.getUid(),"","","","",instructor, Arrays.asList(),Arrays.asList(),Arrays.asList(),0,new Date());
                        firestore.collection("users").document(user.getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Register.this, "You are loggedIn successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, Dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });



    }

    private boolean isEmpty(String email, String password, String name){
        if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
            return true;
        }
        return false;
    }

    private ArrayList<String> getRoles()
    {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Student");
        roles.add("Instructor");
        return roles;
    }
}
