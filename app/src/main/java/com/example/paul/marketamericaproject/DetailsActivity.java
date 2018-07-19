package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailsActivity extends AppCompatActivity {

    Button btnBack;
    TextView txtName, txtPrice, txtRatings, txtLongDescr;
    ImageView img;
    RatingBar ratingBar;
    ImageButton btnCart, btnFave;
    ProgressDialog load;

    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    DatabaseReference mUserRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btnBack = findViewById(R.id.btnBack);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtRatings = findViewById(R.id.txtRatings);
        txtLongDescr = findViewById(R.id.txtDescr);
        img = findViewById(R.id.imgProd);
        ratingBar = findViewById(R.id.ratingBar);
        btnCart = findViewById(R.id.btnCart);
        btnFave = findViewById(R.id.btnFave);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference(); //points to the entire database root
        mUserRef = mRootRef.child("users").child(mAuth.getCurrentUser().getUid()); //points to the current user's data
        user = mAuth.getCurrentUser();

        load = new ProgressDialog(DetailsActivity.this);
        load.setCancelable(false);
        load.setMessage("Loading details");
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.show();

        Intent i = getIntent();
        final Product prod = (Product) i.getSerializableExtra(CatalogActivity.PRODUCT_OBJ);

        new DetailsAsync(DetailsActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products/" +
                        prod.getID() + "?publisherID=TEST&locale=en_US");

        //check product status and set favorite button on or off as needed
        SharedPreferences pref = this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        if (pref.getStringSet("favorites", null) == null) {
            btnFave.setImageResource(R.drawable.star_off);
        } else {
            Set<String> faveIDList = pref.getStringSet("favorites", null);
            if(faveIDList.contains(prod.getID())) {
                btnFave.setImageResource(R.drawable.star_on);
            } else {
                btnFave.setImageResource(R.drawable.star_off);
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild("cart")) {
                            Cart cart = snapshot.child("cart").getValue(Cart.class);
                            cart.addItem(prod.getID());
                            mUserRef.child("cart").setValue(cart);
                        } else {
                            Cart cart = new Cart();

                            cart.addItem(prod.getID());
                            mUserRef.child("cart").setValue(cart);
                        }
                        Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

            }
        });

        btnFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = readState(prod);

                if (isFavorite) {
                    btnFave.setImageResource(R.drawable.star_off);
                    isFavorite = false;
                    saveState(isFavorite, prod);
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_LONG).show();
                } else {
                    btnFave.setImageResource(R.drawable.star_on);
                    isFavorite = true;
                    saveState(isFavorite, prod);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void setupScreen(Product prod) {
        txtName.setText(prod.getName());
        txtPrice.setText("$" + prod.getPrice().toString());
        txtRatings.setText(prod.getRatingsCount().toString() + " reviews");
        txtLongDescr.setText(Html.fromHtml(prod.getShortDescr()));
        Picasso.get().load("" + prod.getImageURL() ).resize(120, 120).centerCrop().into(img);

        ratingBar.setRating(prod.getRatingScore().floatValue());

        load.dismiss();
    }

    private void saveState(boolean isFavorite, Product p) {
        SharedPreferences pref = this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);

        if(isFavorite) {
            if (pref.getStringSet("favorites", null) == null) {
                Set<String> faveIDList = new HashSet<>();
                faveIDList.add(p.getID());

                SharedPreferences.Editor prefEdit = pref.edit();
                prefEdit.putStringSet("favorites", faveIDList);
                prefEdit.commit();
            } else {
                Set<String> faveIDList = pref.getStringSet("favorites", null);
                faveIDList.add(p.getID());

                SharedPreferences.Editor prefEdit = pref.edit();
                prefEdit.putStringSet("favorites", faveIDList);
                prefEdit.commit();
            }
        } else {
            Set<String> faveIDList = pref.getStringSet("favorites", null);
            faveIDList.remove(p.getID());

            SharedPreferences.Editor prefEdit = pref.edit();
            prefEdit.putStringSet("favorites", faveIDList);
            prefEdit.commit();
        }

    }

    private boolean readState(Product p) {
        SharedPreferences pref = this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        if (pref.getStringSet("favorites", null) == null) {
            return false;
        }
        else if(pref.getStringSet("favorites", null).contains(p.getID())) {
            return true;
        } else {
            return false;
        }
    }
}
