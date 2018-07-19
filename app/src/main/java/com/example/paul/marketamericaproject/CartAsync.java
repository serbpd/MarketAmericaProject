package com.example.paul.marketamericaproject;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class CartAsync extends AsyncTask<String, Void, Product> {

    CartActivity activity;
    FavoritesActivity activityFaves;
    Product product;

    public CartAsync(CartActivity activity) {
        this.activity = activity;
    }

    public CartAsync(FavoritesActivity activity) {
        this.activityFaves = activity;
    }

    @Override
    protected Product doInBackground(String ... params) {

        HttpURLConnection connection = null;
        Product result;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("apikey", "l7xx27cc84ed751d4f90b732d87240b2e0fa");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = DetailsParser.DetailsPullParser.parseDetails(connection.getInputStream());
                product = copy(result);
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

        return product;
    }

    @Override
    protected void onPostExecute(Product product) {
        if(activity != null) {
            activity.buildCart(product);
        } else {
            activityFaves.buildFaves(product);
        }

        super.onPostExecute(product);
    }

    public Product copy(Product p) throws ParseException {
        Product newProd = new Product(p.getName(), p.getBrand(), p.getShortDescr(), p.getImageURL(), p.getID(), p.getPrice());
        newProd.setRatingsCount(p.getRatingsCount());
        newProd.setRatingScore(p.getRatingScore());

        return newProd;
    }
}
