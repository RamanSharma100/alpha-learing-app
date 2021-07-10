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
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;



public class CoursesList extends Fragment {

    private List<Course> courses = new ArrayList<>();
    private List<String> courseIds = new ArrayList<>();
    private String userId;
    private boolean instructor;
    private RecyclerView recyclerView;

    public CoursesList() {
        // Required empty public constructor
    }

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
        courses = HomeScreenFragment.courses;
        courseIds = HomeScreenFragment.courseIds;

        View view = inflater.inflate(R.layout.fragment_courses_list, container, false);
        recyclerView = view.findViewById(R.id.yourCoursesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        CourseListAdaptor adapter = new CourseListAdaptor(getContext(),courses,userId,courseIds, "home");
        recyclerView.setAdapter(adapter);


        return view;
    }


}
