package com.example.alphalearning;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HomeScreenFragment extends Fragment {


    public static List<Course> courses = new ArrayList<>();
    public static List<String> courseIds = new ArrayList<>();
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private User userData;
    private TextView greetUser;
    private ProgressBar progressBar;
    private View view;




    public HomeScreenFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        progressBar = view.findViewById(R.id.yourCoursesProgress);


//        getting user document from FireBase

        firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot != null){
                    userData = documentSnapshot.toObject(User.class);
                    greetUser = view.findViewById(R.id.greetUser);
                    String userGreetText = "Hello, "+ userData.getName() ;
                    greetUser.setText(userGreetText);
                    firestore.collection("courses").whereEqualTo("createdBy", userData.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            courses.clear();
                            courseIds.clear();

                            for(DocumentSnapshot document : queryDocumentSnapshots){
                                Course course = document.toObject(Course.class);
                                courses.add(course);
                                courseIds.add(document.getId());
                            }
                            firestore.collection("courses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot documentSnapshots) {
                                    for(DocumentSnapshot course: documentSnapshots){
                                        if(userData.getEnrolledCourses().indexOf(course.getId()) >= 0){
                                            Course course2 = documentSnapshot.toObject(Course.class);
                                            courses.add(course2);
                                            courseIds.add(documentSnapshot.getId());
                                        }
                                    }
                                    if(courses.isEmpty()){
                                        Fragment fragment = new NotFoundFragment();
                                        Bundle arguments = new Bundle();
                                        arguments.putBoolean( "instructor" , userData.isInstructor());
                                        fragment.setArguments(arguments);
                                        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                        ft.replace(R.id.courses, fragment);
                                        ft.commit();
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Fragment fragment = new CoursesList();
                                        Bundle arguments = new Bundle();
                                        arguments.putBoolean( "instructor" , userData.isInstructor());
                                        arguments.putString("userId", userData.getUid());
                                        fragment.setArguments(arguments);
                                        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                        ft.replace(R.id.courses, fragment);
                                        ft.commit();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "no courses found enrolled by you", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("data",e.getMessage());
            }
        });





//        ListView lv = (ListView) view.findViewById(R.id.listf);
        return view;
    }


}
