package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    ListView listView;
    Button btnBack, btnRefresh;
    ProgressDialog load;
    CustomCatalogListAdapter itemList;
    ArrayList<Product> productsArray;
    Set<String> faveIDList;

    FirebaseUser user;
    private FirebaseAuth mAuth;
    int numFaves;

    public static final String PRODUCT_OBJ = "a_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.btnBack);
        btnRefresh = findViewById(R.id.btnRefresh);
        productsArray = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FavoritesActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        //create a loading bar
        load = new ProgressDialog(FavoritesActivity.this);
        load.setCancelable(false);
        load.setMessage("Loading your favorites");
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.show();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //retrieve the user's favorites list
        SharedPreferences pref = this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        faveIDList = pref.getStringSet("favorites", null);
        if(faveIDList != null) {
            numFaves = faveIDList.size();
        } else {
            numFaves = 0;
        }

        //check if favorites exist, then put them into an array to send each ID to the API to grab their info
        if(faveIDList != null && numFaves > 0) {
            for(int i = 0; i < faveIDList.size(); i++) {
                String[] IDArray = faveIDList.toArray(new String[faveIDList.size()]);
                Log.d("faves", faveIDList.toString());

                new CartAsync(FavoritesActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products/" +
                        IDArray[i] + "?publisherID=TEST&locale=en_US");
            }
        } else {
            //if there are no favorites
            load.dismiss();
            Toast.makeText(getApplicationContext(), "No favorites to display", Toast.LENGTH_LONG).show();
        }

        //bring up a product's details when clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;

                Intent viewProduct = new Intent( getBaseContext(), DetailsActivity.class);

                viewProduct.putExtra(PRODUCT_OBJ, productsArray.get(pos));

                startActivity(viewProduct);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
    }

    //used by CartAsync to build an array of Products and display them in the listview
    public void buildFaves(Product prod) {
        productsArray.add(prod);

        if(productsArray.size() == numFaves) {
            itemList = new CustomCatalogListAdapter(FavoritesActivity.this, R.layout.catalog_item, productsArray);
            listView.setAdapter(itemList);
            itemList.notifyDataSetChanged();
            load.dismiss();
        }
    }
}
