package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class medical extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id = mAuth.getUid();

    TextView name,unit,zone,location,departing,arriving,movstatus,medtstatus,phone,movremark,totalpeople;
    Button save,back;
    EditText remark;
    String[] approve;

    @Override
    public void onBackPressed() {

        Intent admin = new Intent(medical.this,admin.class);
        startActivity(admin);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);



        name = findViewById(R.id.name);
        unit = findViewById(R.id.unit);
        zone = findViewById(R.id.zone);
        unit = findViewById(R.id.unit);
        departing = findViewById(R.id.departing);
        arriving = findViewById(R.id.arriving);
        movstatus = findViewById(R.id.movstatus);
        medtstatus = findViewById(R.id.medstatus);
        phone = findViewById(R.id.phone);
        arriving = findViewById(R.id.arriving);
        movremark = findViewById(R.id.movremark);
        remark = findViewById(R.id.medremark1);
        location = findViewById(R.id.location);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);
        totalpeople = findViewById(R.id.totalpeople);

        approve = getResources().getStringArray(R.array.status);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("users").document(admin.snapid)
                        .update(
                                "medRemark",remark.getText().toString()
                        );

                Intent save = new Intent(medical.this,admin.class);
                startActivity(save);
                finish();



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent login = new Intent(medical.this,login.class);
                startActivity(login);
                finish();

            }
        });





        db.collection("users").document(admin.snapid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot d) {
                                              name.setText(d.getString("first")+" "+d.getString("last"));
                                              unit.setText("Unit : "+d.getString("unit"));
                                              zone.setText("Containment Zone : "+d.getString("zone"));
                                              departing.setText("Departure Date : " + d.getString("DepartureDate"));
                                              arriving.setText("Expected Arrival : " +d.getString( "ExpectedArrival"));
                                              movstatus.setText(approve[(int) (long) d.get("movement")]);
                                              medtstatus.setText(approve[(int) (long) d.get("final")]);
                                              movremark.setText("Movement Remark : " +d.getString("movRemark"));
                                              remark.setText(d.getString("Remark"));
                                              location.setText(d.getString("location"));
                                              phone.setText("+91 "+d.getString("phone"));
                                              totalpeople.setText("Number of person : " + (String)d.get("people").toString());


                                          }
                                      }

                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(medical.this, "Failed to read database. Contact support", Toast.LENGTH_SHORT).show();
            }
        });




    }

}
