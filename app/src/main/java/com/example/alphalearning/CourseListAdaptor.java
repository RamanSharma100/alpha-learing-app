package com.example.alphalearning;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseListAdaptor extends RecyclerView.Adapter<CourseListAdaptor.ViewHolder> {


    private Context context;
    private List<Course> coursesList = new ArrayList<>();
    private List<String> courseIds =  new ArrayList<>();
    private  ViewGroup parent;
    private String userId, fragmentName;
    private FirebaseFirestore firestore;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public CourseListAdaptor(){}



    public CourseListAdaptor(Context context, List<Course> coursesList, String userId, List<String> courseIds, String fragmentName) {
        this.coursesList = coursesList;
        this.context = context;
        this.userId = userId;
        this.courseIds = courseIds;
        this.fragmentName = fragmentName;
    }

    private Bitmap getImageBitmap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list_view, parent, false);

        firestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TextView title, courseCategory;
        ImageView courseImage;
        Button courseBtn;
        LinearLayout layout;

        title = holder.itemView.findViewById(R.id.courseName);
        courseImage = holder.itemView.findViewById(R.id.courseImage);
        courseBtn = holder.itemView.findViewById(R.id.courseBtn);
        courseCategory = holder.itemView.findViewById(R.id.courseCategory);
        layout = holder.itemView.findViewById(R.id.layoutCourseList);



        title.setText(coursesList.get(position).getName());
        courseCategory.setText(coursesList.get(position).getCategory());


        if(!fragmentName.equals("home")){
            layout.setVisibility(View.GONE);
        }

        if(userId.equals(coursesList.get(position).getCreatedBy())){
            courseBtn.setText("Edit Course");
        }else{
            courseBtn.setVisibility(View.GONE);
        }


        if(coursesList.get(position).getThumbnail() == null || coursesList.get(position).getThumbnail().equals("")){
            courseImage.setImageResource(R.drawable.no_image_found);
        }else{
            Bitmap bitmapImg = this.getImageBitmap(coursesList.get(position).getThumbnail());

            courseImage.setImageBitmap(bitmapImg);
        }



        courseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("course", courseIds.get(position));
                Intent intent = new Intent(parent.getContext(), CourseDescription.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseId", courseIds.get(position));
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

        courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.equals(coursesList.get(position).getCreatedBy())){
                    Intent intent = new Intent(parent.getContext(), EditCourse.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("courseId", courseIds.get(position));
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    parent.getContext().startActivity(intent);
                }else{
                    Toast.makeText(context, "View Course", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }




}
