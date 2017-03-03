package com.badr.nwes.beyondbadr.index;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.badr.nwes.beyondbadr.ProportionCalculator;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.BookDataSource;
import com.badr.nwes.beyondbadr.data.ChapterDataSource;
import com.badr.nwes.beyondbadr.data.PListLoader;
import com.badr.nwes.beyondbadr.data.PageDataSource;
import com.badr.nwes.beyondbadr.data.SectionDataSource;
import com.badr.nwes.beyondbadr.reader.content.CustomHorizontalScrollView;
import com.badr.nwes.beyondbadr.reader.content.CustomImageView;
import com.badr.nwes.beyondbadr.reader.content.DataSourceMapper;
import com.badr.nwes.beyondbadr.FirstLevelActivity;
import com.badr.nwes.beyondbadr.reader.content.StackBorderLoader;
import com.badr.nwes.beyondbadr.reader.FullScreenAnimatedActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

class Timer extends CountDownTimer {
    InfScrollPaged scroll_;
    final AtomicInteger how_many_ = new AtomicInteger();
    final AtomicInteger init_position_ = new AtomicInteger();
    long total_time_;
    final AtomicLong last_ran_ = new AtomicLong(0);
    BoolHolder special_mode_ ;
    BoolHolder touched_;
    IntHolder current_page_;
    final AtomicInteger inc_ = new AtomicInteger();

    public Timer(long totaltime, long interval, InfScrollPaged scrool_input)
    {
        super(totaltime,interval);
        total_time_ = totaltime;
        scroll_=scrool_input;
        special_mode_ = null;
    }

    public Timer(long totaltime, long interval, InfScrollPaged scrool_input, BoolHolder special_mode, BoolHolder touched)
    {
        super(totaltime,interval) ;
        total_time_ = totaltime;
        scroll_=scrool_input;
        special_mode_ = special_mode;
        touched_ = touched;
    }
    public void SetInitialPositionAndDistanceToScroll(int how_many, int init_position, IntHolder current, int inc){


        how_many_.getAndSet(how_many);
        init_position_.getAndSet(init_position);
        current_page_ = current;
        inc_.getAndSet(inc);

    }

    public void ResetLastRun(){
        last_ran_.getAndSet(0);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long current_run = ( ( how_many_.get() * (total_time_ - millisUntilFinished) ) / total_time_);
        scroll_.scrollBy ( (int)(current_run - last_ran_.get()) , 0);
        last_ran_.getAndSet(current_run);
    }

    @Override
    public void onFinish() {
        scroll_.scrollTo( how_many_.get() + init_position_.get(), 0);
        last_ran_.getAndSet(0);

        if(current_page_ != null ) {
            this.current_page_.getHolder().getAndSet(this.current_page_.getHolder().get() + inc_.get());
        }
        else {
            try {
                throw new Exception("onFinish : NUll current_page");
            }catch (Exception e) {

            }
        }
        if(touched_ != null)
            touched_.getHolder().getAndSet(false);
        if(special_mode_ != null )
            special_mode_.getHolder().getAndSet(false);

    }
}


class BoolHolder {
    private final AtomicBoolean holder = new AtomicBoolean();

    public BoolHolder(boolean holder){
        this.getHolder().set(holder);
    }


    public AtomicBoolean getHolder() {
        return holder;
    }
}

class IntHolder{
    private final AtomicInteger holder = new AtomicInteger();

    public IntHolder(int holder){
        this.getHolder().set(holder);
    }

    public AtomicInteger getHolder() {
        return holder;
    }

}

/**
 * Created by Kelvin on 2016-10-14.
 */
public class InfScrollPaged extends CustomHorizontalScrollView implements  GestureDetector.OnGestureListener
{
    public static final BookDataSource book = new BookDataSource();

    private HorizontalViewPager m_ref_hor_view_pager=null;

    private BoolHolder m_touched = new BoolHolder(false);

    public boolean Touched (){

        return m_touched.getHolder().get();
    }

    private GestureDetector gesture_detector_;

    protected IntHolder CurrentPage = new IntHolder(0);
    //return this.CurrentPage;

    private LinearLayout main_layout_;
    private int[]init_pos_chapter_;
    private int[]end_pos_chapter_;
    private int temp_n_childs=0;

    private final AtomicReference<Timer> timer_ = new AtomicReference<Timer>();
    private int scale_dx_=0;
    private int mMaximumVelocity;
    private int mDensityAdjustedSnapVelocity;
    private BoolHolder special_mode_=new BoolHolder(false);
    private int num_of_chapters;

    private final ThreadLocal<Boolean> is_flinging = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private Intent intent_full_scroll;

    // Argument to getVelocity for units to give pixels per second (1 = pixels per millisecond).
    public final int ANIMATION_TIME_IN_MS=250;

    public final int VELOCITY_UNIT_PIXELS_PER_MS=1;
            /*
             * Velocity of a swipe (in density-independent pixels per second) to force a swipe to the
             * next/previous screen. Adjusted into mDensityAdjustedSnapVelocity on init.
             */
    public final int SNAP_VELOCITY_DIP_PER_SECOND=600;


    public InfScrollPaged(Context context, int size_widht, int size_height)
    {
        super(context);
        this.init(context,size_widht,size_height);
    }

    public void init(Context context, int size_widht, int size_height)
    {
        //Log.Info("Initializing", "Estoy siendo inicializadoooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        intent_full_scroll = new Intent(context,  FullScreenAnimatedActivity.class );

        timer_.set(new Timer(ANIMATION_TIME_IN_MS, 5, this, special_mode_, m_touched));
        CurrentPage.getHolder().getAndSet(0);
        m_touched.getHolder().getAndSet(false);
        float density;
        density = context.getResources().getDisplayMetrics().density;

        gesture_detector_ = new GestureDetector(context, this);

        mDensityAdjustedSnapVelocity = (int)(density * SNAP_VELOCITY_DIP_PER_SECOND);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity() / 1000;

        this.setLayoutParams( new LinearLayout.LayoutParams (size_widht, size_height) );
        main_layout_ = new LinearLayout (context);
        main_layout_.setOrientation( LinearLayout.HORIZONTAL ) ;
        main_layout_.setLayoutParams( new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT) );

        PListLoader loader = new PListLoader(context);

        ArrayList<ChapterDataSource> chapters = new ArrayList<ChapterDataSource>();

        //Log.Info("cargado de chapters: " , "chapter: " + i );
        for (int i = 0; i < 6; i++) {
            ChapterDataSource chapter = loader.LoadChapterDataSource("", "chapter" + (i + 1) + ".xml");
            chapters.add(chapter);
        }

            //chapters.Add(chapter)
				/*
				chapters.Add(chapter);
				chapters.Add(chapter);
				chapters.Add(chapter);
				chapters.Add(chapter);

				chapters.Add(chapter);*/


        book.Chapters = chapters;

        num_of_chapters = book.Chapters.size();

        StackBorderLoader stack_border_bitmap_gen = new StackBorderLoader(context);
        // Two variables , the range of a page X will be between [init_pos_chapter[X] , end_pos_chapter > (closed , open);
        init_pos_chapter_ = new int[num_of_chapters];
        end_pos_chapter_ = new int[num_of_chapters];

        scale_dx_ = (int) ( (1.0 * size_widht) / SystemConfiguration.NUMBER_OF_THUMBNAILS_IN_SCREEN );
        int cont = 0;


        for(int ind_chapter = 0; ind_chapter < num_of_chapters ;  ind_chapter++){
            ChapterDataSource ch = book.Chapters.get(ind_chapter);
            LinearLayout chapter_layout = new LinearLayout(context);
            chapter_layout.setOrientation(LinearLayout.HORIZONTAL );
            chapter_layout.setLayoutParams( new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT) );
            int num_of_sections = ch.Sections.size();
            if(ind_chapter > 0) init_pos_chapter_[ind_chapter] = end_pos_chapter_[ind_chapter - 1];
            if(ind_chapter > 0) end_pos_chapter_[ind_chapter] = end_pos_chapter_[ind_chapter - 1];
            for(int ind_section = 0 ; ind_section <num_of_sections; ind_section++){
                SectionDataSource sec =  ch.Sections.get(ind_section);
                int number_r =sec.Pages.size();

                LinearLayout section_layout = new LinearLayout(context);
                section_layout.setOrientation(  LinearLayout.HORIZONTAL );
                section_layout.setLayoutParams( new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT) );
                end_pos_chapter_[ind_chapter] += (number_r * scale_dx_);
                for(int ind_image = 0; ind_image <number_r ;  ind_image++){
                    PageDataSource page = sec.Pages.get(ind_image);
                    CustomImageView image = new CustomImageView(context);

                    if(ind_chapter == 0 ) temp_n_childs += scale_dx_;
                    image.setLayoutParams(new LinearLayout.LayoutParams( scale_dx_ , LinearLayout.LayoutParams.MATCH_PARENT));
                    //image.SetImageResource( thumbIds[ind_chapter * 2 + ind_section] );

                    try {
                        InputStream input_image = context.getAssets().open(page.ThumbSource);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(page.ThumbSource, scale_dx_,size_height, context);
                        Bitmap copy_border = stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
                            Canvas comboImage = new Canvas(copy_border);
                            // Then draw the second on top of that

                            float [] boundaries = new float[4];

                            ProportionCalculator.CalculeImageBoundaries(copy_border.getWidth(), copy_border.getHeight(), boundaries);
                            comboImage.drawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight()), new RectF(boundaries[0], boundaries[1], copy_border.getWidth() - boundaries[2], copy_border.getHeight() - boundaries[3]), null);
                            //comboImage.DrawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.Width, input_image_bitmap.Height), new RectF(23, 23, copy_border.Width -23, copy_border.Height - 30), null);
                            ////Log.Info("cargado de datos", "imagen: " + ind_chapter + " " + ind_section + " " + ind_image);

                    image.setImageBitmap(copy_border);


                    //Log.Info("mm", "image matrix: " + image.ImageMatrix);
                    PageIndex pageIndex = new PageIndex();
                    pageIndex.chapter = ind_chapter;
                    pageIndex.section = ind_section;
                    pageIndex.page = ind_image;
                    pageIndex.width = scale_dx_;
                    pageIndex.height = size_height;
                    pageIndex.pos_x = end_pos_chapter_[ind_chapter] - (( number_r - ind_image) * scale_dx_);
                    pageIndex.unique_id = cont;
                    image.setTag( pageIndex );
                    if (ind_chapter == 0) {

                        image.setAlpha(0.0f); //todo : chapter 0


                    }
                    //image.Click += new EventHandler(OnClick);

                    section_layout.addView(image);
                    cont++;
                }
                chapter_layout.addView(section_layout);
            }
            main_layout_.addView(chapter_layout);
        }
        ///////////////////////////////////////////////////////////////////
        this.addView(main_layout_);
    }

    public void SetRefObsHorViewPager(HorizontalViewPager ref_hor_view_pager){
        m_ref_hor_view_pager = ref_hor_view_pager;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        is_flinging.set(false);
        if(m_ref_hor_view_pager != null && m_ref_hor_view_pager.Touched() == true)
            return true;
        gesture_detector_.onTouchEvent(event);

        m_touched.getHolder().getAndSet(true);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.Info("up" , "action down? : ");
                m_touched.getHolder().getAndSet(true);
                timer_.get().cancel();
                timer_.get().ResetLastRun();
                break;
            case MotionEvent.ACTION_MOVE:
                m_touched.getHolder().getAndSet(true);
                timer_.get().cancel();
                timer_.get().ResetLastRun();
                break;

            case MotionEvent.ACTION_UP:
                //Log.Info("up" , "action up? : ");
                int scrollx = this.getScrollX() ;

                int distance = 0;
                //Log.Info(">>", "Is flinging: " + is_flinging + " "  + m_scroller.FinalX + " " + m_scroller.IsFinished );
                if( is_flinging.get() == true) break;
                if( scrollx > end_pos_chapter_[this.CurrentPage.getHolder().get()] - this.getMeasuredWidth() + 1 && scrollx <  end_pos_chapter_[this.CurrentPage.getHolder().get()]) {
                    if(! m_scroller.isFinished())
                        m_scroller.abortAnimation();

                    int center =  end_pos_chapter_[this.CurrentPage.getHolder().get()] - this.getMeasuredWidth() + scale_dx_;
                    if(scrollx > center){
                        distance =  end_pos_chapter_[this.CurrentPage.getHolder().get()] - scrollx  ;
                        timer_.get().SetInitialPositionAndDistanceToScroll(distance, scrollx, this.CurrentPage, 1);
                        timer_.get().start();
                    }else{
                        distance = ( end_pos_chapter_[this.CurrentPage.getHolder().get()] - this.getMeasuredWidth() + 1) - scrollx;
                        timer_.get().SetInitialPositionAndDistanceToScroll(distance, scrollx, null, 0);
                        timer_.get().start();
                    }
                }else if (scrollx < init_pos_chapter_[this.CurrentPage.getHolder().get()] ) {
                    int center =  init_pos_chapter_[this.CurrentPage.getHolder().get()] - scale_dx_;
                    if(scrollx > center){
                        special_mode_.getHolder().getAndSet(false);
                        distance =  init_pos_chapter_[this.CurrentPage.getHolder().get()] - scrollx;
                        timer_.get().SetInitialPositionAndDistanceToScroll(distance, scrollx, null, 0);
                        timer_.get().start();
                    }else{
                        special_mode_.getHolder().getAndSet(true);
                        distance =  init_pos_chapter_[this.CurrentPage.getHolder().get() - 1]  - scrollx ;
                        timer_.get().SetInitialPositionAndDistanceToScroll(distance, scrollx, this.CurrentPage, -1);
                        timer_.get().start();
                    }
                }else
                    m_touched.getHolder().getAndSet(false);
                break;
            default : break;

        }
        return super.onTouchEvent(event);
    }


    @Override
    public int TestPosition (int scrollx, int page)
    {
        if (scrollx >=  end_pos_chapter_[page] - this.getMeasuredWidth() + 1 && scrollx <=  end_pos_chapter_[page] + 1)
            return 1;
        if(scrollx <= init_pos_chapter_[page] ) return -1;
        return 0;
    }
    public int GetLowerLimitPage (int page)
    {
        return  end_pos_chapter_[page] - this.getMeasuredWidth() + 1;
    }
    public int EndPositionPage(int page){
        //Log.Info("pageeeee", "num_page: " + page);
        return end_pos_chapter_[page];
    }
    public int InitPositionPage(int page){
        return init_pos_chapter_[page];
    }

    public boolean SpecialMode()
    {
        return special_mode_.getHolder().get();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);



        int distance = 0;

        if(is_flinging.get() == true && x > end_pos_chapter_[CurrentPage.getHolder().get()] - getMeasuredWidth() + 1){
            if(! m_scroller.isFinished()) m_scroller.abortAnimation();
            int center =  end_pos_chapter_[CurrentPage.getHolder().get()] - getMeasuredWidth() + scale_dx_;
            ////Log.Info ("current: " , "Initial Velocity: " + fling_velocity_x_ +  "  Current Velocity: " + m_scroller.CurrVelocity.ToString() );
            if(Math.abs(m_scroller.getCurrVelocity())  >  4 * mDensityAdjustedSnapVelocity || x > center){
                distance =  end_pos_chapter_[CurrentPage.getHolder().get()] - x  ;
                timer_.get().SetInitialPositionAndDistanceToScroll(distance, x, CurrentPage, 1);
                timer_.get().start();
            }else{
                distance = ( end_pos_chapter_[CurrentPage.getHolder().get()] - getMeasuredWidth() + 1) - x;
                timer_.get().SetInitialPositionAndDistanceToScroll(distance, x, null, 0);
                timer_.get().start();
            }
            is_flinging.set(false);
        }else if (is_flinging.get() == true && x < init_pos_chapter_[CurrentPage.getHolder().get()] ){
            if(! m_scroller.isFinished()) m_scroller.abortAnimation();
            int center =  init_pos_chapter_[CurrentPage.getHolder().get()] - scale_dx_;
            float current_velocity = Math.abs(m_scroller.getCurrVelocity());
            if( ( current_velocity <=  4 * mDensityAdjustedSnapVelocity && x > center) ){
                distance =  init_pos_chapter_[CurrentPage.getHolder().get()] - x;
                timer_.get().SetInitialPositionAndDistanceToScroll(distance, x, null, 0);
                timer_.get().start();
            }else{
                special_mode_.getHolder().getAndSet(true);
                distance =  init_pos_chapter_[CurrentPage.getHolder().get()]  - x ;
                timer_.get().SetInitialPositionAndDistanceToScroll(distance, x, CurrentPage, -1);
                timer_.get().start();
            }
            is_flinging.set(false);
        }else if(is_flinging.get() == true && x == m_scroller.getFinalX()){
            //Log.Info(",,,", "llegue al final x!! " + m_scroller.FinalX + " " + x );
            m_touched.getHolder().getAndSet(false);
            is_flinging.set(false);
        }
    }


    public int SnapToItem(int chapter, int section, int image_id){
        String key_csi = chapter + "_" + section + "_" + image_id;
        int position = DataSourceMapper.MAPPER_CSI.get(key_csi);
        return SnapToItem(position);
    }

    public int SnapToItem(int position_item){
        if (DataSourceMapper.DS_LISTA_CHAPTER != null) FirstLevelActivity.gallery_img.setCurrentItem(DataSourceMapper.DS_LISTA_CHAPTER[position_item], false);
        if(DataSourceMapper.DS_LISTA_CHAPTER == null) return 0;
        int position_tablet = 0;
        int num_chapter = DataSourceMapper.DS_LISTA_CHAPTER[position_item];
        CurrentPage.getHolder().getAndSet(num_chapter);
        int position_x = position_item * scale_dx_;
        int position_to_scroll = position_x - this.getMeasuredWidth() / 2 + scale_dx_ / 2;
        int scroll_direction = TestPosition(position_to_scroll, num_chapter);
        if(scroll_direction == 0){
            position_tablet = this.getMeasuredWidth() / 2 - scale_dx_ / 2;
            this.scrollTo(position_to_scroll, this.getScrollY());
        }else{
            //Log.Info("InfScrollPaged", "InfScrollPaged, need more calculations!: " + position_to_scroll + " " + num_chapter + " " + position_item);
            if(scroll_direction == 1){
                position_tablet = getMeasuredWidth() - (end_pos_chapter_[num_chapter] - position_item * scale_dx_ );
                this.scrollTo(end_pos_chapter_[num_chapter] - getMeasuredWidth(), this.getScrollY());
            }else{
                position_tablet = position_item * scale_dx_ - init_pos_chapter_[num_chapter];
                this.scrollTo(init_pos_chapter_[num_chapter], this.getScrollY());
            }
        }
        return position_tablet;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        is_flinging.set(true);
        int scrollx = getScrollX();
        m_scroller.fling (scrollx,getScrollY(), (int) (-velocityX /10) ,  0, 0, end_pos_chapter_[num_of_chapters - 1] - getMeasuredWidth(), getScrollY(), getScrollY());
        if(m_scroller.getFinalX()<= scrollx + 2 && m_scroller.getFinalX() >= scrollx - 2 ){
            is_flinging.set(false);
        }


        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }


    public void SetImageAlpha(int position, int alpha){
        LinearLayout main_layout = (LinearLayout) this.getChildAt(0);
        LinearLayout chapter_layout = (LinearLayout) main_layout.getChildAt(DataSourceMapper.DS_LISTA_CHAPTER[position]);
        LinearLayout section_layout = (LinearLayout) chapter_layout.getChildAt(DataSourceMapper.DS_LISTA_SECTION[position]);
        CustomImageView custom_image = (CustomImageView) section_layout.getChildAt(DataSourceMapper.DS_LISTA_IMAGE[position] );
        custom_image.setImageAlpha(alpha );
    }

}
