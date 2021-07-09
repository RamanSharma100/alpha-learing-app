package com.example.alphalearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EditCourseVideosListAdaptor extends RecyclerView.Adapter<EditCourseVideosListAdaptor.ViewHolder>{

    private List<Videos> videosList = new ArrayList<>();
    private List<String> videoIds = new ArrayList<>();
    private String courseId;
    private Context context;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    public EditCourseVideosListAdaptor() {
    }

    public EditCourseVideosListAdaptor(Context context,List<Videos> videosList, List<String> videoIds, String courseId) {
        this.videosList = videosList;
        this.videoIds = videoIds;
        this.courseId = courseId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_course_video_list, parent, false);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        return new EditCourseVideosListAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialButton delBtn;
        TextView videoCount, videoTitle;
        delBtn = holder.itemView.findViewById(R.id.editCourseDelVideo);
        videoTitle = holder.itemView.findViewById(R.id.editCourseVideoTitle);
        videoCount = holder.itemView.findViewById(R.id.editCourseVideoCount);

        String count  = position+1 + ".";
        videoCount.setText(count);
        videoTitle.setText(videosList.get(position).getVideoTitle());
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Deleting Video...");
                progressDialog.show();
                storage.getReferenceFromUrl(videosList.get(position).getVideoUrl()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firestore.collection("videos").document(videoIds.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firestore.collection("courses").document(courseId).update("videos", FieldValue.arrayRemove(videoIds.get(position))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        videosList.remove(position);
                                        videoIds.remove(position);
                                     Toast.makeText(context, "Video Deleted Successfully!", Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, e.getMessage()+"", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, e.getMessage()+"", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, e.getMessage()+"", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
