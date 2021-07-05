package com.example.alphalearning;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class HeaderFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_header, container, false);
    }


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.profileBtn :
                Toast.makeText(getActivity(), "profile", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
