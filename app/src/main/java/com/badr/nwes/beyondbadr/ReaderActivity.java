package com.badr.nwes.beyondbadr;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.reader.CustomViewPager;
import com.badr.nwes.beyondbadr.reader.SecondLevelGalleryImg;


/**
 * Created by Kelvin on 2016-10-14.
 */
class ReaderActivity extends AppCompatActivity implements IFinalizeActivity {
    CustomViewPager pager_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout rel_layout = new RelativeLayout(this);
        rel_layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        int image_id_resource = this.getIntent().getIntExtra("image_id", 0);
        int image_width = this.getIntent().getIntExtra("image_width", 100);
        int image_height = this.getIntent().getIntExtra("image_height", 100);

        int ind_chapter = this.getIntent().getIntExtra("ind_chapter", 0);
        int ind_section = this.getIntent().getIntExtra("ind_section", 0);
        int ind_image = this.getIntent().getIntExtra("ind_image", 0);


        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();

        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();

        int unique_id_image = this.getIntent().getIntExtra("unique_id_image", 0);

        SecondLevelGalleryImg gallery = new SecondLevelGalleryImg(this, this);
        pager_ = gallery.GetGalleryLayout();
        pager_.setLayoutParams( new RelativeLayout.LayoutParams(img_big_sz_x_, img_big_sz_y_) );

        pager_.setCurrentItem(unique_id_image, false);
        rel_layout.addView(pager_);
        setContentView(rel_layout);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    //@// TODO: 2016-10-07 implements this functions
    @Override
    public void finalizeNow() {

    }

    @Override
    public void readyToFinalize() {

    }
}
