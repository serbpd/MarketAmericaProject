package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    EditText editUsername;
    EditText editPass;
    Button btnLogin;
    Button btnRegister;

    ProgressDialog load;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        editUsername = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        //take user to registration screen
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a loading bar for signing in
                load = new ProgressDialog(WelcomeActivity.this);
                load.setCancelable(false);
                load.setMessage("Logging in");
                load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                load.show();

                //try signing in the user as long as both blanks were filled
                if(!editUsername.getText().toString().isEmpty() || !editPass.getText().toString().isEmpty()) {
                    mAuth.signInWithEmailAndPassword(editUsername.getText().toString(), editPass.getText().toString())
                            .addOnCompleteListener(WelcomeActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("login", "signInWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Log.d("user", mAuth.getCurrentUser().getUid());

                                        //send the user to the home screen
                                        load.dismiss();
                                        Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        load.dismiss();
                                        Log.w("login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
