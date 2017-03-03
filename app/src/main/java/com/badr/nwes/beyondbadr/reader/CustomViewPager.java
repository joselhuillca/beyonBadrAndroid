package com.badr.nwes.beyondbadr.reader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.FirstLevelActivity;
import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.ProportionCalculator;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.reader.content.CustomImageView;
import com.badr.nwes.beyondbadr.reader.content.DataSourceMapper;
import com.badr.nwes.beyondbadr.reader.content.StackBorderLoader;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;
import com.badr.nwes.beyondbadr.index.PageIndex;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class CustomViewPager extends ViewPager {

    private IFinalizeActivity m_finalizer;

    private static float [] sm_matrix_values;
    private static Object sm_lock = new Object();
    private static ImageView sm_animated_image;
    private static int sm_num_images = 0;
    private static int sm_touched_image_id = -1;
    private static RelativeLayout sm_root_layout;

    private boolean EnabledViewPager;
    public final boolean getEnabledViewPager()
    {
        return EnabledViewPager;
    }
    public final void setEnabledViewPager(boolean value)
    {
        EnabledViewPager = value;
    }
    private StackBorderLoader m_stack_border_bitmap_gen;
    private PointF m_start_pos_finger1 = new PointF();
    private PointF m_start_pos_finger2 = new PointF();
    private PointF [] m_curr_finger;
    private float m_start_finger_slope;
    private float m_start_finger_separation;
    private Matrix m_matrix;
    private Matrix m_start_state;
    //private int m_screen_pos_x = 0;
    //private int m_screen_pos_y = 0;
    private int m_image_id;
    public final boolean getIsTouchedInAnimation()
    {
        return m_is_touched;
    }
    private boolean m_is_touched = false;
    private Runnable m_runnable_animation;
    private float m_x_medio;
    private float m_y_medio;
    private boolean is_scrolling = false;

    public CustomViewPager(Context context, IFinalizeActivity finalizer)
    {
        super(context);

        setEnabledViewPager(true);
        m_finalizer = finalizer;
        m_start_pos_finger1 = new PointF();
        m_start_pos_finger2 = new PointF();
        m_is_touched = false;

        sm_animated_image = new ImageView(context);
        //Log.Info("meme", ""+ sm_animated_image.ImageMatrix);
        sm_matrix_values = new float[9];
        //intent_full_scroll = new Intent(this.Context,typeof(FullScreenAnimatedActivity) );

        //sm_animated_image.SetScaleType(ScaleType.Matrix);
        m_stack_border_bitmap_gen = new StackBorderLoader(context);
        m_matrix = new Matrix();
        m_image_id = sm_num_images++;
        m_curr_finger = new PointF[2];
        sm_animated_image.setScaleType(ImageView.ScaleType.MATRIX);
        sm_animated_image.setImageMatrix( m_matrix );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(EnabledViewPager == false)
            return false;
         int action = ev.getAction();

        if(is_scrolling == false && action == MotionEvent.ACTION_MOVE && ev.getPointerCount() == 2){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(EnabledViewPager == false){
            m_is_touched = true;
            return false;
        }

        if(action == MotionEvent.ACTION_UP && m_is_touched == true){

            m_is_touched = false;
            //Log.Info("", "CustomViewPager: UP!!!!!!" );
            return true;
        }

        if(EnabledViewPager == true && m_is_touched == true){
            return true;
        }
        CustomImageView.ResetFirstToSecondImage();
        //Log.Info("", " CustomViewPager: fui activado y estoy siendo usado");

        //Log.Info("mm", "CustomViewPager1: " + ev.PointerCount + "   sm_touched_image_id = " + sm_touched_image_id + "    Action = " + MotionEventActions.Move);
        if(is_scrolling == false){
            //Log.Info("mm", "CustomViewPager2: " + ev.PointerCount + "   sm_touched_image_id = " + sm_touched_image_id + "    Action = " + MotionEventActions.Move);
            if(ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE && sm_touched_image_id == -1){
                //Log.Info("mm", "CustomViewPager3: " + ev.PointerCount + "   sm_touched_image_id = " + sm_touched_image_id + "    Action = " + MotionEventActions.Move);
                m_finalizer.readyToFinalize();
                //Log.Info("mm", "CustomViewPager4: " + ev.PointerCount + "   sm_touched_image_id = " + sm_touched_image_id + "    Action = " + MotionEventActions.Move+ "  M_FIN = " + m_finalizer.ToString());
                InitializeAnimatedImage();
                m_start_pos_finger1.set ( ev.getX(0) , ev.getY(0));
                m_start_pos_finger2.set(ev.getX(1), ev.getY(1));
                m_curr_finger[0] = new PointF( ev.getX(0) , ev.getY(0));
                m_curr_finger[1] = new PointF(ev.getX(1), ev.getY(1));

                m_y_medio = (   m_start_pos_finger1.y + m_start_pos_finger2.y) / 2.0f;
                m_x_medio = (  m_start_pos_finger1.x + m_start_pos_finger2.x) / 2.0f;
                m_start_finger_slope = (1.0f *( m_start_pos_finger1.y - m_start_pos_finger2.y)) / ( m_start_pos_finger1.x - m_start_pos_finger2.x) ;
                m_start_finger_separation = ( m_start_pos_finger1.x - m_start_pos_finger2.x) * ( m_start_pos_finger1.x - m_start_pos_finger2.x) + ( m_start_pos_finger1.y - m_start_pos_finger2.y) *( m_start_pos_finger1.y - m_start_pos_finger2.y) ;
                return  true;
            }
            if(sm_touched_image_id == m_image_id) {
                if(action == MotionEvent.ACTION_UP ){
                    sm_animated_image.getImageMatrix().getValues(sm_matrix_values);

                    float []start_state_values = new float[9];
                    m_start_state.getValues(start_state_values);

                    //float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
                    boolean flag_reset = false;

                    flag_reset = true;

                    float current_scale = (float)Math.sqrt(sm_matrix_values[0] * sm_matrix_values[0]  + sm_matrix_values[1] * sm_matrix_values[1]);
                    float desired_scale_to_go = SystemConfiguration.SCALE_PROP_SC_TO_FS_LEVEL *  start_state_values[0];
                    if(current_scale > desired_scale_to_go){
                        PerformAnimation(start_state_values, flag_reset);
                    }else{
                        m_finalizer.finalizeNow();
                    }
                    //AnimateFullScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset);
                    return true;
						/*sm_touched_image_id = -1;
						sm_root_layout.RemoveView(sm_animated_image);*/

                    //this.Alpha = 255;
                }else if( ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE){
                    this.setAlpha( 0 );
                    sm_animated_image.setImageAlpha( 255 );
                    int index = ev.getActionIndex();

                    m_curr_finger[0].set((float)ev.getX(0), (float)ev.getY(0));

                    if(ev.getPointerCount() == 2) m_curr_finger[1].set((float)ev.getX(1),(float) ev.getY(1));
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

                    sm_animated_image.setImageMatrix( m_matrix );
                    invalidate();

                }

                return true;
            }
        }

        if(action == MotionEvent.ACTION_MOVE){
            is_scrolling = true;
        }
        if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
            is_scrolling = false;
        }
        return super.onTouchEvent(ev);
    }

    public void InitializeAnimatedImage(){
        if(sm_touched_image_id != -1)
            return;
        //Log.Info("ima", "lo desabilite");
        //Parent.RequestDisallowInterceptTouchEvent(true);
        sm_touched_image_id = m_image_id;
        sm_animated_image.setImageAlpha(255);

        PageIndex pageIndex =  (PageIndex)this.getTag() ;
        sm_root_layout = null;
        sm_root_layout  = (RelativeLayout)this.getParent();

        sm_animated_image.setLayoutParams( new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) );

        int position = this.getCurrentItem();
        String filepath = InfScrollPaged.book.Chapters.get(DataSourceMapper.DS_LISTA_CHAPTER[position]).Sections.get(DataSourceMapper.DS_LISTA_SECTION[position]).Pages.get(DataSourceMapper.DS_LISTA_IMAGE[position]).MediumSource;
        float [] boundaries = new float[4];
        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();

        int req_width = (int) (img_big_sz_x_ / SystemConfiguration.NUMBER_OF_THUMBNAILS_IN_SCREEN);

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(filepath, img_big_sz_x_ ,img_big_sz_y_, getContext());

        Bitmap copy_border = m_stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
        int scale_factor = Math.min(img_big_sz_x_ /( 2 * copy_border.getWidth() ), img_big_sz_y_ /(2 * copy_border.getHeight()));
        Bitmap copy_border2  = Bitmap.createScaledBitmap(copy_border, copy_border.getWidth() * scale_factor, copy_border.getHeight() * scale_factor, false);
        Canvas comboImage = new Canvas(copy_border2);

        ProportionCalculator.CalculeImageBoundaries(copy_border2.getWidth(), copy_border2.getHeight(), boundaries);

        Rect tmpRect = new Rect (0, 0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight());
        RectF tmpRect2 =  new RectF (boundaries [0], boundaries [1], comboImage.getWidth() - boundaries [2], comboImage.getHeight() - boundaries [3]);

        comboImage.drawBitmap(input_image_bitmap, tmpRect, tmpRect2, null);

        sm_animated_image.setImageBitmap(copy_border2);

        Drawable drawable = sm_animated_image.getDrawable();
        ProportionCalculator.CalculeBackgroundBoundaries(img_big_sz_x_, img_big_sz_y_, boundaries);

        float image_width_needed = boundaries[2] - boundaries[0];
        float image_height_needed = boundaries[3] - boundaries[1];

        float sx_needed = image_width_needed / drawable.getIntrinsicWidth();
        float sy_needed = image_height_needed / drawable.getIntrinsicHeight();
        //float sx_needed = image_width_needed / img_big_sz_x_;
        //float sy_needed = image_height_needed / img_big_sz_y_;
        float tx_needed = boundaries[0];
        float ty_needed = boundaries[1];
        //Log.Info("mm" , "customViewPager: " + " " + img_big_sz_x_ + " " + img_big_sz_y_+ " " + image_width_needed + " " + image_height_needed + " " + sx_needed + " " + sy_needed);

        Matrix temp_matrix = new Matrix();
        float []t_matrix_values = new float[9];
        t_matrix_values[0] = sx_needed;
        t_matrix_values[1] = 0;
        t_matrix_values[2] = tx_needed;
        t_matrix_values[3] = 0;
        t_matrix_values[4] = sy_needed;
        t_matrix_values[5] = ty_needed;
        t_matrix_values[6] = t_matrix_values[7] = 0;
        t_matrix_values[8] = 1;

        temp_matrix.setValues(t_matrix_values);
        sm_animated_image.setImageMatrix( temp_matrix );

        sm_root_layout.addView(sm_animated_image);

        m_matrix.reset() ;
        m_start_state = new Matrix(temp_matrix);

        this.setAlpha(0);

        invalidate();
     }

    public void PerformAnimation(float []target_matrix_values, boolean flag_reset){
        sm_animated_image.getImageMatrix().getValues(sm_matrix_values);
        float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
        m_sx = target_matrix_values[0] - sm_matrix_values[0] ;
        m_sy = target_matrix_values[4] - sm_matrix_values[4] ;
        m_rx = target_matrix_values[1] - sm_matrix_values[1];
        m_ry = target_matrix_values[3] - sm_matrix_values[3];
        m_tx = target_matrix_values[2] - sm_matrix_values[2];
        m_ty = target_matrix_values[5] - sm_matrix_values[5];
        AnimateFullScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset);
    }

    private void  AnimateFullScreen(final float m_sx, final  float m_sy, final float m_rx, final float m_ry, final float m_tx, final float m_ty, final boolean flag_reset){
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        long tick = System.currentTimeMillis() ;

        final long startTime = tick;
        final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS  ;

        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];

        m_runnable_animation = new Runnable() {
            @Override
            public void run() {
                long tick = System.currentTimeMillis() ;
                float t = (float) ( (tick) - startTime) /  (float)(duration);

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
                    if(flag_reset == true){

                        setAlpha(1.0f);
                    }
                    FirstLevelActivity.scroll_img.SetImageAlpha(getCurrentItem(), 255);
                    ResetSecondToFirstImage();
                 }
            }
        };
        this.postDelayed(m_runnable_animation, 0);
    }

    public static void ResetSecondToFirstImage(){
        sm_touched_image_id = -1;
        //sm_animated_image.Drawable = null;
        //System.GC.Collect();
        if(sm_root_layout != null) sm_root_layout.removeView(sm_animated_image);
    }
}
