package com.example.paul.marketamericaproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

public class ProductParser {

    public static class ProductPullParser{

        static public ArrayList<Product> parseProducts(InputStream inputStream) throws IOException, ParseException {
            ArrayList<Product> products = new ArrayList<>();

            //takes in JSON from a product and turns it into a Product object to display in the catalog
            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                //goes through and stores the whole JSON
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                //gets the entire list of products
                JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("products");

                //goes through each product in JSON and creates a Product object using each node of info
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject p = jsonArray.getJSONObject(i);
                    Product prod = new Product(p.getString("name"), p.getString("brand"), p.getString("description"),
                            p.getString("imageUrl"), p.getString("id"), Double.parseDouble(p.getString("minimumPrice").substring(1)));

                    products.add(prod);
                }

                //returns the array of parsed products
                return products;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
