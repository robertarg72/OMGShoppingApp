package com.ling_argume.omgshoppingapp.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ling_argume.omgshoppingapp.DatabaseContract;
import com.ling_argume.omgshoppingapp.DatabaseManager;
import com.ling_argume.omgshoppingapp.R;
import com.ling_argume.omgshoppingapp.model.Order;

import java.util.List;

import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_NUMBER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_OWNER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_TYPE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_ORDER_DATE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_ORDER_QUANTITY;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_STATUS;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry._ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_DELIVERED_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_ID_PREFIX;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_IN_PROCESS_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;


public class OrdersManagementListAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final List<Order> list;
    Order item;
    public DatabaseManager dbm;


    public OrdersManagementListAdapter(Context context, List<Order> orders) {
        super(context, R.layout.order_management_row, orders);
        this.context = context;
        this.list = orders;
    }

    public void setDatabaseManager(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final OrdersManagementListAdapter.ViewHolder view;

        // Get employeeId and bring all orders belonging to all customer
        String employeeId = getFromSharedPreferences(context, SHARED_PREFERENCES_EMPLOYEE_ID);

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.order_management_row, parent, false);

            // Hold the view objects in an object
            view = new OrdersManagementListAdapter.ViewHolder();
            view.prefix = rowView.findViewById(R.id.order_id_prefix);
            view.id = rowView.findViewById(R.id.order_id);
            view.date = rowView.findViewById(R.id.order_date);
            view.status = rowView.findViewById(R.id.order_status);
            view.switchButton = rowView.findViewById(R.id.status_switch_button);
            view.switchButton.setTag(position);

            rowView.setTag(view);
        } else {
            view = (OrdersManagementListAdapter.ViewHolder) rowView.getTag();
        }

        // Set data for each view in the row
        item = list.get(position);
        view.prefix.setText(ORDER_ID_PREFIX);
        view.id.setText(String.valueOf(item.getId()));
        view.date.setText(item.getOrderDate());
        view.status.setText(item.getStatus());

        // Make sure the onCheckedChangedListener wont be fired during initialization
        view.switchButton.setOnCheckedChangeListener(null);

        // Set different color and text for different status
        int statusColor;
        if( item.getStatus().compareTo(ORDER_DELIVERED_TEXT) == 0) {
            statusColor = R.color.colorDelivered;
            view.switchButton.setChecked(true);
        }
        else {
            statusColor = R.color.colorInProcess;
            view.switchButton.setChecked(false);
        }
        view.status.setTextColor(context.getResources().getColor((statusColor)));

        // Now we set onCheckedChangeListener
        view.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                int statusColor;
                String newStatus;

                // Get current item position and retrieve order information
                int switchPosition = (int)compoundButton.getTag();
                Order currentOrder = list.get(switchPosition);

                if (bChecked) {
                    statusColor = R.color.colorDelivered;
                    newStatus = ORDER_DELIVERED_TEXT;

                } else {
                    statusColor = R.color.colorInProcess;
                    newStatus = ORDER_IN_PROCESS_TEXT;
                }
                currentOrder.setStatus(newStatus);
                view.status.setTextColor(context.getResources().getColor((statusColor)));
                view.status.setText(newStatus);
                updateOrderStatusInDB(currentOrder, newStatus);
            }
        });

        return rowView;
    }

    private void updateOrderStatusInDB(Order currentOrder, String newStatus) {

        String fields[] = { _ID, COLUMN_CUSTOMER_ID, COLUMN_PRODUCT_ID, COLUMN_EMPLOYEE_ID,
                COLUMN_ORDER_QUANTITY, COLUMN_SHIPPING_ADDRESS, COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER,
                COLUMN_CARD_OWNER, COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS
        };

        String record[] = new String[14];
        record[0] = String.valueOf(currentOrder.getId());
        record[1] = String.valueOf(currentOrder.getCustomerId());
        record[2] = String.valueOf(currentOrder.getProductId());
        record[3] = String.valueOf(currentOrder.getEmployeeId());
        record[4] = String.valueOf(currentOrder.getQuantity());
        record[5] = currentOrder.getShippingAddress();
        record[6] = currentOrder.getCardType();
        record[7] = currentOrder.getCardNumber();
        record[8] = currentOrder.getCardOwner();
        record[9] = currentOrder.getCardExpirationMonth();
        record[10] = currentOrder.getGetCardExpirationYear();
        record[11] = currentOrder.getCardSecurityCode();
        record[12] = currentOrder.getOrderDate();
        record[13]= newStatus;

        ContentValues values = new ContentValues();

        dbm.updateRecord(values, DatabaseContract.OrderEntry.TABLE_NAME, fields, record);
    }

    public static class ViewHolder{
        public TextView prefix;
        public TextView id;
        public TextView date;
        public TextView status;
        public Switch switchButton;
    }

}
