package com.nganlth.freshfoodadmin.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nganlth.freshfoodadmin.Adapter.CategoryAdapter;
import com.nganlth.freshfoodadmin.BottomSheet.BottomSheetCategoryAdd;
import com.nganlth.freshfoodadmin.DAO.CategoryDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.R;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    // Biến toàn cục
    public static RecyclerView rcvCategory;
    ImageView imgAddcategory;
    CategoryDAO categoryDAO;
    ArrayList<Category> dataCategory;
    public static CategoryAdapter categoryAdapter;
    public static DatabaseReference mDataFood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);
        rcvCategory = view.findViewById(R.id.rcvCategory);
        imgAddcategory = view.findViewById(R.id.imgAddCategory);
        // -----------------
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        rcvCategory.setLayoutManager(layoutManager);
        rcvCategory.hasFixedSize();
        rcvCategory.setHasFixedSize(true);
        // -----------------

        categoryDAO = new CategoryDAO(getContext());
        mDataFood = FirebaseDatabase.getInstance().getReference("TheLoai");
        dataCategory = new ArrayList<Category>();
        dataCategory = categoryDAO.getAllCategory();

        imgAddcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetCategoryAdd categories_add = new BottomSheetCategoryAdd();
                categories_add.show(getFragmentManager(), categories_add.getTag());
            }
        });


        return view;
    }
}
