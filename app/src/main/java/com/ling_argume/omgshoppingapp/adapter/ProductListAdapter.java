package com.ling_argume.omgshoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.ling_argume.omgshoppingapp.R;
import com.ling_argume.omgshoppingapp.model.Product;
import static com.ling_argume.omgshoppingapp.utils.Utils.AVAILABLE_TEXT_PREFIX;

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

			rowView = inflater.inflate(R.layout.product_row, parent, false);

			// Hold the view objects in an object
			view = new ViewHolder();
			view.name = rowView.findViewById(R.id.name);
			view.image = rowView.findViewById(R.id.image);
            view.description = rowView.findViewById(R.id.description);
            view.price = rowView.findViewById(R.id.price);
			view.quantity = rowView.findViewById(R.id.product_availability);

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
		view.quantity.setText(AVAILABLE_TEXT_PREFIX + String.valueOf(item.getQuantity()));

		return rowView;
	}

	public static class ViewHolder{
		public TextView name;
		public ImageView image;
		public TextView description;
		public TextView price;
		public TextView quantity;
		public TextView category;
	}
}
