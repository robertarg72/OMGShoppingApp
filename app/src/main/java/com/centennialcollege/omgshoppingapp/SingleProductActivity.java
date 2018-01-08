package com.centennialcollege.omgshoppingapp;

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

import com.centennialcollege.omgshoppingapp.database.DatabaseContract;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.model.Order;
import com.centennialcollege.omgshoppingapp.model.Product;

import static android.provider.BaseColumns._ID;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_NUMBER;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_OWNER;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CARD_TYPE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_ORDER_DATE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_SHIPPING_CITY;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_SHIPPING_POSTALCODE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_STATUS;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_TOTAL_PRICE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_DATE;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_STATUS;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.ORDER_DEFAULT_EMPLOYEE_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.ORDER_ITEM_AVAILABLE;
import static com.centennialcollege.omgshoppingapp.utils.Utils.ORDER_SHOPPING_CART;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY;
import static com.centennialcollege.omgshoppingapp.utils.Utils.getCurrentDateTime;
import static com.centennialcollege.omgshoppingapp.utils.Utils.getFromSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.saveToSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

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
//        Intent i = getIntent();
//        productId = i.getStringExtra("product_id");

        Bundle extras = getIntent().getExtras();
        productId = extras.getString("product_id");
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

            // Show error if no quantity is set
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

            // First check if shopping cart exists, otherwise create an order with status "shopping cart"

            Order shoppingCart = dbm.getShoppingCartOrder();
            if(shoppingCart.getId() ==  0) {
                shoppingCart = createShoppingCart();
            }
            createOrderItem(shoppingCart.getId(), qty, ORDER_ITEM_AVAILABLE );

            Toast.makeText(SingleProductActivity.this, "Added to Shopping Cart Successfully", Toast.LENGTH_SHORT).show();
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
            case R.id.shopping_cart_screen:
                next = new Intent( SingleProductActivity.this, ShoppingCartActivity.class);
                next.setFlags(next.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( SingleProductActivity.this, CategoriesActivity.class);
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
            case R.id.about_screen:
                next = new Intent( SingleProductActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Order createShoppingCart() {

        String fields[] = {"", COLUMN_CUSTOMER_ID, COLUMN_EMPLOYEE_ID,
                COLUMN_SHIPPING_ADDRESS, COLUMN_SHIPPING_CITY, COLUMN_SHIPPING_POSTALCODE,
                COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER, COLUMN_CARD_OWNER,
                COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS,
                COLUMN_TOTAL_PRICE
        };

        String record[] = new String[15];
        record[0] = "";
        record[1] = customerId;
        record[2] = employeeId;

        record[3] = ""; //shipping address
        record[4] = ""; //shipping city
        record[5] = ""; //shipping postal code
        record[6] = ""; //card type
        record[7] = ""; //card number
        record[8] = ""; //card owner
        record[9] = ""; //card expiration month
        record[10]= ""; //card expiration year
        record[11]= ""; //card security code

        record[12] = getCurrentDateTime();
        record[13] = ORDER_SHOPPING_CART; //status
        record[14] = "0"; // total price

        ContentValues values = new ContentValues();
        dbm.addRecord(values, DatabaseContract.OrderEntry.TABLE_NAME, fields, record);

        return dbm.getShoppingCartOrder();
    }

    private void createOrderItem(int orderId, int quantity, String status) {

        String fields[] = {_ID, COLUMN_PRODUCT_ID, COLUMN_ORDER_ID, COLUMN_ORDER_ITEM_QUANTITY,
                            COLUMN_ORDER_ITEM_DATE, COLUMN_ORDER_ITEM_STATUS };

        String record[] = new String[6];
        record[0] = "";
        record[1] = productId;
        record[2] = String.valueOf(orderId);
        record[3] = String.valueOf(quantity);
        record[4] = getCurrentDateTime();
        record[5] = status;

        if(!(dbm.updateIfOrderItemExists(orderId, Integer.parseInt(productId), quantity))) {

            ContentValues values = new ContentValues();
            dbm.addRecord(values, DatabaseContract.OrderItemEntry.TABLE_NAME, fields, record);

        }
    }


    private void updateProductAvailability(int qty, int availableQty) {
        ContentValues values;

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
    }

}
