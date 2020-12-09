package com.nganlth.freshfoodadmin.DAO;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nganlth.freshfoodadmin.Model.Admin;

public class AdminDAO {
    DatabaseReference mData;
    String key;
    Context context;

    public AdminDAO(Context context) {
        this.mData = FirebaseDatabase.getInstance().getReference("Admin");
        this.context = context;
    }
    public void update (final Admin admin){
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Đọc dữ liệu trong database
                for (DataSnapshot data: snapshot.getChildren()){
                    // Kiểm tra cột email có trùng với email đăng nhập hay không
                    if (data.child("email").getValue(String.class).equals(admin.getEmail())){
                        // Mã
                        key = data.getKey();
                        // Set dữ liệu đã chỉnh sửa
                        mData.child(key).setValue(admin)
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
}
