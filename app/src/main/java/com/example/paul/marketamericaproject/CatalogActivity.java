package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogActivity extends AppCompatActivity {

    Button btnSearch;
    Button btnBack;
    EditText editSearch;
    ListView listView;
    ImageButton btnSortAsc, btnSortDesc;
    CustomCatalogListAdapter itemList;
    ArrayList<Product> productsArray;
    ProgressDialog load;

    public static final String PRODUCT_OBJ = "a_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        editSearch = findViewById(R.id.editSearch);
        listView = findViewById(R.id.listView);
        btnSortAsc = findViewById(R.id.btnSortAsc);
        btnSortDesc = findViewById(R.id.btnSortDesc);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        load = new ProgressDialog(CatalogActivity.this);
        load.setCancelable(false);
        load.setMessage("Loading catalog");
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.show();

        new ProductAsync(CatalogActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products?publisherID=TEST&locale=en_US&perPage=15&term=");

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

        btnSortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(productsArray);
                itemList.notifyDataSetChanged();
            }
        });

        btnSortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(productsArray);
                Collections.reverse(productsArray);
                itemList.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int pos = position;

                Intent viewProduct = new Intent( getBaseContext(), DetailsActivity.class);

                viewProduct.putExtra(PRODUCT_OBJ, productsArray.get(pos));

                startActivity(viewProduct);
            }
        });
    }

    public void setupProducts(ArrayList<Product> data) {
        productsArray = new ArrayList<>();
        productsArray = data;
        Collections.sort(productsArray);
        itemList = new CustomCatalogListAdapter(CatalogActivity.this, R.layout.catalog_item, productsArray);
        listView.setAdapter(itemList);
        itemList.notifyDataSetChanged();

        load.dismiss();
    }
}
