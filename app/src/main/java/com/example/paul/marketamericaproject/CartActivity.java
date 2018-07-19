package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView listView;
    Button btnBack;
    Button btnCheckout;

    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    DatabaseReference mUserRef;
    Cart cart;
    ArrayList<Product> productsArray;
    CustomCatalogListAdapter itemList;
    int removeIndex;
    int cartSize = 0;
    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.listViewCart);
        btnBack = findViewById(R.id.btnBack);
        btnCheckout = findViewById(R.id.btnCheckout);
        productsArray = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference(); //points to the entire database root
        mUserRef = mRootRef.child("users").child(mAuth.getCurrentUser().getUid()); //points to the current user's data

        load = new ProgressDialog(CartActivity.this);
        load.setCancelable(false);
        load.setMessage("Loading your cart");
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.show();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(i);
            }
        });

        //checks to see contents of user's cart
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //if cart items exist, put them into an array and contact the details API to grab data for each item
                if (snapshot.hasChild("cart")) {
                    Cart cart = snapshot.child("cart").getValue(Cart.class);

                    ArrayList<String> cartIDs = cart.getContents();
                    cartSize = cartIDs.size();

                    Log.d("cartsize", "" + cartSize);

                    if(cartSize > 0) {
                        for (int i = 0; i < cartIDs.size(); i++) {
                            new CartAsync(CartActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products/" +
                                    cart.getID(i) + "?publisherID=TEST&locale=en_US");
                        }
                    }
                } else {
                    load.dismiss();
                    Toast.makeText(getApplicationContext(), "No cart items to display", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    //gets called by the CartAsync to put each Product into the listview
    public void buildCart(Product prod) {
        productsArray.add(prod);

        //once all products are retrieved, show them in the listview
        if(productsArray.size() == cartSize) {
            itemList = new CustomCatalogListAdapter(CartActivity.this, R.layout.catalog_item, productsArray);
            listView.setAdapter(itemList);
            itemList.notifyDataSetChanged();
            load.dismiss();

            //long tapping will remove the item from the firebase database
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    removeIndex = i;
                    mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild("cart")) {
                                //remove item from user's database cart
                                Cart cart = snapshot.child("cart").getValue(Cart.class);
                                cart.removeItem(removeIndex);
                                mUserRef.child("cart").setValue(cart);

                                //remove item from local productsArray
                                productsArray.remove(removeIndex);

                                itemList.notifyDataSetChanged();
                            }
                            Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                    return false;
                }
            });
        }
    }

}
