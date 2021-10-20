package com.hunain.sharerecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hunain.sharerecipe.databinding.ActivitySettingBinding;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    Uri imageuri ;
    FirebaseStorage storage;
    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseFirestore  firestore;
    String currentuserid;
    Uri downloaduri = null;
    String usernamme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        currentuserid = auth.getCurrentUser().getUid();
         usernamme = binding.stusername.getText().toString();



        firestore.collection("User").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if(task.isSuccessful()){
                     if(task.getResult().exists()){
                         String name = task.getResult().getString("name");
                         String imgUri = task.getResult().getString("image");
                            binding.stusername.setText(name);
                            Picasso.get().load(imgUri).placeholder(R.drawable.pro).into(binding.pronimg);

                     }
                 }
            }
        });

        binding.selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

                CropImage.activity().
                        setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);

            }
        });


        binding.btnusernmae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernamme = binding.stusername.getText().toString();

                if(!usernamme.isEmpty()){
                    binding.progressBar.setVisibility(View.VISIBLE);
                    StorageReference imgref = storageReference.child("Profile_pic").child(currentuserid + "jpg");
                    imgref.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                saveToFireStore(task, usernamme, imgref);
                                Toast.makeText(SettingActivity.this, "saved", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(SettingActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SettingActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void saveToFireStore(Task<UploadTask.TaskSnapshot> task, String usernamme, StorageReference imgref) {
            imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloaduri = uri;
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name",usernamme);
                    map.put("image",downloaduri.toString());

                    firestore.collection("User").document(currentuserid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SettingActivity.this, "profile saved sucessfully", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SettingActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    });

                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imageuri = result.getUri();
                binding.pronimg.setImageURI(imageuri);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE ){
                Toast.makeText(SettingActivity.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ok", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(this, "denied", Toast.LENGTH_LONG);
            }
        }
    }

    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            binding.pronimg.setImageURI(imageuri);
            binding.stusername.setText(usernamme);
        }
    }
}
