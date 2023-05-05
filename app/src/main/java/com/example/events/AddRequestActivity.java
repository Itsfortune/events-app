package com.example.events;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class AddRequestActivity extends AppCompatActivity {

    EditText Club,Location,Date;
    Button btnRequest,btnBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        Club = (EditText)findViewById(R.id.txtClub);
        Location = (EditText)findViewById(R.id.txtLocation);
        Date = (EditText)findViewById(R.id.txtDate);

        btnRequest=(Button) findViewById(R.id.btnRequest);
        btnBack=(Button) findViewById(R.id.btnBack);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                clearAll();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
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
            Toast.makeText(AddRequestActivity.this, "Please Fill the Club Name, Location and Date.", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase.getInstance().getReference().child("Request").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AddRequestActivity.this, "Data Inserted Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(AddRequestActivity.this, "Error While Inserting Data.", Toast.LENGTH_SHORT).show();
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
