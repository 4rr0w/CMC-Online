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

public class admin extends AppCompatActivity  {
    private RecyclerView list;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference userref;
    private NodeAdapter adapter;
    public static String snapid;
    TextView name,unit,label;
    public String unit_str;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        list = findViewById(R.id.recycler);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

        db.collection("admin").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot d) {
                                              unit_str = d.getString("unit");
                                              name.setText(d.getString("first")+" "+d.getString("last"));
                                              unit.setText("Unit : " + unit_str);

                                              if((int)(long)d.get("type")==1){
                                                  label.setText("Approver");

                                              }
                                              if((int)(long)d.get("type")==2){
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

        adapter.setOnItemClickListner(new NodeAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                snapid = documentSnapshot.getId();
                Intent approve= new Intent(admin.this,userapprove.class);
                startActivity(approve);
                finish();



            }
        });



    }



    private void setRecycler(){

        Query query = db.collection("users");

        FirestoreRecyclerOptions<Userslist> options = new FirestoreRecyclerOptions.Builder<Userslist>()
                .setQuery(query,Userslist.class)
                .build();

        adapter = new NodeAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
