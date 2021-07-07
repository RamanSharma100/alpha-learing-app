package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN =4000 ;

    //----------------------------------------------------------------------Variable----------------------------------------------------------------------//
    private Animation up_anim,down_anim;
    private ImageView logo;
    private TextView appname,tagline;
    private FirebaseUser user;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----------------------------------------------------------------------Remove ActionBar----------------------------------------------------------------------//


        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e){}
        setContentView(R.layout.activity_main);
        //----------------------------------------------------------------------Declaration----------------------------------------------------------------------//

        logo=findViewById(R.id.imageView);
        appname=findViewById(R.id.appName);
        tagline=findViewById(R.id.tagLine);
        auth = FirebaseAuth.getInstance();

        //----------------------------------------------------------------------Animation----------------------------------------------------------------------//


        up_anim= AnimationUtils.loadAnimation(this,R.anim.upanim);
        down_anim=AnimationUtils.loadAnimation(this,R.anim.bottomanim);

        logo.setAnimation(up_anim);
        appname.setAnimation(down_anim);
        tagline.setAnimation(down_anim);

        //----------------------------------------------------------------------Splash Screen Delayed----------------------------------------------------------------------//

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user = auth.getCurrentUser();
                Intent intent=new Intent(MainActivity.this,Login.class);

                if(user != null){
                    intent = new Intent(MainActivity.this, Dashboard.class);

                }

                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(logo,"logo_image");
                pairs[1]=new Pair<View,String>(appname,"logo_text");

                if(android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                        startActivity(intent, options.toBundle());

                }
            }
        },SPLASH_SCREEN);



    }
}