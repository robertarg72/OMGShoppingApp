package com.centennialcollege.omgshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.centennialcollege.omgshoppingapp.database.DatabaseManager;
import com.centennialcollege.omgshoppingapp.utils.Utils;

import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_CUSTOMER_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_EMPLOYEE_ID;
import static com.centennialcollege.omgshoppingapp.utils.Utils.SHARED_PREFERENCES_USER_KEY;
import static com.centennialcollege.omgshoppingapp.utils.Utils.saveToSharedPreferences;
import static com.centennialcollege.omgshoppingapp.utils.Utils.tableCreatorString;
import static com.centennialcollege.omgshoppingapp.utils.Utils.tables;

public class LoginActivity extends AppCompatActivity {

    private DatabaseManager db;
    String un;
    String psd;
    EditText username;
    EditText password;
    Button btn;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database manager
        db = new DatabaseManager(this);
        db.dbInitialize(tables, tableCreatorString, Utils.getInitialImages(this));
        //db.dbInitialize(tables, tableCreatorString, null);

        RadioGroup rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                flag = i == R.id.radioButton ? 0 : 1;
            }
        });

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void onClicked(View view) {
        String customerId;
        String employeeId;
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        String userName = username.getText().toString();
        String passWord = password.getText().toString();
        if (flag == 0) {
            customerId = customerLogin(userName, passWord);
            if (customerId != null) {
                saveToSharedPreferences(this, SHARED_PREFERENCES_USER_KEY, username.getText().toString());
                saveToSharedPreferences(this, SHARED_PREFERENCES_CUSTOMER_ID, customerId);
                //Intent intent = new Intent(this, ProductsActivity.class);
                Intent intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            }


        } else {

            //if (employeeLogin(userName, passWord)) {
            employeeId = employeeLogin(userName, passWord);
            if (employeeId != null) {
                saveToSharedPreferences(this, SHARED_PREFERENCES_USER_KEY, username.getText().toString());
                saveToSharedPreferences(this, SHARED_PREFERENCES_EMPLOYEE_ID, employeeId);
                Intent intent = new Intent(this, OrdersManagementActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String customerLogin( String username, String password) {
        return db.getCustomerId(username, password);

    }

    public String employeeLogin( String username, String password) {
        return db.getEmployeeId(username, password);

    }

}
