package com.ling_argume.omgshoppingapp;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ling_argume.omgshoppingapp.SingleProductActivity;

import com.ling_argume.omgshoppingapp.adapter.ProductListAdapter;
import com.ling_argume.omgshoppingapp.model.Product;
import java.util.List;

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

        List<Product> list = dbm.getProducts();

        ListView lv = (ListView) findViewById(R.id.products_list);

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

            //Toast.makeText(getBaseContext(), "WOWWWWWWWW", Toast.LENGTH_LONG).show();
            Intent i = new Intent( ProductsActivity.this, SingleProductActivity.class);
            i.putExtra("product_id", String.valueOf(id + 1));

            startActivity(i);

        }
    };



}
