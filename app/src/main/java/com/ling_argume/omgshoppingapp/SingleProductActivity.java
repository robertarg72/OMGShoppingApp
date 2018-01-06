package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ling_argume.omgshoppingapp.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_IN_PROCESS_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_STORE;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST;
import static com.ling_argume.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY;
import static com.ling_argume.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.saveToSharedPreferences;
import static com.ling_argume.omgshoppingapp.utils.Utils.setUserGreetingTextView;

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
    EditText quantityView;
    TextView availableQuantityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        // Get CustomerId from Shared Preferences
        customerId = getFromSharedPreferences(this, SHARED_PREFERENCES_CUSTOMER_ID);
        employeeId = ORDER_DEFAULT_EMPLOYEE_ID;
        // Retrieve product id from previous Activity
        Intent i = getIntent();
        productId = i.getStringExtra("product_id");

        // Get all details for Product with that id
        product = dbm.getSingleProduct(productId);

        //Set views with product data
        name = findViewById(R.id.single_product_name);
        name.setText(product.getName());

        image = findViewById(R.id.single_product_image);
        image.setImageBitmap(product.getImage());

        description = findViewById(R.id.single_product_description);
        description.setText(product.getDescription());

        price = findViewById(R.id.single_product_price);
        price.setText(product.getPrice());

        category = findViewById(R.id.single_product_category);
        category.setText(product.getCategory());

        availableQuantityView = findViewById(R.id.single_product_availability);
        availableQuantityView.setText(String.valueOf(product.getQuantity()));

    }

    public void onButtonClick(View v){
        int id = v.getId();
        if( id == R.id.btn_place_order){

            // Show error if no quantity is there
            quantityView = findViewById(R.id.qty);

            String qtyString = quantityView.getText().toString().trim();
            if (qtyString.length() == 0) {
                Toast.makeText(SingleProductActivity.this, "Add Quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty = Integer.valueOf(qtyString);
            if(qty == 0) {
                Toast.makeText(SingleProductActivity.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show error if quantity is more than available items
            int availableQty = Integer.valueOf(availableQuantityView.getText().toString());
            if (qty > availableQty){
                Toast.makeText(SingleProductActivity.this, "Quantity cannot be greater than available items", Toast.LENGTH_SHORT).show();

                return;
            }

            // Save the new order in DB

            String fields[] = {"", COLUMN_CUSTOMER_ID, COLUMN_PRODUCT_ID, COLUMN_EMPLOYEE_ID,
                    COLUMN_ORDER_QUANTITY, COLUMN_SHIPPING_ADDRESS, COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER,
                    COLUMN_CARD_OWNER, COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                    COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS
            };

            String record[] = new String[14];
            record[1] = customerId;
            record[2] = productId;
            record[3] = employeeId;

            // Quantity will be set by the user
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
            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.CANADA);
            record[12] = df.format(c.getTime());

            record[13]= ORDER_IN_PROCESS_TEXT;

            ContentValues values = new ContentValues();

            dbm.addRecord(values, DatabaseContract.OrderEntry.TABLE_NAME, fields, record);

            // We decrease available quantity in Products table
            String newAvailableQuantity = String.valueOf(availableQty - qty);
            values = new ContentValues();
            String productFields[] = {DatabaseContract.ProductEntry._ID, DatabaseContract.ProductEntry.COLUMN_QUANTITY};
            String productRecords[] = { productId, newAvailableQuantity};
            dbm.updateRecord(values, DatabaseContract.ProductEntry.TABLE_NAME, productFields, productRecords);

            // Setup a flag to let know Products List Activity that at least one Product stock was updated
            saveToSharedPreferences(this, SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG, "Flag" );
            saveToSharedPreferences(this, SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST, productId );
            saveToSharedPreferences(this, SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY, newAvailableQuantity );

            // Refresh value in Available qty label as well
            availableQuantityView.setText(newAvailableQuantity);

            Toast.makeText(SingleProductActivity.this, "Order created successfully", Toast.LENGTH_SHORT).show();

            // Redirect user to see the list of this orders
            Intent i = new Intent( SingleProductActivity.this, OrdersActivity.class);
            startActivity(i);
            //finish();
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
                next = new Intent( SingleProductActivity.this, ProductsActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( SingleProductActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                product = dbm.getSingleProduct(productId);
                availableQuantityView.setText(String.valueOf(product.getQuantity()));
                return true;
            case R.id.login_screen:
                next = new Intent( SingleProductActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
