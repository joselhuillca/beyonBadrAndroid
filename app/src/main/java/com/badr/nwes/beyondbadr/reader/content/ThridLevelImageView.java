package com.badr.nwes.beyondbadr.reader.content;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.SystemConfiguration;

/**
 * Created by Kelvin on 2016-10-14.
 */
class ThridLevelImageView extends ImageView {
    private IFinalizeActivity m_finalizer;
    private static float []sm_matrix_values;
    private static Object sm_lock = new Object();
    //private static ImageView sm_animated_image ;
    private static int sm_num_images = 0;
    private static int sm_touched_image_id = -1;
    //private static RelativeLayout sm_root_layout  ;

    private Transformation transformation;
    private PointF m_start_pos_finger1;
    private PointF m_start_pos_finger2;
    private PointF []m_curr_finger;
    private float m_start_finger_slope;
    private float m_start_finger_separation;
    private Matrix m_matrix;
    private Matrix m_start_state;
    //private int m_screen_pos_x = 0;
    //private int m_screen_pos_y = 0;
    private int m_image_id;
    private boolean m_flag_up;
    private Runnable m_runnable_animation;
    private float m_x_medio;
    private float m_y_medio;
    private boolean is_scrolling = false;

    public ThridLevelImageView(Context context) {
        super(context);

    }
    public ThridLevelImageView(Context context, IFinalizeActivity finalizer){
        super(context);
        m_flag_up = false;
        m_finalizer = finalizer;
        m_start_pos_finger1 = new PointF();
        m_start_pos_finger2 = new PointF();

        //sm_animated_image = new ImageView(this.Context);
        sm_matrix_values = new float[9];
        //intent_full_scroll = new Intent(this.Context,typeof(FullScreenAnimatedActivity) );

        //sm_animated_image.SetScaleType(ScaleType.Matrix);
        //Transformation pepe = new Transformation();
        transformation = new Transformation();
        m_matrix = new Matrix();
        m_image_id = sm_num_images ++;
        m_curr_finger = new PointF[2];
        this.setScaleType(ScaleType.MATRIX);
    }

    public Matrix TransformationMatrix(){
        return transformation.getMatrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.setMatrix( transformation.getMatrix());
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(m_flag_up == true)
            return true;
        int action = ev.getAction();
        //Log.Info("mm", "ThirdLevelImageView:  sm_touched_image_id = " + sm_touched_image_id + " " + m_image_id);
        if(is_scrolling == false){
            if(ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE && sm_touched_image_id == -1){
                m_finalizer.readyToFinalize();
                sm_touched_image_id = m_image_id;
                //InitializeAnimatedImage();
                m_start_pos_finger1.set ( ev.getX(0) , ev.getY(0));
                m_start_pos_finger2.set(ev.getX(1), ev.getY(1));
                m_curr_finger[0] = new PointF( ev.getX(0) , ev.getY(0));
                m_curr_finger[1] = new PointF(ev.getX(1), ev.getY(1));

                m_start_state = new Matrix(this.getMatrix());
                //Log.Info("", "ThirdLevelImage: Start: " + m_start_state);
                m_y_medio = (   m_start_pos_finger1.y + m_start_pos_finger2.y) / 2.0f;
                m_x_medio = (  m_start_pos_finger1.x + m_start_pos_finger2.x) / 2.0f;
                m_start_finger_slope = (1.0f *( m_start_pos_finger1.y - m_start_pos_finger2.y)) / ( m_start_pos_finger1.x - m_start_pos_finger2.x) ;
                m_start_finger_separation = ( m_start_pos_finger1.x - m_start_pos_finger2.x) * ( m_start_pos_finger1.x - m_start_pos_finger2.x) + ( m_start_pos_finger1.y - m_start_pos_finger2.y) *( m_start_pos_finger1.y - m_start_pos_finger2.y) ;
                return  true;
            }
            if(sm_touched_image_id == m_image_id) {
                if(action == MotionEvent.ACTION_UP ){
                    transformation.getMatrix().getValues(sm_matrix_values);
                    m_flag_up = true;
                    float []start_state_values = new float[9];
                    this.getMatrix().getValues(start_state_values);
                    m_matrix.reset() ;
                    //float m_sx, m_sy, m_rx, m_ry, m_tx, m_ty;
                    //bool flag_reset = false;

                    //flag_reset = true;

                    float current_scale = (float)Math.sqrt(sm_matrix_values[0] * sm_matrix_values[0]  + sm_matrix_values[1] * sm_matrix_values[1]);
                    float desired_scale_to_go = SystemConfiguration.SCALE_PROP_SC_TO_FS_LEVEL *  start_state_values[0];

                    if(current_scale > desired_scale_to_go){


                        SettingAnimation(1, 1, 0,0, 0, 0,true,SystemConfiguration.SCALE_ANIMATION_MILLISECONDS  );
                        //PerformAnimation(start_state_values, flag_reset);
                    }else{
                        m_finalizer.finalizeNow();
                    }
                    //AnimateFullScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset);
                    return true;
						/*sm_touched_image_id = -1;
						sm_root_layout.RemoveView(sm_animated_image);*/

                    //this.Alpha = 255;
                }else if( ev.getPointerCount() == 2 && action == MotionEvent.ACTION_MOVE ){

                    this.setImageAlpha(255);
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


                    transformation.getMatrix().set( m_start_state);
                    transformation.getMatrix().postScale(Math.abs(scale_factor), Math.abs (scale_factor), m_x_medio, m_y_medio);

                    transformation.getMatrix().postRotate(angle, m_x_medio, m_y_medio);
                    transformation.getMatrix().postTranslate(dx, dy);
                    //Log.Info("", "ThirdLevelImage: matrix = " + this.Matrix);
                    ////Log.Info("d", "moving " + angle + " " + m_x_medio + " " + m_y_medio + " "  + dx + " " + dy + " " + scale_factor + " " + m_matrix);

                    this.invalidate();

                }

                return true;
            }
        }


        return super.onTouchEvent(ev);
    }

    public void SettingAnimation(float m_sx, float m_sy, float m_rx, float m_ry, float m_tx, float m_ty, boolean flag_reset, long duration){

        this.TransformationMatrix().getValues(sm_matrix_values);
        m_sx = m_sx - sm_matrix_values[0];
        m_sy = m_sy - sm_matrix_values[4];
        m_rx = m_rx - sm_matrix_values[1];
        m_ry = m_ry - sm_matrix_values[3];
        m_tx = m_tx - sm_matrix_values[2];
        m_ty = m_ty - sm_matrix_values[5];
        AnimateReturnScreen(m_sx, m_sy, m_rx, m_ry, m_tx, m_ty, flag_reset, duration);
    }

    private void  AnimateReturnScreen(final float m_sx, final float m_sy, final float m_rx, final float m_ry, final float m_tx, final float m_ty, final boolean flag_reset, final long duration){
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        long tick = System.currentTimeMillis();

        final long startTime = tick;
        final Matrix matrix_opt = new Matrix();
        final float []m_curr_matrix_values = new float[9];

        m_runnable_animation =  new Runnable() {
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
                transformation.getMatrix().set(matrix_opt);
                ////Log.Info("mmmm", "Animation: Matrix = " + clone_image.ImageMatrix);
                invalidate();
                if (t < 1f) {
                    post(m_runnable_animation);
                }else{
                    sm_touched_image_id = -1;
                    if(flag_reset== true)
                        m_flag_up = false;
                    //if(flag_reset == false){
                    //	StartSecondLevelActivity();
                    //}else{
                    //System.Threading.Interlocked.Decrement(ref SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG);

                    //FirstLevelActivity.scroll_img.SetImageAlpha(this.CurrentItem, 255);
                    //ResetSecondToFirstImage();
                    //}

                }
            }
        };
        this.postDelayed(m_runnable_animation, 0);
    }
}
