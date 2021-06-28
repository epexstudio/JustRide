package com.example.cse.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {
    TextView fullname,username;
    EditText et_fullname,et_phone,et_email;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fullname=findViewById(R.id.fname);
        username=findViewById(R.id.uname);
        et_fullname=findViewById(R.id.et_fname);
        et_phone=findViewById(R.id.et_phn);
        et_email=findViewById(R.id.et_email);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userId=fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullname.setText(documentSnapshot.getString("fName"));
                username.setText(documentSnapshot.getString("uName"));
                et_fullname.setText(documentSnapshot.getString("fName"));
                et_phone.setText(documentSnapshot.getString("phnNo"));
                et_email.setText(documentSnapshot.getString("email"));
            }
        });

    }

    public void BacktoMap(View view) {
        Intent intent = new Intent(ProfileActivity.this,Activity1.class);
        startActivity(intent);
        finish();
    }
}
