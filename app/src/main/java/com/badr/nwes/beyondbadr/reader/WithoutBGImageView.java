package com.badr.nwes.beyondbadr.reader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.NSFrame;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;
import com.badr.nwes.beyondbadr.index.PageIndex;
import com.badr.nwes.beyondbadr.reader.content.CropExpandingActivity;
import com.badr.nwes.beyondbadr.reader.content.StackBorderLoader;

import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by Kelvin on 2016-10-14.
 */
public class WithoutBGImageView extends ImageView {

    public static String SX_MATRIX = "sx_matrix";
    public static String SY_MATRIX = "sy_matrix";
    public static String RX_MATRIX = "rx_matrix";
    public static String RY_MATRIX = "ry_matrix";
    public static String TX_MATRIX = "tx_matrix";
    public static String TY_MATRIX = "ty_matrix";

    public static String SX_BACK_MATRIX = "sx_back_matrix";
    public static String SY_BACK_MATRIX = "sy_back_matrix";
    public static String TX_BACK_MATRIX = "tx_back_matrix";
    public static String TY_BACK_MATRIX = "ty_back_matrix";


    public static ImageView image_to_set;
    float sx_back;
    float sy_back;
    float tx_back;
    float ty_back;

    private StackBorderLoader m_stack_border_bitmap_gen;
    private static Intent sm_intent_full_screen;
    private PointF m_start_pos_finger1;
    private PointF m_start_pos_finger2;
    private PointF[] m_curr_finger;
    private float m_start_finger_slope;
    private float m_start_finger_separation;
    private Matrix m_matrix;
    private Matrix m_start_state;
    //private int m_screen_pos_x = 0;
    //private int m_screen_pos_y = 0;

    private static float[] sm_matrix_values;
    private static Object sm_lock = new Object();
    private static ImageView sm_animated_image;
    private static int sm_num_images = 0;
    private static int sm_touched_image_id = -1;
    private int m_image_id;
    private static RelativeLayout sm_root_layout;
    private Runnable m_runnable_animation;
    //private Runnable m_rotation_animation;
    //private InfScrollPaged m_scroll_paged;
    private float m_x_medio;
    private float m_y_medio;
    public boolean EnabledViewPager;
    public NSFrame m_cropframe_info;

    public static ImageView CurrentImage() {
        return sm_animated_image;

    }

    public WithoutBGImageView(Context context, NSFrame cropframe_info) {
        super(context);

        m_cropframe_info = cropframe_info;
        EnabledViewPager = true;
        m_start_pos_finger1 = new PointF();
        m_start_pos_finger2 = new PointF();
        sm_animated_image = null;
        sm_animated_image = new ImageView(context);
        if (sm_matrix_values == null) {

            sm_matrix_values = new float[9];
            sm_intent_full_screen = new Intent(context, CropExpandingActivity.class);
        }
        m_stack_border_bitmap_gen = new StackBorderLoader(context);
        sm_animated_image.setScaleType(ScaleType.MATRIX);
        m_matrix = new Matrix();
        m_image_id = sm_num_images++;
        sm_touched_image_id = -1;
        m_curr_finger = new PointF[2];
        //this.Click += new EventHandler(OnClick);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(v);
            }
        });
    }

    void OnClick(Object sender) {
        if (SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG != 0)
            return;

        //System.Threading.Interlocked.Increment(ref SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG);
        SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG++;

        //Intent intent_full_scroll = new Intent(context_, typeof( FullScreenDefaultView));
        CustomViewPager p_view_pager = (CustomViewPager) this.getParent().getParent();
        p_view_pager.setEnabledViewPager(false);

        PageIndex pageIndex = (PageIndex) this.getTag();
        sm_intent_full_screen.putExtra("ind_chapter", pageIndex.chapter);
        sm_intent_full_screen.putExtra("ind_section", pageIndex.section);
        sm_intent_full_screen.putExtra("ind_image", pageIndex.page);
        sm_intent_full_screen.putExtra("ind_layer", pageIndex.layer);

        sm_intent_full_screen.putExtra("ind_pos_x", pageIndex.pos_x);
        sm_intent_full_screen.putExtra("ind_pos_y", pageIndex.pos_y);
        sm_intent_full_screen.putExtra("ind_width", pageIndex.width);
        sm_intent_full_screen.putExtra("ind_height", pageIndex.height);

        sm_root_layout = null;
        sm_root_layout = (RelativeLayout) this.getParent();
        sm_animated_image.setImageAlpha(0);
        sm_root_layout.addView(sm_animated_image);
        this.setAlpha(0);
        CropToFullLoading(pageIndex.width, pageIndex.height, pageIndex.pos_x, pageIndex.pos_y);
    }


    private void CropToFullLoading(float layer_width, float layer_height, float layer_pos_x, float layer_pos_y){
        PageIndex page_index =  (PageIndex)this.getTag();
        String filepath = InfScrollPaged.book.Chapters.get(page_index.chapter).Sections.get(page_index.section).Pages.get(page_index.page).Slides.get(0).Layers.get(page_index.layer).LargePath;

        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(filepath, img_big_sz_x_ ,img_big_sz_y_, getContext());
        sm_animated_image.setImageBitmap(input_image_bitmap);

        //Log.Info("pepe", "matrixxxxxxxxxx-1: " + sm_animated_image.ImageMatrix);
        int intr_width = sm_animated_image.getDrawable().getIntrinsicWidth();
        int intr_height = sm_animated_image.getDrawable().getIntrinsicHeight();
        sm_animated_image.setImageMatrix(  this.getImageMatrix() );

        float []temp_values = new float[9];

        float proportional_screen_y = (1.0f * img_big_sz_y_ * layer_height) / m_cropframe_info.Height ;
        float proportional_screen_x = (1.0f * img_big_sz_x_ * layer_width) / m_cropframe_info.Width;
        float prop_sx = (1.0f *  proportional_screen_x) / intr_width ;
        float prop_sy = (1.0f *  proportional_screen_y) / intr_height;

        //Log.Info("pepe", "m_crop_height: " + prop_sx + " " +prop_sy + " intrh * sy =" + intr_height * temp_values[4]);
        //Log.Info("pepe", "m_crop_width: " + m_cropframe_info.Width + " " + layer_width);
        sm_animated_image.getImageMatrix().getValues(temp_values);
        temp_values[0] = prop_sx;
        temp_values[4] = prop_sy;
        temp_values[2] = - m_cropframe_info.X * ( ( 1.0f * layer_width) / m_cropframe_info.Width);
        //temp_values[2] = pageIndex.pos_x - m_cropframe_info.X * ( ( 1.0f * pageIndex.width) / m_cropframe_info.Width) ;
        temp_values[5] = - m_cropframe_info.Y * ( ( 1.0f * layer_height) / m_cropframe_info.Height) ;
        //temp_values[5] = pageIndex.pos_y - m_cropframe_info.Y * ( ( 1.0f * pageIndex.height) / m_cropframe_info.Height) ;


        Matrix temp_matrix = new Matrix();
        temp_matrix.setValues(temp_values);
        sm_animated_image.setImageMatrix(temp_matrix);

        //Log.Info("pepe", "matrixxxxxxxxxx: " + sm_animated_image.ImageMatrix);

        //sm_animated_image.LayoutParameters = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FillParent, RelativeLayout.LayoutParams.FillParent);
        sm_animated_image.setLayoutParams( new RelativeLayout.LayoutParams( (int)layer_width,(int) layer_height) );
        sm_animated_image.setImageAlpha( 255 );
        sm_animated_image.setX(layer_pos_x);
        sm_animated_image.setY(layer_pos_y);

        float hor_left_prop = 1.0f - (1.0f * m_cropframe_info.Width) / img_big_sz_x_;
        float ver_left_prop = 1.0f - (1.0f * m_cropframe_info.Height) / img_big_sz_y_;

        float desired_sx, desired_sy, layout_tx =0, layout_ty = 0;

        if(hor_left_prop < ver_left_prop){
            desired_sy = ((1.0f * img_big_sz_x_ * img_big_sz_x_) / m_cropframe_info.Width) / intr_width - temp_values[4];
            desired_sx = ((1.0f * img_big_sz_x_ * img_big_sz_x_) / m_cropframe_info.Width) / intr_width - temp_values[0];
            //layout_tx = (desired_sx * intr_height * temp_values[2]) / pageIndex.width-  temp_values[2];
            float delta_bx = (m_cropframe_info.X * img_big_sz_x_ ) / m_cropframe_info.Width;
            float hbc = (1.0f * img_big_sz_x_ * m_cropframe_info.Height) / m_cropframe_info.Width;
            float delta_by = ( delta_bx * m_cropframe_info.Y ) / m_cropframe_info.X ;
            layout_tx = - delta_bx -  temp_values[2];

            layout_ty = 0- temp_values[5];
            if(delta_by + hbc > img_big_sz_y_){
                //layout_ty = img_big_sz_y_ - (delta_by + hbc)- temp_values[5];
            }else	layout_ty = 0- temp_values[5];
            //Log.Info("mm", "WithoutBGIMageView: lx = " + layout_tx + " ly = " + layout_ty + " dby = " + delta_by + " hbc = " + hbc );

            //Log.Info("mm", "Horizontal");
        }else{
            desired_sx = ((1.0f * img_big_sz_y_ * img_big_sz_y_) / m_cropframe_info.Height) / intr_height - temp_values[0];
            desired_sy = ( (1.0f * img_big_sz_y_ * img_big_sz_y_) / m_cropframe_info.Height) / intr_height- temp_values[4];
            float wbc = (1.0f * img_big_sz_y_ * m_cropframe_info.Width) / m_cropframe_info.Height;
            float delta_by = (m_cropframe_info.Y * img_big_sz_y_ ) / m_cropframe_info.Height;
            float delta_bx = ( delta_by * m_cropframe_info.X ) / m_cropframe_info.Y ;

            if(delta_bx + wbc > img_big_sz_x_){
                layout_tx = img_big_sz_x_ - (delta_bx + wbc) - temp_values[2];
            }else	layout_tx = 0 -  temp_values[2];
            //layout_ty = (desired_sy * intr_height * temp_values[5]) / pageIndex.height -  temp_values[5];
            layout_ty = - delta_by -  temp_values[5];
            //Log.Info("mm", "Vertical");
            //float proportional_delta_x1 = (1.0f * img_big_sz_y_ * pageIndex.height) / m_cropframe_info.Height ;
            //layout_tx = ( pageIndex.width * ( (1.0f * img_big_sz_y_) / m_cropframe_info.Height) )
        }
        sm_matrix_values = temp_values;

                /*IRunnable destroy_runnable = new Runnable( () => {
                    sm_animated_image.Alpha = 0;
                    sm_root_layout.RemoveView(sm_animated_image);
                });
                this.PostDelayed(destroy_runnable, 1000);*/

        AnimateFullScreen(desired_sx,desired_sy,0,0,layout_tx,layout_ty, false);
        invalidate();
    }

    private void RotationCorrectionAnimation(){
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        long tick = System.currentTimeMillis();

        final long startTime = tick;

        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];
        sm_animated_image.getImageMatrix().getValues(sm_matrix_values);
        final float scale_sx = (float) Math.sqrt( 1.0f * sm_matrix_values[0] * sm_matrix_values[0] + sm_matrix_values[1] * sm_matrix_values[1] ) - sm_matrix_values[0];
        final float scale_sy = (float) Math.sqrt( 1.0f * sm_matrix_values[3] * sm_matrix_values[3] + sm_matrix_values[4] * sm_matrix_values[4] ) - sm_matrix_values[4];
        final float rot_rx = 0 - sm_matrix_values[1];
        final float rot_ry = 0 - sm_matrix_values[3];
        //Log.Info("", "" + rot_rx);
        final long duration = (long)((SystemConfiguration.SCALE_ANIMATION_MILLISECONDS * (Math.max(rot_rx, rot_ry) % 4) ) / 2.0f);
        m_runnable_animation = null;
        m_runnable_animation = new Runnable() {
            @Override
            public void run() {
                long tick = System.currentTimeMillis();
                float t = (float) ( (tick) - startTime) /  (2 * duration);
                t = t > 1.0f ? 1.0f : t;

                float interpolatedRatio = interpolator.getInterpolation(t);

                m_curr_matrix_values[0] = sm_matrix_values[0] + (interpolatedRatio * scale_sx);
                m_curr_matrix_values[4] = sm_matrix_values[4] + (interpolatedRatio * scale_sy);

                m_curr_matrix_values[1] = sm_matrix_values[1] + (interpolatedRatio * rot_rx);
                m_curr_matrix_values[3] = sm_matrix_values[3] + (interpolatedRatio * rot_ry);
                m_curr_matrix_values[2] = sm_matrix_values[2];
                m_curr_matrix_values[5] = sm_matrix_values[5];
                m_curr_matrix_values[8] = 1.0f;

                matrix_opt.setValues(m_curr_matrix_values);
                //clone_image.ImageMatrix.SetValues();
                sm_animated_image.setImageMatrix(matrix_opt);
                ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                invalidate();
                if (t < 1f) {
                    sm_animated_image.post(m_runnable_animation);
                } else {
                    //Log.Info("mmmm", "Animation: Matrix = " + sm_animated_image.ImageMatrix);
                    //if(flag_reset == false){
                    //	StartSecondLevelActivity();
                    //}else{

                    //FirstLevelActivity.scroll_img.SetImageAlpha(this.CurrentItem, 255);
                    sm_animated_image.getImageMatrix().getValues(sm_matrix_values);
                    int intr_width = sm_animated_image.getDrawable().getIntrinsicWidth();
                    int intr_height = sm_animated_image.getDrawable().getIntrinsicHeight();
                    CropToFullLoading(intr_width * m_curr_matrix_values[0] , intr_height * m_curr_matrix_values[4], m_curr_matrix_values[2], m_curr_matrix_values[5]);
                    //ResetSecondToFirstImage();
                    //}

                }
            }
        };
        this.postDelayed(m_runnable_animation, 0);
    }

    void StartThirdLevelActivity ()
    {
        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();

        sm_animated_image.getImageMatrix().getValues(sm_matrix_values);
        sm_intent_full_screen.putExtra(WithoutBGImageView.SX_MATRIX, sm_matrix_values[0]);
        sm_intent_full_screen.putExtra(WithoutBGImageView.SY_MATRIX, sm_matrix_values[4]);
        sm_intent_full_screen.putExtra(WithoutBGImageView.RX_MATRIX, sm_matrix_values[1]);
        sm_intent_full_screen.putExtra(WithoutBGImageView.RY_MATRIX, sm_matrix_values[3]);
        sm_intent_full_screen.putExtra(WithoutBGImageView.TX_MATRIX, sm_matrix_values[2]);
        sm_intent_full_screen.putExtra(WithoutBGImageView.TY_MATRIX, sm_matrix_values[5]);

        PageIndex page_index = (PageIndex)this.getTag();
        sm_intent_full_screen.putExtra("ind_pos_x", page_index.pos_x);
        sm_intent_full_screen.putExtra("ind_pos_y", page_index.pos_y);
        sm_intent_full_screen.putExtra("ind_width", page_index.width);
        sm_intent_full_screen.putExtra("ind_height", page_index.height);

        int intr_width = sm_animated_image.getDrawable().getIntrinsicWidth();
        int intr_height = sm_animated_image.getDrawable().getIntrinsicHeight();

        float prop_screen_y2 = (1.0f * img_big_sz_y_ * page_index.height) / m_cropframe_info.Height ;
        float prop_screen_x2 = (1.0f * img_big_sz_x_ * page_index.width) / m_cropframe_info.Width;
        float pro_sx2 = (1.0f *  prop_screen_x2) / intr_width ;
        float pro_sy2 = (1.0f *  prop_screen_y2) / intr_height;
        sx_back = pro_sx2;
        sy_back = pro_sy2;
        tx_back = page_index.pos_x - m_cropframe_info.X * ( ( 1.0f * page_index.width) / m_cropframe_info.Width);
        ty_back = page_index.pos_y - m_cropframe_info.Y * ( ( 1.0f * page_index.height) / m_cropframe_info.Height) ;

        sm_intent_full_screen.putExtra(WithoutBGImageView.SX_BACK_MATRIX, sx_back);
        sm_intent_full_screen.putExtra(WithoutBGImageView.SY_BACK_MATRIX, sy_back);
        sm_intent_full_screen.putExtra(WithoutBGImageView.TX_BACK_MATRIX, tx_back);
        sm_intent_full_screen.putExtra(WithoutBGImageView.TY_BACK_MATRIX, ty_back);

        ( (Activity)this.getContext() ).startActivity(sm_intent_full_screen);
        final CustomViewPager p_view_pager = (CustomViewPager)this.getParent().getParent();
        Runnable run_enable_view_pager = new Runnable() {
            @Override
            public void run() {
                p_view_pager.setEnabledViewPager( true );
            }
        };

        this.postDelayed(run_enable_view_pager,SystemConfiguration.SCALE_ANIMATION_MILLISECONDS);
    }

    private void AnimateBackToScreen(float [] target_values, boolean flag_reset){
        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();

        final float m_sx = target_values[0] - sm_matrix_values[0];
        final float m_sy = target_values[4] - sm_matrix_values[4];
        final float m_rx = target_values[1] - sm_matrix_values[1];
        final float m_ry = target_values[3] - sm_matrix_values[3];
        final float m_tx = target_values[2] - sm_matrix_values[2];
        final float m_ty = target_values[5] - sm_matrix_values[5];
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        long tick = System.currentTimeMillis();

        final long startTime = tick;
        final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS ;

        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];
        m_runnable_animation = null;
        m_runnable_animation = new Runnable() {

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
                sm_animated_image.setImageMatrix( matrix_opt );
                ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                invalidate();
                if (t < 1f) {
                    sm_animated_image.post(m_runnable_animation);
                }else{
                    CustomViewPager p_view_pager = (CustomViewPager)getParent().getParent();
                    p_view_pager.setEnabledViewPager(true);
                    //System.Threading.Interlocked.Decrement(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
                    ResetThirdToSecondtImage();
                    setImageAlpha( 255 );
                    EnabledViewPager = true;
                }
            }
        };

        this.postDelayed(m_runnable_animation, 0);
    }


    private void  AnimateFullScreen(final float m_sx, final float m_sy, final float m_rx, final float m_ry, final float m_tx, final float m_ty, final boolean flag_reset){
        final int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        final int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();

        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        long tick = System.currentTimeMillis();

        final long startTime = tick;
        final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS ;

        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];
        PageIndex pageIndex =  (PageIndex)this.getTag();
        int intr_width = sm_animated_image.getDrawable().getIntrinsicWidth();
        int intr_height = sm_animated_image.getDrawable().getIntrinsicHeight();
        final float xx = sm_animated_image.getX();
        final float yy = sm_animated_image.getY();
        final int root_lay_init_width = ((RelativeLayout.LayoutParams)sm_animated_image.getLayoutParams()).width;
        final int root_lay_init_height = ((RelativeLayout.LayoutParams)sm_animated_image.getLayoutParams()).height;
        m_runnable_animation = null;
        m_runnable_animation = new Runnable() {
            @Override
            public void run() {
                long tick = System.currentTimeMillis();
                float t = (float) ( (tick) - startTime) /  (2 * duration);
                t = t > 1.0f ? 1.0f : t;
                float interpolatedRatio = interpolator.getInterpolation(t);
                RelativeLayout.LayoutParams temp_params = (RelativeLayout.LayoutParams)sm_animated_image.getLayoutParams();
                //temp_params.Width = (int) m_sx * intr_width * interpolatedRatio;
                //temp_params.Height = (int) m_sy * intr_height * interpolatedRatio;
                temp_params.width = (int) Math.ceil( root_lay_init_width + (img_big_sz_x_ - root_lay_init_width) * interpolatedRatio);
                temp_params.height = (int) (root_lay_init_height + ( img_big_sz_y_ - root_lay_init_height) * interpolatedRatio);
                //Log.Info("", "WithoutBGImageView: Layout params = (" + temp_params.Height + ", " + temp_params.Width + ")");
                sm_animated_image.setLayoutParams(temp_params);
                float new_x = xx * (1.0f - interpolatedRatio);
                float new_y = yy * (1.0f - interpolatedRatio);

				/*if(new_x < 0){
					float final_x = (new_x + temp_params.Width);
					new_x = final_x - System.Math.Min(temp_params.Width, img_big_sz_x_);
				}
				if(new_y < 0){
					float final_y = (new_y + temp_params.Height);
					new_y = final_y - System.Math.Min(temp_params.Height, img_big_sz_y_);
				}*/

                sm_animated_image.setX (new_x);
                sm_animated_image.setY (new_y);


                m_curr_matrix_values[0] = sm_matrix_values[0] + (interpolatedRatio * m_sx);
                m_curr_matrix_values[4] = sm_matrix_values[4] + (interpolatedRatio * m_sy);

                m_curr_matrix_values[1] = sm_matrix_values[1] + (interpolatedRatio * m_rx);
                m_curr_matrix_values[3] = sm_matrix_values[3] + (interpolatedRatio * m_ry);
                m_curr_matrix_values[2] = sm_matrix_values[2] + (interpolatedRatio * m_tx);
                m_curr_matrix_values[5] = sm_matrix_values[5] + (interpolatedRatio * m_ty);
                m_curr_matrix_values[8] = 1.0f;

                matrix_opt.setValues(m_curr_matrix_values);
                //clone_image.ImageMatrix.SetValues();
                sm_animated_image.setImageMatrix(matrix_opt);
                ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                invalidate();
                if (t < 1f) {
                    sm_animated_image.post(m_runnable_animation);
                }else{
                    //Log.Info("mmmm", "Animation: Matrix = " + sm_animated_image.ImageMatrix);
                    if(flag_reset == false){
                        StartThirdLevelActivity();
                    }else{
                        //CustomViewPager p_view_pager = (CustomViewPager)this.Parent.Parent;
                        //p_view_pager.EnabledViewPager = true;
                    }
                    //System.Threading.Interlocked.Decrement(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
                    //if(flag_reset == true){
                    EnabledViewPager = true;
                    image_to_set = null;
                    image_to_set = (ImageView)WithoutBGImageView.this;
                    //this.Alpha = 255;
                    //}


                    //ResetSecondToFirstImage();
                    //FirstLevelActivity.scroll_img.SetImageAlpha(this.CurrentItem, 255);
                    //ResetSecondToFirstImage();
                    //}

                }
            }
        };
        this.postDelayed(m_runnable_animation, 0);
    }


    void InitializeAnimatedImage ()
    {
        //Log.Info("WBGIV", "WithoutBGImageView: " + "entreeeee");
        getParent().requestDisallowInterceptTouchEvent(true);
        getParent().getParent().requestDisallowInterceptTouchEvent(true);
        CustomViewPager p_view_pager = (CustomViewPager)this.getParent().getParent();
        p_view_pager.setEnabledViewPager(false);
        sm_touched_image_id = m_image_id;
        sm_animated_image.setImageAlpha(0);
        PageIndex pageIndex =   (PageIndex)this.getTag();
        sm_root_layout = null;
        sm_root_layout  = (RelativeLayout)this.getParent();
        //m_scroll_paged = (InfScrollPaged) this.Parent.Parent.Parent.Parent;
        //clone_image.LayoutParameters = new RelativeLayout.LayoutParams( pageIndex.width, pageIndex.height);
        sm_animated_image.setLayoutParams(new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        //clone_image.setwi - pageIndex.width;
        Bitmap bmSrc1 = ((BitmapDrawable)this.getDrawable()).getBitmap();
        Bitmap bmSrc2 = bmSrc1.copy(bmSrc1.getConfig(), true);
        sm_animated_image.setImageBitmap(bmSrc2);

        //sm_animated_image.Matrix = this.Matrix;
        sm_animated_image.setImageMatrix(this.getImageMatrix());
        float []values_matrix_temp = new float[9];
        this.getImageMatrix().getValues(values_matrix_temp);
        //Log.Info("pepe", "COPY IMAGE MATRIX: " + sm_animated_image.ImageMatrix);
        //Log.Info("pepe", "ORIGINAL IMAGE MATRIX: " + this.ImageMatrix);
        //Log.Info("pepe", "image_matrix1: " + this.ImageMatrix);
        //Log.Info("pepe", "image_matrix2: " + sm_animated_image.ImageMatrix);

        //m_screen_pos_y = ( 2 * ((Activity) this.Context).WindowManager.DefaultDisplay.Height) / SystemConfiguration.SCREEN_HEIGHT_DIVIDER - SystemConfiguration.SCREEN_HEIGHT_OFFSET;

        //m_screen_pos_x = pageIndex.pos_x - m_scroll_paged.ScrollX;
        sm_animated_image.setX(0);
        sm_animated_image.setY(0);
        PageIndex page_index=  (PageIndex)this.getTag();
        //sm_animated_image.ImageMatrix.PostTranslate(page_index.pos_x, page_index.pos_y);
        sm_root_layout.addView(sm_animated_image);
        m_matrix.reset() ;
        m_start_state = new Matrix(this.getImageMatrix());
        m_start_state.postTranslate(page_index.pos_x, page_index.pos_y);

        sm_animated_image.setImageMatrix(m_start_state);
        //Log.Info("pepe", "COPY STARTED STATE: " + m_start_state);
        //Log.Info("pepe", "COPY 2 IMAGE MATRIX: " + sm_animated_image.ImageMatrix);
        invalidate();
        //Log.Info("ima", "si que si");
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        CustomViewPager p_view_pager = (CustomViewPager)this.getParent().getParent();
        if(p_view_pager.getEnabledViewPager() == false){
            p_view_pager.requestDisallowInterceptTouchEvent(true);
        }

        if(EnabledViewPager == false)
            return false;
        //switch (action & MotionEventActions.Mask){
        //Log.Info("WBGIV", "WithoutBGImageView: pointer_count = "+ ev.PointerCount );
        synchronized (sm_lock) {
             if(sm_touched_image_id != m_image_id && sm_touched_image_id != -1){
                return true;
            }
            if( action  == MotionEvent.ACTION_POINTER_DOWN && ev.getPointerCount() == 2 && sm_touched_image_id == -1){
                if(SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG != 0) return true;
                InitializeAnimatedImage();
                m_start_pos_finger1.set ( ev.getX(0) , ev.getY(0));
                m_start_pos_finger2.set(ev.getX(1), ev.getY(1));
                m_curr_finger[0] = new PointF( ev.getX(0) , ev.getY(0));
                m_curr_finger[1] = new PointF(ev.getX(1), ev.getY(1));
                m_matrix.reset() ;
                //m_start_state = new Matrix(clone_image.ImageMatrix);
                //m_start_state.PostScale(1.0f / scale_factor, 1.0f / scale_factor);
                //m_start_state.PostTranslate(0, values_matrix_temp[5] * (1.0f - 1.0f/ scale_factor));
                ////Log.Info("pepe", "matrix1: " + m_start_state + " scale: "+ 1.0f/ scale_factor);
                //m_start_state.PostTranslate(screen_pos_x, screen_pos_y);
                PageIndex page_index =  (PageIndex)this.getTag() ;
                m_x_medio = ( 2 * page_index.pos_x + m_start_pos_finger1.x + m_start_pos_finger2.x) / 2.0f;
                m_start_finger_slope = (1.0f *( m_start_pos_finger1.y - m_start_pos_finger2.y)) / ( m_start_pos_finger1.x - m_start_pos_finger2.x) ;
                m_start_finger_separation = ( m_start_pos_finger1.x - m_start_pos_finger2.x) * ( m_start_pos_finger1.x - m_start_pos_finger2.x) + ( m_start_pos_finger1.y - m_start_pos_finger2.y) *( m_start_pos_finger1.y - m_start_pos_finger2.y);
                return  true;
            }
        }

        //Log.Info("mm", "WithoutBGImageView: sm_touched_image_id = " + sm_touched_image_id + "  m_image_id = " + m_image_id);
        if(sm_touched_image_id == m_image_id) {
            if(action == MotionEvent.ACTION_UP ){
                EnabledViewPager = false;
                sm_animated_image.getImageMatrix().getValues(sm_matrix_values);

                float []start_state_values = new float[9];
                m_start_state.getValues(start_state_values);

                //float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
                boolean flag_reset = false;

                float current_scale = (float)Math.sqrt(sm_matrix_values[0] * sm_matrix_values[0]  + sm_matrix_values[1] * sm_matrix_values[1]);
                float desired_scale_to_go = SystemConfiguration.SCALE_PROP_SC_TO_TH_LEVEL *  start_state_values[0];
                if(current_scale < desired_scale_to_go){
                    AnimateBackToScreen(start_state_values, flag_reset);
                    flag_reset = true;
                }else{
                    RotationCorrectionAnimation();
                }

                if(SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG != 0) {
                    EnabledViewPager = true;
                    return true;
                }

                if(flag_reset == false){

                    java.util.concurrent.locks.Lock   lock = new ReentrantLock();

                    lock.lock();
                        SystemConfiguration.SECOND_TO_THIRD_LEVEL_ACTIVITY_FLAG++;
                    lock.unlock();
                }
                return true;
            }else if( ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE){
                this.setAlpha(0);

                sm_animated_image.setImageAlpha(255);
                int index = ev.getAction();

                m_curr_finger[0].set((float)ev.getX(0), (float)ev.getY(0));

                if(ev.getPointerCount() == 2)
                    m_curr_finger[1].set((float)ev.getX(1),(float) ev.getY(1));
                else {
                    return true;
                }
                //Log.Info("index", "index: " + index + " "+ m_curr_finger[0] + " " + m_curr_finger[1]);
                float dx = (m_curr_finger[0].x - m_start_pos_finger1.x + m_curr_finger[1].x - m_start_pos_finger2.x) / 2.0f;
                float dy = (m_curr_finger[0].y - m_start_pos_finger1.y + m_curr_finger[1].y - m_start_pos_finger2.y) / 2.0f;

                float m2 = (1.0f *(m_curr_finger[0].y - m_curr_finger[1].y)) / (m_curr_finger[0].x - m_curr_finger[1].x);

                float angle = - (float)Math.atan( (m_start_finger_slope - m2) / (1 + m_start_finger_slope * m2)) * 90f ;


                float sf = (m_curr_finger[0].x - m_curr_finger[1].x) * (m_curr_finger[0].x - m_curr_finger[1].x) + (m_curr_finger[0].y - m_curr_finger[1].y) * (m_curr_finger[0].y - m_curr_finger[1].y);

                float scale_factor = (float)Math.sqrt( sf / m_start_finger_separation);


                m_matrix.set ( m_start_state);
                m_matrix.postScale(Math.abs(scale_factor), Math.abs (scale_factor), m_x_medio, m_y_medio);

                m_matrix.postRotate(angle, m_x_medio, m_y_medio);
                m_matrix.postTranslate(dx, dy);

                sm_animated_image.setImageMatrix(m_matrix);
                ////Log.Info("d", "moving " + angle + " " + m_x_medio + " " + m_y_medio + " "  + dx + " " + dy + " " + scale_factor + " " + m_matrix);

                invalidate();

            }

        }
        return super.onTouchEvent(ev);
    }

    public static void ResetThirdToSecondtImage(){
        sm_touched_image_id = -1;
        //sm_animated_image.Drawable = null;
        //System.GC.Collect();
        if(sm_root_layout != null)
            sm_root_layout.removeView(sm_animated_image);
    }

}
