package com.ling_argume.omgshoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ling_argume.omgshoppingapp.adapter.OrderListAdapter;
import com.ling_argume.omgshoppingapp.adapter.OrdersManagementListAdapter;
import com.ling_argume.omgshoppingapp.model.Order;
import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;

public class OrdersManagementActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_management);

        dbm = new DatabaseManager(this);


        List<Order> list = dbm.getOrders();

        ListView lv = (ListView) findViewById(R.id.orders_management_list);

        OrdersManagementListAdapter adapter = new OrdersManagementListAdapter(OrdersManagementActivity.this, list);
        adapter.setDatabaseManager(dbm);
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
        }
    };

}
