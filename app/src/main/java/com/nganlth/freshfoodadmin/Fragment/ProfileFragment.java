package com.nganlth.freshfoodadmin.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nganlth.freshfoodadmin.Model.Admin;
import com.nganlth.freshfoodadmin.R;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    TextView tvFullName, tvEmail, tvEmailAddress, tvPhoneNumber, tvAddress;
    Button btnEditProfile, btnChangePassword;
    DatabaseReference mData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        reference(view);
        // Lấy dữ liệu từ database
        mData = FirebaseDatabase.getInstance().getReference("Admin");
        SharedPreferences preferences = ((AppCompatActivity)getActivity()).getSharedPreferences("thongtin.dat",MODE_PRIVATE);
        String strEmail = preferences.getString("Email","");
        String strPass = preferences.getString("Password","");

        // Lấy dữ liệu show ra profile
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Đếm all children trong bảng
                for (DataSnapshot data: snapshot.getChildren()){
                    // Lấy mã == sdt
                    String phone = data.getKey();
                    // Gọi model ra getValue để lấy value và add vào array
                    Admin admin =   data.getValue(Admin.class);
                    String email = admin.getEmail();
                    String password = admin.getPassword();
                    String name = admin.getName();

                    // Nếu email trong bảng admin trùng với email đăng nhập (strEmail) --> Lấy dữ liệu của tài khoản đang login
                    if (email.equals(strEmail)){
                        tvFullName.setText(name);
                        tvEmail.setText(email);
                        tvEmailAddress.setText(email);
                        tvPhoneNumber.setText("+84 - "+phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //------------------------------------------------------

        return view;
    }

    public void reference(View view){
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvEmailAddress = view.findViewById(R.id.tvEmailAddress);
        tvAddress = view.findViewById(R.id.tvAddress);
    }

}
