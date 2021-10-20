package com.hunain.sharerecipe.adapter;

import static com.hunain.sharerecipe.R.drawable.heart;
import static com.hunain.sharerecipe.R.drawable.like;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hunain.sharerecipe.MainActivity;
import com.hunain.sharerecipe.R;
import com.hunain.sharerecipe.describtionActivity;
import com.hunain.sharerecipe.models.Post;
import com.hunain.sharerecipe.models.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActAdapter  extends RecyclerView.Adapter<MainActAdapter. Viewholder>{
    ArrayList<Post> list;
    FirebaseAuth auth;
    String currentuserid;

    Activity context;
    FirebaseFirestore firestore;

    public MainActAdapter(Activity context, ArrayList<Post> list ) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.manisample,parent,false);
        firestore = FirebaseFirestore.getInstance();
        currentuserid = auth.getCurrentUser().getUid();
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Post user = list.get(position);
        holder.setPostPic(user.getImage());
        holder.setRecepename(user.getRecepename());



        String id = user.getUser();



        firestore.collection("User").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if(task.isSuccessful()){
                       String usserName= task.getResult().getString("name");
                      String  image = task.getResult().getString("image");
                       holder.setProfilePic(image);
                       holder.setUername(usserName);
                   }else{
                       Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
            }
        });













    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView  postimage ;
        CircleImageView userimage;
        TextView username, recipename,ingrediants, stepcocking;
        View mview;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;


        }
        public void setPostPic(String urlPost){
            postimage = mview.findViewById(R.id.userpostimg);
            Picasso.get().load(urlPost).placeholder(R.drawable.pro).into(postimage);
        }
        public void setProfilePic(String urlProfile){
            userimage = mview.findViewById(R.id.userimage);
            Picasso.get().load(urlProfile).placeholder(R.drawable.pro).into(userimage);

        }
        public void setUername(String userName){
            username = mview.findViewById(R.id.usernamem);
            username.setText(userName);
        }
        public void setRecepename(String recipeName){
            recipename = mview.findViewById(R.id.pottrecname);
            recipename.setText(recipeName);
        }


    }

}
