package com.example.cmconline;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;


public class admin extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db,db2;
    private NodeAdapter adapter;
    public static String snapid;
    TextView name,unit,label;
    public int usertype;
    String unitstr;
    Button logout;
    LoadingDialog loadingDialog = new LoadingDialog((admin.this));
    Spinner mov,fin,zonespinner,pspinner;
    RelativeLayout disabled;
    int movfiter = 0,finalfilter=0;
    String zonefilter = "Zones",peoplefilter="People";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();


        name = findViewById(R.id.Name);
        unit = findViewById(R.id.Unit);
        label = findViewById(R.id.Label);
        logout = findViewById(R.id.logoutad);
        mov = findViewById(R.id.filtermov);
        fin = findViewById(R.id.filterfinal);

        zonespinner = findViewById(R.id.filterzone);
        pspinner = findViewById(R.id.filterpeople);
        disabled =findViewById(R.id.backfilter);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        unitstr = pref.getString("unit",null);
        CollectionReference dbref = db2.collection("users");
        Query queryinit = dbref.whereEqualTo("unit",unitstr);








        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent login= new Intent(admin.this,login.class);
                startActivity(login);
                finish();
            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.stat,R.layout.simple_spinner);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mov.setAdapter(adapter1);
        mov.setSelection(0);
        mov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if(position == 1 | position == 3 ){

                    fin.setSelection(0);
                    fin.setEnabled(false);


                }
                else {
                    fin.setEnabled(true);

                }

                movfiter = position;
                queryMaker();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.stat,R.layout.simple_spinner);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        fin.setAdapter(adapter2);
        fin.setSelection(0);
        fin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                finalfilter = position;
                queryMaker();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.people,R.layout.simple_spinner);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        pspinner.setAdapter(adapter3);
        pspinner.setSelection(0);
        pspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                peoplefilter = parent.getItemAtPosition(position).toString();
                queryMaker();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.zonefilter,R.layout.simple_spinner);
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown_item);
        zonespinner.setAdapter(adapter4);
        zonespinner.setSelection(0);
        zonespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                zonefilter = parent.getItemAtPosition(position).toString();
                queryMaker();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        db.collection("admin").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot d) {


                                              unitstr = d.getString("unit");
                                              usertype = (int)(long)d.get("type");
                                              name.setText(d.getString("first")+" "+d.getString("last"));
                                              unit.setText(unitstr);

                                              if(usertype==1|usertype==3){
                                                  label.setText("Approving Officer");
                                                  if(usertype==3){label.setText("Approving Officer*");

                                                  }

                                              }
                                              if(usertype==2 ){
                                                  label.setText("Medical Officer");

                                              }




                                          }
                                      }

                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(admin.this, "Failed to read database. Contact support", Toast.LENGTH_SHORT).show();
            }
        });


        setRecycler(queryinit);
        adapter.setOnItemClickListner(new NodeAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                snapid = documentSnapshot.getId();
                if(usertype == 1) {
                    Intent approve = new Intent(admin.this, userapprove.class);
                    startActivity(approve);
                    loadingDialog.dismissDialog();
                    finish();
                }
                if(usertype == 2){
                    Intent medical = new Intent(admin.this, medical.class);
                    startActivity(medical);
                    finish();
                }




            }
        });






    }


    private void queryMaker(){

        Query query = db2.collection("users").whereEqualTo("unit",unitstr);

            if (movfiter == 0 & finalfilter != 0 ){


                if (!zonefilter.equals("Zones")){

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("final",finalfilter - 1).whereEqualTo("zone", zonefilter).whereEqualTo("people", peoplefilter);
                    }else{
                        query = query.whereEqualTo("final",finalfilter - 1).whereEqualTo("zone", zonefilter);
                    }
                }else{

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("final",finalfilter - 1).whereEqualTo("people", peoplefilter);
                    }
                    else{
                        query = query.whereEqualTo("final",finalfilter - 1);
                    }
                }

            }
            else if (finalfilter != 0 & movfiter != 0 ){


                if (!zonefilter.equals("Zones")){

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("movement", movfiter -1).whereEqualTo("final", finalfilter-1).whereEqualTo("zone", zonefilter).whereEqualTo("people", peoplefilter);
                    }
                    else{
                        query = query.whereEqualTo("movement", movfiter -1).whereEqualTo("final", finalfilter-1).whereEqualTo("zone", zonefilter);
                    }
                }else{

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("movement", movfiter -1).whereEqualTo("final", finalfilter-1).whereEqualTo("people", peoplefilter);
                    }else{
                        query = query.whereEqualTo("movement", movfiter -1).whereEqualTo("final", finalfilter-1);

                    }
                }

            }
            else if (movfiter != 0 & finalfilter == 0){


                if (!zonefilter.equals("Zones")){

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("movement", movfiter-1).whereEqualTo("zone", zonefilter).whereEqualTo("people", peoplefilter);
                    }else{
                        query = query.whereEqualTo("movement", movfiter-1).whereEqualTo("zone", zonefilter);
                    }
                }else{

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("movement", movfiter-1).whereEqualTo("people", peoplefilter);
                    }else{
                        query = query.whereEqualTo("movement", movfiter-1);
                    }
                }

            }
            else if(movfiter ==0 & finalfilter == 0){


                if (!zonefilter.equals("Zones")){

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("zone", zonefilter).whereEqualTo("people", peoplefilter);
                    }
                    else{
                        query = query.whereEqualTo("zone", zonefilter);
                    }
                }else{

                    if (!peoplefilter.equals("People")){
                        query = query.whereEqualTo("people", peoplefilter);
                    }
                }



            }



            if (!peoplefilter.equals("People")){
                query = query.whereEqualTo("people", peoplefilter);
            }








        setRecycler(query);
        adapter.startListening();
        adapter.setOnItemClickListner(new NodeAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                snapid = documentSnapshot.getId();
                if(usertype == 1) {
                    Intent approve = new Intent(admin.this, userapprove.class);
                    startActivity(approve);
                    loadingDialog.dismissDialog();
                    finish();
                }
                if(usertype == 2){
                    Intent medical = new Intent(admin.this, medical.class);
                    startActivity(medical);
                    finish();
                }




            }
        });

    }

    private void setRecycler(Query query){
        loadingDialog.startLoadingDialog();
        FirestoreRecyclerOptions<Userslist> options = new FirestoreRecyclerOptions.Builder<Userslist>()
                .setQuery(query,Userslist.class)
                .build();

        adapter = new NodeAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadingDialog.dismissDialog();
    }


    @Override
    protected void onStart(){
        loadingDialog.startLoadingDialog();
        super.onStart();
        adapter.startListening();
        loadingDialog.dismissDialog();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}