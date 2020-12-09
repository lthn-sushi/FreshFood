package com.nganlth.freshfoodadmin.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nganlth.freshfoodadmin.Fragment.FoodFragment;
import com.nganlth.freshfoodadmin.Model.Food;

import java.util.ArrayList;
import java.util.Iterator;

import static com.nganlth.freshfoodadmin.Fragment.FoodFragment.foodsAdapter;

public class FoodDAO {
    DatabaseReference mData;
    String key;
    private Context context;
    FoodFragment foodFragment;
    public FoodDAO(Context context){
        this.mData = FirebaseDatabase.getInstance().getReference("Food");
        this.context = context;

    }

    public FoodDAO(Context context, FoodFragment foodFragment){
        this.mData = FirebaseDatabase.getInstance().getReference("Food");
        this.context = context;
        this.foodFragment = foodFragment;

    }
    // Read all
    public ArrayList<Food> getAllFood(){
        final ArrayList<Food> list = new ArrayList<Food>();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                if (snapshot.exists()){
                    list.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()){
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        Food food = next.getValue(Food.class);
                        list.add(food);
                    }
                    foodsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//            mData.addValueEventListener(listener);
        return list;
    }

    public void insert(Food item) {
        key = mData.push().getKey();
        mData.child(key).setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public void update(final Food item) {

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()) {

                    if (data.child("food_id").getValue(String.class).equals(item.getFood_id())){

                        key = data.getKey();
                        mData.child(key).setValue(item);
                        foodsAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        foodsAdapter.notifyDataSetChanged();
    }
    public void delete(final String foodId) {

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.child("food_id").getValue(String.class).equalsIgnoreCase(foodId)){
                        key = data.getKey();

                        Log.d("getKey", "onCreate: key :" + key);


                        mData.child(key).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
