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
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddCourseImage extends AppCompatActivity {

    private Button browseimage, updateimage;
    private String courseId,userId;
    private Course course;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ImageView imageView;
    private static final int IMAGE_PICK_FILEMANGER_CODE=100;
    private String[] permissioncam;


    private Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProgressDialog progressDialog= new ProgressDialog(AddCourseImage.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        userId = getIntent().getExtras().getString("userId");
        courseId = getIntent().getExtras().getString("courseId");
        course = HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId));
        getSupportActionBar().setTitle("Edit Course Image");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        setContentView(R.layout.activity_add_course_image);

        imageView = findViewById(R.id.addCourseImage);
        browseimage = findViewById(R.id.browseCourseImage);
        updateimage  = findViewById(R.id.uploadCourseImage);


        if(course.getThumbnail().equals("")){
            imageView.setImageResource(R.drawable.no_image_found);
        }else{
            Bitmap imgBitmap = CourseDescription.getImageBitmap(course.getThumbnail());
            imageView.setImageBitmap(imgBitmap);
        }

        progressDialog.dismiss();

        browseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        updateimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });

    }

    private void chooseimage(){

        String[]options={"Filemanager"};
        AlertDialog.Builder builder=new AlertDialog.Builder(AddCourseImage.this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0) {

                            imagepickFilemanager();
                        }
                    }
                }).show();


    }
    private void imagepickFilemanager(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),IMAGE_PICK_FILEMANGER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_FILEMANGER_CODE){
                imageuri=data.getData();
                setimagetoimageview();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    private void setimagetoimageview(){
        try {

            // Setting image on image view using Bitmap
            Bitmap bitmap = MediaStore
                    .Images
                    .Media
                    .getBitmap(
                            getContentResolver(),
                            imageuri);
            imageView.setImageBitmap(bitmap);
        }

        catch (IOException e) {
            // Log the exception
            e.printStackTrace();
        }
    }

    private void uploadimage()
    {
        if (imageuri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference.child(
                            "courses/"
                                    + courseId);

            // adding listeners on upload
            // or failure of image
            ref.putFile(imageuri)
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
                                            firestore.collection("courses").document(courseId).update("thumbnail", uri.toString() ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();

                                                    Toast
                                                            .makeText(AddCourseImage.this,
                                                                    "Image Uploaded!!",
                                                                    Toast.LENGTH_SHORT)
                                                            .show();
                                                    HomeScreenFragment.courses.get(HomeScreenFragment.courseIds.indexOf(courseId)).setThumbnail(uri.toString());

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
                                    .makeText(AddCourseImage.this,
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
    }
}
