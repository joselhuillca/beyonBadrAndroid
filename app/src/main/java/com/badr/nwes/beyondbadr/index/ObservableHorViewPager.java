package com.badr.nwes.beyondbadr.index;

import android.content.Context;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class ObservableHorViewPager extends HorizontalViewPager {

    private ViewPagerListener m_view_pager_listerner = null;

    private ObservableHorScrollView m_hor_scroll_view_to_manip = null;

    public ObservableHorViewPager(Context context)
    {
        super(context);
    }

    public void SetupListener(ViewPagerListener view_pager_listener){
        m_view_pager_listerner = view_pager_listener;
    }

    public void SetHorScrollViewToManip(ObservableHorScrollView hor_scroll_view_to_manip){
        m_hor_scroll_view_to_manip = hor_scroll_view_to_manip;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if( m_view_pager_listerner != null && m_hor_scroll_view_to_manip != null){
            m_view_pager_listerner.onViewPagerScrollChanged(this, m_hor_scroll_view_to_manip, x, y, oldx, oldy);
        }
    }
}
