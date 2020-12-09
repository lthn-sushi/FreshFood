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
import com.nganlth.freshfoodadmin.Adapter.CategoryAdapter;
import com.nganlth.freshfoodadmin.Model.Category;

import java.util.ArrayList;

import static com.nganlth.freshfoodadmin.Fragment.CategoryFragment.rcvCategory;

public class CategoryDAO {
    // Write a message to the database
    DatabaseReference mData;
    private Context context;
    String key;
    CategoryAdapter adapter;


    public CategoryDAO( Context context) {
        //Tạo bảng thể loại
        this.mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        this.context = context;
    }
    // Read all list Category
    public ArrayList<Category> getAllCategory(){
        final ArrayList<Category> dataCategory = new ArrayList<Category>();
        // Hàm đọc dữ liệu liên tục, add vào data
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataCategory.clear(); // Xóa data cũ đi trước khi lấy value
                // Đếm all children trong bảng
                for (DataSnapshot data:snapshot.getChildren()){
                    // Gọi model ra getValue để lấy value và add vào array
                    Category item = data.getValue(Category.class);
                    dataCategory.add(item);

                    adapter = new CategoryAdapter(context,dataCategory);
                    rcvCategory.setAdapter(adapter);

                }

                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mData.addValueEventListener(listener);
        return dataCategory;
    }

    // Thêm loại
    public void insert(Category category){
        //push mã vào biến key
        key = mData.push().getKey();

        mData.child(key).setValue(category)
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
    // Chỉnh sửa loại

    public void update(Category category){
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){

                    if (data.child("maLoai").getValue(String.class).equals(category.getMaLoai())){
                        key = data.getKey();
                        mData.child(key).setValue(category)
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    public void delete(final String maLoai){
//        mData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot data: snapshot.getChildren()){
//                    if (data.child("maLoai").getValue(String.class).equalsIgnoreCase(maLoai)){
//
//                        // data not mData
//                        key = data.getKey();
//                        // Kiểm tra xem đã lấy đúng mã chưa
//                        Log.d("getKey", "onCreate: key :" + key);
//
//                        mData.child(key).removeValue()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                    }
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    public void delete(final String maLoai) {

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.child("maLoai").getValue(String.class).equalsIgnoreCase(maLoai)){
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
