package com.example.alphalearning;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

public class NotFoundFragment extends Fragment {



    public NotFoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Boolean notFound = getArguments().getBoolean("instructor");

        View view = inflater.inflate(R.layout.fragment_not_found, container, false);

        Button button = view.findViewById(R.id.buttonCourses);
        if(notFound){
            button.setText("Create Course");
        }else{
            button.setText("Find Course");
        }

        return view;
    }

}
