package com.example.cmconline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NodeAdapter extends FirestoreRecyclerAdapter<Userslist,NodeAdapter.NodeHolder> {
    private OnItemClickListner listner;

    public NodeAdapter(@NonNull FirestoreRecyclerOptions<Userslist> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull NodeHolder nodeHolder, int i, @NonNull Userslist userslist) {
        nodeHolder.dep.setText("Departure Date\n"+userslist.getDepartureDate());
        nodeHolder.exp.setText("Expected Arrival\n"+userslist.getExpectedArrival());
        nodeHolder.name.setText(userslist.getFirst()+" "+(userslist.getLast()));
        nodeHolder.unit.setText("Unit : "+userslist.getUnit());
        nodeHolder.zone.setText("Zone : "+userslist.getZone());

    }

    @NonNull
    @Override
    public NodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);


        return new NodeHolder(v);
    }

    class NodeHolder extends RecyclerView.ViewHolder{

        TextView dep,exp,name,unit,zone;

        public NodeHolder(@NonNull View itemView) {
            super(itemView);

            dep = itemView.findViewById(R.id.depdate);
            exp = itemView.findViewById(R.id.arrdate);
            name = itemView.findViewById(R.id.name);
            unit = itemView.findViewById(R.id.unit);
            zone = itemView.findViewById(R.id.zone);

            itemView.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner !=null){
                        listner.onItemClick(getSnapshots().getSnapshot(position),position);

                    }

                }
            });

        }
    }

    public interface OnItemClickListner{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);


    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;



    }
}
