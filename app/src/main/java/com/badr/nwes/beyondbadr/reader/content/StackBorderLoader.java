package com.badr.nwes.beyondbadr.reader.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class StackBorderLoader
{
    private static Bitmap STACK_BORDER_BITMAP = null;
    public StackBorderLoader(Context context){
        if(STACK_BORDER_BITMAP == null){
            InputStream stack_border_stream = null;
            try {
                stack_border_stream = context.getAssets().open("stack_border.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();

            STACK_BORDER_BITMAP = BitmapFactory.decodeStream(stack_border_stream,null, options);
        }
    }
    public Bitmap GetStackBorderBitmap(){
        return STACK_BORDER_BITMAP;
    }
}
