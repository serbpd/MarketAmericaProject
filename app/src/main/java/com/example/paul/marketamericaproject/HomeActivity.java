package com.example.paul.marketamericaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    Button btnCatalog;
    Button btnFavorites;
    Button btnCart;
    Button btnLogout;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCatalog = findViewById(R.id.btnCatalog);
        btnCart = findViewById(R.id.btnCart);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnLogout = findViewById(R.id.btnLogout);
        txtWelcome = findViewById(R.id.txtWelcome);

        btnCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, CatalogActivity.class);
                startActivity(i);
            }
        });
    }
}
