package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    EditText phone, password;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.tvphone);
        password = findViewById(R.id.tvpassword);
        mAuth = FirebaseAuth.getInstance();


        progress = new ProgressDialog(this);//progression bar



        findViewById(R.id.btlogin).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (fields()) {
                    validate(phone.getText().toString(), password.getText().toString());
                }
            }
        });


        findViewById(R.id.tosignup).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent tosignup = new Intent(login.this,signup.class);
                startActivity(tosignup);
                finish();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, profile.class));
        }
    }







    private void validate(String userName, String Password) {//this function validate user with Firebase database
        progress.setMessage("Authenticating");
        progress.show();
        mAuth.signInWithEmailAndPassword(userName+"@cmcfirebase.in", Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progress.dismiss();
                    finish();
                    Intent intent = new Intent(login.this, profile.class);//creating a new intent pointing to Profile
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);//starting this new intent
                } else {
                    progress.dismiss();
                    phone.setError("Wrong Username or Password!");
                    Toast.makeText(login.this, "Login Failed!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public boolean fields() {
        boolean is_valid = true;
        if (phone.getText().toString().trim().equalsIgnoreCase("")) {
            phone.setError("This field can not be blank");
            is_valid =false;
        }

        if (!Pattern.compile("\\d{10}").matcher(phone.getText().toString().trim()).matches()) {
            phone.setError("Invalid Number");
            is_valid =false;
        }



        return is_valid;
    }


}
