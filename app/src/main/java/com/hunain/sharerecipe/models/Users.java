package com.hunain.sharerecipe.models;

public class Users {

    String username, useremail, userid, userpass, userpost, userprofile,receipename ,stepcocking,ingrediants;

    public Users(String username, String useremail, String userid, String userpass, String userpost, String userprofile) {
        this.username = username;
        this.useremail = useremail;
        this.userid = userid;
        this.userpass = userpass;
        this.userpost = userpost;
        this.userprofile = userprofile;
    }

    public  Users(String userprofile,String receipename){
        this.userprofile = userprofile;
        this.receipename = receipename;


    }


    public String getReceipename() {
        return receipename;
    }

    public void setReceipename(String receipename) {
        this.receipename = receipename;
    }

    public String getStepcocking() {
        return stepcocking;
    }

    public void setStepcocking(String stepcocking) {
        this.stepcocking = stepcocking;
    }

    public String getIngrediants() {
        return ingrediants;
    }

    public void setIngrediants(String ingrediants) {
        this.ingrediants = ingrediants;
    }

    public Users(String username, String useremail, String userpass) {
        this.username = username;
        this.useremail = useremail;
        this.userpass = userpass;
    }
    public  Users(){};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getUserpost() {
        return userpost;
    }

    public void setUserpost(String userpost) {
        this.userpost = userpost;
    }

    public String getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(String userprofile) {
        this.userprofile = userprofile;
    }
}
