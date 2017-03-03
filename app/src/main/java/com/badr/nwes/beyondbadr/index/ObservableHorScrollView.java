package com.badr.nwes.beyondbadr.index;

import android.content.Context;


interface HorScrollViewListener
{
    boolean onHorScrollChanged(ObservableHorScrollView scrollView, ObservableHorViewPager h_scroll_view_to_manip, int x, int y, int oldx, int oldy);
}

/**
 * Created by Kelvin on 2016-10-14.
 */
public class ObservableHorScrollView extends InfScrollPaged {

    boolean bBreak = false;

    private HorScrollViewListener scrollViewListener = null;

    private ObservableHorViewPager h_scroll_view_to_manip = null;

    public ObservableHorScrollView(Context context, int size_widht, int size_height)
    {
        super(context, size_widht, size_height);
    }

    public void SetupListener(HorScrollViewListener scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }
    public void SetHorViewPagerToManip(ObservableHorViewPager h_scroll_view_to_manip)
    {
        this.h_scroll_view_to_manip = h_scroll_view_to_manip;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        bBreak = false;
        if (scrollViewListener != null && h_scroll_view_to_manip != null)
        {
            bBreak = scrollViewListener.onHorScrollChanged(this, h_scroll_view_to_manip, x, y, oldx, oldy);
        }
       // if (!bBreak)
          super.onScrollChanged(x, y, oldx, oldy);

    }

    @Override
    public boolean bBreak() {
        return  bBreak;
    }

    @Override
    public ObservableHorViewPager getPager () {
        return h_scroll_view_to_manip;
    }

}
