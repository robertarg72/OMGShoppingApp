package com.ling_argume.omgshoppingapp.database;

import android.provider.BaseColumns;


// Description field added to Product table
// Quantity in Product table represent current stock for that product

// Quantity, ShippingAddress, and Card Details fields added to Order table
// Quantity in Order table represents number of units a customer wants to buy
// EmployeeId in Order table represents the last employee who updated status for that order

public final class DatabaseContract {
    private DatabaseContract() {}

    public final class CustomerEntry implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        static final String COLUMN_USERNAME = "username";
        static final String COLUMN_PASSWORD = "password";
        static final String COLUMN_FIRSTNAME = "firstname";
        static final String COLUMN_LASTNAME = "lastname";
        static final String COLUMN_ADDRESS = "address";
        static final String COLUMN_CITY = "city";
        static final String COLUMN_POSTALCODE = "postalcode";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                        COLUMN_PASSWORD + " TEXT NOT NULL, " +
                        COLUMN_FIRSTNAME + " TEXT, " +
                        COLUMN_LASTNAME + " TEXT, " +
                        COLUMN_ADDRESS + " TEXT, " +
                        COLUMN_CITY + " TEXT, " +
                        COLUMN_POSTALCODE + " TEXT)";
    }

    public final class ClerkEntry implements BaseColumns {
        public static final String TABLE_NAME = "clerk";
        static final String COLUMN_USERNAME = "username";
        static final String COLUMN_PASSWORD = "password";
        static final String COLUMN_FIRSTNAME = "firstname";
        static final String COLUMN_LASTNAME = "lastname";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                            COLUMN_PASSWORD  + " TEXT NOT NULL, " +
                            COLUMN_FIRSTNAME + " TEXT, " +
                            COLUMN_LASTNAME + " TEXT)";
    }

    public final class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "product";
        static final String COLUMN_PRODUCTNAME = "productname";
        static final String COLUMN_IMAGE = "imagepath";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        static final String COLUMN_CATEGORY = "category";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PRODUCTNAME + " TEXT NOT NULL, " +
                        COLUMN_IMAGE + " TEXT NOT NULL, " +
                        COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        COLUMN_PRICE + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " INT, " +
                        COLUMN_CATEGORY + " TEXT)";
    }

    public final class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "customerorder";
        public static final String COLUMN_CUSTOMER_ID = "customerid";
        public static final String COLUMN_PRODUCT_ID = "productid";
        public static final String COLUMN_EMPLOYEE_ID = "employeeid";
        public static final String COLUMN_ORDER_QUANTITY = "quantity";
        public static final String COLUMN_SHIPPING_ADDRESS = "shippingaddress";
        public static final String COLUMN_CARD_TYPE = "cardtype";
        public static final String COLUMN_CARD_NUMBER = "cardnumber";
        public static final String COLUMN_CARD_OWNER = "cardowner";
        public static final String COLUMN_CARD_EXPIRATION_MONTH = "cardexpirationmonth";
        public static final String COLUMN_CARD_EXPIRATION_YEAR = "cardexpirationyear";
        public static final String COLUMN_CARD_SECURITY_CODE = "cardsecuritycode";
        public static final String COLUMN_ORDER_DATE = "orderdate";
        public static final String COLUMN_STATUS = "status";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_CUSTOMER_ID + " INTEGER NOT NULL, " +
                        COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " +
                        COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
                        COLUMN_ORDER_QUANTITY + " INT NOT NULL, " +
                        COLUMN_SHIPPING_ADDRESS + " TEXT, " +
                        COLUMN_CARD_TYPE + " TEXT, " +
                        COLUMN_CARD_NUMBER + " TEXT, " +
                        COLUMN_CARD_OWNER + " TEXT, " +
                        COLUMN_CARD_EXPIRATION_MONTH + " TEXT, " +
                        COLUMN_CARD_EXPIRATION_YEAR + " TEXT, " +
                        COLUMN_CARD_SECURITY_CODE + " TEXT, " +
                        COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
                        COLUMN_STATUS + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + COLUMN_CUSTOMER_ID + ") REFERENCES customer(_ID), " +
                        "FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES product(_ID), " +
                        "FOREIGN KEY(" + COLUMN_EMPLOYEE_ID + ") REFERENCES clerk(_ID))";
    }

}






