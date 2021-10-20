package com.hunain.sharerecipe.models;


import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PostId {
@Exclude
    public String PostId;
    public  <T extends PostId>  T withid (@NonNull final String id){
        this.PostId=id;
        return  (T)  this;
    }

}
