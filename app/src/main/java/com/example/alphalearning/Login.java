package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword;
    private String email,password;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}

        setContentView(R.layout.activity_login);

        editEmail =findViewById(R.id.Email);
        editPassword = findViewById(R.id.password);

        final Button loginBtn = findViewById(R.id.loginBtn);
        auth = FirebaseAuth.getInstance();




        loginBtn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();
                if(!isEmpty(email,password)){
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                user = auth.getCurrentUser();

                                Intent intent=  new Intent(Login.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Login.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Invalid Email Or Password!!", Toast.LENGTH_SHORT).show();
                            Log.i("Login",e.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(Login.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmpty(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            return true;
        }
        return false;
    }
}
