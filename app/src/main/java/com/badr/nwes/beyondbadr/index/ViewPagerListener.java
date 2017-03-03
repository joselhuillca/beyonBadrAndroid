package com.badr.nwes.beyondbadr.index;

/**
 * Created by Kelvin on 2016-10-14.
 */
interface ViewPagerListener
{
    void onViewPagerScrollChanged(ObservableHorViewPager view_pager_to_listen, ObservableHorScrollView scroll_view_to_manip, int x, int y, int oldx, int oldy);

}
