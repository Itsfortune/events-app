package com.example.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText Club,Location,Date;
    Button btnAdd,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Club = (EditText)findViewById(R.id.txtClub);
        Location = (EditText)findViewById(R.id.txtLocation);
        Date = (EditText)findViewById(R.id.txtDate);

        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnBack=(Button) findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                clearAll();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("Club", Club.getText().toString());
        map.put("Location", Location.getText().toString());
        map.put("Date", Date.getText().toString());

        if (Club.length() == 0) {
            Toast.makeText(AddActivity.this, "Please Fill the Club Name, Location and Date.", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase.getInstance().getReference().child("Events").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AddActivity.this, "Data Inserted Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(AddActivity.this, "Error While Inserting Data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void clearAll()
    {
        Club.setText("");
        Location.setText("");
        Date.setText("");
    }
}
