package com.ling_argume.omgshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ling_argume.omgshoppingapp.adapter.OrderListAdapter;
import com.ling_argume.omgshoppingapp.model.Order;
import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFENCES_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;

public class OrdersActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dbm = new DatabaseManager(this);

        // Get customerId and bring only the orders belonging to this customer
        String customerId = getFromSharedPreferences(this, SHARED_PREFENCES_STORE, SHARED_PREFENCES_CUSTOMER_ID);
        List<Order> list = dbm.getOrdersByCustomerId(customerId);

        ListView lv = (ListView) findViewById(R.id.orders_list);

        OrderListAdapter adapter = new OrderListAdapter(OrdersActivity.this, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(onListClick);
    }

    @Override
    protected void onDestroy() {
        dbm.close();
        super.onDestroy();
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent( OrdersActivity.this, SingleOrderActivity.class);
            i.putExtra("order_id", String.valueOf(id));

            startActivity(i);

        }
    };

}
