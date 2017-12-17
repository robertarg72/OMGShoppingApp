package com.ling_argume.omgshoppingapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private static final String tables[] = {
        ShoppingDatabaseContract.CustomerEntry.TABLE_NAME,
        ShoppingDatabaseContract.ClerkEntry.TABLE_NAME,
        ShoppingDatabaseContract.ProductEntry.TABLE_NAME,
        ShoppingDatabaseContract.OrderEntry.TABLE_NAME
    };

    private static final String tableCreatorString[] = {
        ShoppingDatabaseContract.CustomerEntry.SQL_CREATE_TABLE,
        ShoppingDatabaseContract.ClerkEntry.SQL_CREATE_TABLE,
        ShoppingDatabaseContract.ProductEntry.SQL_CREATE_TABLE,
        ShoppingDatabaseContract.OrderEntry.SQL_CREATE_TABLE
    };

    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database manager
        db = new DatabaseManager(this);
        db.dbInitialize(tables, tableCreatorString);

        // For testing table creation
        //SQLiteDatabase testdb = db.getReadableDatabase();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
