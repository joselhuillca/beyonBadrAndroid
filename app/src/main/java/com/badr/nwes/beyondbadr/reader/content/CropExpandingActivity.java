package com.badr.nwes.beyondbadr.reader.content;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.BookDataSource;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;
import com.badr.nwes.beyondbadr.reader.WithoutBGImageView;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class CropExpandingActivity extends Activity implements IFinalizeActivity {

    static Object Lock = new Object();
    ThridLevelImageView m_touchable_image;
    Bitmap m_input_image_bitmap;
    //Stream input_image;
    Runnable m_runnable_animation;
    int ind_pos_x;
    int ind_pos_y;
    int ind_width;
    int ind_height;
    private static float[] sm_matrix_values;

    int one_exit = 0;
    float m_sx;
    float m_sy;
    float m_rx;
    float m_ry;
    float m_tx;
    float m_ty;

    float m_back_sx;
    float m_back_sy;
    float m_back_tx;
    float m_back_ty;
    RelativeLayout m_rel_layout;
    boolean animation_canceled = false;

    public void CancelAnimation() {
        animation_canceled = true;
    }

    @Override
    protected void onCreate(Bundle bundle) {

        BookDataSource book = InfScrollPaged.book;
        super.onCreate (bundle);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        m_rel_layout = new RelativeLayout(this);
        m_rel_layout.setLayoutParams( new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        m_sx = this.getIntent().getFloatExtra(WithoutBGImageView.SX_MATRIX, 0.0f);
        m_sy = this.getIntent().getFloatExtra(WithoutBGImageView.SY_MATRIX, 0.0f);
        m_rx = this.getIntent().getFloatExtra(WithoutBGImageView.RX_MATRIX, 0.0f);
        m_ry = this.getIntent().getFloatExtra(WithoutBGImageView.RY_MATRIX, 0.0f);
        m_tx = this.getIntent().getFloatExtra(WithoutBGImageView.TX_MATRIX, 0.0f);
        m_ty = this.getIntent().getFloatExtra(WithoutBGImageView.TY_MATRIX, 0.0f);

        m_back_sx = this.getIntent().getFloatExtra(WithoutBGImageView.SX_BACK_MATRIX, 0.0f);
        m_back_sy = this.getIntent().getFloatExtra(WithoutBGImageView.SY_BACK_MATRIX, 0.0f);
        m_back_tx = this.getIntent().getFloatExtra(WithoutBGImageView.TX_BACK_MATRIX, 0.0f);
        m_back_ty = this.getIntent().getFloatExtra(WithoutBGImageView.TY_BACK_MATRIX, 0.0f);

        ind_pos_x = this.getIntent().getIntExtra("ind_pos_x", 0);
        ind_pos_y = this.getIntent().getIntExtra("ind_pos_y", 123);
        ind_width = this.getIntent().getIntExtra("ind_width", 0);
        ind_height = this.getIntent().getIntExtra("ind_height", 0);


        m_touchable_image = new ThridLevelImageView (this, this);
        m_touchable_image.setLayoutParams( new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) );
        //m_touchable_image.SetImageResource(image_id_resource);
        //string filepath = book.Chapters[ind_chapter].Sections[ind_section].Pages[ind_image].Slides[0].Layers[ind_layer].LargePath;
        //input_image = Assets.Open(filepath);
        //Android.Graphics.BitmapFactory.Options options = new Android.Graphics.BitmapFactory.Options();
        //options.InSampleSize = 8;
        //m_input_image_bitmap = Android.Graphics.BitmapFactory.DecodeStream(input_image);
        m_input_image_bitmap = ((BitmapDrawable)WithoutBGImageView.CurrentImage().getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);

        //m_input_image_bitmap.Compress(Bitmap.CompressFormat.Png, 100, input_image);

        m_touchable_image.setImageBitmap(m_input_image_bitmap);
        m_touchable_image.setScaleType( ImageView.ScaleType.MATRIX);
        Matrix temp_matrix = new Matrix();
        sm_matrix_values = new float[9];
        sm_matrix_values[0] = m_sx;
        sm_matrix_values[4] = m_sy;
        sm_matrix_values[1] = m_rx;
        sm_matrix_values[3] = m_ry;
        sm_matrix_values[2] = m_tx;
        sm_matrix_values[5] = m_ty;
        temp_matrix.setValues(sm_matrix_values);
        m_touchable_image.setImageMatrix(temp_matrix);
        m_touchable_image.invalidate();

        m_touchable_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickImage();
            }
        });
			/*FullScreenAnimation scale = new FullScreenAnimation(ind_pos_x,ind_pos_y, ind_pos_x + ind_width, ind_pos_y + ind_height, img_big_sz_x_, img_big_sz_y_, m_rel_layout);
			scale.FillAfter = true;
			scale.Duration = 300;
			m_touchable_image.StartAnimation(scale); */

        m_rel_layout.addView(m_touchable_image);
        setContentView(m_rel_layout);
        ReduceImage();
    }

    private void ReduceImage(){
        int intr_width = m_touchable_image.getDrawable().getIntrinsicWidth();
        int intr_height = m_touchable_image.getDrawable().getIntrinsicHeight();

        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();

        float big_sx = sm_matrix_values[0];
        float big_width_image = big_sx * intr_width;
        float small_sx = (img_big_sz_x_ * big_sx) / big_width_image - sm_matrix_values[0];

        float big_sy = sm_matrix_values[4];
        float big_height_image = big_sy * intr_height;
        float small_sy = (img_big_sz_y_ * big_sy) / big_height_image - sm_matrix_values[4];

        float small_tx = 0 - sm_matrix_values[2];
        float small_ty = 0 - sm_matrix_values[5];
        AnimateFullScreen(small_sx, small_sy, 0, 0, small_tx, small_ty, false);
    }

    void OnClickImage (){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        synchronized (Lock)
        {
            if(one_exit >=1)
                return;

            one_exit++;

            int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();
            int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();


            this.CancelAnimation();

            float []target_values = new float[9];
            target_values[4] = ( ind_height) / (1.0f * img_big_sz_y_);
            target_values[0] = ( ind_width) / (1.0f * img_big_sz_x_);
            target_values[2] = ind_pos_x;
            target_values[5] = ind_pos_y;
            target_values[8] = 1.0f;

            Matrix target_matrix = new Matrix();
            target_matrix.setValues(target_values);

            m_touchable_image.getImageMatrix().getValues(sm_matrix_values);
            target_values[4] = m_back_sy;
            target_values[0] = m_back_sx;
            target_values[2] = m_back_tx;
            target_values[5] = m_back_ty;
            target_values[8] = 1.0f;


            m_touchable_image.SettingAnimation(1, 1, 0,0, 0, 0,false, SystemConfiguration.SCALE_ANIMATION_MILLISECONDS  );
				/*RectF from = new RectF(0, 0,  img_big_sz_x_, img_big_sz_y_);
				RectF to = new RectF (ind_pos_x, ind_pos_y, ind_pos_x + ind_width, ind_pos_y + ind_height);
				FromRectToRectAnimation scale = new FromRectToRectAnimation(from, to, m_rel_layout);*/
            //Log.Info("m", "tou matrix: " + m_touchable_image.TransformationMatrix);
            FromMatrixToMatrixAnimation scale = new FromMatrixToMatrixAnimation(m_touchable_image.TransformationMatrix(), target_matrix, m_touchable_image, target_values);
            scale.setFillAfter(true);
            scale.setDuration(SystemConfiguration.SCALE_ANIMATION_MILLISECONDS  );
            //m_touchable_image.Matrix.SetValues(sm_matrix_values);
            m_touchable_image.startAnimation(scale);
            WithoutBGImageView.ResetThirdToSecondtImage();
            Runnable wait_runnable = new Runnable() {
                @Override
                public void run() {

                    //Log.Info("mmmmmmm llegue", "end animation?????? ");

                    m_input_image_bitmap = null;
                    m_rel_layout = null;
                    m_touchable_image = null;
                    //input_image = null;
                    //System.GC.Collect();
                    WithoutBGImageView.image_to_set.setImageAlpha(255);
                    CropExpandingActivity.super.onBackPressed ();
                }
            };

            m_touchable_image.postDelayed(wait_runnable, scale.getDuration());

        }
    }


    private void  AnimateFullScreen(final float m_sx, final float m_sy, final float m_rx, final float m_ry, final float m_tx, final float m_ty, final boolean flag_reset){
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        long tick = System.currentTimeMillis();

        final long startTime = tick;
        final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS * 7 ;

        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];
        animation_canceled = false;
        m_runnable_animation = new Runnable(  ) {
            @Override
            public void run() {
                long tick = System.currentTimeMillis();
                float t = (float) ( (tick) - startTime) /  (2 * duration);

                t = t > 1.0f ? 1.0f : t;
                float interpolatedRatio = interpolator.getInterpolation(t);
                m_curr_matrix_values[0] = sm_matrix_values[0] + (interpolatedRatio * m_sx);
                m_curr_matrix_values[4] = sm_matrix_values[4] + (interpolatedRatio * m_sy);
                m_curr_matrix_values[1] = sm_matrix_values[1] + (interpolatedRatio * m_rx);
                m_curr_matrix_values[3] = sm_matrix_values[3] + (interpolatedRatio * m_ry);
                m_curr_matrix_values[2] = sm_matrix_values[2] + (interpolatedRatio * m_tx);
                m_curr_matrix_values[5] = sm_matrix_values[5] + (interpolatedRatio * m_ty);
                m_curr_matrix_values[8] = 1.0f;

                matrix_opt.setValues(m_curr_matrix_values);
                //clone_image.ImageMatrix.SetValues();
                if(animation_canceled == false) {
                    m_touchable_image.setImageMatrix(matrix_opt);
                    ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                    m_touchable_image.invalidate();
                }
                if (t < 1f && animation_canceled == false) {
                    m_touchable_image.post(m_runnable_animation);
                }else{
                    WithoutBGImageView.ResetThirdToSecondtImage();
                }
            }
        };
        m_touchable_image.postDelayed(m_runnable_animation, 0);
    }

    @Override
    public void finalizeNow() {

        onBackPressed();
    }

    @Override
    public void readyToFinalize() {
        WithoutBGImageView.ResetThirdToSecondtImage();

    }
}
