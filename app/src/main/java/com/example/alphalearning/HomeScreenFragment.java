package com.example.alphalearning;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class HomeScreenFragment extends Fragment {

    ArrayList<Array> arrayList = new ArrayList<Array>();
    Boolean instructor = false;

    public HomeScreenFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.courses);
        if(arrayList.isEmpty()){
            Fragment fragment = new NotFoundFragment();
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle args = new Bundle();
            args.putBoolean("instructor", instructor);
            fragment.setArguments(args);
            ft.replace(R.id.courses, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
//        ListView lv = (ListView) view.findViewById(R.id.listf);
        return view;
    }


}
