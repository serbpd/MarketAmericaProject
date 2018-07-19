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

public class DetailsParser {

    public static class DetailsPullParser{

        static public Product parseDetails(InputStream inputStream) throws IOException, ParseException {

            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONObject p = new JSONObject(responseStrBuilder.toString());

                Product prod = new Product(p.getString("name"), p.getString("brand"), p.getString("description"),
                        p.getString("imageURL"), p.getString("id"), Double.parseDouble(p.getString("minimumPrice").substring(1)));

                JSONObject r = p.getJSONObject("reviewData");
                prod.setRatingsCount(Double.parseDouble(r.getString("count")));

                if(r.isNull("rating")) {
                    prod.setRatingScore(0.0);
                } else {
                    prod.setRatingScore(r.getDouble("rating"));
                }

                //returns the new product with more details
                return prod;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
