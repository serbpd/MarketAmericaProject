package com.example.paul.marketamericaproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    Button btnGPS;
    Button btnBack;
    TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btnGPS = findViewById(R.id.btnG);
        btnBack = findViewById(R.id.btnBack);
        txtAddress = findViewById(R.id.txtAddress);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckoutActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        //the button will fetch the user's current location so they don't have to type it in themselves
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(CheckoutActivity.this, Locale.getDefault());

                try {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //this checks to see if the app has location services permissions, and asks if not
                    if (ActivityCompat.checkSelfPermission(CheckoutActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CheckoutActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Location permissions disabled", Toast.LENGTH_LONG).show();
                        //ask for location permission
                        ActivityCompat.requestPermissions(CheckoutActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        return;
                    }
                    //get the user's lat and long coordinates
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    //get the address using lat and long and put the pieces together for the textivew
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); //Here 1 represents max location results to be returned
                    String address = addresses.get(0).getAddressLine(0); //If any additional address line present, check with getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    txtAddress.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Location services unavailable", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
