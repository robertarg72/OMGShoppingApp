package com.centennialcollege.omgshoppingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.centennialcollege.omgshoppingapp.database.DatabaseContract;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.model.Order;
import com.centennialcollege.omgshoppingapp.model.OrderItem;
import com.centennialcollege.omgshoppingapp.model.Product;

import java.util.ArrayList;
import java.util.List;

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
import static com.centennialcollege.omgshoppingapp.utils.Utils.CARD_REQUIRED_DIGITS;
import static com.centennialcollege.omgshoppingapp.utils.Utils.ORDER_IN_PROCESS_TEXT;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SECURITY_CODE_REQUIRED_DIGITS;
import static com.centennialcollege.omgshoppingapp.utils.Utils.getCurrentDateTime;
import static com.centennialcollege.omgshoppingapp.utils.Utils.setUserGreetingTextView;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseManager dbm;
    private Order shoppingCart;
    private EditText shippingAddress;
    private EditText shippingCity;
    private EditText shippingPostalCode;
    private RadioButton selectedRadioButton;
    private EditText cardNumberView;
    private EditText nameOnCardView;
    private EditText cardSecurityCodeView;


    private String username;
    private String paymentType;
    private String address;
    private String city;
    private String postalCode;
    private String expirationMonth;
    private String expirationYear;
    private String cardNumber;
    private String securityCode;
    private String nameOnCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbm = new DatabaseManager(this);

        // Set greeting for logged in user
        username = setUserGreetingTextView(this, R.id.greeting);

        // Get all details from shopping cart
        shoppingCart = dbm.getShoppingCartOrder();

        //Set views with cart data
        shippingAddress = findViewById(R.id.address);
        shippingAddress.setText(shoppingCart.getShippingAddress());

        shippingCity = findViewById(R.id.city);
        shippingCity.setText(shoppingCart.getShippingCity());

        shippingPostalCode = findViewById(R.id.postalcode);
        shippingPostalCode.setText(shoppingCart.getShippingPostalCode());

        String paymentFromOrder = shoppingCart.getCardType();

        if( paymentFromOrder != null && paymentFromOrder.compareTo(getResources().getString(R.string.cardtype_debit)) == 0) {
            selectedRadioButton = findViewById(R.id.debitRadioButton);
        }
        else {
            selectedRadioButton = findViewById(R.id.creditRadioButton);
        }
        selectedRadioButton.isChecked();

        // Add items for the expiration month drop down and expiration year dropdown
        addItemsOnSpinner(R.id.monthSpinner,  R.array.monthExpiration);
        addItemsOnSpinner(R.id.yearSpinner,  R.array.expirationYear);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Spinner spinner = (Spinner) parent;
        String selectedItemText = parent.getItemAtPosition(pos).toString();

        // We have 2 spinner, so first decide which one was selected and then save the selected text
        if(spinner.getId() == R.id.monthSpinner)
        {
            expirationMonth = selectedItemText;
        }
        else if(spinner.getId() == R.id.yearSpinner)
        {
            expirationYear = selectedItemText;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback, but not used in our App.
    }

    private int getValidationErrors() {

        int count = 0;

        EditText addressWidget = findViewById(R.id.address);
        if( addressWidget.getText().toString().length() == 0 ) {
            addressWidget.setError( getResources().getString(R.string.error_message_address));
            count++;
        }
        else
            address = addressWidget.getText().toString();

        EditText cityWidget = findViewById(R.id.city);
        if( cityWidget.getText().toString().length() == 0 ) {
            cityWidget.setError( getResources().getString(R.string.error_message_city) );
            count++;
        }
        else
            city = cityWidget.getText().toString();

        EditText postalCodeWidget = findViewById(R.id.postalcode);
        if( postalCodeWidget.getText().toString().length() == 0 ) {
            postalCodeWidget.setError( getResources().getString(R.string.error_message_postal_code) );
            count++;
        }
        else
            postalCode = postalCodeWidget.getText().toString();


        // Validation for type of payment
        if( "".equals(paymentType)) {
            displayToast(getResources().getString(R.string.error_message_payment_method));
            count++;
        }

        String content;
        String regexStrForNumbersOnly;

        // Card validation

        nameOnCardView = findViewById(R.id.nameOnCard);
        if( nameOnCardView.getText().toString().length() == 0 ) {
            nameOnCardView.setError( getResources().getString(R.string.error_message_nameOnCard_code) );
            count++;
        }
        else
            nameOnCard = postalCodeWidget.getText().toString();

        EditText cardNumberWidget = findViewById(R.id.cardNumber);
        content = cardNumberWidget.getText().toString();
        regexStrForNumbersOnly = "^[0-9]*$";
        if( content.length() == 0 ) {
            cardNumberWidget.setError( getResources().getString(R.string.error_message_card_required) );
            count++;
        }
        else if( !content.matches(regexStrForNumbersOnly) ) {
            cardNumberWidget.setError( getResources().getString(R.string.error_message_card_all_numbers) );
            count++;
        }
        else if( content.length() != CARD_REQUIRED_DIGITS) {
            cardNumberWidget.setError( getResources().getString(R.string.error_message_card_amount_digits) );
            count++;
        }
        else
            cardNumber = cardNumberWidget.getText().toString();

        // Security Code validation
        cardSecurityCodeView = findViewById(R.id.securitycode);
        content = cardSecurityCodeView.getText().toString();
        regexStrForNumbersOnly = "^[0-9]*$";
        if( content.length() == 0 ) {
            cardSecurityCodeView.setError( getResources().getString(R.string.error_message_security_code) );
            count++;
        }
        else if( !content.matches(regexStrForNumbersOnly) ) {
            cardSecurityCodeView.setError( getResources().getString(R.string.error_message_security_code_all_numbers) );
            count++;
        }
        else if( content.length() != SECURITY_CODE_REQUIRED_DIGITS) {
            cardSecurityCodeView.setError( getResources().getString(R.string.error_message_security_code_amount_digits) );
            count++;
        }
        else
            securityCode = cardNumberWidget.getText().toString();

        // Spinner validation
        if( "".equals(expirationYear) || getResources().getString(R.string.spinner_year_default_text).equals(expirationYear)) {
            displayToast(getResources().getString(R.string.error_message_expiration_year));
            count++;
        }

        if( "".equals(expirationMonth) || getResources().getString(R.string.spinner_month_default_text).equals(expirationMonth)) {
            displayToast(getResources().getString(R.string.error_message_expiration_month));
        }

        return count;

    }

    private void addItemsOnSpinner(int viewId, int itemsArrayId) {

        Spinner currentSpinner = findViewById(viewId);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, itemsArrayId, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        currentSpinner.setAdapter(adapter);

        // Add a listener for the selection event of the spinner
        currentSpinner.setOnItemSelectedListener(this);

    }

    public void onPaymentRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.debitRadioButton:
                if (checked)
                    paymentType = getResources().getString(R.string.cardtype_debit);
                break;
            case R.id.creditRadioButton:
                if (checked)
                    paymentType = getResources().getString(R.string.cardtype_credit);
                break;
        }
    }

    public void onButtonClick(View v){
        int id = v.getId();

        if( id == R.id.submitbutton){

            // Only if there are no validation error, then we update DB and generate a final confirmation dialog
            if( getValidationErrors() == 0 ) {
                double totalPrice = updateProductsInShoppingCart();
                updateShoppingCart(totalPrice);

                AlertDialog.Builder confirmationDialog  = new AlertDialog.Builder(this);
                confirmationDialog.setMessage(username + ", thank you for your order.\nYour products will be delivered soon.");
                confirmationDialog.setTitle(getResources().getString(R.string.app_name));
                confirmationDialog.setPositiveButton(getResources().getString(R.string.ok_string), null);
                confirmationDialog.setCancelable(true);


                confirmationDialog.setPositiveButton(getResources().getString(R.string.ok_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(PaymentActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        PaymentActivity.this.finish();
                        Intent next = new Intent( PaymentActivity.this, CategoriesActivity.class);
                        startActivity(next);
                    }
                });
                confirmationDialog.create().show();
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
            case R.id.shopping_cart_screen:
                next = new Intent( PaymentActivity.this, ShoppingCartActivity.class);
                startActivity(next);
                return true;
            case R.id.products_screen:
                next = new Intent( PaymentActivity.this, CategoriesActivity.class);
                startActivity(next);
                return true;
            case R.id.orders_screen:
                next = new Intent( PaymentActivity.this, OrdersActivity.class);
                startActivity(next);
                return true;
            case R.id.update_catalog:
                dbm.updateProductsCatalog();
                return true;
            case R.id.login_screen:
                next = new Intent( PaymentActivity.this, LoginActivity.class);
                startActivity(next);
                finish();
                return true;
            case R.id.about_screen:
                next = new Intent( PaymentActivity.this, AboutActivity.class);
                startActivity(next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private double updateProductsInShoppingCart() {
        List<OrderItem> shoppingItems = dbm.getShoopingCartItems(String.valueOf(shoppingCart.getId()));
        double totalPrice = 0;
        for (OrderItem item: shoppingItems) {
            int itemQty = item.getQuantity();

            String productId = String.valueOf(item.getProductId());
            Product product = dbm.getSingleProduct(productId);
            int availableQty = product.getQuantity();
            updateProductAvailability(productId, itemQty, availableQty);

            double productPrice = Double.parseDouble(product.getPrice());
            totalPrice += productPrice * itemQty;
        }
        return totalPrice;
    }

    private void updateShoppingCart(double totalPrice) {
        ContentValues values = new ContentValues();

        String fields[] = {_ID, COLUMN_SHIPPING_ADDRESS, COLUMN_SHIPPING_CITY, COLUMN_SHIPPING_POSTALCODE,
                COLUMN_CARD_TYPE, COLUMN_CARD_NUMBER, COLUMN_CARD_OWNER,
                COLUMN_CARD_EXPIRATION_MONTH, COLUMN_CARD_EXPIRATION_YEAR,
                COLUMN_CARD_SECURITY_CODE, COLUMN_ORDER_DATE, COLUMN_STATUS,
                COLUMN_TOTAL_PRICE
        };

        String orderRecords[] = { String.valueOf(shoppingCart.getId()), address, city, postalCode,
                                  paymentType, cardNumber, nameOnCard, expirationMonth, expirationYear,
                                  securityCode, getCurrentDateTime(), ORDER_IN_PROCESS_TEXT,
                                  String.valueOf(totalPrice)};

        dbm.updateRecord(values, DatabaseContract.OrderEntry.TABLE_NAME, fields, orderRecords);
    }


    private void updateProductAvailability(String productId, int qty, int availableQty) {

        // We decrease available quantity in Products table
        String newAvailableQuantity = String.valueOf(availableQty - qty);
        ContentValues values = new ContentValues();
        String productFields[] = {DatabaseContract.ProductEntry._ID, DatabaseContract.ProductEntry.COLUMN_QUANTITY};
        String productRecords[] = { productId, newAvailableQuantity};
        dbm.updateRecord(values, DatabaseContract.ProductEntry.TABLE_NAME, productFields, productRecords);
    }

    private void displayToast(String msg)
    {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
