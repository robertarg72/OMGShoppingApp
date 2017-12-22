package com.ling_argume.omgshoppingapp.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.ling_argume.omgshoppingapp.utils.Utils.INTERNAL_STORAGE_FOLDER;

public class ImageHelper {

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Bitmap getBitmapFromPath(String picturePath) {
        if (picturePath == null || picturePath.length() == 0)
            return null;
        return BitmapFactory.decodeFile(picturePath);
    }

    public static Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    // Save a bitmap as a png immage to internal storage.
    // Parameter pictureId must be unique because it will be used as the picture name in the storage
    // If there is any problem, it will return an empty string
    public static String saveBitmapToInternalStorage(Context context, String imageName, Bitmap picture) {

        String picturePath = "";
        File internalStorage = context.getDir(INTERNAL_STORAGE_FOLDER, Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, imageName + ".png");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            picture.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception ex) {
            Log.i("INTERNAL STORAGE", "Problem saving bitmap image", ex);
            picturePath = "";
        }

        return picturePath;
    }

    public static String saveDrawableToInternalStorage(Context context, String imageName, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        // UUID.randomUUID().toString() to generate a unique filename. Problem: files are copied many times

        return ImageHelper.saveBitmapToInternalStorage(context, imageName.replace(" ","_"), bitmap);
    }
}
