package com.ling_argume.omgshoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ling_argume.omgshoppingapp.model.Order;
import com.ling_argume.omgshoppingapp.model.Product;

import static com.ling_argume.omgshoppingapp.utils.Utils.ByteArrayToBitmap;


public class DatabaseManager extends SQLiteOpenHelper {
    //
	private static final String DATABASE_NAME = "omgshopping.db";
    private static final int DATABASE_VERSION = 1;
    //
    private Map initialImages;
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
    public void dbInitialize(String[] tables, String tableCreatorString[], Map initialImages)
    {
  	  this.tables=tables;
  	  this.tableCreatorString=tableCreatorString;
  	  this.initialImages = initialImages;
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Drop existing tables
    	for (int i=0;i<tables.length;i++)
    		db.execSQL("DROP TABLE IF EXISTS " + tables[i]);
        //create them
    	for (int i=0;i<tables.length;i++)
    		db.execSQL(tableCreatorString[i]);

    	// Load initial data in database
    	DatabaseDataWorker worker = new DatabaseDataWorker(db);
    	worker.insertCustomers();
    	worker.insertClerks();
    	worker.insertProducts(initialImages);
    	worker.insertOrders();

    }
    //create the database
    public void createDatabase(Context context)
    {
    	SQLiteDatabase mDatabase;
    	mDatabase = context.openOrCreateDatabase(
    	DATABASE_NAME,
    	SQLiteDatabase.CREATE_IF_NECESSARY,
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
    	for (int i=0;i<tables.length;i++)
    		db.execSQL("DROP TABLE IF EXISTS " + tables[i]);

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

    // Read all records
    public List getTable(String tableName) {
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList row=new ArrayList(); //to store one row
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

        // return table as a list
        return table;
    }

    // Read all product records
    public List<Product> getProducts() {
        List<Product> productsList = new ArrayList<Product>();
        // Select all records
        String selectQuery = "SELECT  * FROM " + DatabaseContract.ProductEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry._ID)));
                product.setName(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCTNAME)));
                product.setDescription(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getString( cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE)));
                product.setImage(ByteArrayToBitmap( cursor.getBlob(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE))));
                product.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_CATEGORY)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY)));
                productsList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return productsList;
    }

    public List<Order> getOrders() {
        List<Order> ordersList = new ArrayList<Order>();
        // Select all records
        String selectQuery = "SELECT  * FROM " + DatabaseContract.OrderEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            { // for each row
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                order.setProductId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_PRODUCT_ID)));
                order.setCustomerId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID)));
                order.setEmployeeId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_EMPLOYEE_ID)));

                order.setOrderDate(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE)));
                order.setOrderDate(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_STATUS)));
                ordersList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return table as a list
        return ordersList;
    }

    public List<Order> getOrdersByCustomerId(String id) {
        Cursor cursor = null;
        List<Order> ordersList = new ArrayList<Order>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{ id };
            cursor = db.query(DatabaseContract.OrderEntry.TABLE_NAME, null,
                    DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID + " = ?", params,
                    null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Order order = new Order();
                    order.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry._ID)));
                    order.setOrderDate(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE)));
                    order.setStatus(cursor.getString( cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_STATUS)));
                    ordersList.add(order);
                } while (cursor.moveToNext());
            }
            return ordersList;
        }finally {
            cursor.close();
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
                product.setImage(ByteArrayToBitmap( cursor.getBlob(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE))));
                product.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_CATEGORY)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_QUANTITY)));
            }
            return product;
        }finally {
            cursor.close();
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

}
