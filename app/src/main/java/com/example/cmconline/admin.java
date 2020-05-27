package com.example.cmconline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.example.cmconline.login.unit_;

public class admin extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db,db2;
    private NodeAdapter adapter;
    public static String snapid;
    TextView name,unit,label;
    public String unit_str;
    public int usertype;
    Button logout;
    LoadingDialog loadingDialog = new LoadingDialog((admin.this));


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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent login= new Intent(admin.this,login.class);
                startActivity(login);
                finish();
            }
        });

        setRecycler();

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

    private void setRecycler(){
        loadingDialog.startLoadingDialog();

        CollectionReference dbref = db2.collection("users");
        Query query;
        if (usertype != 3) {
             query = dbref.whereEqualTo("unit", unit_);
        }
        else{
            query = dbref.orderBy("movement");
        }


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
        db.collection("admin").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot d) {
                                              unit_str = d.getString("unit");
                                              usertype = (int)(long)d.get("type");
                                              name.setText(d.getString("first")+" "+d.getString("last"));
                                              unit.setText("Unit : " + unit_str);

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

        adapter.startListening();
        loadingDialog.dismissDialog();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
