package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogActivity extends AppCompatActivity {

    Button btnSearch;
    Button btnBack;
    EditText editSearch;
    ListView listView;
    CustomCatalogListAdapter itemList;
    ArrayList<Product> productsArray;

    public static final String PRODUCT_OBJ = "a_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        editSearch = findViewById(R.id.editSearch);
        listView = findViewById(R.id.listView);

        new ProductAsync(CatalogActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products?publisherID=TEST&locale=en_US&perPage=15&term=laundry");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CatalogActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProductAsync(CatalogActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products?publisherID=TEST&locale=en_US&perPage=15&term="
                + editSearch.getText());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int pos = position;

                Intent viewPodcast = new Intent( getBaseContext(), DetailsActivity.class);

                viewPodcast.putExtra(PRODUCT_OBJ, productsArray.get(pos));

                startActivity(viewPodcast);
            }
        });
    }

    public void setupProducts(ArrayList<Product> data) {
        productsArray = new ArrayList<>();
        productsArray = data;
        Collections.sort(productsArray);
        itemList = new CustomCatalogListAdapter(CatalogActivity.this, R.layout.catalog_item, productsArray);
        listView.setAdapter(itemList);

        hideKeyboard(CatalogActivity.this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
