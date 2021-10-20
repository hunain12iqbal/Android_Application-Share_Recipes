package com.hunain.sharerecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hunain.sharerecipe.adapter.MainActAdapter;
import com.hunain.sharerecipe.databinding.ActivityMainBinding;
import com.hunain.sharerecipe.models.Post;
import com.hunain.sharerecipe.models.Users;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    ActivityMainBinding binding;
    MainActAdapter adapter;
    ArrayList<Post> list;
    Query query;
    ListenerRegistration listenerRegistration;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        binding.mainrecyview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
         list = new ArrayList<>();
        adapter = new MainActAdapter(MainActivity.this, list);
        binding.mainrecyview.setAdapter(adapter);

       if(auth.getCurrentUser() != null){
          binding.mainrecyview.addOnScrollListener(new RecyclerView.OnScrollListener() {
              @Override
              public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                  super.onScrolled(recyclerView, dx, dy);
                  Boolean isBottom = !binding.mainrecyview.canScrollVertically(1);
                  if(isBottom){
                      Toast.makeText(MainActivity.this, "All Post Reached", Toast.LENGTH_SHORT).show();
                  }
              }
          });


           query = firestore.collection("post").orderBy("time" , Query.Direction.DESCENDING);
           listenerRegistration = query.addSnapshotListener(MainActivity.this, new EventListener<QuerySnapshot>() {
               @Override
               public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                 for(DocumentChange doc: value.getDocumentChanges()){
                     if(doc.getType() == DocumentChange.Type.ADDED){

                         Post usss = doc.getDocument().toObject(Post.class);
                         list.add(usss);
                         adapter.notifyDataSetChanged();
                     }
                     else {
                         adapter.notifyDataSetChanged();
                     }
                 }
                   listenerRegistration.remove();

               }
           });


       }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class );
                startActivity(intent);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent1 = new Intent(MainActivity.this, SigninActivity.class );
                startActivity(intent1);

                break;
            case R.id.uplaod:
                Intent intent2 = new Intent(MainActivity.this, UploadConActivity.class );
                startActivity(intent2);



        }




        return true;
    }

}