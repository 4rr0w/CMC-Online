package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userapprove extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadingDialog loadingDialog = new LoadingDialog((userapprove.this));

    TextView name, unit, zone, location, departing, arriving, phone,medremark,totalpeople;
    EditText movremark;
    Spinner istapprove, finalapprove;
    Button save,back;
    String[] approve;
    int sp1,sp2;

    @Override
    public void onBackPressed() {

        Intent admin = new Intent(userapprove.this,admin.class);
        startActivity(admin);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userapprove);
        loadingDialog.startLoadingDialog();

        name = findViewById(R.id.name);
        unit = findViewById(R.id.unit);
        zone = findViewById(R.id.zone);
        unit = findViewById(R.id.unit);
        departing = findViewById(R.id.departing);
        arriving = findViewById(R.id.arriving);
        phone = findViewById(R.id.phone);
        arriving = findViewById(R.id.arriving);
        movremark = findViewById(R.id.movremark);
        medremark = findViewById(R.id.medremark);
        location = findViewById(R.id.location);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);
        istapprove = findViewById(R.id.istapprove);
        finalapprove = findViewById(R.id.finalapprove);
        totalpeople = findViewById(R.id.totalpeople);
        approve = getResources().getStringArray(R.array.approves);




        //approve[Integer.parseInt(d.getString("Motion Approved"))]

        db.collection("users").document(admin.snapid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot d) {


                                              int i = (int) (long)  d.get("movement");
                                              int j = (int) (long)  d.get("final");

                                              switch(i){
                                                  case 0 : sp1 = 0;
                                                  break;
                                                  case 1: sp1 = 1;
                                                  break;
                                                  case 2 : sp1 = 2;
                                                  break;
                                              }
                                              switch(j){
                                                  case 0 : sp2 = 0;
                                                      break;
                                                  case 1: sp2 = 1;
                                                      break;
                                                  case 2 : sp2 = 2;
                                                      break;
                                              }

                                              name.setText(d.getString("first")+" "+d.getString("last"));
                                              unit.setText("Unit : "+d.getString("unit"));
                                              zone.setText("Containment Zone : "+d.getString("zone"));
                                              departing.setText("Departure Date : " + d.getString("DepartureDate"));
                                              arriving.setText("Expected Arrival : " +d.getString( "ExpectedArrival"));
                                              movremark.setText( d.getString("movRemark"));
                                              medremark.setText("Medical Remark : "+ d.getString("Remark"));
                                              location.setText(d.getString("location"));
                                              phone.setText("+91 "+d.getString("phone"));
                                              totalpeople.setText("Number of person : " + (String)d.get("people").toString());
                                              loadingDialog.dismissDialog();



                                              ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(userapprove.this,R.array.approves,R.layout.simple_spinner1);
                                              adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                              istapprove.setAdapter(adapter);
                                              istapprove.setSelection(sp1);
                                              istapprove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                      try {
                                                          InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                                          imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                                      } catch (Exception e) {
                                                          // TODO: handle exception
                                                      }

                                                      sp1 = position;
                                                      if (position == 1){
                                                          finalapprove.setEnabled(true);
                                                      }else if(position == 0){
                                                          finalapprove.setEnabled(false);
                                                          finalapprove.setSelection(0);
                                                      }
                                                      else if(position ==2){
                                                          finalapprove.setEnabled(false);
                                                          finalapprove.setSelection(2);
                                                      }



                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {

                                                  }
                                              });

                                              ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(userapprove.this,R.array.approves,R.layout.simple_spinner1);
                                              adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                              finalapprove.setAdapter(adapter2);
                                              finalapprove.setSelection(sp2);
                                              finalapprove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                      try {
                                                          InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                                          imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                                      } catch (Exception e) {
                                                          // TODO: handle exception
                                                      }
                                                      sp2 = position;

                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {

                                                  }
                                              });

                                              save.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      db.collection("users").document(admin.snapid)
                                                              .update(
                                                                      "movement",sp1,
                                                                      "final", sp2,
                                                                      "movRemark",movremark.getText().toString()
                                                              );

                                                      Intent save = new Intent(userapprove.this,admin.class);
                                                      startActivity(save);
                                                      finish();
                                                  }
                                              });

                                              back.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {

                                                      Intent save = new Intent(userapprove.this,admin.class);
                                                      startActivity(save);
                                                      finish();

                                                  }
                                              });
                                          }
                                      }

                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismissDialog();
                Toast.makeText(userapprove.this, "Failed to read database. Contact support", Toast.LENGTH_SHORT).show();
            }
        });

}

}
