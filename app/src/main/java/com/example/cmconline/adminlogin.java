package com.example.cmconline;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;




public class adminlogin extends AppCompatActivity {

    public static EditText fname, lname, phone, password,password2, location;
    Spinner type, unit;
    public static String unit_str, firstname, lastname, phonenumber,loc,pass;
    public static int t;

    Button signup;
    boolean is_valid;
    LoadingDialog loadingDialog = new LoadingDialog((adminlogin.this));


    @Override
    protected void onStart() {
        super.onStart();



    }
    @Override
    public void onBackPressed() {

        Intent login = new Intent(adminlogin.this,login.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        fname = findViewById(R.id.tvfname);
        lname = findViewById(R.id.tvlname);
        phone = findViewById(R.id.tvphone);
        location = findViewById(R.id.tvlocation);
        password = findViewById(R.id.tvpassword);
        password2 = findViewById(R.id.tvpassword2);
        type = findViewById(R.id.tvtype);
        unit = findViewById(R.id.tvunit);
        signup = findViewById(R.id.btsignup);







        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.type,R.layout.simple_spinner);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        type.setAdapter(adapter1);
        type.setSelection(0);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                t = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.units,R.layout.simple_spinner);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        unit.setAdapter(adapter2);
        unit.setSelection(0);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                unit_str = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();


                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                is_valid = true;



                if (fname.getText().toString().trim().equalsIgnoreCase("")) {
                    fname.setError("This field can not be blank");
                    is_valid =false;
                }

                if (lname.getText().toString().trim().equalsIgnoreCase("")) {
                    lname.setError("This field can not be blank");
                    is_valid =false;
                }

                if (phone.getText().toString().trim().equalsIgnoreCase("")) {
                    phone.setError("This field can not be blank");
                    is_valid =false;
                }


                if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("This field can not be blank");
                    is_valid =false;
                }

                if (!password.getText().toString().trim().equals(password.getText().toString().trim())) {
                    password.setError("Passwords should match!");
                    is_valid =false;
                }
                if (location.getText().toString().trim().equalsIgnoreCase("")) {
                    location.setError("This field can not be blank");
                    is_valid =false;
                }

                if (t==0) {
                    ((TextView)type.getSelectedView()).setError("This field is required!");
                    is_valid =false;
                }

                if (unit_str.equalsIgnoreCase("(Select Your Unit)")) {
                    ((TextView)unit.getSelectedView()).setError("This field is required!");
                    is_valid =false;
                }



                if (!Pattern.compile("\\d{10}").matcher(phone.getText().toString().trim()).matches()) {
                    phone.setError("Invalid Number");
                    is_valid =false;
                }
                if (password.getText().toString().trim().length() < 6 ){
                    password.setError("Minimun length should be 6");
                    is_valid =false;
                }
                loadingDialog.dismissDialog();
                if (is_valid){

                    firstname = fname.getText().toString();
                    lastname = lname.getText().toString();
                    phonenumber = phone.getText().toString();
                    loc = location.getText().toString();
                    pass = password.getText().toString();


                    Intent tootp = new Intent(adminlogin.this,otp2.class);
                    startActivity(tootp);
                    finish();


                }



            }
        });

    }






}



