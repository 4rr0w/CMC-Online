package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class profile extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id = mAuth.getUid();

    TextView name,unit,zone,location,departing,arriving,movstatus,medtstatus,phone,movremark,medremark,totalpeople;
    Button logout;
    String[] approve;
    LoadingDialog loadingDialog = new LoadingDialog((profile.this));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadingDialog.startLoadingDialog();

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
        medremark = findViewById(R.id.medremark);
        location = findViewById(R.id.location);
        logout = findViewById(R.id.logout);
        totalpeople = findViewById(R.id.totalpeople);

        approve = getResources().getStringArray(R.array.status);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent login = new Intent(profile.this,login.class);
                startActivity(login);
                finish();

            }
        });





        db.collection("users").document(id).get()
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
                                            medremark.setText("Medical Remark : "+d.getString("Remark"));
                                            location.setText(d.getString("location"));
                                            phone.setText("+91 "+d.getString("phone"));
                                            totalpeople.setText("Number of person : " + (String)d.get("people").toString());


                                          }
                                      }

                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Failed to read database. Contact support", Toast.LENGTH_SHORT).show();
            }
        });

        loadingDialog.dismissDialog();

    }

}
