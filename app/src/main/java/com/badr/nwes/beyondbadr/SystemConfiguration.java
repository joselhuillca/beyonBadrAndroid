package com.badr.nwes.beyondbadr;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Kelvin on 2016-10-03.
 */
public class SystemConfiguration
{
    public static int FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG = 0;
    public static int SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG = 0;
    public static int ONE_INTENT_ACTIVATION_FLAG = 0;
    public static int SIZE_RESOLUTION_SCREEN_HEIGHT = 1504;
    public static int SIZE_RESOLUTION_SCREEN_WIDTH = 2560;

    public static float NUMBER_OF_THUMBNAILS_IN_SCREEN = 4.5f;

    public static int SCREEN_HEIGHT_DIVIDER = 4;
    public static int SCREEN_HEIGHT_OFFSET = 40;

    public static int GRID_COLUMNS = 7;
    public static int GRID_ROWS = 4;
    public static long DEVICE_MEMORY = 0;
    //public static float


    /////////////// Configuration for the ProportionCalculator class
    public static float BACKGROUND_BORDER_HOR_PROPORTION = 0.15f;
    public static float BACKGROUND_BORDER_VER_PROPORTION = 0.25f;
    public static float OFFSET_CONTENT_VER_TOP_PROPORTION = 0.40f;
    public static float OFFSET_CONTENT_HOR_TOP_PROPORTION = 0.50f;
    /////////////////////////////////////////////////////////

    public static float SCALE_PROP_FS_TO_SC_LEVEL = 0.2f;
    public static float SCALE_PROP_SC_TO_FS_LEVEL = 0.9f;
    public static float SCALE_PROP_SC_TO_TH_LEVEL = 1.5f;
    public static int SCALE_ANIMATION_MILLISECONDS = 350;


    //////////////////////////////////////////////////////////////////

    public static int DIMENSION_DESING_WIDTH = 640;
    public static int DIMENSION_DESING_HEIGHT = 1136;
    public static int WIDTH_PIXEL;
    public static int HEIGHT_PIXEL;
    public static float DENSITY;

    public static int IndiceActual=0;

    // Replace here you own Facebook App Id, if you don't have one go to
    // https://developers.facebook.com/apps
    public static String AppId = "1070001599751830";
    // For extensive list of available extended permissions refer to
    // https://developers.facebook.com/docs/reference/api/permissions/
    //</remarks>
    public static String ExtendedPermissions = "user_about_me,publish_actions";

    public static String azul= "#00c6ff";
    public static String lila = "#de2ef6";
    public static String verde ="#65c921";
    public static String amarillo = "#ffd200";
    public static String naranja = "#ff9600";
    public static String rosa = "#ff3891";

    public static ArrayList<Bitmap> adds;

    public static String[] ListaColores = {azul,lila,verde,amarillo,naranja,rosa};
    public static String[] ListaColores50Campamentos = { "#E98300", "#97233F", "#5B8F22",azul };
    public static String colorGlobal = azul;
    public static String colorPartida = "#AA272F";
    public static String colorCalzado = "#6AADE4";
    public static String colorExpertos = "#00549F";

    public static int TYPE_TEXT = 1;
    public static int TYPE_IMAGE = 2;


    public static int getHeight(int value){

        return HEIGHT_PIXEL*value/DIMENSION_DESING_HEIGHT;
    }

    public static int getWidth(int value){
        return WIDTH_PIXEL*value/DIMENSION_DESING_WIDTH;
    }

    public static void setWidthPixel(int value){
        WIDTH_PIXEL=value;
    }

    public static void setHeigthPixel(int value){
        HEIGHT_PIXEL=value;
    }

    public static void setDensity(float value){
        DENSITY=value;
    }
    public static float getMinDpi () {
        float widthDp = WIDTH_PIXEL / DENSITY;
        float heightDp = HEIGHT_PIXEL / DENSITY;
        float smallestWidth = Math.min(widthDp, heightDp);
        return smallestWidth;
    }


    public static Drawable getDrawableResource (Context context, int res ) {
        float widthDp = WIDTH_PIXEL / DENSITY;
        float heightDp = HEIGHT_PIXEL / DENSITY;
        float smallestWidth = Math.min(widthDp, heightDp);
        Drawable ret = null;
        if (smallestWidth > 600) {
            ret =  context.getResources().getDrawableForDensity( res, DisplayMetrics.DENSITY_HIGH );
        }
        else {
            ret =  context.getResources().getDrawableForDensity( res, DisplayMetrics.DENSITY_DEFAULT);
        }
        return ret;
    }

    public static  int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            int heightRatio = (int)Math.round((float) height / (float) reqHeight);
            int widthRatio = (int)Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight, Context context) {

        // First decode with inJustDecodeBounds=true to check dimensions
        /*final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        AssetManager assetManager = context.getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeStream(istr, null, options);
        return bitmap; */
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        AssetManager assetManager = context.getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr, null, options);
        return bitmap;
    }

    public static String setJsonString(String path,Context context){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(path);

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer);

        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }
}

