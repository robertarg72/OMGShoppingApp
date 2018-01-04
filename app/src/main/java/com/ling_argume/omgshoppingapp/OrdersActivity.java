package com.ling_argume.omgshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ling_argume.omgshoppingapp.adapter.OrderListAdapter;
import com.ling_argume.omgshoppingapp.model.Order;

import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class OrdersActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        // Get customerId and bring only the orders belonging to this customer
        String customerId = getFromSharedPreferences(this, SHARED_PREFERENCES_STORE, SHARED_PREFERENCES_CUSTOMER_ID);
        List<Order> list = dbm.getOrdersByCustomerId(customerId);

        ListView lv = findViewById(R.id.orders_list);

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
        i.putExtra("order_id", String.valueOf(id + 1));

        startActivity(i);
        }
    };

    //Initialize the contents of the Activity's standard options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        //show the menu
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return true;
    }
    // called whenever an item in your options menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent next;
        //handle menu items  y their id
        switch (item.getItemId())
        {
            case R.id.products_screen:
                next = new Intent( OrdersActivity.this, ProductsActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                break;
//            case R.id.login_screen:
//                next = new Intent( OrdersActivity.this, LoginActivity.class);
//                startActivity(next);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
