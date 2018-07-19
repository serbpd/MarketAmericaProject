package com.example.paul.marketamericaproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutActivity extends AppCompatActivity {

    Button btnGPS, btnBack;
    TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //planned to add GPS address locating
        btnGPS.findViewById(R.id.btnGPS);
        btnBack.findViewById(R.id.btnBack);
        txtAddress.findViewById(R.id.txtAddress);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckoutActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
    }
}
