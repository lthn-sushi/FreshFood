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
import com.nganlth.freshfoodadmin.Adapter.CategoryAdapter;
import com.nganlth.freshfoodadmin.DAO.CategoryDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.nganlth.freshfoodadmin.Fragment.CategoryFragment.categoryAdapter;
import static com.nganlth.freshfoodadmin.Fragment.CategoryFragment.rcvCategory;

public class BottomSheetCategoryEdit extends BottomSheetDialogFragment {
    EditText edCategoryIDEdit, edCategoryNameEdit;
    Button btnUpdateCategory, btnChooseCategoryEdit;
    private ImageView ivCategoryImageEdit;
    CategoryDAO categoryDAO;
    ArrayList<Category> dataCategory;
    Category category;
    private Uri uri;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    public BottomSheetCategoryEdit(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_edit_category, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        edCategoryIDEdit = view.findViewById(R.id.edCategoryIDEdit);
        edCategoryNameEdit = view.findViewById(R.id.edCategoryNameEdit);
        ivCategoryImageEdit = view.findViewById(R.id.ivCategoryImageChooseEdit);
        btnUpdateCategory = view.findViewById(R.id.btnUpdateCategory);
        btnChooseCategoryEdit = view.findViewById(R.id.btnChooseCategoryEdit);

        Bundle bundle = getArguments();
        String CategoryID = bundle.getString("maLoai");
        String CategoryName = bundle.getString("tenLoai");
        String CategoryImage = bundle.getString("hinhLoai");

        Picasso.get().load(CategoryImage).into(ivCategoryImageEdit);

        edCategoryIDEdit.setText(CategoryID);
        edCategoryNameEdit.setText(CategoryName);
        categoryDAO = new CategoryDAO(getContext());

        btnChooseCategoryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
                categoryDAO = new CategoryDAO(getContext());
                dataCategory = categoryDAO.getAllCategory();
                Toast.makeText(getActivity(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });
        return view;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ivCategoryImageEdit.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void LoadData() {

        if(uri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Categories/"+ UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref. getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String maLoai = edCategoryIDEdit.getText().toString().trim();
                                    String tenLoai = edCategoryNameEdit.getText().toString().trim();
                                    category = new Category();
                                    category.setMaLoai(maLoai);
                                    category.setTenLoai(tenLoai);
                                    category.setHinhLoai(uri.toString());

                                    categoryDAO =new CategoryDAO(getActivity());
                                    categoryDAO.update(category);
                                    progressDialog.dismiss();

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
