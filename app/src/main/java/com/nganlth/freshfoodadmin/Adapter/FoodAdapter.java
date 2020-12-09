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
import com.nganlth.freshfoodadmin.BottomSheet.BottomSheetFoodEdit;
import com.nganlth.freshfoodadmin.DAO.FoodDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.Model.Food;
import com.nganlth.freshfoodadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {
    Context context;
    ArrayList<Food> dataFood;
    FoodDAO foodDAO;
    //Lấy ảnh
    FirebaseStorage firebaseStorage;

    public FoodAdapter(Context context, ArrayList<Food> dataFood) {
        this.context = context;
        this.dataFood = dataFood;
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_food,parent,false);
        FoodAdapter.MyViewHolder holder = new FoodAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holder, int position) {
        Food item = dataFood.get(position);
        Picasso.get().load(item.getFoodImage()).into(holder.ivFoodImages);
        holder.tvFoodID.setText(item.getFood_id());
        holder.tvFoodName.setText(item.getFoodName());
        holder.tvFoodAmount.setText(item.getSoLuong()+"");
        holder.tvFoodPrice.setText(item.getGia()+" Đ");
        holder.tvFoodNSX.setText(item.getNsx());
        holder.tvFoodHSD.setText(item.getHsd());
        holder.tvCategoryID.setText(item.getCategories_id());
        firebaseStorage = FirebaseStorage.getInstance();

        holder.cvFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("food_id", dataFood.get(position).getFood_id()+"");
                args.putString("FoodName", dataFood.get(position).getFoodName()+"");
                args.putString("FoodImage", dataFood.get(position).getFoodImage());
                args.putDouble("Gia", dataFood.get(position).getGia());
                args.putString("soLuong", dataFood.get(position).getSoLuong()+"");
                args.putString("NSX", dataFood.get(position).getNsx()+"");
                args.putString("HSD", dataFood.get(position).getHsd()+"");
                args.putString("categories_id", dataFood.get(position).getCategories_id()+"");

                BottomSheetFoodEdit bottomSheetFoodEdit = new BottomSheetFoodEdit();
                bottomSheetFoodEdit.setArguments(args);
                bottomSheetFoodEdit.show(((AppCompatActivity)context).getSupportFragmentManager(), bottomSheetFoodEdit.getTag());
            }
        });

        holder.cvFood.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                foodDAO = new FoodDAO(context);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn muốn xóa "+dataFood.get(position).getFoodName()+ "?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Có",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String Food_id = dataFood.get(position).getFood_id();
                                String HinhAnh = dataFood.get(position).getFoodImage();
                                foodDAO.delete(Food_id);
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
                                Toast.makeText(context, "Xóa thành công "+dataFood.get(position).getFoodName(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Không",
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
        return dataFood.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvFood;
        ImageView ivFoodImages;
        TextView tvFoodID, tvFoodName,tvFoodAmount, tvFoodPrice, tvFoodNSX, tvFoodHSD, tvCategoryID;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvFood = itemView.findViewById(R.id.itemFood);
            ivFoodImages = itemView.findViewById(R.id.ivFoodImage);
            tvFoodID = itemView.findViewById(R.id.tvFoodID);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodAmount = itemView.findViewById(R.id.tvFoodAmount);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvFoodNSX = itemView.findViewById(R.id.tvFoodNSX);
            tvFoodHSD = itemView.findViewById(R.id.tvFoodHSD);
            tvCategoryID = itemView.findViewById(R.id.tvCategoryID);

        }
    }
}
