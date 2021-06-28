package com.example.cse.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView sinupbtn,fgtpsswd;
    TextInputLayout username,passwrd;
    TextView msg,msg2;
    ImageView logo_img;
    Button loginbttn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        msg=findViewById(R.id.tv_msg);
        msg2=findViewById(R.id.tv_msg2);
        logo_img=findViewById(R.id.logo_img);
        loginbttn=findViewById(R.id.loginbttn);
        progressBar=findViewById(R.id.progressBar);
        username=findViewById(R.id.usrname);
        passwrd=findViewById(R.id.paswd);
        fgtpsswd=findViewById(R.id.fgtpsswd);

        fAuth=FirebaseAuth.getInstance();

        sinupbtn = findViewById(R.id.sinupbtn);

        fgtpsswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

        loginbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getEditText().getText().toString();
                String password = passwrd.getEditText().getText().toString();

                if (email.isEmpty()|| password.isEmpty()) {
                    username.setError("Email is required");
                    passwrd.setError("Password is required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(LoginActivity.this,Activity1.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Error !"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        sinupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                Pair[] pairs= new Pair[2];
                pairs[0] = new Pair<View,String>(msg,"slogan_tran");
                pairs[1] = new Pair<View,String>(msg2,"slogan2_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
    }
}

