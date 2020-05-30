package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
                if (!Pattern.compile("\\d{10}").matcher(resetphone.getText().toString().trim()).matches()) {
                    resetphone.setError("Invalid Number");
                    is_valid =false;
                }
                if (resetphone.getText().toString().trim().equals("")) {
                    resetphone.setError("Invalid Number");
                    is_valid =false;
                }
                 if (is_valid){
                     resetnumber = resetphone.getText().toString();

                     firebaseAuth.fetchSignInMethodsForEmail(resetnumber+ "@cmcfirebase.in")
                             .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                     boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                     if (isNewUser) {
                                         Toast.makeText(reset.this, "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                                         resetphone.setText("");
                                     } else {

                                         Intent toresetopt = new Intent(reset.this, verifyreset.class);
                                         startActivity(toresetopt);
                                         finish();

                                     }

                                 }
                             });
                 }

            }
        });


    }
}