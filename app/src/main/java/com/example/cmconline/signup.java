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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;




public class signup extends AppCompatActivity {
    DatePickerDialog picker;
    DatabaseReference dref;
    public static EditText fname, lname, phone, departure, expctedarrival ,password,password2, location,numpeople,email;
    Spinner zone, unit;
    public static String zone_str, unit_str, firstname, lastname, phonenumber,depdate,exparrdate,loc,pass,people,emailstr;
    Button signup;
    boolean is_valid;



    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public void onBackPressed() {

        Intent login = new Intent(signup.this,login.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname = findViewById(R.id.tvfname);
        lname = findViewById(R.id.tvlname);
        phone = findViewById(R.id.tvphone);
        location = findViewById(R.id.tvlocation);
        departure = findViewById(R.id.tvdep);
        expctedarrival = findViewById(R.id.tvarrival);
        password = findViewById(R.id.tvpassword);
        password2 = findViewById(R.id.tvpassword2);
        zone = findViewById(R.id.tvzone);
        unit = findViewById(R.id.tvunit);
        signup = findViewById(R.id.btsignup);
        numpeople = findViewById(R.id.tvperson);
        email = findViewById(R.id.tvemail);







        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.zones,R.layout.simple_spinner);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        zone.setAdapter(adapter1);
        zone.setSelection(0);
        zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                zone_str = parent.getItemAtPosition(position).toString();
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



        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(signup.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                departure.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        expctedarrival.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(signup.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expctedarrival.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                is_valid = true;

                if(email.getText().toString().trim().equals("")){
                    email.setError("Enter a valid Email.");
                    is_valid = false;

                }



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

                if (departure.getText().toString().trim().equalsIgnoreCase("")) {
                    departure.setError("This field can not be blank");
                    is_valid =false;
                }

                if (expctedarrival.getText().toString().trim().equalsIgnoreCase("")) {
                    expctedarrival.setError("This field can not be blank");
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
                if (numpeople.getText().toString().trim().equalsIgnoreCase("")) {
                    numpeople.setError("This field can not be blank");
                    is_valid =false;
                }
                if (numpeople.getText().toString().trim().equalsIgnoreCase("0")) {
                    numpeople.setError("Can't be zero.");
                    is_valid =false;
                }

                if (zone_str.equalsIgnoreCase("(Select Containment Zone)")) {
                    ((TextView)zone.getSelectedView()).setError("This field is required!");
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

                if (is_valid){

                    firstname = fname.getText().toString();
                    lastname = lname.getText().toString();
                    phonenumber = phone.getText().toString();
                    depdate = departure.getText().toString();
                    exparrdate = expctedarrival.getText().toString();
                    loc = location.getText().toString();
                    pass = password.getText().toString();
                    people = numpeople.getText().toString();
                    emailstr = email.getText().toString();

                    Intent tootp = new Intent(signup.this,otp.class);
                    startActivity(tootp);
                    finish();


                }



            }
        });

    }






}



