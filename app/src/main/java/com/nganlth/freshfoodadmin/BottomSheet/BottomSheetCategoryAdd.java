package com.nganlth.freshfoodadmin.BottomSheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nganlth.freshfoodadmin.DAO.CategoryDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class BottomSheetCategoryAdd extends BottomSheetDialogFragment {

    EditText edCategoryID, edCategoryName;
    Button btnChooseImage, btnAddCategory;
    ImageView imgCategoryImage;

    Category category;
    CategoryDAO categoryDAO;
    ArrayList<Category> dataCategory;
    Uri uri;
    FirebaseStorage storage;
    StorageReference reference;
    private final int PICK_IMAGE_REQUEST = 71;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_add_category, container, false);
        edCategoryID = view.findViewById(R.id.edCategoryID);
        edCategoryName = view.findViewById(R.id.edCategoryName);
        imgCategoryImage = view.findViewById(R.id.ivCategoryImageChoose);
        btnChooseImage = view.findViewById(R.id.btnChooseCategory);
        btnAddCategory = view.findViewById(R.id.btnAddCategory);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                Toast.makeText(getActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }

    private void chooseImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),PICK_IMAGE_REQUEST);
    }
    // Hàm trả về dữ liệu
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Sai ở đây --> " resultCode "
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            uri = data.getData();
            try {
                // Trung gian chứa ảnh trước khi thêm vào firebase
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Thiếu set Image cho img
                imgCategoryImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadData(){
        // Nếu uri khác null
        if (uri != null){
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            final StorageReference ref = reference.child("Categories/"+ UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String categoryID = edCategoryID.getText().toString().trim();
                            String categoryName = edCategoryName.getText().toString().trim();

                            Category category = new Category();

                            category.setMaLoai(categoryID);
                            category.setTenLoai(categoryName);
                            category.setHinhLoai(uri.toString());

                            CategoryDAO categoryDAO = new CategoryDAO(getActivity());
                            categoryDAO.insert(category);
                            progressDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred()/
                            snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading "+(int)progress+" %");
                }
            });

        }
    }
}
