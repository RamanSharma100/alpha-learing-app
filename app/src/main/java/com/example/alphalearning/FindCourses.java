package com.example.alphalearning;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class FindCourses extends Fragment {

    private List<Course> courses = new ArrayList<>();
    private java.util.List<String> courseIds = new ArrayList<>();
    private String userId;
    private boolean instructor;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;

    public FindCourses(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        instructor = getArguments().getBoolean("instructor");
        userId = getArguments().getString("userId");

        View view = inflater.inflate(R.layout.fragment_find_courses, container, false);
        recyclerView = view.findViewById(R.id.FindCoursesRecyclerView);
        progressBar = view.findViewById(R.id.findCoursesProgress);
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("courses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot document:  queryDocumentSnapshots){
                        Course course = document.toObject(Course.class);
                        courses.add(course);
                        courseIds.add(document.getId());
                    }

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    CourseListAdaptor adapter = new CourseListAdaptor(getContext(),courses,userId,courseIds, "findCourses");
                    recyclerView.setAdapter(adapter);
            }
        });




        return view;
    }



}
