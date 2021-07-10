package com.example.alphalearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Date;
import java.util.Arrays;
import java.util.UUID;

public class AddCourseVideo extends AppCompatActivity {

    private EditText title;
    private VideoView videoView;
    private Button uploadvideobtn;
    private Button browsevideobtn;
    private static final int VIDEO_PICK_FILEMANGER_CODE=100;
    private static final int VIDEO_PICK_CAMERA_CODE=101;
    private static final int CAMERA_REQUEST_CODE=102;
    private String courseId, userId, videoTitle;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;


    private String[] cameraPermissions;


    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_video);

        getSupportActionBar().setTitle("UPLOAD VIDEO TO THE COURSE");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        title=findViewById(R.id.addVideoEdtTxt);
        videoView=findViewById(R.id.addVideoView);
        uploadvideobtn=findViewById(R.id.addVideoUploadBtn);
        browsevideobtn=findViewById(R.id.addVideoBrowseBtn);

        courseId = getIntent().getExtras().getString("courseId");
        userId = getIntent().getExtras().getString("userId");

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


        uploadvideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoTitle = title.getText().toString();

                if(videoTitle.equals("")){
                    Toast.makeText(AddCourseVideo.this, "Please add video title!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(videoUri == null){
                    Toast.makeText(AddCourseVideo.this, "Please select video!!", Toast.LENGTH_LONG).show();
                    return;
                }


                ProgressDialog progressDialog
                        = new ProgressDialog(AddCourseVideo.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference.child(
                        "videos/"
                                + courseId + "/"+ videoTitle + UUID.randomUUID().toString());

                // adding listeners on upload
                // or failure of image
                ref.putFile(videoUri)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        // Image uploaded successfully
                                        // Dismiss dialog

                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final Videos video = new Videos(uri.toString(),videoTitle, courseId, userId, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

                                                firestore.collection("videos").add(video).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        firestore.collection("courses").document(courseId).update("videos", FieldValue.arrayUnion(documentReference.getId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();

                                                                Toast
                                                                        .makeText(AddCourseVideo.this,
                                                                                "Video Uploaded!!",
                                                                                Toast.LENGTH_SHORT)
                                                                        .show();
                                                                HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId)).getVideos().add(documentReference.getId());
                                                                Intent intent = new Intent(AddCourseVideo.this, EditCourse.class);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("courseId", courseId);
                                                                bundle.putString("userId", userId);
                                                                intent.putExtras(bundle);
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast
                                                                .makeText(AddCourseVideo.this,
                                                                        "Failed " + e.getMessage(),
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                });





                                            }
                                        });



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(AddCourseVideo.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int)progress + "%");
                                    }
                                });
            }
        });

        browsevideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoPickdialog();
            }
        });

    }

    private void videoPickdialog() {

        String[]options={"Camera","Filemanager"};


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0) {

                            if (!checkcamerapermission()) {
                                requestCameraPermissions();

                            }else{
                                videopickcamera();
                            }
                        } else if (i == 1) {

                            videopickFilemanager();
                        }
                    }
                }).show();
    }
    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private boolean checkcamerapermission(){
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean result2= ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK)== PackageManager.PERMISSION_GRANTED;


        return result1 && result2;
    }


    private void videopickFilemanager(){

        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Videos"),VIDEO_PICK_FILEMANGER_CODE);
    }
    private void videopickcamera(){
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }
    private void setVideotovideoview(){
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);


        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.pause();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        videopickcamera();
                    }
                    else{
                        Toast.makeText(this, "camera & storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK){
            if(requestCode==VIDEO_PICK_FILEMANGER_CODE){
                videoUri=data.getData();

                setVideotovideoview();
            }
            else if(requestCode==VIDEO_PICK_CAMERA_CODE){
                videoUri=data.getData();
                setVideotovideoview();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
