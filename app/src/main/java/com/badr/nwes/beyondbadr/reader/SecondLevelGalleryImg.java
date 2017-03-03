package com.badr.nwes.beyondbadr.reader;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.reader.content.BigImagePagerAdapter;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class SecondLevelGalleryImg {
    private CustomViewPager gallery;
    private LinearLayout layout;

    public SecondLevelGalleryImg(Context context, IFinalizeActivity finalizer)
    {
        //Log.Info ("inicie 2do activity", "SecondACtivity initiatedddddddddddd");
        layout = new LinearLayout(context);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        layout.setOrientation( LinearLayout.HORIZONTAL );

        gallery = new CustomViewPager(context, finalizer);

         gallery.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        gallery.setAdapter( new BigImagePagerAdapter(context,  finalizer) );
    }

    public CustomViewPager GetGalleryLayout()
    {
        return gallery;
    }

}
