package com.centennialcollege.omgshoppingapp.utils;

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

import static com.centennialcollege.omgshoppingapp.utils.Utils.INTERNAL_STORAGE_FOLDER;

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

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Bitmap getBitmapFromPath(String picturePath) throws OutOfMemoryError {
        if (picturePath == null || picturePath.length() == 0)
            return null;
        //return BitmapFactory.decodeFile(picturePath);

        BitmapFactory.Options options = new BitmapFactory.Options();

        //int sampleSize = calculateInSampleSize(options, 120, 120);
        //options.inSampleSize = sampleSize;

        // Force minimum sample size to avoid OutOfMemory exception
        options.inSampleSize = 8;
        return BitmapFactory.decodeFile(picturePath,options);
    }

    public static Bitmap getBitmapFromResource(Context context, Integer resouceId) throws OutOfMemoryError {

        BitmapFactory.Options options = new BitmapFactory.Options();
        // Force minimum sample size to avoid OutOfMemory exception
        options.inSampleSize = 8;
        return BitmapFactory.decodeResource(context.getResources(), resouceId);
    }

    public static Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        return BitmapFactory.decodeStream(arrayInputStream);
    }

    // Save a bitmap as a png immage to internal storage.
    // Parameter pictureId must be unique because it will be used as the picture name in the storage
    // If there is any problem, it will return an empty string
    public static String saveBitmapToInternalStorage(Context context, String imageName, Bitmap picture) {

        String picturePath;
        File internalStorage = context.getDir(INTERNAL_STORAGE_FOLDER, Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, imageName + ".png");
        picturePath = reportFilePath.toString();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(reportFilePath);
            picture.compress(Bitmap.CompressFormat.JPEG, 50, fos);
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

    // Calculates the sample size to decode an Bitmap image. This prevent Out of memory exceptions
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
