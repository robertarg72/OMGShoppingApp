package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import static com.ling_argume.omgshoppingapp.utils.Utils.BALL;
import static com.ling_argume.omgshoppingapp.utils.Utils.BOOTS;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_CLOTHES;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_ELECTRONICS;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_HOME;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_LIBRARY;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_MUSIC;
import static com.ling_argume.omgshoppingapp.utils.Utils.CATEGORY_SPORTS;
import static com.ling_argume.omgshoppingapp.utils.Utils.CHAIR;
import static com.ling_argume.omgshoppingapp.utils.Utils.GUITAR;
import static com.ling_argume.omgshoppingapp.utils.Utils.ORDER_DELIVERED_TEXT;
import static com.ling_argume.omgshoppingapp.utils.Utils.PENCIL;
import static com.ling_argume.omgshoppingapp.utils.Utils.PHONE;


public class DatabaseDataWorker {
    private SQLiteDatabase mDb;

    public DatabaseDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    public void insertCustomers() {
        insertCustomer("rob", "123", "Robert", "Argume", "2627 mccowan", "scarborough", "m1s5t1");
        insertCustomer("ling", "123", "Ling", "Bao", "24 sheppard avenue", "scarborough", "m1s5t1");
        insertCustomer("joe", "123", "Joe", "Doe", "2627 mccowan", "scarborough", "m1s5t1");
    }

     private void insertCustomer(String username, String password, String firstName, String lastName, String address, String city, String postalCode) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CustomerEntry.COLUMN_USERNAME, username);
         values.put(DatabaseContract.CustomerEntry.COLUMN_PASSWORD, password);
         values.put(DatabaseContract.CustomerEntry.COLUMN_FIRSTNAME, firstName);
         values.put(DatabaseContract.CustomerEntry.COLUMN_LASTNAME, lastName);
         values.put(DatabaseContract.CustomerEntry.COLUMN_ADDRESS , address);
         values.put(DatabaseContract.CustomerEntry.COLUMN_CITY, city);
         values.put(DatabaseContract.CustomerEntry.COLUMN_POSTALCODE, postalCode);

        long newRowId = mDb.insert(DatabaseContract.CustomerEntry.TABLE_NAME, null, values);
    }

    public void insertClerks() {
        insertClerk("clerk1", "123", "robert", "argume");
        insertClerk("clerk2", "123", "john", "doe");
    }

    private void insertClerk(String username, String password, String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ClerkEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.ClerkEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.ClerkEntry.COLUMN_FIRSTNAME, firstName);
        values.put(DatabaseContract.ClerkEntry.COLUMN_LASTNAME, lastName);

        long newRowId = mDb.insert(DatabaseContract.ClerkEntry.TABLE_NAME, null, values);
    }

    public void insertProducts(Map<String, String> initialImages) {


        insertProduct(BOOTS, "Fine winter boots", "220.50", 20, CATEGORY_CLOTHES, initialImages.get(BOOTS));

        insertProduct(PENCIL, "4B pencil for art drawing", "10.75", 85, CATEGORY_LIBRARY, initialImages.get(PENCIL));
        insertProduct(BALL, "FIFA approved soccer ball", "31.40", 15, CATEGORY_SPORTS, initialImages.get(BALL));
        insertProduct(PHONE, "Unlocked S8 smart phone", "120.00", 10, CATEGORY_ELECTRONICS, initialImages.get(PHONE));
        insertProduct(GUITAR, "Legendary hard rock model", "383.50", 5, CATEGORY_MUSIC, initialImages.get(GUITAR));
        insertProduct(CHAIR, "Basic but durable chair", "22.00", 42, CATEGORY_HOME, initialImages.get(CHAIR));
        //insertProduct(NOTEBOOK, "Notebook Corei7 quadcore 8GB RAM", "849.99", 12, CATEGORY_COMPUTERS, initialImages.get(NOTEBOOK));


    }

    private void insertProduct(String productName, String description, String price, int quantity, String category, String filePath) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME, productName);
        values.put(DatabaseContract.ProductEntry.COLUMN_IMAGE, filePath);
        values.put(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION, description);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRICE, price);
        values.put(DatabaseContract.ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(DatabaseContract.ProductEntry.COLUMN_CATEGORY , category);

        long newRowId = mDb.insert(DatabaseContract.ProductEntry.TABLE_NAME, null, values);
    }

    public void insertOrders() {
        insertOrder(1, 1, 1, 2, "123 Finch Ave",
                "Credit", "45901234567", "Joe Doe", "jun",
                "2021", "287", "11/04/2017", ORDER_DELIVERED_TEXT);


    }

    private void insertOrder(int customerId, int productId, int employeeId, int quantity,
                             String shippingAddress, String cardType, String cardNumber,
                             String cardOwner, String expirationMonth, String expirationYear,
                             String securityCode, String orderDate, String orderStatus ) {

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID, customerId);
        values.put(DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID, productId);
        values.put(DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID, employeeId);
        values.put(DatabaseContract.OrderEntry.COLUMN_ORDER_QUANTITY, quantity);
        values.put(DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS , shippingAddress);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_TYPE , cardType);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_NUMBER , cardNumber);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_OWNER , cardOwner);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH , expirationMonth);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR , expirationYear);
        values.put(DatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE , securityCode);
        values.put(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE , orderDate);
        values.put(DatabaseContract.OrderEntry.COLUMN_STATUS , orderStatus);



        long newRowId = mDb.insert(DatabaseContract.OrderEntry.TABLE_NAME, null, values);
    }

}




