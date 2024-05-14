package com.example.tuktradecircle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tm.R;
import com.example.tuktradecircle.adapters.ActiveUserAdapter;
import com.example.tuktradecircle.adapters.CategoryAdapter;
import com.example.tuktradecircle.models.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private RecyclerView activeUsersRecyclerView;
    private List<CategoryModel> categoryModelList;
    private CategoryAdapter categoryAdapter;
    FirebaseFirestore db;
    private ActiveUserAdapter activeUserAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = root.findViewById(R.id.rec_category);
        activeUsersRecyclerView = root.findViewById(R.id.new_product_rec);
        db= FirebaseFirestore.getInstance();

        //category
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        categoryModelList= new ArrayList<>();
        categoryAdapter= new CategoryAdapter(getActivity(),categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel= document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                            }

                        } else {

                        }
                    }
                });


        return root;
    }


}