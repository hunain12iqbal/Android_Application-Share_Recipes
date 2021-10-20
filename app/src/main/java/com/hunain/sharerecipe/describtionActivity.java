package com.hunain.sharerecipe;

import static com.hunain.sharerecipe.R.drawable.heart;
import static com.hunain.sharerecipe.R.drawable.like;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hunain.sharerecipe.databinding.ActivityDescribtionBinding;
import com.hunain.sharerecipe.models.Post;
import com.hunain.sharerecipe.models.PostId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class describtionActivity extends AppCompatActivity {
    ActivityDescribtionBinding binding;
    ArrayList<Post> list;
    Query query;
    ListenerRegistration listenerRegistration;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String currentuserid;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDescribtionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        currentuserid = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        String str = intent.getStringExtra("postimage");
        String recipename = intent.getStringExtra("recipename");
        String ingediants = intent.getStringExtra("ingrediants");
        String stepcockong  = intent.getStringExtra("stepcocking");
        Picasso.get().load(str).placeholder(R.drawable.pro).into(binding.recipepost);
        binding.recipepostname.setText(recipename);
        binding.recipeingredaints.setText(ingediants);
        binding.recipestep.setText(stepcockong);


            Post post = new Post();
              String postid = post.PostId;
        binding.likepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           firestore.collection("post/"+ postid+"/Likes").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(!task.getResult().exists()){
                        Map<String ,Object> likemap  = new HashMap<>();
                        likemap.put("time" , FieldValue.serverTimestamp());
                        firestore.collection("post/"+postid+"/Likes").document(currentuserid).set(likemap);


                    }else{
                             firestore.collection("post/"+postid+"/Likes").document(currentuserid).delete();
                    }
               }
           });
            }
        });
            firestore.collection("psot/"+postid+"/Likes").document(currentuserid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               if(error == null){
                   if(value.exists()){
                       binding.likepic.setImageDrawable(getDrawable(heart));
                   }
                   else{
                       binding.likepic.setImageDrawable(getDrawable(like));
                   }
               }

                }
            });








    }

}