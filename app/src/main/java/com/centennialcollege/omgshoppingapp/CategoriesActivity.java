package com.centennialcollege.omgshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.centennialcollege.omgshoppingapp.adapter.CategoryAdapter;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.model.Category;

import java.util.ArrayList;

import static com.centennialcollege.omgshoppingapp.utils.ImageHelper.getBitmapFromResource;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_ALL;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_CLOTHES;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_ELECTRONICS;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_HOME;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_LIBRARY;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_MUSIC;
import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_SPORTS;
import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private RecyclerView.LayoutManager categoryLayoutManager;
    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, CATEGORY_ALL, getBitmapFromResource(this, R.mipmap.category_all)));
        categories.add(new Category(2, CATEGORY_CLOTHES, getBitmapFromResource(this, R.mipmap.category_clothes)));
        categories.add(new Category(3, CATEGORY_HOME, getBitmapFromResource(this, R.mipmap.category_home)));
        categories.add(new Category(4, CATEGORY_ELECTRONICS, getBitmapFromResource(this, R.mipmap.category_electronics)));
        categories.add(new Category(5, CATEGORY_SPORTS, getBitmapFromResource(this, R.mipmap.category_sports)));
        categories.add(new Category(6, CATEGORY_MUSIC, getBitmapFromResource(this, R.mipmap.category_music)));
        categories.add(new Category(7, CATEGORY_LIBRARY, getBitmapFromResource(this, R.mipmap.category_library)));

        categoriesRecyclerView = findViewById(R.id.recycler_view_categories);
        categoriesRecyclerView.setHasFixedSize(true);
        categoryLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        categoryAdapter = new CategoryAdapter(this, categories);

        categoriesRecyclerView.setLayoutManager(categoryLayoutManager);
        categoriesRecyclerView.setAdapter(categoryAdapter);

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
                next = new Intent( CategoriesActivity.this, ShoppingCartActivity.class);
                next.setFlags(next.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(next);
                return true;
            case R.id.products_screen:
                break;
            case R.id.orders_screen:
                next = new Intent( CategoriesActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                return true;
            case R.id.login_screen:
                next = new Intent( CategoriesActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            case R.id.about_screen:
                next = new Intent( CategoriesActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
