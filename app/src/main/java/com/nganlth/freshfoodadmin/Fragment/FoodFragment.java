package com.nganlth.freshfoodadmin.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nganlth.freshfoodadmin.Adapter.FoodAdapter;
import com.nganlth.freshfoodadmin.BottomSheet.BottomSheetFoodAdd;
import com.nganlth.freshfoodadmin.DAO.FoodDAO;
import com.nganlth.freshfoodadmin.Model.Food;
import com.nganlth.freshfoodadmin.R;

import java.util.ArrayList;

public class FoodFragment extends Fragment {

    public static RecyclerView rcvFood;
    FoodDAO foodDAO;
    DatabaseReference mDataFood;
    public static FoodAdapter foodsAdapter;
    ArrayList<Food> dataFood;
    ImageView ivAddFood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food,container,false);

        ivAddFood = view.findViewById(R.id.imgAddFood);
        mDataFood = FirebaseDatabase.getInstance().getReference("Food");
        rcvFood = view.findViewById(R.id.rcvFood);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvFood.setLayoutManager(layoutManager);
        rcvFood.hasFixedSize();
        rcvFood.setHasFixedSize(false);

        foodDAO = new FoodDAO(getContext());
        dataFood = new ArrayList<Food>();
        dataFood = foodDAO.getAllFood();

        foodsAdapter = new FoodAdapter(getContext(),dataFood);
        rcvFood.setAdapter(foodsAdapter);
        foodsAdapter.notifyDataSetChanged();

        ivAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFoodAdd bottomSheetFoodAdd = new BottomSheetFoodAdd();
                bottomSheetFoodAdd.show(getFragmentManager(),bottomSheetFoodAdd.getTag());

            }
        });

        return view;
    }
}
