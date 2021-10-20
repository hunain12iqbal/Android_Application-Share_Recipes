package com.hunain.sharerecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hunain.sharerecipe.databinding.ActivityUploadConBinding;
import com.hunain.sharerecipe.models.PostModel;
import com.hunain.sharerecipe.models.Users;

import java.util.HashMap;

public class UploadConActivity extends AppCompatActivity {

    ActivityUploadConBinding binding;
    Uri postimage = null;
    private FirebaseAuth auth;

    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseFirestore  firestore;
    String currentuserid;
    private Uri postImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadConBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermission();
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        currentuserid = auth.getCurrentUser().getUid();

    binding.selectimg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



             Intent intent = new Intent();
             intent.setAction(Intent.ACTION_GET_CONTENT);
             intent.setType("image/*");
             startActivityForResult(intent,33);


        }
    });

    binding.btnupload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String receipename = binding.recipename.getText().toString();
            String ingrediants = binding.ingredimats.getText().toString();
            String stepcocking = binding.stepcocking.getText().toString();
            if(!receipename.isEmpty()){
                StorageReference postrref = storageReference.child("post_images").child(FieldValue.serverTimestamp().toString()+".jpg");
                postrref.putFile(postimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                               postrref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       HashMap<String, Object> postmap = new HashMap<>();
                                       postmap.put("image",uri.toString());
                                       postmap.put("user",currentuserid);
                                       postmap.put("Recipename", receipename);
                                       postmap.put("ingrediants",ingrediants);
                                       postmap.put("Stepcocking",stepcocking);
                                       postmap.put("time",FieldValue.serverTimestamp());

                                       firestore.collection("post").add(postmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentReference> task) {
                                               if(task.isSuccessful()){
                                                   Toast.makeText(UploadConActivity.this, "Post Added Succesfuuly", Toast.LENGTH_SHORT).show();
                                                   startActivity(new Intent(UploadConActivity.this, MainActivity.class));
                                                   finish();
                                               }
                                               else{
                                                   Toast.makeText(UploadConActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });

                                   }
                               });
                        }
                        else{
                            Toast.makeText(UploadConActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
            else{
                Toast.makeText(UploadConActivity.this, "Upload Image and write name", Toast.LENGTH_SHORT).show();
            }




        }
    });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data.getData() != null) {
            postimage = data.getData();
            binding.recipepic.setImageURI(postimage); }super.onActivityResult(requestCode, resultCode, data); }
    private void checkPermission() {

        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"ok",Toast.LENGTH_LONG);
            }
            else {
                Toast.makeText(this,"denied",Toast.LENGTH_LONG);
            }
        }

    }
}