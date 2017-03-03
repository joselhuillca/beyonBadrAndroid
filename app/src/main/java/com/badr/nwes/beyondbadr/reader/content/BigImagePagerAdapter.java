package com.badr.nwes.beyondbadr.reader.content;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.DataSourceType;
import com.badr.nwes.beyondbadr.data.NSLayer;
import com.badr.nwes.beyondbadr.data.NSPage;
import com.badr.nwes.beyondbadr.data.PListLoader;
import com.badr.nwes.beyondbadr.data.PageDataSource;
import com.badr.nwes.beyondbadr.reader.CustomViewPager;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;
import com.badr.nwes.beyondbadr.index.PageIndex;
import com.badr.nwes.beyondbadr.reader.WithoutBGImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class BigImagePagerAdapter extends PagerAdapter {
    private Context context_;
    private IFinalizeActivity finalizer_;

    //Android.Graphics.Bitmap input_image_bitmap;
    //Stream input_image;
    public BigImagePagerAdapter(Context context, IFinalizeActivity finalizer) {

        if (DataSourceMapper.DS_LISTA_CHAPTER == null)
            DataSourceMapper.InitIndex();
        context_ = context;
        finalizer_ = finalizer;
    }


    @Override
    public int getCount() {
        return DataSourceMapper.NUM_OF_ELEMS;
    }

    private void LoadPageDataSource (PageDataSource page, int position, RelativeLayout section_layout )
    {
        PListLoader loader = new PListLoader(context_);
        //page = loader.LoadPageDataSource(page.ContentDir, page.ContentFile);
        //InfScrollPaged.book.Chapters[DataSourceMapper.DS_LISTA_CHAPTER[position] ].Sections[  DataSourceMapper.DS_LISTA_SECTION[position] ].Pages[ DataSourceMapper.DS_LISTA_IMAGE[position] ] = page;

        for(int i = 0; i < page.Slides.size(); i++) {
            NSPage slide = page.Slides.get(i);
            for(int j = 0; j < slide.Layers.size(); j++) {
                NSLayer layer = slide.Layers.get(j);
					/*if (layer.Type == DataSourceType.BACKGROUND) {
						ImageView image = null;
						image = new ImageView(context_);
						//image.SetX (layer.ThumbFrame.X );
						//image.SetY (layer.ThumbFrame.Y );

						image.LayoutParameters = new RelativeLayout.LayoutParams((int)RelativeLayout.LayoutParams.FillParent, (int)RelativeLayout.LayoutParams.FillParent);
						image.SetScaleType( ImageView.ScaleType.FitXy);

						Stream input_image = context_.Assets.Open(layer.ThumbPath);
						Android.Graphics.BitmapFactory.Options options = new Android.Graphics.BitmapFactory.Options();
						options.InSampleSize = 2;
						Android.Graphics.Bitmap input_image_bitmap = Android.Graphics.BitmapFactory.DecodeStream(input_image);

						image.SetImageBitmap(input_image_bitmap);

						section_layout.AddView(image);
					}*/
                if (layer.Type == DataSourceType.IMAGE) {
                    WithoutBGImageView image = null;
                    image = new WithoutBGImageView(context_, layer.CroppedFrame);
                    image.setX (layer.ThumbFrame.X );
                    image.setY (layer.ThumbFrame.Y );

                    image.setLayoutParams( new RelativeLayout.LayoutParams((int)layer.ThumbFrame.Width, (int)layer.ThumbFrame.Height) );

                    Bitmap input_image_bitmap  = SystemConfiguration.decodeSampledBitmapFromResource(layer.ThumbPath,0,0, this.context_);

                    image.setImageBitmap(input_image_bitmap);

                    PageIndex pageIndex = new PageIndex();
                    pageIndex.chapter = DataSourceMapper.DS_LISTA_CHAPTER[position] ;
                    pageIndex.section = DataSourceMapper.DS_LISTA_SECTION[position] ;
                    pageIndex.page = DataSourceMapper.DS_LISTA_IMAGE[position] ;
                    pageIndex.layer = j;
                    pageIndex.pos_x = (int)layer.ThumbFrame.X;
                    pageIndex.pos_y = (int)layer.ThumbFrame.Y;
                    pageIndex.height = (int)layer.ThumbFrame.Height;
                    pageIndex.width = (int)layer.ThumbFrame.Width;

                    image.setTag (pageIndex);
                    section_layout.addView(image);
                }
            }
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals( (RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PageDataSource page = InfScrollPaged.book.Chapters.get(DataSourceMapper.DS_LISTA_CHAPTER[position]).Sections.get(DataSourceMapper.DS_LISTA_SECTION[position]).Pages.get(DataSourceMapper.DS_LISTA_IMAGE[position]);

        NSPage nspage;
        if ( page.Slides.size() > 0 )
            nspage = page.Slides.get(0);

        RelativeLayout section_layout = new RelativeLayout(context_);

        section_layout.setLayoutParams( new ViewPager.LayoutParams ());


        String filepath = InfScrollPaged.book.Chapters.get(DataSourceMapper.DS_LISTA_CHAPTER[position]).Sections.get(DataSourceMapper.DS_LISTA_SECTION[position]).Pages.get(DataSourceMapper.DS_LISTA_IMAGE[position]).FullSource;
        //Log.Info ("loading", "loading file path: " + nspage.BackgroundImage);
        final ImageView touchable_image = new ImageView(context_);

        AssetManager assetManager = context_.getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap input_image_bitmap  = BitmapFactory.decodeStream(istr, null, options);

        touchable_image.setImageBitmap(input_image_bitmap);


        touchable_image.setScaleType( ImageView.ScaleType.FIT_XY);
        touchable_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewPager vi_pager = (CustomViewPager)  touchable_image.getParent().getParent();
                if ( vi_pager.getEnabledViewPager()  && vi_pager.getIsTouchedInAnimation() == false){

                    //sm_animated_image.getImageMatrix().getValues(sm_matrix_values);

                    finalizer_.finalizeNow();
                }
            }
        });
        touchable_image.setLayoutParams( new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT) );
        //Log.Info("flo", "info :" + section_layout.LayoutParameters.Width + " " + section_layout.LayoutParameters.Height );
        //touchable_image.LayoutParameters.Height = 1504;
        section_layout.addView(touchable_image);
        LoadPageDataSource(page, position, section_layout);

        //touchable_image.SetX(0.0f);
        //touchable_image.SetY(0.0f);
        ( (ViewPager) container).addView(section_layout, 0);
        return section_layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Log.Info("Memory" , "Second level Destroying item: " + position);
        RelativeLayout view = (RelativeLayout) object;
        ((ViewPager) container).removeView(view);
        view = null;
        //System.GC.Collect();
    }
}
