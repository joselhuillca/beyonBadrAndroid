package com.badr.nwes.beyondbadr.index;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class ScrollViewSynchronizer implements ViewPagerListener, HorScrollViewListener {

    ObservableHorViewPager m_obs_hor_view_pager;

    ObservableHorScrollView m_obs_hor_scroll_view;

    int x_down_last = 0;

    int first_down_x = -1;

    public ScrollViewSynchronizer(ObservableHorViewPager obs_hor_view_pager, ObservableHorScrollView obs_hor_scroll_view) {

        m_obs_hor_view_pager = obs_hor_view_pager;

        m_obs_hor_scroll_view = obs_hor_scroll_view;

        m_obs_hor_view_pager.SetupListener(this);

        m_obs_hor_view_pager.SetHorScrollViewToManip(m_obs_hor_scroll_view);

        m_obs_hor_scroll_view.SetupListener(this);
        m_obs_hor_scroll_view.SetHorViewPagerToManip(m_obs_hor_view_pager);

        m_obs_hor_view_pager.SetRefObsHorScrollView(m_obs_hor_scroll_view);
        m_obs_hor_scroll_view.SetRefObsHorViewPager(m_obs_hor_view_pager);

    }

    @Override
    public boolean onHorScrollChanged (ObservableHorScrollView scrollView, ObservableHorViewPager h_scroll_view_to_manip, int x, int y, int oldx, int oldy)
    {


        //loga.Text = x.ToString () + " " + oldx.ToString ();
        if(scrollView.Touched() == false)
            return false;
        int current_page = h_scroll_view_to_manip.getCurrentItem();

        int screen_to_scroll = scrollView.TestPosition (x, current_page);

        if (screen_to_scroll != 0 ) {
            int direction = (screen_to_scroll > 0) ? 0 : 1;
            if(direction == 1  && current_page <= 0 )   return false;


            int lower_limit_x = scrollView.GetLowerLimitPage (current_page - direction);
            ////Log.Info("llegue", "llegue: " + direction + " " + current_page);
            int position_x = 0 ;
            //loga.Text = x.ToString () + " " + oldx.ToString () + " " + position_x.ToString ();
            ////Log.Info ("ahora", "position_x :  " + scrollView.EndPositionPage(current_page) + " " + x.ToString() );

            if(scrollView.SpecialMode() == true){
                if(current_page <= 0 )  return false;

                //Log.Info("asdf" , "special_mode - current_page = " + current_page);
                int delta_mov = scrollView.EndPositionPage(current_page - 1) - x_down_last;
                int x_up_last = h_scroll_view_to_manip.GetLowerLimitPage(current_page ) - delta_mov;
                int total_dist_up = x_up_last - h_scroll_view_to_manip.GetLowerLimitPage(current_page - 1);
                int total_dist_down = x_down_last - scrollView.InitPositionPage(current_page - 1);
                int curr_dist_down = x - scrollView.InitPositionPage(current_page - 1);
                if(total_dist_down == 0)   return false;

                int curr_dist_up = ( curr_dist_down * total_dist_up ) / total_dist_down;
                position_x = h_scroll_view_to_manip.GetLowerLimitPage(current_page - 1) + curr_dist_up;
                ////Log.Info ( "ahora", "case: " + curr_dist_up + " " + position_x);

                h_scroll_view_to_manip.scrollTo (position_x, 0);
            }else{
                position_x = h_scroll_view_to_manip.GetLowerLimitPage(current_page - direction) + x - lower_limit_x;

                h_scroll_view_to_manip.scrollTo (position_x, 0);
                x_down_last = x;
                ////Log.Info ( "ahora", "specialx " + " " + x_down_last);
            }
            if(screen_to_scroll > 0 && scrollView.EndPositionPage(current_page) == x){
                h_scroll_view_to_manip.setCurrentItem(current_page + 1, false); //todo: remove scroll

            }else if (screen_to_scroll < 0 && scrollView.InitPositionPage(current_page - 1) == x){
                ////Log.Info("up" , "final");
                h_scroll_view_to_manip.setCurrentItem(current_page - 1, false); //todo: remove scroll
            }
            ////Log.Info ("ahora", "position_x :  " + scrollView.EndPositionPage(current_page) + " " + x.ToString() + " " + h_scroll_view_to_manip.CurrentItem );
        }
        scrollView.invalidate();
        h_scroll_view_to_manip.invalidate();
        return false;

    }

    @Override
    public void onViewPagerScrollChanged(ObservableHorViewPager view_pager_to_listen, ObservableHorScrollView scroll_view_to_manip, int x, int y, int oldx, int oldy) {
        if(scroll_view_to_manip.Touched() == true)
            return;

        final int current_page = scroll_view_to_manip.CurrentPage.getHolder().get();
        int up_low_limit = view_pager_to_listen.GetLowerLimitPage(current_page);
        int up_upper_limit = view_pager_to_listen.GetUpperLimitPage(current_page);
        int total_dist_up = up_upper_limit - up_low_limit;
        int total_dist_down = 0;
        int curr_dist_up = 0;
        int curr_dist_down = 0;
        int x_down_to_scroll = 0;
        if(first_down_x == -1) {
            first_down_x = scroll_view_to_manip.getScrollX();
            ////Log.Info("mmmmm" , "first_down_x: " + first_down_x );
        }

        if( x >= up_low_limit){

            total_dist_down = scroll_view_to_manip.EndPositionPage(current_page) - first_down_x;
            curr_dist_up = x - up_low_limit ;
            curr_dist_down = (total_dist_down * curr_dist_up ) / total_dist_up;
            x_down_to_scroll = curr_dist_down + first_down_x;
            scroll_view_to_manip.scrollTo(x_down_to_scroll, scroll_view_to_manip.getScrollY());

            if(x >= up_upper_limit) {
                first_down_x = -1;
                scroll_view_to_manip.CurrentPage.getHolder().getAndIncrement();
                ////Log.Info("mmm" , "llegue");
            }
        }else{

            total_dist_up = -total_dist_up;
            total_dist_down = scroll_view_to_manip.InitPositionPage(current_page - 1) - first_down_x;
            curr_dist_up = x - up_low_limit;
            curr_dist_down = (total_dist_down * curr_dist_up ) / total_dist_up;
            x_down_to_scroll = curr_dist_down + first_down_x;
            ////Log.Info("mmm" , "x: " + x + "   - low_limit: " + up_low_limit + "  down: " + x_down_to_scroll);
            scroll_view_to_manip.scrollTo(x_down_to_scroll, scroll_view_to_manip.getScrollY());
            if(x <= view_pager_to_listen.GetLowerLimitPage(current_page - 1)){
                first_down_x = -1;
                scroll_view_to_manip.CurrentPage.getHolder().getAndDecrement();
                ////Log.Info("mmm" , "llegue");
            }
        }
        if (x == up_low_limit){
            first_down_x = -1;
            ////Log.Info("mmm" , "llegue2");
        }
        view_pager_to_listen.invalidate();
        scroll_view_to_manip.invalidate();
    }

 }
