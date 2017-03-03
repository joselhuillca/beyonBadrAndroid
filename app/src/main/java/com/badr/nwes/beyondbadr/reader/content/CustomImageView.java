package com.badr.nwes.beyondbadr.reader.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badr.nwes.beyondbadr.ProportionCalculator;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.TabSmartphone.TabMore;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;
import com.badr.nwes.beyondbadr.index.PageIndex;
import com.badr.nwes.beyondbadr.reader.FullScreenAnimatedActivity;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class CustomImageView extends ImageView implements OnClickListener {
    public static String SX_MATRIX="sx_matrix";
    public static String SY_MATRIX="sy_matrix";
    public static String RX_MATRIX="rx_matrix";
    public static String RY_MATRIX="ry_matrix";
    public static String TX_MATRIX="tx_matrix";
    public static String TY_MATRIX="ty_matrix";

    private StackBorderLoader m_stack_border_bitmap_gen;
    private static Intent intent_full_scroll;
    private PointF m_start_pos_finger1;
    private PointF m_start_pos_finger2;
    private PointF[]m_curr_finger;
    private float m_start_finger_slope;
    private float m_start_finger_separation;
    private Matrix m_matrix;
    private Matrix m_start_state;
    private int screen_pos_x=0;
    private int screen_pos_y=0;

    private static float[]sm_matrix_values;
    private static Object sm_lock = new Object();
    private static ImageView clone_image;
    private static int sm_num_images=0;
    private static int sm_touched_image_id=-1;
    private int m_image_id;
    private static RelativeLayout root_layout;
    private Runnable wait_runnable;
    private InfScrollPaged m_scroll_paged;

    private TabMore tabmore;

    private float m_x_medio;
    private float m_y_medio;
    public boolean EnabledViewPager;


    public CustomImageView(Context context){
        super(context);
        EnabledViewPager = true;
        m_start_pos_finger1 = new PointF();
        m_start_pos_finger2 = new PointF();
        if(clone_image == null) {
            clone_image = new ImageView(this.getContext());
            sm_matrix_values = new float[9];
            intent_full_scroll = new Intent(this.getContext(),FullScreenAnimatedActivity.class );
        }
        m_stack_border_bitmap_gen = new StackBorderLoader (context);
        clone_image.setScaleType(ScaleType.MATRIX);
        m_matrix = new Matrix();
        m_image_id = sm_num_images ++;
        m_curr_finger = new PointF[2];
        //this.Click += new EventHandler(OnClick);

        this.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {



        if(SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG != 0)
            return;

        java.util.concurrent.locks.Lock   lock = new ReentrantLock();

        lock.lock();
        SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG++;
        lock.unlock();

        //Log.info("me llamaron", "porfavorrrrrrrrrrrr : " + SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);

        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();


        PageIndex pageIndex =  (PageIndex)this.getTag();

        if(root_layout == null) {
            root_layout = (RelativeLayout) this.getParent().getParent().getParent().getParent().getParent();
        }


        m_scroll_paged = (InfScrollPaged) this.getParent().getParent().getParent().getParent();
        //clone_image.LayoutParameters = new RelativeLayout.LayoutParams( pageIndex.width, pageIndex.height);
        clone_image.setLayoutParams( new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) );
        //clone_image.setwi - pageIndex.width;
        Bitmap bmSrc1 = ((BitmapDrawable)this.getDrawable()).getBitmap();
        //Bitmap bmSrc2 = bmSrc1.Copy(bmSrc1.GetConfig(), true);

        String filepath = InfScrollPaged.book.Chapters.get(pageIndex.chapter).Sections.get(pageIndex.section).Pages.get( pageIndex.page ).MediumSource;


        int req_width = (int) (img_big_sz_x_ / SystemConfiguration.NUMBER_OF_THUMBNAILS_IN_SCREEN);
        //int req_height = img_big_sz_y_ / SystemConfiguration.SCREEN_HEIGHT_DIVIDER ;
        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(filepath,img_big_sz_x_ ,img_big_sz_y_ , getContext());
        //Log.Info("","input size: " + input_image_bitmap.Height + " " + input_image_bitmap.Width);
        Bitmap copy_border = m_stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
        int scale_factor = Math.min(img_big_sz_x_ /( 2 * copy_border.getWidth() ), img_big_sz_y_ /(2 * copy_border.getHeight()));
        copy_border  = Bitmap.createScaledBitmap(copy_border, copy_border.getWidth() * scale_factor, copy_border.getHeight() * scale_factor, false);
        Canvas comboImage = new Canvas(copy_border);


        float [] boundaries = new float[4];

        ProportionCalculator.CalculeImageBoundaries(copy_border.getWidth(), copy_border.getHeight(), boundaries);
        comboImage.drawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight()), new RectF(boundaries[0], boundaries[1], copy_border.getWidth() - boundaries[2], copy_border.getHeight() - boundaries[3]), null);

        clone_image.setImageBitmap(copy_border);

        //clone_image.SetImageBitmap(bmSrc2);

        //clone_image.Matrix = this.Matrix;
        clone_image.setImageMatrix(this.getImageMatrix());
        float []values_matrix_temp = new float[9];
        this.getImageMatrix().getValues(values_matrix_temp);
        //Log.Info("pepe", "matrix1: " + this.Matrix);
        //Log.Info("pepe", "matrix2: " + clone_image.Matrix);
        //Log.Info("pepe", "image_matrix1: " + this.ImageMatrix);
        //Log.Info("pepe", "image_matrix2: " + clone_image.ImageMatrix);

        screen_pos_y = ( 2 * ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getHeight()) / SystemConfiguration.SCREEN_HEIGHT_DIVIDER - SystemConfiguration.SCREEN_HEIGHT_OFFSET;

        screen_pos_x = pageIndex.pos_x - m_scroll_paged.getScrollX();
        clone_image.setX(0);
        clone_image.setY(0);

        root_layout.addView(clone_image);

        m_matrix.reset() ;
        m_start_state = new Matrix(clone_image.getImageMatrix());
        m_start_state.postScale(1.0f / scale_factor, 1.0f / scale_factor);
        m_start_state.postTranslate(0, values_matrix_temp[5] * (1.0f - 1.0f/ scale_factor));
        //Log.Info("pepe", "matrix1: " + m_start_state + " scale: "+ 1.0f/ scale_factor);
        m_start_state.postTranslate(screen_pos_x, screen_pos_y);
        clone_image.setImageMatrix(m_start_state);
        invalidate();

        float []bg_boundaries = new float[4];
        clone_image.getImageMatrix().getValues(sm_matrix_values);


        Drawable drawable = clone_image.getDrawable();

        float image_width = drawable.getIntrinsicWidth() * (float)Math.sqrt((double) (sm_matrix_values[0] * sm_matrix_values[0] + sm_matrix_values[1] * sm_matrix_values[1]));
        float image_height  = drawable.getIntrinsicHeight() * (float)Math.sqrt( (double)( sm_matrix_values[3] * sm_matrix_values[3] + sm_matrix_values[4] * sm_matrix_values[4]));
        //Rect image_bounds = drawable.Bounds;
        ProportionCalculator.CalculeBackgroundBoundaries(img_big_sz_x_, img_big_sz_y_, bg_boundaries);

        float image_width_needed = bg_boundaries[2] - bg_boundaries[0];
        float image_height_needed = bg_boundaries[3] - bg_boundaries[1];

        float sx_needed = image_width_needed / drawable.getIntrinsicWidth();
        float sy_needed = image_height_needed / drawable.getIntrinsicHeight();
        float tx_needed = bg_boundaries[0];
        float ty_needed = bg_boundaries[1];

        float []m_start_state_values = new float[9];

        m_start_state.getValues(m_start_state_values);
        //clone_image.ImageMatrix.GetValues(sm_matrix_values);

        float desired_scale_to_go = SystemConfiguration.SCALE_PROP_FS_TO_SC_LEVEL * (sx_needed + m_start_state_values[0]);
        float current_scale = (float)Math.sqrt(sm_matrix_values[0] * sm_matrix_values[0]  + sm_matrix_values[1] * sm_matrix_values[1]);
        float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
        boolean flag_reset = false;

        m_sx = sx_needed - sm_matrix_values[0] ;
        m_sy = sy_needed - sm_matrix_values[4] ;
        m_rx = 0.0f - sm_matrix_values[1];
        m_ry = 0.0f - sm_matrix_values[3];
        m_tx = tx_needed - sm_matrix_values[2];
        m_ty = ty_needed - sm_matrix_values[5];
        AnimateFullScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (this.getAlpha() == 0.0f)
            return true;

        if(EnabledViewPager == false)
            return false;

        int action = ev.getActionMasked() ;
        int img_big_sz_x_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        int img_big_sz_y_ = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getHeight();


        synchronized(sm_lock){
            //Log.Info("ima", "touched img id: " + sm_touched_image_id + " " + "img id: " + m_image_id + " " + ev.PointerCount);
            if(sm_touched_image_id != m_image_id && sm_touched_image_id != -1){
                return true;

            }

            if(ev.getPointerCount() == 2 && action == MotionEvent.ACTION_POINTER_DOWN && sm_touched_image_id == -1){
                if(SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG != 0)
                    return true;
                //System.Threading.Interlocked.Increment(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
                //Log.Info("ima", "lo desabilite");
                getParent().requestDisallowInterceptTouchEvent(true);
                sm_touched_image_id = m_image_id;
                clone_image.setImageAlpha(0);
                PageIndex pageIndex =  (PageIndex)this.getTag();
                if(root_layout == null)
                    root_layout  = (RelativeLayout)this.getParent().getParent().getParent().getParent().getParent();
                m_scroll_paged = (InfScrollPaged) this.getParent().getParent().getParent().getParent();
                //clone_image.LayoutParameters = new RelativeLayout.LayoutParams( pageIndex.width, pageIndex.height);
                clone_image.setLayoutParams( new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) );
                //clone_image.setwi - pageIndex.width;
                Bitmap bmSrc1 = ((BitmapDrawable)this.getDrawable()).getBitmap();
                //Bitmap bmSrc2 = bmSrc1.Copy(bmSrc1.GetConfig(), true);

                String filepath = InfScrollPaged.book.Chapters.get(pageIndex.chapter).Sections.get(pageIndex.section).Pages.get(pageIndex.page).MediumSource;


                int req_width = (int) (img_big_sz_x_ / SystemConfiguration.NUMBER_OF_THUMBNAILS_IN_SCREEN);
                //int req_height = img_big_sz_y_ / SystemConfiguration.SCREEN_HEIGHT_DIVIDER ;
                Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(filepath,img_big_sz_x_ ,img_big_sz_y_, getContext() );
                //Log.Info("","input size: " + input_image_bitmap.Height + " " + input_image_bitmap.Width);
                Bitmap copy_border = m_stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
                int scale_factor = Math.min(img_big_sz_x_ /( 2 * copy_border.getWidth() ), img_big_sz_y_ /(2 * copy_border.getHeight()));
                copy_border  = Bitmap.createScaledBitmap(copy_border, copy_border.getWidth() * scale_factor, copy_border.getHeight() * scale_factor, false);
                Canvas comboImage = new Canvas(copy_border);


                float [] boundaries = new float[4];

                ProportionCalculator.CalculeImageBoundaries(copy_border.getWidth(), copy_border.getHeight(), boundaries);
                comboImage.drawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight()), new RectF(boundaries[0], boundaries[1], copy_border.getWidth() - boundaries[2], copy_border.getHeight() - boundaries[3]), null);

                clone_image.setImageBitmap(copy_border);
                //clone_image.SetImageBitmap(bmSrc2);

                //clone_image.Matrix = this.Matrix;
                clone_image.setImageMatrix(this.getImageMatrix());
                float []values_matrix_temp = new float[9];
                this.getImageMatrix().getValues(values_matrix_temp);
                //Log.Info("pepe", "matrix1: " + this.Matrix);
                //Log.Info("pepe", "matrix2: " + clone_image.Matrix);
                //Log.Info("pepe", "image_matrix1: " + this.ImageMatrix);
                //Log.Info("pepe", "image_matrix2: " + clone_image.ImageMatrix);

                screen_pos_y = ( 2 * ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getHeight()) / SystemConfiguration.SCREEN_HEIGHT_DIVIDER - SystemConfiguration.SCREEN_HEIGHT_OFFSET;

                screen_pos_x = pageIndex.pos_x - m_scroll_paged.getScrollX();
                clone_image.setX(0);
                clone_image.setY(0);

                root_layout.addView(clone_image);

                m_start_pos_finger1.set ( ev.getX(0), ev.getY(0));
                m_start_pos_finger2.set( ev.getX(1), ev.getY(1));
                m_curr_finger[0] = new PointF( ev.getX(0), ev.getY(0));
                m_curr_finger[1] = new PointF(ev.getX(1), ev.getY(1));
                m_matrix.reset() ;
                m_start_state = new Matrix(clone_image.getImageMatrix());
                m_start_state.postScale(1.0f / scale_factor, 1.0f / scale_factor);
                m_start_state.postTranslate(0, values_matrix_temp[5] * (1.0f - 1.0f/ scale_factor));
                //Log.Info("pepe", "matrix1: " + m_start_state + " scale: "+ 1.0f/ scale_factor);
                m_start_state.postTranslate(screen_pos_x, screen_pos_y);

                m_y_medio = (  2 * screen_pos_y + m_start_pos_finger1.y + m_start_pos_finger2.y) / 2.0f;
                m_x_medio = ( 2 * screen_pos_x + m_start_pos_finger1.x + m_start_pos_finger2.x) / 2.0f;
                m_start_finger_slope = (1.0f *( m_start_pos_finger1.y - m_start_pos_finger2.y)) / ( m_start_pos_finger1.x - m_start_pos_finger2.x) ;
                m_start_finger_separation = ( m_start_pos_finger1.x - m_start_pos_finger2.x) * ( m_start_pos_finger1.x - m_start_pos_finger2.x) + ( m_start_pos_finger1.y - m_start_pos_finger2.y) *( m_start_pos_finger1.y - m_start_pos_finger2.y) ;
                clone_image.setImageMatrix(m_start_state);
                invalidate();
                //Log.Info("ima", "si que si");
                return true;
            }
            ////Log.Info("ima", "pass touched img id: " + sm_touched_image_id + " " + "img id: " + m_image_id );

        }
        if(sm_touched_image_id == m_image_id) {
            ////Log.Info("d", "llegueeeeeeeeeeeeeeeeeeeeeeeeeeee");
            if(action == MotionEvent.ACTION_UP ){
                EnabledViewPager = false;
                float []bg_boundaries = new float[4];
                clone_image.getImageMatrix().getValues(sm_matrix_values);


                Drawable drawable = clone_image.getDrawable();

                float image_width = drawable.getIntrinsicWidth() * (float)Math.sqrt((double) (sm_matrix_values[0] * sm_matrix_values[0] + sm_matrix_values[1] * sm_matrix_values[1]));
                float image_height  = drawable.getIntrinsicHeight() * (float)Math.sqrt( (double)( sm_matrix_values[3] * sm_matrix_values[3] + sm_matrix_values[4] * sm_matrix_values[4]));
                //Rect image_bounds = drawable.Bounds;
                ProportionCalculator.CalculeBackgroundBoundaries(clone_image.getWidth(), clone_image.getHeight(), bg_boundaries);


                float image_width_needed = bg_boundaries[2] - bg_boundaries[0];
                float image_height_needed = bg_boundaries[3] - bg_boundaries[1];

                float sx_needed = image_width_needed / drawable.getIntrinsicWidth();
                float sy_needed = image_height_needed / drawable.getIntrinsicHeight();
                float tx_needed = bg_boundaries[0];
                float ty_needed = bg_boundaries[1];

                float []m_start_state_values = new float[9];

                m_start_state.getValues(m_start_state_values);
                //clone_image.ImageMatrix.GetValues(sm_matrix_values);

                float desired_scale_to_go = SystemConfiguration.SCALE_PROP_FS_TO_SC_LEVEL * (sx_needed + m_start_state_values[0]);
                float current_scale = (float)Math.sqrt(sm_matrix_values[0] * sm_matrix_values[0]  + sm_matrix_values[1] * sm_matrix_values[1]);
                float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
                boolean flag_reset = false;

                if(current_scale >= desired_scale_to_go){
                    m_sx = sx_needed - sm_matrix_values[0] ;
                    m_sy = sy_needed - sm_matrix_values[4] ;
                    m_rx = 0.0f - sm_matrix_values[1];
                    m_ry = 0.0f - sm_matrix_values[3];
                    m_tx = tx_needed - sm_matrix_values[2];
                    m_ty = ty_needed - sm_matrix_values[5];
                }else{
                    m_sx = m_start_state_values[0] - sm_matrix_values[0] ;
                    m_sy = m_start_state_values[4] - sm_matrix_values[4] ;
                    m_rx = m_start_state_values[1] - sm_matrix_values[1];
                    m_ry = m_start_state_values[3] - sm_matrix_values[3];
                    m_tx = m_start_state_values[2] - sm_matrix_values[2];
                    m_ty = m_start_state_values[5] - sm_matrix_values[5];
                    flag_reset = true;
                }
                //Log.Info("o", "CustomImageVIew: Opening multiples tiemessss");
                if(SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG != 0) {
                    EnabledViewPager = true;
                    return true;
                }
                if(flag_reset == false){

                    java.util.concurrent.locks.Lock   lock = new ReentrantLock();
                    lock.lock();
                    //System.Threading.Interlocked.Increment(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
                    SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG++;
                    lock.unlock();

                }

                AnimateFullScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset);
                return true;

            }else if( ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE){
                this.setAlpha(0); // fixed
                clone_image.setImageAlpha(255);
                int index = ev.getActionIndex();

                //m_curr_finger[index].Set((float)ev.GetX(index), (float)ev.GetY(index));
                m_curr_finger[0].set((float)ev.getX(0), (float)ev.getY(0));
                //PointF curr_finger2;
                if(ev.getPointerCount() == 2)
                    m_curr_finger[1].set((float)ev.getX(1),(float) ev.getY(1));
                else {
                    //touched_by_two = false;
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

                clone_image.setImageMatrix(m_matrix);
                ////Log.Info("d", "moving " + angle + " " + m_x_medio + " " + m_y_medio + " "  + dx + " " + dy + " " + scale_factor + " " + m_matrix);

                invalidate();

            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void StartSecondLevelActivity(){

        //Log.Info("Threads", "INFscrollPaged: thread entered: " + SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
        CustomImageView image = (CustomImageView)this ;
        PageIndex pageIndex =  (PageIndex)image.getTag() ;

        float []start_state_values = new float[9];
        m_start_state.getValues(start_state_values);

        intent_full_scroll.putExtra(SX_MATRIX, start_state_values[0]);
        intent_full_scroll.putExtra(SY_MATRIX, start_state_values[4]);
        intent_full_scroll.putExtra(RX_MATRIX, start_state_values[1]);
        intent_full_scroll.putExtra(RY_MATRIX, start_state_values[3]);
        intent_full_scroll.putExtra(TX_MATRIX, start_state_values[2]);
        intent_full_scroll.putExtra(TY_MATRIX, start_state_values[5]);
        intent_full_scroll.putExtra("ind_chapter", pageIndex.chapter);
        intent_full_scroll.putExtra("ind_section", pageIndex.section);
        intent_full_scroll.putExtra("ind_image", pageIndex.page);
        intent_full_scroll.putExtra("unique_id_image", pageIndex.unique_id);
        intent_full_scroll.putExtra("image_width", pageIndex.width);
        intent_full_scroll.putExtra("image_height", pageIndex.height);
        intent_full_scroll.putExtra("image_position_x", pageIndex.pos_x - this.getScrollX());

        ( (Activity)this.getContext() ).startActivity(intent_full_scroll);
    }

    public static void ResetFirstToSecondImage(){
        sm_touched_image_id = -1;
        if(root_layout != null) root_layout.removeView(clone_image);
    }

    private void  AnimateFullScreen(final float m_sx, final float m_sy, final float m_rx,final  float m_ry, final float m_tx, final float m_ty,final  boolean flag_reset){
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final long tick = System.currentTimeMillis();
        final long startTime = tick;
        final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS;
        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];

        wait_runnable = new Runnable() {
            @Override
            public void run() {
                long tick = System.currentTimeMillis();
                float t = (float) ( (tick) - startTime) / (duration);
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
                clone_image.setImageMatrix(matrix_opt);
                ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                invalidate();
                if (t < 1f) {
                    clone_image.post(wait_runnable);
                }else{
                    if(flag_reset == false){
                        StartSecondLevelActivity();
                    }else{
                        //System.Threading.Interlocked.Decrement(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);
                        ResetFirstToSecondImage();
                    }
                    setImageAlpha(255);
                    EnabledViewPager = true;
                }
            }
        };
        this.postDelayed(wait_runnable, 0);
    }


}
