package com.example.alphalearning;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotFoundFragment extends Fragment {


    private FirebaseAuth auth;

    public NotFoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Boolean notFound = getArguments().getBoolean("instructor");

        View view = inflater.inflate(R.layout.fragment_not_found, container, false);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();

        Button button = view.findViewById(R.id.buttonCourses);
        if(notFound){
            button.setText("Create Course");
        }else{
            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notFound){
                    Intent intent = new Intent(getContext(), CreateCourse.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", user.getUid());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

}
