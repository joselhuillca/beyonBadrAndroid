package com.badr.nwes.beyondbadr.reader;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.FirstLevelActivity;
import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.reader.content.CustomImageView;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class FullScreenAnimatedActivity extends Activity implements IFinalizeActivity {

    public void finalizeNow ()
    {
        onBackPressed();
    }

    public void readyToFinalize(){

        CustomImageView.ResetFirstToSecondImage();
        //Log.Info("FullScreenD", "On key Down : Exit" );

        FirstLevelActivity.PAGE_TO_GO = pager_.getCurrentItem();

        FirstLevelActivity.scroll_img.SetImageAlpha(pager_.getCurrentItem(), 0);

        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();

        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();

        x_pos_image = FirstLevelActivity.scroll_img.SnapToItem(pager_.getCurrentItem());

        y_pos_image = ( 2 * img_big_sz_y_) / 3;
    }


    RelativeLayout rel_layout;
    CustomViewPager pager_;
    int image_width;
    int image_height;
    int x_pos_image = 0;
    int y_pos_image = 0;
    int one_exit = 0;
    static Object Lock = new Object();
    float []m_thumb_matrix_values = new float[9];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

         super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rel_layout = new RelativeLayout(this);
        rel_layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        m_thumb_matrix_values[0] = this.getIntent().getFloatExtra(CustomImageView.SX_MATRIX, 0.0f);
        m_thumb_matrix_values[4] = this.getIntent().getFloatExtra(CustomImageView.SY_MATRIX, 0.0f);
        m_thumb_matrix_values[1] = this.getIntent().getFloatExtra(CustomImageView.RX_MATRIX, 0.0f);
        m_thumb_matrix_values[3] = this.getIntent().getFloatExtra(CustomImageView.RY_MATRIX, 0.0f);
        m_thumb_matrix_values[2] = this.getIntent().getFloatExtra(CustomImageView.TX_MATRIX, 0.0f);
        m_thumb_matrix_values[5] = this.getIntent().getFloatExtra(CustomImageView.TY_MATRIX, 0.0f);
        m_thumb_matrix_values[6] = m_thumb_matrix_values[7] = 0.0f;
        m_thumb_matrix_values[8] = 1.0f;

        int image_id_resource = this.getIntent().getIntExtra("image_id", 0);
        image_width = this.getIntent().getIntExtra("image_width", 100);
        image_height = this.getIntent().getIntExtra("image_height", 100);
        int image_position_x = this.getIntent().getIntExtra("image_position_x", 100);

        int ind_chapter = this.getIntent().getIntExtra("ind_chapter", 0);
        int ind_section = this.getIntent().getIntExtra("ind_section", 0);
        int ind_image = this.getIntent().getIntExtra("ind_image", 0);

        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();

        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();


        int unique_id_image = this.getIntent().getIntExtra("unique_id_image", 0);
        SecondLevelGalleryImg gallery = new  SecondLevelGalleryImg(this, this);
        pager_ = gallery.GetGalleryLayout();
        pager_.setLayoutParams( new RelativeLayout.LayoutParams(img_big_sz_x_, img_big_sz_y_) );

        pager_.setCurrentItem(unique_id_image, false);

        //rel_layout.SetBackgroundColor(Color.Black);

			/*FullScreenAnimation scale = new FullScreenAnimation(image_position_x,( 2 *img_big_sz_y_) / 3, image_position_x + image_width, ( 2 *img_big_sz_y_) / 3 + image_height, img_big_sz_x_, img_big_sz_y_, rel_layout);
			//scale.FillAfter = true;
			scale.Duration = 300;
			pager_.StartAnimation(scale); */

        rel_layout.addView(pager_);
        setContentView(rel_layout);



    }

    @Override
    protected void onResume() {
        SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG = 0;
        super.onResume();
    }

    @Override
    public void onBackPressed() {
         synchronized (Lock)
        {
            if(one_exit >=1)
                return;

            java.util.concurrent.locks.Lock   lock = new ReentrantLock();
            lock.lock();
            one_exit++;
            lock.unlock();

            readyToFinalize();
            m_thumb_matrix_values[2] = x_pos_image;
            pager_.InitializeAnimatedImage();
            pager_.PerformAnimation(m_thumb_matrix_values, false);
            pager_.setEnabledViewPager (false );

            Runnable wait_runnable= new Runnable() {
                @Override
                public void run() {
                        //Log.Info("mmmmmmm llegue", "end animation?????? " + one_exit);
                        rel_layout = null;
                        pager_ = null;
                        FullScreenAnimatedActivity.super.onBackPressed ();
                }
            };
            rel_layout.postDelayed(wait_runnable, SystemConfiguration.SCALE_ANIMATION_MILLISECONDS);
        }
    }

}
