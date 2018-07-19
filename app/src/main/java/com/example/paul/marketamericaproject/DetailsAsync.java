package com.example.paul.marketamericaproject;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.Parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class DetailsAsync extends AsyncTask<String, Void, Product> {

    DetailsActivity activity;
    Product product;

    public DetailsAsync(DetailsActivity activity) {
        this.activity = activity;
    }

    //contacts the API to retrieve product data
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
                Log.d("demo", "result from async" + result.toString());
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

    //sends the retrieved Product back to the DetailsActivity
    @Override
    protected void onPostExecute(Product product) {
        activity.setupScreen(product);

        super.onPostExecute(product);
    }

    //method that transforms data retrieved from the API into a Product object
    public Product copy(Product p) throws ParseException {
        Product newProd = new Product(p.getName(), p.getBrand(), p.getShortDescr(), p.getImageURL(), p.getID(), p.getPrice());
        newProd.setRatingsCount(p.getRatingsCount());
        newProd.setRatingScore(p.getRatingScore());

        return newProd;
    }

}
