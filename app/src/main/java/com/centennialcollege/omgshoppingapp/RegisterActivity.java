package com.centennialcollege.omgshoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.centennialcollege.omgshoppingapp.database.DatabaseContract;
import com.centennialcollege.omgshoppingapp.database.DatabaseManager;

import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_EMPLOYEE_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_USER_KEY;
import static com.centennialcollege.omgshoppingapp.utils.Utils.saveToSharedPreferences;

public class RegisterActivity extends AppCompatActivity {

    EditText firstNameET;
    EditText lastNameET;
    EditText addressET;
    EditText cityET;
    EditText postalCodeET;
    EditText userNameET;
    EditText passwordET;

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void onCreateClicked(View view) {
        dbm = new DatabaseManager(this);

        firstNameET = findViewById(R.id.firstName);
        lastNameET = findViewById(R.id.lastName);
        addressET = findViewById(R.id.address);
        cityET = findViewById(R.id.city);
        postalCodeET = findViewById(R.id.postalCode);
        userNameET = findViewById(R.id.userName);
        passwordET = findViewById(R.id.password);

        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String address = addressET.getText().toString();
        String city = cityET.getText().toString();
        String postalCode = postalCodeET.getText().toString();
        String userName = userNameET.getText().toString();
        String passWord = passwordET.getText().toString();

        ContentValues cv  = new ContentValues();
        cv.put(DatabaseContract.CustomerEntry.COLUMN_FIRSTNAME, firstName);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_LASTNAME, lastName);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_ADDRESS, address);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_CITY, city);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_POSTALCODE, postalCode);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_USERNAME, userName);
        cv.put(DatabaseContract.CustomerEntry.COLUMN_PASSWORD, passWord);

        dbm.addRecordUsingContentValues(cv, DatabaseContract.CustomerEntry.TABLE_NAME);

        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}




