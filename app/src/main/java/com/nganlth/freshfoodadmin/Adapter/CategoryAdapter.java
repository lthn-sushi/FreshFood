package com.nganlth.freshfoodadmin.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nganlth.freshfoodadmin.BottomSheet.BottomSheetCategoryEdit;
import com.nganlth.freshfoodadmin.DAO.CategoryDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Category> dataCategory;
    //Lấy ảnh
    FirebaseStorage firebaseStorage;
    CategoryDAO categoryDAO;

    public CategoryAdapter(Context context, ArrayList<Category> dataCategory) {
        this.context = context;
        this.dataCategory = dataCategory;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_category,parent,false);
        CategoryAdapter.MyViewHolder holder = new CategoryAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Category item = dataCategory.get(position);
        Picasso.get().load(item.getHinhLoai()).into(holder.ivCategoryImage);
        holder.tvCategoryName.setText(item.getTenLoai());
        firebaseStorage = FirebaseStorage.getInstance();

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("maLoai", dataCategory.get(position).getMaLoai()+"");
                bundle.putString("tenLoai", dataCategory.get(position).getTenLoai()+"");
                bundle.putString("hinhLoai", dataCategory.get(position).getHinhLoai()+"");

                BottomSheetCategoryEdit bottomSheetCategoryEdit = new BottomSheetCategoryEdit();
                bottomSheetCategoryEdit.setArguments(bundle);
                bottomSheetCategoryEdit.show(((AppCompatActivity)context).getSupportFragmentManager(), bottomSheetCategoryEdit.getTag());
            }
        });
        holder.cvCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                categoryDAO = new CategoryDAO(context);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn muốn xóa "+dataCategory.get(position).getTenLoai()+ "?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String MaLoai = dataCategory.get(position).getMaLoai();
                                String HinhAnh = dataCategory.get(position).getHinhLoai();
                                categoryDAO.delete(MaLoai);
                                String hinh = HinhAnh;
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(hinh);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Xóa thành công ảnh", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {

                                    }
                                });
                                Toast.makeText(context, "Xóa thành công "+dataCategory.get(position).getTenLoai(), Toast.LENGTH_SHORT).show();
                                categoryDAO.getAllCategory();
                                // sau khi xóa đọc lại dữ liệu
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvCategory;
        TextView tvCategoryName;
        ImageView ivCategoryImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvCategory = itemView.findViewById(R.id.itemCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);



        }
    }
}
