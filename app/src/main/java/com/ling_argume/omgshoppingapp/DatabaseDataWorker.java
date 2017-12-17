package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jim.
 */

public class DatabaseDataWorker {
    private SQLiteDatabase mDb;

    public DatabaseDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    public void insertCustomers() {
        insertCustomer("rob", "123", "robert", "argume", "2627 mccowan", "scarborough", "m1s5t1");
        insertCustomer("ling", "123", "john", "doe", "24 sheppard avenue", "scarborough", "m1s5t1");
        insertCustomer("tim", "123", "Thomas", "Green", "2627 mccowan", "scarborough", "m1s5t1");
    }

     private void insertCustomer(String username, String password, String firstName, String lastName, String address, String city, String postalCode) {
        ContentValues values = new ContentValues();
        values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_USERNAME, username);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_PASSWORD, password);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_FIRSTNAME, firstName);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_LASTNAME, lastName);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_ADDRESS , address);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_CITY, city);
         values.put(ShoppingDatabaseContract.CustomerEntry.COLUMN_POSTALCODE, postalCode);

        long newRowId = mDb.insert(ShoppingDatabaseContract.CustomerEntry.TABLE_NAME, null, values);
    }

    public void insertClerks() {
        insertClerk("clerk1", "123", "robert", "argume");
        insertClerk("clerk2", "123", "john", "doe");
    }

    private void insertClerk(String username, String password, String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(ShoppingDatabaseContract.ClerkEntry.COLUMN_USERNAME, username);
        values.put(ShoppingDatabaseContract.ClerkEntry.COLUMN_PASSWORD, password);
        values.put(ShoppingDatabaseContract.ClerkEntry.COLUMN_FIRSTNAME, firstName);
        values.put(ShoppingDatabaseContract.ClerkEntry.COLUMN_LASTNAME, lastName);


        long newRowId = mDb.insert(ShoppingDatabaseContract.ClerkEntry.TABLE_NAME, null, values);
    }

    public void insertProducts() {
        insertProduct("Boots", "winter boots", "220.50", 20, "men shoes");
        insertProduct("Pencil", "color red pencil", "10.75", 85, "library");

    }

    private void insertProduct(String productName, String description, String price, int quantity, String category) {
        ContentValues values = new ContentValues();
        values.put(ShoppingDatabaseContract.ProductEntry.COLUMN_PRODUCTNAME, productName);
        values.put(ShoppingDatabaseContract.ProductEntry.COLUMN_DESCRIPTION, description);
        values.put(ShoppingDatabaseContract.ProductEntry.COLUMN_PRICE, price);
        values.put(ShoppingDatabaseContract.ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(ShoppingDatabaseContract.ProductEntry.COLUMN_CATEGORY , category);


        long newRowId = mDb.insert(ShoppingDatabaseContract.ProductEntry.TABLE_NAME, null, values);
    }

    public void insertOrders() {
        insertOrder(1, 1, 1, 2, "123 Finch Ave",
                "Credit", "45901234567", "Joe Doe", "jun",
                "2021", "287", "11/04/2017", "In-Process");


    }

    private void insertOrder(int customerId, int productId, int employeeId, int quantity,
                             String shippingAddress, String cardType, String cardNumber,
                             String cardOwner, String expirationMonth, String expirationYear,
                             String securityCode, String orderDate, String orderStatus ) {

        ContentValues values = new ContentValues();
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID, customerId);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_PRODUCT_ID, productId);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID, employeeId);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_QUANTITY, quantity);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS , shippingAddress);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_TYPE , cardType);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_NUMBER , cardNumber);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_OWNER , cardOwner);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_MONTH , expirationMonth);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_EXPIRATION_YEAR , expirationYear);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_CARD_SECURITY_CODE , securityCode);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_ORDER_DATE , orderDate);
        values.put(ShoppingDatabaseContract.OrderEntry.COLUMN_STATUS , orderStatus);


        long newRowId = mDb.insert(ShoppingDatabaseContract.OrderEntry.TABLE_NAME, null, values);
    }

}




