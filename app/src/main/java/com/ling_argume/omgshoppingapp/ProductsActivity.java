package com.ling_argume.omgshoppingapp;


import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.ling_argume.omgshoppingapp.adapter.ProductListAdapter;
import com.ling_argume.omgshoppingapp.model.Product;
import java.util.List;

import static com.ling_argume.omgshoppingapp.utils.Utils.getInitialImages;
import static com.ling_argume.omgshoppingapp.utils.Utils.tableCreatorString;
import static com.ling_argume.omgshoppingapp.utils.Utils.tables;

public class ProductsActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        dbm = new DatabaseManager(this);
        dbm.dbInitialize(tables, tableCreatorString, getInitialImages(this));

        // For testing table creation
        //SQLiteDatabase testdb = dbm.getReadableDatabase();

        List<Product> list = dbm.getProducts();

        ListView lv = (ListView) findViewById(R.id.products_list);

        ProductListAdapter adapter = new ProductListAdapter(ProductsActivity.this, list);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dbm.close();
        super.onDestroy();
    }

}
