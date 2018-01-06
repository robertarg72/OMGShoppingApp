package com.ling_argume.omgshoppingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.widget.TextView;

import com.ling_argume.omgshoppingapp.database.DatabaseContract;
import com.ling_argume.omgshoppingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.ling_argume.omgshoppingapp.utils.ImageHelper.saveDrawableToInternalStorage;


public class Utils {

    // Constant for greeting the username
    private static final String GREETING = "Hello, ";
    public static final String AVAILABLE_TEXT_PREFIX = "Available: ";

    // Constants to be used for Orders
    public static final String ORDER_DEFAULT_EMPLOYEE_ID = "1";
    public static final String ORDER_ID_PREFIX = "Order Nbr ";
    public static final String ORDER_IN_PROCESS_TEXT = "In-Process";
    public static final String ORDER_DELIVERED_TEXT = "Delivered";
    public static final String ORDER_SHOPPING_CART = "Shopping-Cart";

    // Constants to be used for each order item
    public static final String ORDER_ITEM_AVAILABLE = "Stock Available";
    public static final String ORDER_ITEM_NOT_AVAILABLE = "Stock Not Available";

    // Internal Storage Folder, where images will be stored
    static final String INTERNAL_STORAGE_FOLDER = "ProductPictures";

    // Product Categories
    public static final String CATEGORY_LIBRARY = "Library";
    public static final String CATEGORY_SPORTS = "Sports";
    public static final String CATEGORY_COMPUTERS = "Computers";
    public static final String CATEGORY_ELECTRONICS = "Electronics";
    public static final String CATEGORY_MUSIC = "Music";
    public static final String CATEGORY_HOME = "Home";
    public static final String CATEGORY_CLOTHES = "Clothing";


    // To add new image:
    // 1. Copy image to drawables resource folder
    // 2. Add new string for the corresponding product. Content must be product name.
    // 3. Add new entry in "drawableMapping" Map, using data in 1. and 2.
    // 4. Make sure the product's category exists. If not, add it as a category string.
    // 5. Add one more product to be loaded by the DatabaseDataWorker
    public static final String BOOTS = "Winter Boots";
    public static final String PENCIL = "Artistic Pencil";
    public static final String BALL = "Soccer Ball";
    public static final String CHAIR = "Basic Chair";
    public static final String PHONE = "S8 Phone";
    public static final String GUITAR = "AL 200 Guitar";
    //public static final String NOTEBOOK = "Samsung Notebook";
    //public static final String DESKTOP = "Dell Desktop Computer";
    //public static final String SMART_TV = "Samsung Smart TV";
    //public static final String PINGPONG_TABLE = "Thibar Table Tennis table";

    private static Map<String, Integer> drawableMapping = new HashMap<String, Integer>(){{
        put(BOOTS, R.mipmap.boots);
        put(PENCIL, R.mipmap.pencil);
        put(BALL, R.mipmap.ball);
        put(CHAIR, R.mipmap.chair);
        put(PHONE, R.mipmap.phone);
        put(GUITAR, R.mipmap.guitar);
        //put(NOTEBOOK, R.drawable.notebook);
        //put(DESKTOP, R.drawable.desktop);
        //put(SMART_TV, R.drawable.smart_tv);
        //put(PINGPONG_TABLE, R.drawable.pingpongtable);
    }};

    // Constants for using Shared Preferences
    private static final String SHARED_PREFERENCES_STORE = "OMGASharedPreferences";
    public static final String SHARED_PREFERENCES_USER_KEY = "UserName";
    public static final String SHARED_PREFERENCES_CUSTOMER_ID = "CustomerID";
    public static final String SHARED_PREFERENCES_EMPLOYEE_ID = "EmployeeID";
    public static final String SHARED_PREFERENCES_PRODUCT_QUANTITY_UPDATED_FLAG = "QuantityUpdated";
    public static final String SHARED_PREFERENCES_UPDATED_FLAG_VALUE = "Flag";
    public static final String SHARED_PREFERENCES_UPDATED_PRODUCTS_LIST = "ProductsUpdated";
    public static final String SHARED_PREFERENCES_UPDATED_PRODUCTS_QUANTITY = "ProductQuantities";



    // Constants for SQLite creation of tables
    public static final String tables[] = {
            DatabaseContract.CustomerEntry.TABLE_NAME,
            DatabaseContract.ClerkEntry.TABLE_NAME,
            DatabaseContract.ProductEntry.TABLE_NAME,
            DatabaseContract.OrderEntry.TABLE_NAME,
            DatabaseContract.OrderItemEntry.TABLE_NAME
    };

    public static final String tableCreatorString[] = {
            DatabaseContract.CustomerEntry.SQL_CREATE_TABLE,
            DatabaseContract.ClerkEntry.SQL_CREATE_TABLE,
            DatabaseContract.ProductEntry.SQL_CREATE_TABLE,
            DatabaseContract.OrderEntry.SQL_CREATE_TABLE,
            DatabaseContract.OrderItemEntry.SQL_CREATE_TABLE
    };

    // Public static methods to be used for all classes in the project

    public static void saveToSharedPreferences(Context context, String key, String value) {
        SharedPreferences myPreference = context.getSharedPreferences(SHARED_PREFERENCES_STORE, 0);
        SharedPreferences.Editor prefEditor = myPreference.edit();
        prefEditor.putString(key, value);
        //prefEditor.commit(); // this writes to persistent storage inmediately
        prefEditor.apply(); // this will handle writing in the background
    }

    public static String getFromSharedPreferences(Context context, String key) {
        SharedPreferences myPref = context.getSharedPreferences(SHARED_PREFERENCES_STORE, MODE_PRIVATE);
        return myPref.getString(key,"");
    }

    public static Map<String, String> getInitialImages(Context context) {
        Map<String, String> imagePathMapping = new HashMap<>();

        for (Map.Entry<String, Integer> entry : drawableMapping.entrySet()) {
            //imagePathMapping.put(BOOTS, saveDrawableToInternalStorage(context, R.drawable.boots));
            imagePathMapping.put(entry.getKey(), saveDrawableToInternalStorage(context, entry.getKey(), entry.getValue()));
        }

        return imagePathMapping;

        // Using lambda expression (requires API 24)
        //imagePathMapping.forEach((k,v)->{imagePathMapping.put(k, saveDrawableToInternalStorage(context, v)); });

//        Map<String,Bitmap> initialImages =  new HashMap<String,Bitmap>();
//        Bitmap boots = BitmapFactory.decodeResource(context.getResources(), R.drawable.boots);
//        Bitmap pencil = BitmapFactory.decodeResource(context.getResources(), R.drawable.pencil);
//        Bitmap ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
//        Bitmap chair = BitmapFactory.decodeResource(context.getResources(), R.drawable.chair);
//        Bitmap phone = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone);
//        Bitmap guitar = BitmapFactory.decodeResource(context.getResources(), R.drawable.guitar);
//
//        initialImages.put(BOOTS, boots);
//        initialImages.put(PENCIL, pencil);
//        initialImages.put(BALL, ball);
//        initialImages.put(CHAIR, chair);
//        initialImages.put(PHONE, phone);
//        initialImages.put(GUITAR, guitar);
//
//        return initialImages;

    }

    public static void setUserGreetingTextView(Activity context, int textViewId) {
        String username = getFromSharedPreferences(context, SHARED_PREFERENCES_USER_KEY);
        TextView loggedInUserGreeting = context.findViewById(textViewId);
        String greeting = GREETING + username;
        loggedInUserGreeting.setText(greeting);
        loggedInUserGreeting.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.CANADA);
        return df.format(c.getTime());
    }


}
