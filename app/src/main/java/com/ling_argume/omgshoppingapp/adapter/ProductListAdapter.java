package com.ling_argume.omgshoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ling_argume.omgshoppingapp.R;
import com.ling_argume.omgshoppingapp.model.Product;

import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {
	private final Context context;
	private final List<Product> list;

	public ProductListAdapter(Context context, List<Product> products) {
		super(context, R.layout.product_row, products);
		this.context = context;
		this.list = products;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder view;

		if(rowView == null)
		{
            // Get a new instance of the row layout view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.product_row, null);

			// Hold the view objects in an object
			view = new ViewHolder();
			view.name = (TextView) rowView.findViewById(R.id.name);
			view.image = (ImageView) rowView.findViewById(R.id.image);
            view.description = (TextView) rowView.findViewById(R.id.description);
            view.price = (TextView) rowView.findViewById(R.id.price);

			rowView.setTag(view);
		} else {
			view = (ViewHolder) rowView.getTag();
		}

		// Set data for each view in the row
		Product item = list.get(position);
		view.name.setText(item.getName());
		view.image.setImageBitmap(item.getImage());
		view.description.setText(item.getDescription());
		view.price.setText(item.getPrice());

		return rowView;
	}

	public static class ViewHolder{
		public TextView name;
		public ImageView image;
		public TextView description;
		public TextView price;
		public int quantity;
		public TextView category;
	}
}
