package com.centennialcollege.omgshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.centennialcollege.omgshoppingapp.adapter.OrderListAdapter;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.model.Order;

import java.util.List;

import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class OrdersActivity extends AppCompatActivity {

    private DatabaseManager dbm;
    List<Order> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        // Get customerId and bring only the orders belonging to this customer
        String customerId = getFromSharedPreferences(this, SHARED_PREFERENCES_CUSTOMER_ID);
        list = dbm.getOrdersByCustomerId(customerId);

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
//        Intent i = new Intent( OrdersActivity.this, SingleOrderActivity.class);
//
//        i.putExtra("order_id", String.valueOf(list.get(position).getId()));
//
//        startActivity(i);
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
            case R.id.shopping_cart_screen:
                next = new Intent( OrdersActivity.this, ShoppingCartActivity.class);
                next.setFlags(next.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( OrdersActivity.this, CategoriesActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                break;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                return true;
            case R.id.login_screen:
                next = new Intent( OrdersActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            case R.id.about_screen:
                next = new Intent( OrdersActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
