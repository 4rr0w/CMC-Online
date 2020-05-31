package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;
import java.util.regex.Pattern;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class login extends AppCompatActivity {
    EditText phone, password;
    TextView otplogin;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressDialog progress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    public static int type = 0;
    public static String unit_;
    LoadingDialog loadingDialog = new LoadingDialog((login.this));

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.tvphone);
        password = findViewById(R.id.tvpassword);
        otplogin = findViewById(R.id.otplogin);
        mAuth = FirebaseAuth.getInstance();


        otplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent totoplogin = new Intent(login.this,reset.class);
                startActivity(totoplogin);
                finish();

            }
        });







        findViewById(R.id.btlogin).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                loadingDialog.startLoadingDialog();



                mAuth.signInAnonymously()
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            String email;
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    if (fields()) {
                                        db.collection("emails").document(phone.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot document) {
                                                email = document.getString("email");
                                               mAuth.signOut();
                                                validate(email, password.getText().toString());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(login.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });




                                    }else{
                                        mAuth.signOut();
                                        loadingDialog.dismissDialog();
                                    }



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismissDialog();

                                }

                                // ...
                            }
                        });


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

        findViewById(R.id.toadmin).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent toadmin= new Intent(login.this,adminlogin.class);
                startActivity(toadmin);
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
        if ((mAuth.getCurrentUser() != null)) {

            loadingDialog.startLoadingDialog();
            log();



        }
    }







    private void validate(String userName, String Password) {//this function validate user with Firebase database
        mAuth.signInWithEmailAndPassword(userName, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    log();
                } else {

                    phone.setError("Wrong Username or Password!");
                    Toast.makeText(login.this, "Login Failed!!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();

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


    public void log(){




        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult().exists()){
                    Intent intent = new Intent(login.this, profile.class);//creating a new intent pointing to Profile
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);//starting this new intent
                }
                else{
                    db2.collection("admin").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                if(!Objects.requireNonNull(task.getResult()).exists()) {
                                    mAuth.getCurrentUser().delete();
                                    Toast.makeText(login.this, "Your Account was Deleted.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                        }
                    });

                    db2.collection("admin").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot d2) {
                            Intent admin;
                            unit_ = d2.getString("unit");

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("unit",unit_);
                            editor.apply();


                            if(d2.get("type")==null){

                                mAuth.signOut();
                                loadingDialog.dismissDialog();
                                return;
                            }

                            type = (int) (long)d2.get("approved");
                            switch(type){
                                case 0 : admin = new Intent(login.this,waiting.class);
                                break;
                                case 1 : admin = new Intent(login.this,admin.class);
                                break;
                                default : admin = new Intent(login.this,waiting.class);


                            }
                            loadingDialog.dismissDialog();
                            startActivity(admin);
                            finish();


                        }
                    });


                }
            }
        });








    }


}



