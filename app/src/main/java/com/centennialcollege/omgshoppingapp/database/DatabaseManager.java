package com.centennialcollege.omgshoppingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.centennialcollege.omgshoppingapp.model.Order;
import com.centennialcollege.omgshoppingapp.model.OrderItem;
import com.centennialcollege.omgshoppingapp.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderEntry.COLUMN_STATUS;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID;
import static com.centennialcollege.omgshoppingapp.database.DatabaseContract.ProductEntry.COLUMN_CATEGORY;
import static com.centennialcollege.omgshoppingapp.utils.ImageHelper.getBitmapFromPath;
import static com.centennialcollege.omgshoppingapp.utils.Utils.ORDER_SHOPPING_CART;

public class DatabaseManager extends SQLiteOpenHelper {
    //
	private static final String DATABASE_NAME = "omgshopping.db";
    private static final int DATABASE_VERSION = 1;
    //
    private static Map<String, String> initialImages;
    private Context context;
    //
    private String tables[]; //table names
    private String tableCreatorString[]; //SQL statements to create tables

    //class constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }
    //initialize database table names and DDL statements
    public void dbInitialize(String[] tables, String tableCreatorString[], Map<String, String> initialImages)
    {
  	  this.tables = tables;
  	  this.tableCreatorString = tableCreatorString;
  	  this.initialImages = initialImages;
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Drop existing tables
    	//for (int i=0;i<tables.length;i++)
        //db.execSQL("DROP TABLE IF EXISTS " + tables[i]);


        for (String aTable: tables) {
            db.execSQL("DROP TABLE IF EXISTS " + aTable);
        }

        //create them
//    	for (int i=0;i<tables.length;i++)
//    		db.execSQL(tableCreatorString[i]);

        for (String aCreateString: tableCreatorString)
            db.execSQL(aCreateString);

    	// Load initial data into database
        loadInitialData(db);
    }

    //create the database
    public void createDatabase(Context context)
    {
    	SQLiteDatabase mDatabase;
    	mDatabase = context.openOrCreateDatabase(
    	DATABASE_NAME,
    	//SQLiteDatabase.CREATE_IF_NECESSARY,
                SQLiteDatabase.OPEN_READWRITE,
    	null);
    }

   //delete the database
    public void deleteDatabase(Context context)
    {
    	context.deleteDatabase(DATABASE_NAME);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
//    	for (int i=0;i<tables.length;i++)
//    		db.execSQL("DROP TABLE IF EXISTS " + tables[i]);

        for (String aTable: tables)
            db.execSQL("DROP TABLE IF EXISTS " + aTable);

        // Create tables again
        onCreate(db);
    }

    /////////////////////////
    // Database operations
    /////////////////////////
    // Add a new record
    public void addRecord(ContentValues values, String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=1;i<record.length;i++)
        	values.put(fields[i], record[i]);
        // Insert the row
        db.insert(tableName, null, values);
        db.close(); //close database connection
    }
    public void addRecordUsingContentValues(ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the row
        db.insert(tableName, null, values);
        db.close(); //close database connection
    }
    // Read all records
    public List getTable(String tableName) {
        List<List<String>> table = new ArrayList<>(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> row = new ArrayList<>(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst())
        {
        	do
        	{ // for each row
        		for (int i = 0; i < cursor.getColumnCount(); i++) {
        			row.add(cursor.getString(i));
        		}

                table.add(row); //add row to the list

            } while (cursor.moveToNext());
        }
        cursor.close();

        // return table as a list
        return table;
    }

    // Read all products that belong to a specific category
    public List<Product> getProductsByCategory(String category) {
        List<Product> productsList = new ArrayList<>();
        // Select all records
//        String selectQuery = "SELECT  * FROM " + DatabaseContract.ProductEntry.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.ProductEntry.TABLE_NAME, null,
                COLUMN_CATEGORY + " = '" + category + "'", null,
                null, null, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                Product product = new Product();
                try {
                    Bitmap productImage = getBitmapFromPath(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE)));
                    product.setImage(productImage);
                }
                catch (OutOfMemoryError e) {
                    Log.d(TAG, "getProducts: OutOfMemory when loading product image");
                    //e.printStackTrace();
                }

                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry._ID)));
                product.setName(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME)));
                product.setDescription(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE)));
                product.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY)));
                productsList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return productsList;
    }


    // Read all product records
    public List<Product> getProducts() {
        List<Product> productsList = new ArrayList<>();
        // Select all records
        String selectQuery = "SELECT  * FROM " + DatabaseContract.ProductEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                Product product = new Product();
                try {
                    Bitmap productImage = getBitmapFromPath(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE)));
                    product.setImage(productImage);
                }
                catch (OutOfMemoryError e) {
                    Log.d(TAG, "getProducts: OutOfMemory when loading product image");
                    //e.printStackTrace();
                }

                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry._ID)));
                product.setName(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME)));
                product.setDescription(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE)));
                product.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY)));
                productsList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return productsList;
    }

    // Get all order except the shopping cart order
    public List<Order> getOrders() {
        List<Order> ordersList = new ArrayList<>();
        // Select all records
//        String selectQuery = "SELECT  * FROM " + DatabaseContract.OrderEntry.TABLE_NAME;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                        COLUMN_STATUS + " <> '" + ORDER_SHOPPING_CART + "'", null,
                        null, null, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                //order.setProductId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID)));
                order.setCustomerId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID)));
                order.setEmployeeId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID)));
                //order.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_QUANTITY)));
                order.setOrderDate(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE)));
                order.setStatus(cursor.getString( cursor.getColumnIndex(COLUMN_STATUS)));
                ordersList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return ordersList;
    }

    public List<Order> getOrdersByCustomerId(String id) {
        Cursor cursor = null;
        List<Order> ordersList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{ id };
            cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                    DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID + " = ? AND " +
                            DatabaseContract.OrderEntry.COLUMN_STATUS + " <> '" + ORDER_SHOPPING_CART + "'", params,
                    null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Order order = new Order();
                    order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                    order.setOrderDate(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE)));
                    order.setStatus(cursor.getString( cursor.getColumnIndex(COLUMN_STATUS)));
                    ordersList.add(order);
                } while (cursor.moveToNext());
            }
            return ordersList;
        }finally {
            if( cursor != null) {
                cursor.close();
            }
        }
    }

    public Order getSingleOrder(String id) {
        Cursor cursor = null;
        Order order = new Order();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{ id };
            cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                    "_ID = ?", params,
                    null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                //order.setProductId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID)));
                order.setEmployeeId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID)));
                order.setCustomerId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID)));
                //order.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_QUANTITY)));
                order.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            }
            return order;
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public Order getShoppingCartOrder() {
        Cursor cursor = null;
        Order order = new Order();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                    COLUMN_STATUS + " = '" + ORDER_SHOPPING_CART + "'", null,
                    null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                order.setEmployeeId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID)));
                order.setCustomerId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID)));
                order.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            }
            return order;
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public Product getSingleProduct(String id) {
        Cursor cursor = null;
        Product product = new Product();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{ id };
            cursor = db.query(DatabaseContract.ProductEntry.TABLE_NAME, null,
                    "_ID = ?", params,
                    null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry._ID)));
                product.setName(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME)));
                product.setDescription(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE)));

                product.setImage(getBitmapFromPath(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE))));

                product.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY)));
            }
            return product;
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public Boolean userIsValid(String username, String password ) {

        // Select all records
        String selectQuery = "select * from  customer where username=? and password=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username,password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;

    }

    public String getCustomerId(String username, String password ) {
        String customerId = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ username, password };
        String[] columns = new String[]{DatabaseContract.CustomerEntry._ID};
            Cursor cursor = db.query(DatabaseContract.CustomerEntry.TABLE_NAME, columns,
                DatabaseContract.CustomerEntry.COLUMN_USERNAME + " = ? AND " +
                        DatabaseContract.CustomerEntry.COLUMN_PASSWORD + "= ?", params,
                null, null, null);

        if (cursor.moveToFirst()) {
           customerId = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry._ID));
        }
        cursor.close();
        return customerId;
    }

    public String getEmployeeId(String username, String password ) {
        String employeeId = null;
        // Select all records
//        String selectQuery = "select _id from  clerk where username='?' and password='?'";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{username,password});

        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ username, password };
        String[] columns = new String[]{DatabaseContract.ClerkEntry._ID};
        Cursor cursor = db.query(DatabaseContract.ClerkEntry.TABLE_NAME, columns,
                DatabaseContract.ClerkEntry.COLUMN_USERNAME + " = ? AND " +
                        DatabaseContract.ClerkEntry.COLUMN_PASSWORD + "= ?", params,
                null, null, null);

        if (cursor.moveToFirst()) {
            employeeId = cursor.getString(cursor.getColumnIndex(DatabaseContract.ClerkEntry._ID));
        }
        cursor.close();
        return employeeId;
    }

    // Update a record
    public int updateRecord(ContentValues values, String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=1;i<record.length;i++)
        	values.put(fields[i], record[i]);

        // updating row with given id = record[0]
        return db.update(tableName, values, fields[0] + " = ?",
                new String[] { record[0] });
    }

    // Delete a record with a given id
    public void deleteRecord(String tableName, String idName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, idName + " = ?",
                new String[] { id });
        db.close();
    }

    // Update products information
    // For now will reload initial data to db, but the idea is to connect to a web service and
    // retrieve updated information for the products catalgo
    public void updateProductsCatalog() {
        SQLiteDatabase db = this.getWritableDatabase();
        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.updateProducts();
    }

    private void loadInitialData(SQLiteDatabase db) {
        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.insertCustomers();
        worker.insertClerks();
        worker.insertProducts(initialImages);
        worker.insertOrders();
    }

    public boolean updateIfOrderItemExists(int order_id, int product_id, int Qty){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] params = new String[]{Integer.toString(order_id), Integer.toString(product_id) };

        Cursor cursorOrderItem = db.query(DatabaseContract.OrderItemEntry.TABLE_NAME, null,
                COLUMN_ORDER_ID + " = ? AND " +
                        DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID + " = ?",
                params, null, null, null);

        if(cursorOrderItem.moveToFirst()) {

            int qty = cursorOrderItem.getInt(cursorOrderItem.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY));

            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY, qty + Qty);

            db.update(DatabaseContract.OrderItemEntry.TABLE_NAME, cv,
                    COLUMN_ORDER_ID + " = ? AND " +
                            DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID + " = ?",
                    params);

            cursorOrderItem.close();

            return true;
        }

        return false;
    }

    public String updateOrderquantity(int order_item_id, int Qty){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursorOrderItem = db.query(DatabaseContract.OrderItemEntry.TABLE_NAME, null,
                DatabaseContract.OrderItemEntry._ID + " = ?",
                new String[]{Integer.toString(order_item_id)}, null, null, null);

        cursorOrderItem.moveToFirst();

        int pid = cursorOrderItem.getInt(cursorOrderItem.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID));

        Cursor cursorProduct = db.query(DatabaseContract.ProductEntry.TABLE_NAME, null,
                DatabaseContract.ProductEntry._ID + " = ?",
                new String[]{Integer.toString(pid)}, null, null, null);

        cursorProduct.moveToFirst();

        int availableQty = cursorProduct.getInt(cursorProduct.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY));

        if (availableQty < Qty)
            return "Quantity cannot be greater than " + availableQty;

        String[] params = new String[]{Integer.toString(order_item_id) };

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY, Qty);

        db.update(DatabaseContract.OrderItemEntry.TABLE_NAME, cv,
                DatabaseContract.OrderItemEntry._ID + " = ?",
                params);

        return "Quantity Updated";
    }

    public List<Product> getCustomerOrderItems(String CustID) {
        List<Product> productsList = new ArrayList<>();

        String[] params = new String[]{ CustID, ORDER_SHOPPING_CART };

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID + " = ? AND " +
                        DatabaseContract.OrderEntry.COLUMN_STATUS + " = ?",
                params,
                null, null, null);

        if(cursor.getCount() == 0)
            return productsList;

        if (cursor.moveToFirst()) {
            int orderID = cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID));

            Cursor cursorOrderItems = db.query(DatabaseContract.OrderItemEntry.TABLE_NAME, null,
                    COLUMN_ORDER_ID + " = ?",
                    new String[] { Integer.toString(orderID) },
                    null, null, null);

            if (cursorOrderItems.moveToFirst()) {

                do { // for each row

                    int productID = cursorOrderItems.getInt(cursorOrderItems.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID));

                    Cursor cursorProduct = db.query(DatabaseContract.ProductEntry.TABLE_NAME, null,
                            DatabaseContract.ProductEntry._ID + " = ?",
                            new String[] { Integer.toString(productID) },
                            null, null, null);
                    cursorProduct.moveToFirst();

                    Product product = new Product();

                    try {
                        Bitmap productImage = getBitmapFromPath(cursorProduct.getString(cursorProduct.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE)));
                        product.setImage(productImage);
                    } catch (OutOfMemoryError e) {
                        Log.d(TAG, "getProducts: OutOfMemory when loading product image");
                        //e.printStackTrace();
                    }

                    product.setId(cursorOrderItems.getInt(cursorOrderItems.getColumnIndex(DatabaseContract.OrderItemEntry._ID)));

                    int pNameColIndex = cursorProduct.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME);
                    String pName = cursorProduct.getString(pNameColIndex);
                    product.setName(pName);
                    product.setDescription(cursorProduct.getString(cursorProduct.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)));
                    product.setPrice(cursorProduct.getString(cursorProduct.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE)));
                    product.setCategory(cursorProduct.getString(cursorProduct.getColumnIndex(COLUMN_CATEGORY)));
                    product.setQuantity(cursorOrderItems.getInt(cursorOrderItems.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY)));

                    productsList.add(product);

                    cursorProduct.close();
                } while (cursorOrderItems.moveToNext());

                cursorOrderItems.close();
            }
        }

        cursor.close();

        // return table as a list
        return productsList;
    }

    public List<OrderItem> getShoopingCartItems(String shoppingCartId){
        List<OrderItem> itemsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.OrderItemEntry.TABLE_NAME, null,
                COLUMN_ORDER_ID + " = '" + shoppingCartId + "'", null,
                null, null, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                OrderItem item = new OrderItem();
                item.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                item.setProductId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ITEM_QUANTITY)));

                itemsList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return itemsList;
    }

    public boolean deleteOrderItem(int ID) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(DatabaseContract.OrderItemEntry.TABLE_NAME,
                DatabaseContract.OrderItemEntry._ID + " = ?",
                new String[] { Integer.toString(ID) }) > 0;

    }

}
