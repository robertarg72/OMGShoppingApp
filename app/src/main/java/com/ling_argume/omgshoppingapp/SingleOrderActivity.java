package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ling_argume.omgshoppingapp.model.Order;
import com.ling_argume.omgshoppingapp.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_NUMBER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_OWNER;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CARD_TYPE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_ORDER_DATE;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_ORDER_QUANTITY;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS;
import static com.ling_argume.omgshoppingapp.DatabaseContract.OrderEntry.COLUMN_STATUS;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_DEFAULT_EMPLOYEE_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_DELIVERED_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_IN_PROCESS_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;

public class SingleOrderActivity extends AppCompatActivity {

    private DatabaseManager dbm;
    String orderId;
    String productId;
    String customerId = "";
    String employeeId = "";

    Order order;
    Product product;
    TextView name;
    ImageView image;
    TextView description;
    TextView price;
    TextView category;
    EditText quantityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        dbm = new DatabaseManager(this);

        // Get CustomerId from Shared Preferences
        customerId = getFromSharedPreferences(this, SHARED_PREFERENCES_STORE, SHARED_PREFERENCES_CUSTOMER_ID);
        employeeId = ORDER_DEFAULT_EMPLOYEE_ID;
        // Retrieve product id from previous Activity
        Intent i = getIntent();
        orderId = i.getStringExtra("order_id");
        order = dbm.getSingleOrder(orderId);

        productId = String.valueOf(order.getProductId());

        // Get all details for Product with that id
        product = dbm.getSingleProduct(productId);

        //Set views with product data
        name = (TextView) findViewById(R.id.order_single_product_name);
        name.setText(product.getName());

        image = (ImageView) findViewById(R.id.order_single_product_image);
        image.setImageBitmap(product.getImage());

        description = (TextView) findViewById(R.id.order_single_product_description);
        description.setText(product.getDescription());

        price = (TextView) findViewById(R.id.order_single_product_price);
        price.setText(product.getPrice());

        category = (TextView) findViewById(R.id.order_single_product_category);
        category.setText(product.getCategory());

        quantityView = (EditText) findViewById(R.id.order_editable_qty);
        quantityView.setText(String.valueOf(order.getQuantity()));
    }

    public void onClick(View v){
        int id = v.getId();
        if( id == R.id.btn_update_order){

            if( order.getStatus().compareTo(ORDER_DELIVERED_TEXT) == 0) {
                Toast.makeText(SingleOrderActivity.this, "Sorry, the order is already Delivered.", Toast.LENGTH_SHORT).show();
            }
            else {
                // Update order in DB

                String fields[] = {_ID, COLUMN_CUSTOMER_ID, COLUMN_PRODUCT_ID, COLUMN_EMPLOYEE_ID,
                        COLUMN_ORDER_QUANTITY, COLUMN_SHIPPING_ADDRESS, COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER,
                        COLUMN_CARD_OWNER, COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                        COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS
                };

                String record[] = new String[14];
                record[0] = String.valueOf(order.getId());
                record[1] = String.valueOf(order.getCustomerId());
                record[2] = String.valueOf(order.getProductId());
                record[3] = String.valueOf(order.getEmployeeId());

                // Quantity will be set by the user
                quantityView = (EditText) findViewById(R.id.order_editable_qty);
                record[4] = quantityView.getText().toString();

                record[5] = ""; //shipping address
                record[6] = ""; //card type
                record[7] = ""; //card number
                record[8] = ""; //card owner
                record[9] = ""; //card expiration month
                record[10] = ""; //card expiration year
                record[11] = ""; //card security code

                // Order Data and time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                record[12] = df.format(c.getTime());

                record[13] = ORDER_IN_PROCESS_TEXT;

                ContentValues values = new ContentValues();

                dbm.updateRecord(values, DatabaseContract.OrderEntry.TABLE_NAME,fields, record);

                // Redirect user to see the list of this orders
                Intent i = new Intent( SingleOrderActivity.this, OrdersActivity.class);
                startActivity(i);
            }

        }
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
            case R.id.products_screen:
                next = new Intent( SingleOrderActivity.this, ProductsActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( SingleOrderActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
//            case R.id.login_screen:
//                next = new Intent( SingleOrderActivity.this, LoginActivity.class);
//                startActivity(next);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
