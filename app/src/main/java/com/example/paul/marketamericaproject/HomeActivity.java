package com.example.paul.marketamericaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    Button btnCatalog;
    Button btnFavorites;
    Button btnCart;
    Button btnLogout;
    TextView txtWelcome;
    ImageView img;

    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    DatabaseReference mUserRef;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCatalog = findViewById(R.id.btnCatalog);
        btnCart = findViewById(R.id.btnCart);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnLogout = findViewById(R.id.btnLogout);
        txtWelcome = findViewById(R.id.txtWelcome);
        img = findViewById(R.id.imgBtn);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference(); //points to the entire database root
        mUserRef = mRootRef.child("users").child(mAuth.getCurrentUser().getUid()); //points to the current user's data

        btnCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, CatalogActivity.class);
                startActivity(i);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, FavoritesActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(i);
            }
        });

        //clicking on the image will let the user take a picture and set their own image there
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if camera permission is granted
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Camera permissions disabled", Toast.LENGTH_LONG).show();
                    //ask for camera permission
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                //start the camera up
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        //get the user's name from firebase to give them a welcome message
        mUserRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                txtWelcome.setText("Welcome " + name + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(pic);
        }
    }
}
