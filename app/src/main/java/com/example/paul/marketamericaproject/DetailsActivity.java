package com.example.paul.marketamericaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    Button btnBack;
    TextView txtName, txtPrice, txtRatings, txtLongDescr;
    ImageView img;
    RatingBar ratingBar;

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

        Intent i = getIntent();
        Product prod = (Product) i.getSerializableExtra(CatalogActivity.PRODUCT_OBJ);

        //new ProductAsync(DetailsActivity.this).execute("https://api.shop.com/AffiliatePublisherNetwork/v1/products/" +
                        //prod.getID() + "?publisherID=TEST&locale=en_US");


        txtName.setText(prod.getName());
        txtPrice.setText("$" + prod.getPrice().toString());
        Picasso.get().load("" + prod.getImageURL() ).into(img);
    }
}
