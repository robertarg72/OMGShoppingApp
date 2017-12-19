package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ling_argume.omgshoppingapp.adapter.ProductListAdapter;
import com.ling_argume.omgshoppingapp.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_NUMBER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_OWNER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_TYPE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_ORDER_DATE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_QUANTITY;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_STATUS;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFENCES_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.getInitialImages;
import static com.ling_argume.omgshoppingapp.utils.Utils.tableCreatorString;
import static com.ling_argume.omgshoppingapp.utils.Utils.tables;

public class SingleProductActivity extends AppCompatActivity {

    private DatabaseManager dbm;
    String productId;
    String customerId = "";
    String employeeId = "";
    Product product;
    TextView name;
    ImageView image;
    TextView description;
    TextView price;
    TextView category;
    TextView quantityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        dbm = new DatabaseManager(this);

        // Get CustomerId from Shared Preferences
        customerId = getFromSharedPreferences(this, SHARED_PREFENCES_STORE, SHARED_PREFENCES_CUSTOMER_ID);

        // Retrieve product id from previous Activity
        Intent i = getIntent();
        productId = i.getStringExtra("product_id");

        // Get all details for Product with that id
        product = dbm.getSingleProduct(productId +1);

        //Set views with product data
        name = (TextView) findViewById(R.id.single_product_name);
        name.setText(product.getName());

        image = (ImageView) findViewById(R.id.single_product_image);
        image.setImageBitmap(product.getImage());

        description = (TextView) findViewById(R.id.single_product_description);
        description.setText(product.getDescription());

        price = (TextView) findViewById(R.id.single_product_price);
        price.setText(product.getPrice());

        category = (TextView) findViewById(R.id.single_product_category);
        category.setText(product.getCategory());
    }

    public void onClick(View v){
        int id = v.getId();
        if( id == R.id.btn_place_order){

            // Save the new order in DB

            String fields[] = {"", COLUMN_CUSTOMER_ID, COLUMN_PRODUCT_ID, COLUMN_EMPLOYEE_ID,
                    COLUMN_QUANTITY, COLUMN_SHIPPING_ADDRESS, COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER,
                    COLUMN_CARD_OWNER, COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                    COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS
            };

            String record[] = new String[14];
            record[1] = customerId;
            record[2] = productId;
            record[3] = employeeId;

            // Quantity will be set by the user
            EditText quantityView = (EditText) findViewById(R.id.qty);
            record[4] = quantityView.getText().toString();

            record[5] = ""; //shipping address
            record[6] = ""; //card type
            record[7] = ""; //card number
            record[8] = ""; //card owner
            record[9] = ""; //card expiration month
            record[10]= ""; //card expiration year
            record[11]= ""; //card security code

            // Order Data and time
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            record[12] = df.format(c.getTime());

            record[13]= "In-Process";

            ContentValues values = new ContentValues();

            dbm.addRecord(values, DatabaseContract.OrderEntry.TABLE_NAME,fields, record);

            // Redirect user to see the list of this orders
            Intent i = new Intent( SingleProductActivity.this, OrdersActivity.class);
            startActivity(i);
        }
    }



}
