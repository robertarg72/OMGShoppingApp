package com.ling_argume.omgshoppingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ling_argume.omgshoppingapp.adapter.ProductListAdapter;
import com.ling_argume.omgshoppingapp.database.DatabaseManager;
import com.ling_argume.omgshoppingapp.model.Product;

import java.security.cert.CertificateRevokedException;
import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.*;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class ProductsActivity extends AppCompatActivity {

    private DatabaseManager dbm;
    ProductListAdapter adapter;
    List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        Bundle extras = getIntent().getExtras();

        String requestedCategory = (extras != null) ? extras.getString(CATEGORY_PARAM) : CATEGORY_ALL;

        // Set up list adapter for products list view, with database info
        if( requestedCategory.compareTo(CATEGORY_ALL) == 0) {
            list = dbm.getProducts();
        }else {
            list = dbm.getProductsByCategory(requestedCategory);
        }

        ListView lv = this.findViewById(R.id.products_list);

        adapter = new ProductListAdapter(ProductsActivity.this, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(onListClick);
    }

    @Override
    protected void onDestroy() {
        dbm.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String flag = getFromSharedPreferences(this, SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG);
        if (flag.compareTo(SHARED_PREFERENCES_UPDATED_FLAG_VALUE) == 0) {
            // Retrieve values
            saveToSharedPreferences(this, SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG, "");
            String productId = getFromSharedPreferences(this, SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST);
            String quantity = getFromSharedPreferences(this, SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY);

            // Get product, update stock, and notify the adapter of data source update
            Product product = list.get(Integer.valueOf(productId)-1);
            product.setQuantity(Integer.valueOf(quantity));
            adapter.notifyDataSetChanged();
        }
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
            case R.id.shopping_cart_screen:
                next = new Intent( ProductsActivity.this, ShoppingCartActivity.class);
                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( ProductsActivity.this, CategoriesActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( ProductsActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                updateProductsListAdapter();
                return true;
            case R.id.login_screen:
                next = new Intent( ProductsActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateProductsListAdapter() {
        list = dbm.getProducts();
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

}
