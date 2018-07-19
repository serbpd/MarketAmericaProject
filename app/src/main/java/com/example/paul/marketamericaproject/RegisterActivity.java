package com.example.paul.marketamericaproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editFName;
    EditText editLName;
    EditText editEmail;
    EditText editPass;
    EditText editPass2;
    Button btnRegister;
    Button btnBack;

    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editFName = findViewById(R.id.editFName);
        editLName = findViewById(R.id.editLName);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);
        editPass2 = findViewById(R.id.editPassRepeat);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference(); //points to the entire database root

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editPass.getText().toString().equals(editPass2.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPass.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("dowork", "createUserWithEmail:success");
                                        Toast.makeText(getApplicationContext(), "New user successfully created", Toast.LENGTH_LONG).show();

                                        Map<String, String> userData = new HashMap<String, String>();
                                        mUserRef = mRootRef.child("users").child(mAuth.getCurrentUser().getUid()); //points to the current user's data

                                        userData.put("name", editFName.getText() + " " + editLName.getText());
                                        userData.put("email", editEmail.getText().toString());
                                        mUserRef.setValue(userData);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("dowork", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPass.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
