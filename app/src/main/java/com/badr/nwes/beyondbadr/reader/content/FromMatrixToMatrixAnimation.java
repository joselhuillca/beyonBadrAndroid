package com.badr.nwes.beyondbadr.reader.content;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Kelvin on 2016-10-14.
 */

public class FromMatrixToMatrixAnimation extends Animation {

    float[] m_initial_state;
    float[] m_target_state;
    float[] m_curr_state;
    float[] m_img_init_state;
    float[] m_img_end_state;
    float[] m_img_curr_state;
    ThridLevelImageView m_image_view;
    public FromMatrixToMatrixAnimation(Matrix init_state, Matrix target_state, ThridLevelImageView image_view, float[] mv_target){
        super();

        m_image_view = image_view;
        m_img_init_state = new float[9];
        m_img_curr_state = new float[9];
        m_image_view.getImageMatrix().getValues(m_img_init_state);

        m_initial_state = new float[9];
        m_target_state = new float[9];
        m_curr_state = new float[9];
        init_state.getValues(m_initial_state);
        target_state.getValues(m_target_state);

        for(int i = 0; i < 6 ; i++){
            m_target_state[i] -= m_initial_state[i];
            mv_target[i] -= m_img_init_state[i];
        }
        m_img_end_state = mv_target;
        m_img_curr_state[8] = 1.0f;
        m_curr_state[8] = 1.0f;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        for(int ind = 0; ind < 6 ; ++ind){
            m_img_curr_state[ind] = m_img_init_state[ind] + m_img_end_state[ind] * interpolatedTime;
            m_curr_state[ind] = m_initial_state[ind] + m_target_state[ind] * interpolatedTime;
        }
        t.getMatrix().setValues(m_curr_state);
        m_image_view.getImageMatrix().setValues(m_img_curr_state);
        m_image_view.getImageMatrix().postTranslate(0,0);
        m_image_view.invalidate();

    }
}
