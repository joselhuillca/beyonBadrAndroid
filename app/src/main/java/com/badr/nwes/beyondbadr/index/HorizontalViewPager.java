package com.badr.nwes.beyondbadr.index;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Kelvin on 2016-10-14.
 */
class HorizontalViewPager extends ViewPager {

    private BoolHolder m_touched = new BoolHolder (false) ;

    public boolean Touched(){
        return m_touched.getHolder().get();
    }

    ObservableHorScrollView m_reference_object;
    public HorizontalViewPager ( Context context) {
        super(context);
    }

    public HorizontalViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int GetLowerLimitPage(int position)
    {
        return this.getMeasuredWidth() * position;
    }

    public void SetRefObsHorScrollView(ObservableHorScrollView reference_object){
        m_reference_object = reference_object;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(m_reference_object != null && m_reference_object.Touched())
                    return true;

                m_touched.getHolder().getAndSet(true);

                //m_reference_object.Enabled = false;
                //Log.Info("de", "desabilite todooooo");
                break;
            case MotionEvent.ACTION_MOVE:
                if(m_reference_object != null && m_reference_object.Touched())
                    return true;
                m_touched.getHolder().getAndSet(true);
                break;
            case MotionEvent.ACTION_UP:
                m_touched.getHolder().getAndSet(false);
                //Log.Info("quququq", "porque me levanteeeee up!");
                break;

        }
        return true; //todo: remove scroll event
        //return super.onTouchEvent(ev);
    }

    public int GetUpperLimitPage(int position){

      return this.getMeasuredWidth() * (position + 1) ;
    }
}
