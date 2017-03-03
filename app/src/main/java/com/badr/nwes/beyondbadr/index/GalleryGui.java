package com.badr.nwes.beyondbadr.index;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.SystemConfiguration;


class ImagePagerAdapter extends PagerAdapter {

    Context context_;

    public ImagePagerAdapter( Context context){
        context_ = context;
    }

    @Override
    public int getCount() {
        return 5; // @todo : change Count dynamic
    }

    @Override
    public boolean isViewFromObject(View p0, Object p1) {
        return p0.equals  ( (ImageView) p1); //@// TODO: 2016-10-15  is equals??
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context_);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //
        int img_big_sz_x_ = ((Activity)context_).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)context_).getWindowManager().getDefaultDisplay().getHeight();


        String input_image = "chapter" + (position+1) + ".jpg";

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(input_image, img_big_sz_x_, img_big_sz_y_, context_);

        imageView.setImageBitmap(input_image_bitmap);

        ( (ViewPager) container).addView(imageView, 0);
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ImageView view = (ImageView) object;
        ((ViewPager) container).removeView(view);
        view = null;

        //super.destroyItem(container, position, object);
    }

}
/**
 * Created by Kelvin on 2016-10-14.
 */
public class GalleryGui
{
    private ObservableHorViewPager gallery = null;
    private LinearLayout layout = null;
    int image_sz_x_;
    int image_sz_y_;

    public GalleryGui(Context context, int image_sz_x, int image_sz_y, boolean put_error)
    {
        image_sz_x_ = image_sz_x;
        image_sz_y_ = image_sz_y;

        layout = new LinearLayout(context);
        layout.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT) );
        layout.setOrientation( LinearLayout.HORIZONTAL ) ;

        gallery = new ObservableHorViewPager(context);

        gallery.setLayoutParams( new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) );

        gallery.setAdapter( new ImagePagerAdapter(context));
    }
    public ObservableHorViewPager GetGalleryLayout()
    {
        return gallery;
    }

}
