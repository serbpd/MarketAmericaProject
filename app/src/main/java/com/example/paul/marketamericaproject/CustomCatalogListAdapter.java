package com.example.paul.marketamericaproject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class CustomCatalogListAdapter extends ArrayAdapter<Product> {

    public CustomCatalogListAdapter(Context context, int resource, ArrayList<Product> prodList) {
        super(context, resource, prodList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Product prod = getItem(position);
        ViewHolder viewHolder;

        if ( convertView == null ) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_item, null);

            viewHolder.imgProd = (ImageView) convertView.findViewById(R.id.imgProd);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            viewHolder.txtShortDescr = (TextView) convertView.findViewById(R.id.txtDescr);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if ( prod.getImageURL() != null && !TextUtils.isEmpty(prod.getImageURL())) {
            Picasso.get().load("" + prod.getImageURL() ).into(viewHolder.imgProd);
        }
        else {
            viewHolder.imgProd.setImageResource(android.R.drawable.presence_offline);
        }

        if ( prod.getName() != null && !TextUtils.isEmpty(prod.getName() )) {
            viewHolder.txtName.setText(prod.getName());
        }
        else {
            viewHolder.txtName.setText("No Name");
        }

        if ( prod.getPrice() != null && !TextUtils.isEmpty(prod.getPrice().toString() )) {
            viewHolder.txtPrice.setText("$" + prod.getPrice());
        }
        else {
            viewHolder.txtPrice.setText("N/A");
        }

        if ( prod.getShortDescr() != null && !TextUtils.isEmpty(prod.getShortDescr() )) {
            viewHolder.txtShortDescr.setText(prod.getShortDescr());
        }
        else {
            viewHolder.txtShortDescr.setText("No Description");
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imgProd;
        TextView txtName;
        TextView txtPrice;
        TextView txtShortDescr;
    }
}
