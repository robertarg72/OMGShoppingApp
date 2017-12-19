package com.ling_argume.omgshoppingapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ling_argume.omgshoppingapp.R;
import com.ling_argume.omgshoppingapp.model.Order;

import java.util.List;

public class OrderListAdapter  extends ArrayAdapter<Order> {
    private final Context context;
    private final List<Order> list;

    public OrderListAdapter(Context context, List<Order> orders) {
        super(context, R.layout.order_row, orders);
        this.context = context;
        this.list = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        OrderListAdapter.ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.order_row, null);

            // Hold the view objects in an object
            view = new OrderListAdapter.ViewHolder();
            view.id = (TextView) rowView.findViewById(R.id.order_id);
            view.date = (TextView) rowView.findViewById(R.id.order_date);
            view.status = (TextView) rowView.findViewById(R.id.order_status);

            rowView.setTag(view);
        } else {
            view = (OrderListAdapter.ViewHolder) rowView.getTag();
        }

        // Set data for each view in the row
        Order item = list.get(position);
        view.id.setText(String.valueOf(item.getId()));
        view.date.setText(item.getOrderDate());
        view.status.setText(item.getStatus());

        return rowView;
    }

    public static class ViewHolder{
        public TextView id;
        public TextView date;
        public TextView status;
    }

}
