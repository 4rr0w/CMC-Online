package com.example.cmconlineproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class reset extends AppCompatActivity {

    EditText resetphone;
    Button sendresetotp;
    public static String resetnumber;
    FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {

        Intent login = new Intent(reset.this,login.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        resetphone = findViewById(R.id.resetpassphone);
        sendresetotp = findViewById(R.id.sendotp);

        firebaseAuth = FirebaseAuth.getInstance();




        sendresetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean is_valid = true;

                if (resetphone.getText().toString().trim().equals("")) {
                    resetphone.setError("Invalid Email");
                    is_valid =false;
                }
                 if (is_valid){
                     resetnumber = resetphone.getText().toString();
                     boolean validmail;

                     String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                             "[a-zA-Z0-9_+&*-]+)*@" +
                             "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                             "A-Z]{2,7}$";

                     Pattern pat = Pattern.compile(emailRegex);
                     validmail = pat.matcher(resetnumber).matches();

                     if(validmail) {

                         firebaseAuth.fetchSignInMethodsForEmail(resetnumber)
                                 .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                     @Override
                                     public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                         boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                         if (isNewUser) {
                                             Toast.makeText(reset.this, "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                                             resetphone.setText("");
                                         } else {



                                             firebaseAuth.sendPasswordResetEmail(resetnumber)
                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()) {
                                                                 sendresetotp.setEnabled(false);
                                                                 Toast.makeText(reset.this, "Password reset link is sent to :  " + resetnumber, Toast.LENGTH_SHORT).show();
                                                             }
                                                         }
                                                     });


//                                         Intent toresetopt = new Intent(reset.this, verifyreset.class);
//                                         startActivity(toresetopt);
//                                         finish();

                                         }

                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
                     } else{
                         Toast.makeText(reset.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                     }
                 }

            }
        });


    }
}