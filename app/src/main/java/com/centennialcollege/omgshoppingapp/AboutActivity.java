package com.centennialcollege.omgshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.centennialcollege.omgshoppingapp.database.DatabaseManager;

import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class AboutActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);
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
                next = new Intent( AboutActivity.this, ShoppingCartActivity.class);
                next.setFlags(next.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( AboutActivity.this, CategoriesActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( AboutActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                return true;
            case R.id.login_screen:
                next = new Intent( AboutActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            case R.id.about_screen:
                next = new Intent( AboutActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
