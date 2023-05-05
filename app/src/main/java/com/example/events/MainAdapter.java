package com.example.events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.FirebaseDatabase;
        import com.orhanobut.dialogplus.DialogPlus;
        import com.orhanobut.dialogplus.ViewHolder;

        import java.util.HashMap;
        import java.util.Map;
        import java.util.Objects;

        import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull MainModel model) {
        holder.Club.setText(model.getClub());
        holder.Location.setText(model.getLocation());
        holder.Date.setText(model.getDate());





        holder.btnEdit.setOnClickListener(v -> {
            final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_pop_up))
                    .setExpanded(true,1300)
                    .create();

            //dialogPlus.show();

            View view = dialogPlus.getHolderView();

            EditText Club= view.findViewById(R.id.txtClub);
            EditText Location= view.findViewById(R.id.txtLocation);
            EditText Date= view.findViewById(R.id.txtDate);


            Button btnUpdate= view.findViewById(R.id.btnUpdate);

            Club.setText(model.getClub());
            Location.setText(model.getLocation());
            Date.setText(model.getDate());


            dialogPlus.show();

            btnUpdate.setOnClickListener(v1 -> {
                Map<String,Object> map= new HashMap<>();
                map.put("Club",Club.getText().toString());
                map.put("Location",Location.getText().toString());
                map.put("Date",Date.getText().toString());


                FirebaseDatabase.getInstance().getReference().child("Events")
                        .child(getRef(position)
                                .getKey()).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.Club.getContext(),"Data Updated Successfully.",Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(holder.Club.getContext(),"Error While Updating.",Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
            });
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog
                        .Builder(holder.Club.getContext());
                builder.setTitle("Are You Sure!");
                builder.setMessage("Deleted Data Can't be Undo.");

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        FirebaseDatabase.getInstance().getReference().child("Events")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });


                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(holder.Club.getContext(),"Cancelled.",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView Club,Location,Date;
        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img= (CircleImageView)itemView.findViewById(R.id.img1);
            Club= itemView.findViewById(R.id.clubtext);
            Location= itemView.findViewById(R.id.locationtext);
            Date= itemView.findViewById(R.id.datetext);

            btnEdit= itemView.findViewById(R.id.btnEdit);
            btnDelete= itemView.findViewById(R.id.btnDelete);

        }
    }


}
