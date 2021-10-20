package com.hunain.sharerecipe.models;

import android.net.Uri;

public class PostModel {

    String userpost, userprofile, receipename, stepcocking, ingrediants, recipepic, username;

    public PostModel(String receipename, String stepcocking, String ingrediants) {
        this.receipename = receipename;
        this.stepcocking = stepcocking;
        this.ingrediants = ingrediants;
    }

    public PostModel() {
    }

    public PostModel(String userprofile, String username, String recipepic, String receipename) {
        this.receipename = receipename;
        this.username = username;
        this.recipepic = recipepic;
        this.userprofile = userprofile;

    }

    public String getUserpost() {
        return userpost;
    }

    public String getUserprofile() {
        return userprofile;
    }

    public String getReceipename() {
        return receipename;
    }

    public String getStepcocking() {
        return stepcocking;
    }

    public String getIngrediants() {
        return ingrediants;
    }

    public String getRecipepic() {
        return recipepic;
    }

    public String getUsername() {
        return username;
    }
}


