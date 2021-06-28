package com.example.cse.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Compressor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout regName;
    TextInputLayout regUsername;
    TextInputLayout regEmail;
    TextInputLayout regPassword;
    TextInputLayout cnfrmPsswd;
    TextInputLayout regPhone;
    Button regBtn;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseFirestore fStore;
    CountryCodePicker c_code;

    private ImageView Profile_Image;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    private TextView link_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_up);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        c_code = findViewById(R.id.countrycode);
        regName =findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regPhone =findViewById(R.id.reg_phone);
        regEmail =findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.usr_pswd);
        cnfrmPsswd = findViewById(R.id.tf_cnfrm_pswd);
        regBtn = findViewById(R.id.reg_btn);
        link_login = findViewById(R.id.tv_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignin();
            }
        });
    }


    public void CallverifyOtp(View view) {

        if(!validatePhn() | !validatename() | !validateUsername() | !validateEmail() | !validatepassword() | !validateCnfrmPsswd()){
            return;
        }

        String fullname = regName.getEditText().getText().toString();
        String uname = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        String phoneNo = regPhone.getEditText().getText().toString().trim();
        String _phoneNo = c_code.getDefaultCountryCodeWithPlus()+phoneNo;

        Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
        intent.putExtra("fullname",fullname);
        intent.putExtra("usrname",uname);
        intent.putExtra("emailid",email);
        intent.putExtra("passwd",password);
        intent.putExtra("phoneNo",_phoneNo);

        startActivity(intent);

    }

    private Boolean validatename() {
        String val = regName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            regEmail.setErrorEnabled(false);
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatepassword() {
        String val = regPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        }
        if (val.length() < 6) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCnfrmPsswd() {
        String val = regPassword.getEditText().getText().toString().trim();
        String val2 = cnfrmPsswd.getEditText().getText().toString().trim();
        if (!val.equals(val2)) {
            cnfrmPsswd.setError("Password Not matching");
            return false;
        } else {
            cnfrmPsswd.setError(null);
            return true;
        }


    }

    private Boolean validatePhn(){
        String val = regPhone.getEditText().toString().trim();
        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Profile_Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openSignin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void GoBack(View view){
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
