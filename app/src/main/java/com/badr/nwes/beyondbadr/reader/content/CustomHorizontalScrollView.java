package com.badr.nwes.beyondbadr.reader.content;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.badr.nwes.beyondbadr.index.ObservableHorViewPager;

/**
 * Created by Kelvin on 2016-10-14.
 */
public  class CustomHorizontalScrollView  extends FrameLayout {

    private final float MAX_SCROLL_FACTOR=0.5f;

    private final String TAG="InfScrollPage";

    private static Rect m_temp_rect=new Rect();
    protected OverScroller m_scroller;
    private EdgeEffect m_edge_glow_left;
    private EdgeEffect m_edge_glow_right;

    private int m_last_motion_x;

    private boolean m_is_being_dragged=false;

    private VelocityTracker m_velocity_tracker;

    private boolean m_fill_viewport;

    private boolean m_smooth_scrolling_enabled=true;
    protected int m_touch_slop;
    private int m_minimum_velocity;
    private int m_maximum_velocity;
    private int m_overscroll_distance;
    private int m_overfling_distance;

    private final int INVALID_POINTER=-1;

    private int m_active_pointer_id= INVALID_POINTER;


    public CustomHorizontalScrollView(Context context){
        super(context);
        InitScrollView();
    }



    @Override
    protected float getLeftFadingEdgeStrength() {
        if(getChildCount() == 0){
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        if( getScrollX() < length ){ //duda
            return getScrollX() / (float) length; //duda
        }
        return 1.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        if(getChildCount() == 0){
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        int right_edge = getWidth() - getPaddingRight();
        int span = getChildAt(0).getRight() - getScrollX() - right_edge;
        if(span < length){
            return span / (float) length;
        }
        return 1.0f;
    }

    public int GetMaxScrollAmount(){
        return (int) ( MAX_SCROLL_FACTOR * getRight() - getLeft());
    }

    private void InitScrollView(){
        m_edge_glow_left = new EdgeEffect(this.getContext());
        m_edge_glow_right = new EdgeEffect(this.getContext());
        m_scroller = new OverScroller(this.getContext());
        this.setFocusable( true );
        this.setDescendantFocusability ( ViewGroup.FOCUS_AFTER_DESCENDANTS );
        this.setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
        m_touch_slop = configuration.getScaledTouchSlop();
        m_minimum_velocity = configuration.getScaledMinimumFlingVelocity();
        m_maximum_velocity = configuration.getScaledMaximumFlingVelocity();
        m_overscroll_distance = configuration.getScaledOverscrollDistance();
        m_overfling_distance = configuration.getScaledOverflingDistance();
    }


    @Override
    public void addView(View child) {
        if(getChildCount() > 0){
            try {
                throw new  Exception("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if(getChildCount() > 0){
            try {
                throw new  Exception("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if(getChildCount() > 0){
            try {
                throw new  Exception("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(getChildCount() > 0){
            try {
                throw new  Exception("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.addView(child, index, params);
    }



    public boolean IsFillViewport(){
        return m_fill_viewport;
    }

    public void SetFillViewport(boolean fill_viewport){
        if(fill_viewport != m_fill_viewport){
            m_fill_viewport = fill_viewport;
            requestLayout();
        }
    }

    public boolean IsSmoothScrollingEnabled()
    {
        return m_smooth_scrolling_enabled;
    }

    public void SetSmoothScrollingEnabled(boolean smooth_scrolling_enabled){
        m_smooth_scrolling_enabled = smooth_scrolling_enabled;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(! m_fill_viewport)
            return;

        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        if(width_mode == MeasureSpec.UNSPECIFIED)
            return;

        if(getChildCount() > 0){
            View child = getChildAt(0);
            int width = getMeasuredWidth();
            if(child.getMeasuredWidth() < width){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int child_height_measure_spec  = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingLeft(), lp.height);
                width -= getPaddingLeft();
                width -= getPaddingRight();
                int child_width_measure_spec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                child.measure(child_width_measure_spec, child_height_measure_spec);
            }
        }

    }
    private boolean InChild(int x, int y){
        if( getChildCount() > 0){
            int scrollx = getScrollX();
            View child  = getChildAt(0);
            return ! (y < child.getTop() || y >= child.getBottom() || x < child.getLeft() - scrollx || x >= child.getRight() - scrollx );
        }
        return false;
    }

    private void InitOrResetVelocityTracker(){
        if(m_velocity_tracker == null){
            m_velocity_tracker = VelocityTracker.obtain();
        }else{
            m_velocity_tracker.clear();
        }
    }

    private void  InitVelocitytrackerIfNotExists(){
        if(m_velocity_tracker == null){
            m_velocity_tracker = VelocityTracker.obtain();
        }
    }

    private void RecycleVelocityTracker(){
        if(m_velocity_tracker != null){
            m_velocity_tracker.recycle();
            m_velocity_tracker = null;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept){
            RecycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if( ( action == MotionEvent.ACTION_MOVE) && (m_is_being_dragged) )
            return true;
        //Log.Info("ahora", "me interceptaron!!!!");
        switch (action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE:
                int active_pointer_id = m_active_pointer_id;
                if( active_pointer_id == INVALID_POINTER) break;
                int pointer_index = ev.findPointerIndex(active_pointer_id);
                if(pointer_index == -1)
                    break;
                int x = (int) ev.getX(pointer_index);
                int x_diff = (int) Math.abs (x - m_last_motion_x);
                if(x_diff > m_touch_slop){
                    m_is_being_dragged = true;
                    m_last_motion_x = x;
                    InitVelocitytrackerIfNotExists();
                    m_velocity_tracker.addMovement(ev);
                    if(this.getParent() != null)
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                int x2 = (int) ev.getX();
                if( ! InChild(x2, (int) ev.getY() ) ){
                    m_is_being_dragged = false;
                    RecycleVelocityTracker();
                    break;
                }
                m_last_motion_x = x2;
                m_active_pointer_id = ev.getPointerId(0);
                InitOrResetVelocityTracker();
                m_velocity_tracker.addMovement(ev);

                m_is_being_dragged = ! m_scroller.isFinished();
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
            case MotionEvent.ACTION_UP:
                m_is_being_dragged = false;
                m_active_pointer_id = INVALID_POINTER;
                if(m_scroller.springBack(getScrollX(), getScrollY(),0, GetScrollRange(), 0, 0) ){
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int index = ev.getActionIndex();
                m_last_motion_x = (int) ev.getX(index);
                m_active_pointer_id = ev.getPointerId(index);
                break;
            case  MotionEvent.ACTION_POINTER_UP:
                OnSecundaryPointerUp(ev);
                //Log.Info("a", "active pointer: " + m_active_pointer_id);
                //m_last_motion_x = (int) ev.GetX(ev.FindPointerIndex(m_active_pointer_id) );
                break;
        }
        return m_is_being_dragged;
    }

    public boolean bBreak () {
        return false;
    }
    public int TestPosition (int scrollx, int page) {
        return 0;
    }
    public ObservableHorViewPager getPager () {
        return null;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        InitVelocitytrackerIfNotExists();
        m_velocity_tracker.addMovement(ev);


        int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                if(getChildCount() == 0)
                    return false;
                if( (m_is_being_dragged = ! m_scroller.isFinished()) ) {
                    ViewParent parent = getParent();
                    if(parent != null){
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if( ! m_scroller.isFinished()){
                    m_scroller.abortAnimation();
                }
                m_last_motion_x = (int) ev.getX();
                m_active_pointer_id = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                int active_pointer_index  = ev.findPointerIndex(m_active_pointer_id);
                if(active_pointer_index == -1) break;
                int x = (int) ev.getX(active_pointer_index);

                int current_page = getPager().getCurrentItem();




                int delta_x = m_last_motion_x - x;
                if( ! m_is_being_dragged && Math.abs(delta_x) > m_touch_slop){
                    ViewParent parent = getParent();
                    if(parent != null){
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    m_is_being_dragged = true;
                    if(delta_x > 0)	delta_x -= m_touch_slop;
                    else delta_x += m_touch_slop;
                }

                if(m_is_being_dragged){
                    m_last_motion_x = x;
                    int old_x = getScrollX();
                    int old_y = getScrollY();
                    int range = GetScrollRange();
                    int over_scroll_mode = getOverScrollMode();
                    boolean can_over_scroll = (over_scroll_mode == View.OVER_SCROLL_ALWAYS) || ( over_scroll_mode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0 );

                    if( overScrollBy(delta_x, 0, getScrollX() , 0, range, 0, m_overscroll_distance, 0, true) ){
                        m_velocity_tracker.clear();
                    }
                    onScrollChanged(getScrollX(), getScrollY(), old_x, old_y);

                    if(can_over_scroll ){
                        int pulled_to_x = old_x + delta_x;
                        if(pulled_to_x < 0){
                            m_edge_glow_left.onPull( (float) delta_x / getWidth() );
                            if( ! m_edge_glow_right.isFinished()){
                                m_edge_glow_right.onRelease();
                            }
                        }else if (pulled_to_x > range){
                            m_edge_glow_right.onPull( (float) delta_x / getWidth() );
                            if(! m_edge_glow_left.isFinished()){
                                m_edge_glow_left.onRelease();
                            }
                        }
                        if( m_edge_glow_left != null && ( ! m_edge_glow_left.isFinished() || ! m_edge_glow_right.isFinished() ) ){
                            invalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(m_is_being_dragged){
                    VelocityTracker velocity_tracker = m_velocity_tracker;
                    velocity_tracker.computeCurrentVelocity(1000, m_maximum_velocity);
                    int initial_velocity = (int) velocity_tracker.getXVelocity(m_active_pointer_id);
                    if( getChildCount()  > 0){
                        if ( ( Math.abs(initial_velocity) > m_minimum_velocity) ){
                            Fling (-initial_velocity);
                        }else{
                            if( m_scroller.springBack(getScrollX() , getScrollY(), 0, GetScrollRange(), 0, 0 ) ){
                                invalidate();
                            }
                        }
                    }
                    m_active_pointer_id = INVALID_POINTER;
                    m_is_being_dragged = false;
                    RecycleVelocityTracker();
                    if(m_edge_glow_left != null){
                        m_edge_glow_left.onRelease();
                        m_edge_glow_right.onRelease();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (m_is_being_dragged && getChildCount() > 0){
                    if(m_scroller.springBack(getScrollX(), getScrollY(), 0, GetScrollRange(), 0, 0 ) ){
                        invalidate();
                    }
                    m_active_pointer_id = INVALID_POINTER;
                    m_is_being_dragged = false;
                    RecycleVelocityTracker();
                    if(m_edge_glow_left != null){
                        m_edge_glow_left.onRelease();
                        m_edge_glow_right.onRelease();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                OnSecundaryPointerUp(ev);
                break;
        }
        return true;
    }


    private void OnSecundaryPointerUp(MotionEvent ev){
        int pointer_index = ( ( (int) (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) ) >>  (int) MotionEvent.ACTION_POINTER_INDEX_SHIFT );
        int pointer_id = ev.getPointerId(pointer_index);
        if(pointer_id == m_active_pointer_id){
            int new_pointer_index = (pointer_index == 0 ? 1 : 0);
            m_last_motion_x = (int)ev.getX(new_pointer_index);
            m_active_pointer_id = ev.getPointerId(new_pointer_index);
            if(m_velocity_tracker != null){
                m_velocity_tracker.clear();
            }
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent ev) {

        if( (ev.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0){
            switch(ev.getAction()){
                case MotionEvent.ACTION_SCROLL:
                    if(! m_is_being_dragged ){
                        float hscroll;
                        if( ( ev.getMetaState() & KeyEvent.META_SHIFT_ON) != 0 ){
                            hscroll = - ev.getAxisValue(MotionEvent.AXIS_VSCROLL );
                        }else{
                            hscroll = ev.getAxisValue(MotionEvent.AXIS_HSCROLL);
                        }
                        if(hscroll != 0){
                            TypedValue out_value = new TypedValue();
                            float horizontal_scroll_factor = out_value.getDimension(Resources.getSystem().getDisplayMetrics());
                            int  delta = (int) (hscroll * horizontal_scroll_factor) ;
                            int range = GetScrollRange();
                            int old_scroll_x = getScrollX();
                            int new_scroll_x = old_scroll_x + delta;
                            if(new_scroll_x < 0){
                                new_scroll_x = 0;
                            }else if(new_scroll_x > range){
                                new_scroll_x = range;
                            }
                            if(new_scroll_x != old_scroll_x){
                                super.scrollTo(new_scroll_x, getScrollY());
                                return true;
                            }
                        }
                    }
                    break;
            }
        }

        return super.onGenericMotionEvent(ev);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if( ! m_scroller.isFinished()){
            setScrollX(scrollX);
            setScrollY(scrollY);
            if(clampedX){
                m_scroller.springBack(getScrollX(), getScrollY(), 0, GetScrollRange(), 0,0);
            }
        }else{
            super.scrollTo(scrollX, scrollY);
        }
        awakenScrollBars();
    }


    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo (info);
        info.setScrollable ( GetScrollRange() > 0 );
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent e) {
        super.onInitializeAccessibilityEvent(e);

        e.setScrollable ( GetScrollRange() > 0 );
        e.setScrollX ( getScrollX() );
        e.setScrollY( getScrollY() ) ;
        e.setMaxScrollX(GetScrollRange());
        e.setMaxScrollY( getScrollY() );
    }


    private int GetScrollRange(){
        int scroll_range = 0;
        if(getChildCount() > 0){
            View child  = getChildAt(0);
            scroll_range = Math.max(0, child.getWidth() - ( this.getWidth() - this.getPaddingLeft() - this.getPaddingRight() ) );
        }
        return scroll_range;
    }

    @Override
    protected int computeHorizontalScrollRange() {

        int count = getChildCount();
        int content_width = getWidth() - getPaddingLeft() - getPaddingRight();
        if(count == 0)
            return content_width;
        int scroll_range = getChildAt(0).getRight();
        int scroll_x = getScrollX();
        int over_scroll_right = Math.max(0, scroll_range - content_width);
        if(scroll_x < 0)
            scroll_range -= scroll_x;
        else if (scroll_x > over_scroll_right){
            scroll_range += scroll_x - over_scroll_right;
        }
        return scroll_range;

    }

    @Override
    protected int computeHorizontalScrollOffset() {
        return Math.max (0, super.computeHorizontalScrollOffset () );
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int child_width_measure_spec, child_height_measure_spec;
        child_height_measure_spec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height );
        child_width_measure_spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int child_height_measure_spec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed, lp.height );
        int child_width_measure_spec = MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        child.measure(child_width_measure_spec, child_height_measure_spec );
    }

    @Override
    public void computeScroll() {
        if(m_scroller.computeScrollOffset()){
            int old_x = getScrollX();
            int old_y = getScrollY();
            int x = m_scroller.getCurrX();
            int y = m_scroller.getCurrY();
            if(old_x != x || old_y != y){
                int range = GetScrollRange();
                int over_scroll_mode = this.getOverScrollMode();
                boolean can_over_scroll = ( ( over_scroll_mode == View.OVER_SCROLL_ALWAYS) || (over_scroll_mode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0) );
                overScrollBy(x - old_x, y - old_y, old_x, old_y, range, 0, m_overfling_distance, 0, false);
                onScrollChanged(getScrollX(), getScrollY(), old_x, old_y);
                if( can_over_scroll){
                    if( x < 0 && old_x >= 0){
                        m_edge_glow_left.onAbsorb( ( int) m_scroller.getCurrVelocity());
                    }else if (x > range && old_x <= range){
                        m_edge_glow_right.onAbsorb( (int ) m_scroller.getCurrVelocity());
                    }
                }
            }
            awakenScrollBars();
            postInvalidate();
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        scrollTo(getScrollX(), getScrollY());

    }

    public void Fling(int velocity_x){
        if(getChildCount() > 0){
            int width = getWidth() - getPaddingRight() - getPaddingLeft();
            int right = getChildAt(0).getWidth();
            m_scroller.fling(getScrollX(), getScrollY(), velocity_x, 0, 0, Math.max(0, right - width), 0, 0 , width / 2, 0);
            postInvalidate();
        }
    }


    @Override
    public void scrollTo(int x, int y) {
        if(getChildCount() > 0){
            View child = getChildAt(0);
            x = Clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
            y = Clamp(y, getHeight() - getPaddingTop() - getPaddingBottom(), child.getHeight());
            if(x != getScrollX() || y != getScrollY()){
                super.scrollTo (x, y);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(m_edge_glow_left != null){
            int scroll_x = getScrollX();
            if( ! m_edge_glow_left.isFinished() ){
                int restore_count = canvas.save();
                int height = getHeight() - getPaddingTop() - getPaddingBottom();
                canvas.rotate(270);
                canvas.translate(-height + getPaddingTop(), Math.min(0, scroll_x ) );
                m_edge_glow_left.setSize(height, getWidth());
                if( m_edge_glow_left.draw(canvas) )
                    invalidate();
                canvas.restoreToCount(restore_count);
            }
            if(! m_edge_glow_right.isFinished()){
                int restore_count = canvas.save();
                int width = getWidth();
                int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(90);
                canvas.translate( - getPaddingTop(), - ( Math.max( GetScrollRange(), scroll_x)  + width ) ) ;
                m_edge_glow_right.setSize(height, width);
                if(m_edge_glow_right.draw(canvas) )
                    invalidate();
                canvas.restoreToCount(restore_count);
            }
        }
    }
    private int Clamp( int n, int my, int child){
        if(my >= child  || n < 0)
            return 0;
        if( ( my + n) > child )
            return child - my;
        return n;
    }


}
