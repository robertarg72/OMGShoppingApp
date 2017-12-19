package com.ling_argume.omgshoppingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ling_argume.omgshoppingapp.DatabaseContract;
import com.ling_argume.omgshoppingapp.R;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Utils {

    public static final String BOOTS = "Winter Boots";
    public static final String PENCIL = "Artistic Pencil";
    public static final String BALL = "Soccer Ball";
    public static final String CHAIR = "Basic Chair";
    public static final String PHONE = "S8 Phone";
    public static final String GUITAR = "AL 200 Guitar";

    public static final String SHARED_PREFENCES_STORE = "OMGASharedPreferences";
    public static final String SHARED_PREFENCES_USER_KEY = "UserName";
    public static final String SHARED_PREFENCES_CUSTOMER_ID = "CustomerID";
    public static final String SHARED_PREFENCES_EMPLOYEE_ID = "EmployeeID";


    public static final String tables[] = {
            DatabaseContract.CustomerEntry.TABLE_NAME,
            DatabaseContract.ClerkEntry.TABLE_NAME,
            DatabaseContract.ProductEntry.TABLE_NAME,
            DatabaseContract.OrderEntry.TABLE_NAME
    };

    public static final String tableCreatorString[] = {
            DatabaseContract.CustomerEntry.SQL_CREATE_TABLE,
            DatabaseContract.ClerkEntry.SQL_CREATE_TABLE,
            DatabaseContract.ProductEntry.SQL_CREATE_TABLE,
            DatabaseContract.OrderEntry.SQL_CREATE_TABLE
    };

    public static Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public static void saveToSharedPreferences(Context context, String key, String value) {
        SharedPreferences myPreference = context.getSharedPreferences(SHARED_PREFENCES_STORE, 0);
        SharedPreferences.Editor prefEditor = myPreference.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    public static String getFromSharedPreferences(Context context, String store, String key) {
        SharedPreferences myPref = context.getSharedPreferences(SHARED_PREFENCES_STORE, MODE_PRIVATE);
        return myPref.getString(key,"");
    }

    public static Map getInitialImages(Context context){
        Map<String,Bitmap> initialImages =  new HashMap<String,Bitmap>();

        Bitmap boots = BitmapFactory.decodeResource(context.getResources(), R.drawable.boots);
        Bitmap pencil = BitmapFactory.decodeResource(context.getResources(), R.drawable.pencil);
        Bitmap ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        Bitmap chair = BitmapFactory.decodeResource(context.getResources(), R.drawable.chair);
        Bitmap phone = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone);
        Bitmap guitar = BitmapFactory.decodeResource(context.getResources(), R.drawable.guitar);

        initialImages.put(BOOTS, boots);
        initialImages.put(PENCIL, pencil);
        initialImages.put(BALL, ball);
        initialImages.put(CHAIR, chair);
        initialImages.put(PHONE, phone);
        initialImages.put(GUITAR, guitar);

        return initialImages;
    }
}
