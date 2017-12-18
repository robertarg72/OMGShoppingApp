package com.ling_argume.omgshoppingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import com.ling_argume.omgshoppingapp.utils.*;

import static com.ling_argume.omgshoppingapp.utils.Utils.getInitialImages;
import static com.ling_argume.omgshoppingapp.utils.Utils.tableCreatorString;
import static com.ling_argume.omgshoppingapp.utils.Utils.tables;

public class LoginActivity extends AppCompatActivity {

    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database manager
        db = new DatabaseManager(this);
        db.dbInitialize(tables, tableCreatorString, getInitialImages(this));

        // For testing table creation
        //SQLiteDatabase testdb = db.getReadableDatabase();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

}
