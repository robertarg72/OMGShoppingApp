package com.centennialcollege.omgshoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.centennialcollege.omgshoppingapp.R;
import com.centennialcollege.omgshoppingapp.model.Product;

import java.util.List;

public class CartListAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> list;

    public CartListAdapter(Context context, List<Product> cartProducts) {
        super(context, R.layout.cart_row, cartProducts);
        this.context = context;
        this.list = cartProducts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        CartListAdapter.ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.cart_row, parent, false);

            // Hold the view objects in an object
            view = new CartListAdapter.ViewHolder();
            view.name = rowView.findViewById(R.id.name);
            view.image = rowView.findViewById(R.id.image);
            view.description = rowView.findViewById(R.id.description);
            view.price = rowView.findViewById(R.id.price);
            view.quantity = rowView.findViewById(R.id.product_quantity);

            rowView.setTag(view);
        } else {
            view = (CartListAdapter.ViewHolder) rowView.getTag();
        }

        // Set data for each view in the row
        Product item = list.get(position);
        view.name.setText(item.getName());
        view.image.setImageBitmap(item.getImage());
        view.description.setText(item.getDescription());
        view.price.setText(item.getPrice());
        view.quantity.setText(String.valueOf(item.getQuantity()));

        return rowView;
    }

    public static class ViewHolder{
        public TextView name;
        public ImageView image;
        TextView description;
        TextView price;
        TextView quantity;
        public TextView category;
    }
    
}