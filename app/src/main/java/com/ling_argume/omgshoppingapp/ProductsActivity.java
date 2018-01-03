package com.ling_argume.omgshoppingapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Typeface;

import com.ling_argume.omgshoppingapp.adapter.ProductListAdapter;
import com.ling_argume.omgshoppingapp.model.Product;
import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.GREETING;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_USER_KEY;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class ProductsActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        dbm = new DatabaseManager(this);
        //dbm.dbInitialize(tables, tableCreatorString, getInitialImages(this));

        // For testing table creation
        //SQLiteDatabase testdb = dbm.getReadableDatabase();

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        // Set up list adapter for products list view, with database info
        List<Product> list = dbm.getProducts();
        ListView lv = (ListView) this.findViewById(R.id.products_list);

        ProductListAdapter adapter = new ProductListAdapter(ProductsActivity.this, list);
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

            Intent i = new Intent( ProductsActivity.this, SingleProductActivity.class);
            i.putExtra("product_id", String.valueOf(id + 1));

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
                break;
            case R.id.orders_screen:
                next = new Intent( ProductsActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
//            case R.id.login_screen:
//                next = new Intent( ProductsActivity.this, LoginActivity.class);
//                startActivity(next);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
