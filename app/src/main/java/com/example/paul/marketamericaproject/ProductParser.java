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

            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("products");

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
