package com.hunain.sharerecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hunain.sharerecipe.databinding.ActivitySignup2Binding;
import com.hunain.sharerecipe.models.Users;

public class SignupActivity extends AppCompatActivity {

    Button btnsignup;
    EditText email;
    EditText pass, username;
    TextView haveacc;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("creating account");
        progressDialog.setMessage("Creating  Account...");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        btnsignup = findViewById(R.id.btnsignup);
        email = findViewById(R.id.emailup);
        pass = findViewById(R.id.passup);
        username = findViewById(R.id.usernameup);
        haveacc = findViewById(R.id.haveaccount);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailst = email.getText().toString();
                String passst = pass.getText().toString();
                if (TextUtils.isEmpty(emailst.toString())) {
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(passst)) {
                    pass.setError("Password is required!");
                } else {

                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.show();
                                    if (task.isSuccessful()) {
                                        Users users = new Users(username.getText().toString(), email.getText().toString(), pass.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(users);
                                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(SignupActivity.this, "Account created Successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });



        haveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }
}