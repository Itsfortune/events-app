package com.example.events;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView haveAccount;
    EditText inputEmail,inputPassword,inputConfirmPassword,inputFullName,inputPhone;
    Button btnRegister;
    CheckBox isAdmin,isUser;
    String emailPattern= "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mStore;
    Boolean valid = true;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        haveAccount = findViewById(R.id.alreadyHaveAccount);
        btnRegister =findViewById(R.id.btnRegister);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputFullName=findViewById(R.id.inputFullName);
        inputPhone=findViewById(R.id.inputPhone);
        inputPassword=findViewById(R.id.inputPassword);
        inputConfirmPassword=findViewById(R.id.inputConfirmPassword);
        isAdmin=findViewById(R.id.isAdmin);
        isUser=findViewById(R.id.isUser);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mStore=FirebaseFirestore.getInstance();


        isUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isAdmin.setChecked(false);
                }
            }
        });

        isAdmin.setVisibility(View.GONE);
        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isUser.setChecked(false);
                }
            }
        });


        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });
    }
    private void PerforAuth(){
        String email=inputEmail.getText().toString();

        String password=inputPassword.getText().toString();
        String confirmPassword=inputConfirmPassword.getText().toString();

        if(!(isAdmin.isChecked()||isUser.isChecked())){
            Toast.makeText(getApplicationContext(),"Select The Account Type!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"Enter Correct Email.",Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()||password.length()<6)
        {
            Toast.makeText(getApplicationContext(),"Enter Correct Password.",Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(),"Password Don't Match.",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Please Wait While Registration.....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            if (!(inputFullName.getText().toString().isEmpty() && inputEmail.getText().toString().isEmpty() && inputPassword.getText().toString().isEmpty())) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user=mAuth.getCurrentUser();
                            DocumentReference df=mStore.collection("Events").document(user.getUid());
                            Map<String,Object> userInfo=new HashMap<>();
                            userInfo.put("FullName",inputFullName.getText().toString());
                            userInfo.put("Email",inputEmail.getText().toString());
                            userInfo.put("Phone",inputPhone.getText().toString());
                            userInfo.put("Password",inputPassword.getText().toString());

                            if (isAdmin.isChecked()){
                                setVisible(false);
                                userInfo.put("isAdmin","1");

                            }
                            if (isUser.isChecked()){
                                setVisible(false);
                                userInfo.put("isUser","1");
                            }

                            df.set(userInfo);

                            progressDialog.dismiss();


                            sendUserToNextActivity();

                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Incorrect details, try again" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        }

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
