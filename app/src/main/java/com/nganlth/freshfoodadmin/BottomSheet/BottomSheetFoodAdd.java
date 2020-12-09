package com.nganlth.freshfoodadmin.BottomSheet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nganlth.freshfoodadmin.DAO.CategoryDAO;
import com.nganlth.freshfoodadmin.DAO.FoodDAO;
import com.nganlth.freshfoodadmin.Model.Category;
import com.nganlth.freshfoodadmin.Model.Food;
import com.nganlth.freshfoodadmin.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.nganlth.freshfoodadmin.Fragment.CategoryFragment.mDataFood;

public class BottomSheetFoodAdd extends BottomSheetDialogFragment {

    EditText edFoodID, edFoodName, edFoodAmount, edFoodPrice;
    Button btnAddFood, btnChooseImageFood;
    TextView tvNSX, tvHSD;
    Spinner spCategoryID;
    ImageView ivFoodImageChoose;
    FoodDAO foodDAO;
    Food food;
    ArrayList<Food> dataFood = new ArrayList<Food>();
    CategoryDAO categoryDAO;
    ArrayList<Category> dataCategory = new ArrayList<Category>();
    ArrayAdapter<Category> categoryArrayAdapter;
    int i;
    String show="";
    private Uri uri;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;


    public BottomSheetFoodAdd(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_add_food, container, false);
        edFoodID = view.findViewById(R.id.edFoodID);
        edFoodName = view.findViewById(R.id.edFoodName);
        edFoodAmount = view.findViewById(R.id.edFoodAmount);
        edFoodPrice = view.findViewById(R.id.edFoodPrice);
        tvNSX = view.findViewById(R.id.tvNSX);
        tvHSD = view.findViewById(R.id.tvHSD);
        spCategoryID = view.findViewById(R.id.spCategoryID);
        ivFoodImageChoose = view.findViewById(R.id.ivFoodImageChoose);
        btnChooseImageFood = view.findViewById(R.id.btnChooseFoodImage);
        btnAddFood = view.findViewById(R.id.btnAddFood);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        edFoodPrice.addTextChangedListener(onTextChangedListener());


        //get spinner Categories_id+++++++++
        categoryDAO = new CategoryDAO(getContext());
        getAllLoai();

        ShowAdapterLoai();
        spCategoryID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                show = dataCategory.get(spCategoryID.getSelectedItemPosition()).getTenLoai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCategoryID.setSelection(checkpositionspinenr(show));
//        ++++++++++++++++++++++++++++

        foodDAO = new FoodDAO(getContext());

        tvNSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateNSX();
            }
        });

        tvHSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateHSD();
            }
        });

        btnChooseImageFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });


        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
                Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
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
                ivFoodImageChoose.setImageBitmap(bitmap);
            } catch (IOException e) {
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

            StorageReference ref = storageReference.child("Food/"+ UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref. getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String food_id = edFoodID.getText().toString().trim();
                                    String food_name = edFoodName.getText().toString().trim();
                                    int so_luong = Integer.parseInt(edFoodAmount.getText().toString().trim());
                                    String gia = edFoodPrice.getText().toString();
                                    String NSX = tvNSX.getText().toString().trim();
                                    String HSD = tvHSD.getText().toString().trim();
                                    String categories_id = show;
                                    dataCategory = new ArrayList<>();
                                    dataFood= new ArrayList<>();
                                    foodDAO = new FoodDAO(getContext());
                                    DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                    format.setParseBigDecimal(true);
                                    BigDecimal number = null;
                                    try {
                                        number = (BigDecimal) format.parse(gia);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    double so = Double.parseDouble(number+"");


                                    food = new Food();
                                    food.setFood_id(food_id);
                                    food.setFoodName(food_name);
                                    food.setFoodImage(uri.toString());
                                    food.setSoLuong(so_luong);
                                    food.setGia(Double.parseDouble(so+""));
                                    food.setNsx(NSX);
                                    food.setHsd(HSD);
                                    food.setCategories_id(categories_id);
                                    foodDAO =new FoodDAO(getActivity());
                                    foodDAO.insert(food);
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


    //DatePickerDialog
    private void DateNSX(){
        Date today =new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int months = calendar.get(Calendar.MONTH);
        final int years = calendar.get(Calendar.YEAR);

        final Calendar calendar1 = Calendar.getInstance();
        int date = calendar1.get(Calendar.DAY_OF_MONTH);
        int month = calendar1.get(Calendar.MONTH);
        int year = calendar1.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                calendar1.set(i,i1,i2);
                tvNSX.setText(simpleDateFormat.format(calendar1.getTime()));
            }
        },years, months,dayOfWeek);
        datePickerDialog.show();
    }

    private void DateHSD(){
        Date today =new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int months = calendar.get(Calendar.MONTH);
        final int years = calendar.get(Calendar.YEAR);

        final Calendar calendar1 = Calendar.getInstance();
        int date = calendar1.get(Calendar.DAY_OF_MONTH);
        int month = calendar1.get(Calendar.MONTH);
        int year = calendar1.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                calendar1.set(i,i1,i2);
                tvHSD.setText(simpleDateFormat.format(calendar1.getTime()));
            }
        },years, months,dayOfWeek);
        datePickerDialog.show();
    }

    public ArrayList<Category> getAllLoai(){
        dataCategory = new ArrayList<>();
        mDataFood = FirebaseDatabase.getInstance().getReference("TheLoai");
        mDataFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    dataCategory.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()){
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        Category category = next.getValue(Category.class);
                        dataCategory.add(category);
                    }
                    categoryArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dataCategory;
    }

    public void ShowAdapterLoai(){
        categoryDAO = new CategoryDAO(getActivity());
        dataCategory = new ArrayList<>();
        dataCategory.clear();
        dataCategory.addAll(getAllLoai());

        categoryArrayAdapter= new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_item, dataCategory);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryID.setAdapter(categoryArrayAdapter);
    }

//    public boolean xetTrung(String maLoai) {
//
////        ds_food=new ArrayList<>();
////        foodDAO = new FoodDAO(getActivity());
////        ds_food=foodDAO.();
//
//
//        Boolean xet = false;
//
//        for (int i = 0; i < ds_food.size(); i++) {
//            String ma = ds_food.get(i).getFoodID();
//            if (ma.equalsIgnoreCase(maLoai)) {
//                xet = true;
//                break;
//            }
//        }
//        return xet;
//
//    }

    public int checkpositionspinenr(String str){

        for (int i =0; i<dataCategory.size();i++){
            if (str.equals(dataCategory.get(i).getTenLoai())){
                return i;
            }
        }
        return 0;
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edFoodPrice.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edFoodPrice.setText(formattedString);
                    edFoodPrice.setSelection(edFoodPrice.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edFoodPrice.addTextChangedListener(this);
            }
        };

    }
}
