package com.centennialcollege.omgshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.centennialcollege.omgshoppingapp.adapter.CartListAdapter;
import com.centennialcollege.omgshoppingapp.adapter.ProductListAdapter;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.model.Product;

import java.util.List;

import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_ALL;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_PARAM;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_FLAG_VALUE;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY;
import static com.centennialcollege.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.saveToSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class ShoppingCartActivity extends AppCompatActivity {

    private DatabaseManager dbm;
    CartListAdapter adapter;
    List<Product> list;
    Product product;
    TextView availableQuantityView;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        Bundle extras = getIntent().getExtras();

        // Set up list adapter for products list view, with database info
        // Get UserID
        String CustID = getFromSharedPreferences(this, SHARED_PREFERENCES_CUSTOMER_ID);

        list = dbm.getCustomerOrderItems(CustID);

        ListView lv = this.findViewById(R.id.order_Items_list);

        adapter = new CartListAdapter(ShoppingCartActivity.this, list);
        lv.setAdapter(adapter);


        Helper.getListViewSize(lv);
    }

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
                next = new Intent( ShoppingCartActivity.this, ShoppingCartActivity.class);
                next.setFlags(next.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( ShoppingCartActivity.this, CategoriesActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( ShoppingCartActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                return true;
            case R.id.login_screen:
                next = new Intent( ShoppingCartActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            case R.id.about_screen:
                next = new Intent( ShoppingCartActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        dbm.close();
        super.onDestroy();
    }

}