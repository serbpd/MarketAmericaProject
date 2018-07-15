package com.example.paul.marketamericaproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class ProductAsync extends AsyncTask<String, Void, ArrayList<Product>> {

    CatalogActivity activity;
    ArrayList<Product> prodList;

    public ProductAsync(CatalogActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Product> doInBackground(String ... params) {

        HttpURLConnection connection = null;
        ArrayList<Product> result;

        prodList = new ArrayList<>();

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("apikey", "l7xx27cc84ed751d4f90b732d87240b2e0fa");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = ProductParser.ProductPullParser.parseProducts(connection.getInputStream());
                prodList = result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return prodList;
    }

    @Override
    protected void onPostExecute(ArrayList<Product> products)
    {
        if (products.size() > 0 && products != null)
        {
            Log.d("demo", products.toString());
            activity.setupProducts(products);
        }

        super.onPostExecute(products);
    }
}
