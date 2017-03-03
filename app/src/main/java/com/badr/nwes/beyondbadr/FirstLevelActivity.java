package com.badr.nwes.beyondbadr;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.NSFrame;
import com.badr.nwes.beyondbadr.index.GalleryGui;
import com.badr.nwes.beyondbadr.index.ObservableHorScrollView;
import com.badr.nwes.beyondbadr.index.ObservableHorViewPager;
import com.badr.nwes.beyondbadr.index.ScrollViewSynchronizer;
import com.microsoft.xbox.toolkit.SoundManager;
import com.microsoft.xbox.toolkit.ThreadManager;

import java.util.ArrayList;

/**
 * Created by Kelvin on 2016-10-07.
 */
public class FirstLevelActivity extends Activity {

    public static ObservableHorViewPager gallery_img;

    public static ObservableHorScrollView scroll_img;

    ScrollViewSynchronizer m_synchronizer;

    ArrayList<Button> menuButtons = new ArrayList<Button>();

    public static int PAGE_TO_GO = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();

        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();

        RelativeLayout rel_layout = new RelativeLayout(this);
        rel_layout.setLayoutParams( new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        GalleryGui gallery_gui = new GalleryGui(this, img_big_sz_x_, img_big_sz_y_, true);
        gallery_img = gallery_gui.GetGalleryLayout();

        scroll_img = new ObservableHorScrollView(this, img_big_sz_x_ , img_big_sz_y_/  SystemConfiguration.SCREEN_HEIGHT_DIVIDER);
        scroll_img.setX(0);
        scroll_img.setY( 2 * img_big_sz_y_ / SystemConfiguration.SCREEN_HEIGHT_DIVIDER - SystemConfiguration.SCREEN_HEIGHT_OFFSET);

        m_synchronizer = new ScrollViewSynchronizer(gallery_img, scroll_img);

        rel_layout.addView(gallery_img);
        rel_layout.addView(scroll_img);

        NSFrame base_screen_desc =   new NSFrame(0, 0, 1600, 900);

        NSFrame base_screen_2 =   new NSFrame(0, 0, 1366, 768);

        NSFrame chapterButtonFrame =  new NSFrame(280, 817, 220, 95);
        NSFrame menuFrame = new NSFrame(78, 130, 885, 55);
        NSFrame logoFrame = new NSFrame(1160, 115, 70, 70);


        int textr_y = (chapterButtonFrame.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);

        int textlugar_w = (chapterButtonFrame.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int textlugar_h = (chapterButtonFrame.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int menu_w = (menuFrame.Width * img_big_sz_x_) / (base_screen_2.Width);
        int menu_h = (menuFrame.Height * img_big_sz_y_) / (base_screen_2.Height);

        final Button menuBnt = new Button(this);

        menuBnt.setLayoutParams(new RelativeLayout.LayoutParams(menu_w, menu_h));

        menuBnt.setText("");
          menuBnt.setBackgroundResource( R.drawable.menu0);
        //readButton2.setAlpha(0.75f);

        menuBnt.setX(  (menuFrame.X * img_big_sz_x_) / (base_screen_2.Width)  );
        menuBnt.setY(  (menuFrame.Y * img_big_sz_x_) / (base_screen_2.Width) );

        menuBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gallery_img.setCurrentItem(index,true);
                ThreadManager.UIThreadPost(new Runnable() {
                    public void run() {
                        SoundManager.getInstance().playSound(R.raw.sndbuttonbackandroid);
                    }
                });
                onBackPressed();
            }
        });
        rel_layout.addView(menuBnt);

/*        Button logBnt = new Button(this);
        int logo_w = (logoFrame.Width * img_big_sz_x_) / (base_screen_2.Width);
        int logo_h = (logoFrame.Height * img_big_sz_y_) / (base_screen_2.Height);


        logBnt.setLayoutParams(new RelativeLayout.LayoutParams(logo_w, logo_h));

        logBnt.setText("");
        logBnt.setBackgroundResource( R.drawable.icon);
        //readButton2.setAlpha(0.75f);

        logBnt.setX(  (logoFrame.X * img_big_sz_x_) / (base_screen_2.Width)  );
        logBnt.setY(  (logoFrame.Y * img_big_sz_x_) / (base_screen_2.Width) );

        logBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gallery_img.setCurrentItem(index,true);
                ThreadManager.UIThreadPost(new Runnable() {
                    public void run() {
                        SoundManager.getInstance().playSound(R.raw.sndbuttonbackandroid);
                    }
                });
                onBackPressed();
            }
        });
        rel_layout.addView(logBnt);*/

        String []buttonNames = new String[]{"About", "Characters", "Comic", "Art"};
        for (int i = 0; i  < buttonNames.length; i++) {
            final int index = i;
            Button readButton2 = new Button(this);

            readButton2.setLayoutParams(new RelativeLayout.LayoutParams(textlugar_w, textlugar_h));

            readButton2.setText(buttonNames[i]);
            readButton2.setTextColor(Color.parseColor("white"));
            readButton2.setTextSize (TypedValue.COMPLEX_UNIT_PX, SystemConfiguration.getHeight (28) );
            //readButton2.setBackgroundColor(Color.parseColor("black"));
            readButton2.setBackgroundResource( R.drawable.button);
            //readButton2.setAlpha(0.75f);

            readButton2.setX(textlugar_w * index);
            readButton2.setY(textr_y);

            readButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThreadManager.UIThreadPost(new Runnable() {
                        public void run() {
                            SoundManager.getInstance().playSound(R.raw.sndbuttonselectandroid);
                        }
                    });

                    for (Button b : menuButtons)
                        b.setBackgroundResource(R.drawable.button);
                    if (index == 0) {
                        menuBnt.setBackgroundResource( R.drawable.menu0);
                        menuButtons.get(index).setBackgroundResource(R.drawable.menubnt0);
                    }
                    if (index == 1) {
                        menuBnt.setBackgroundResource( R.drawable.menu1);
                        menuButtons.get(index).setBackgroundResource(R.drawable.menubnt1);
                    }
                    if (index == 2){
                        menuBnt.setBackgroundResource( R.drawable.menu2);
                        menuButtons.get(index).setBackgroundResource(R.drawable.menubnt2);
                    }
                    if (index == 3) {
                        menuBnt.setBackgroundResource( R.drawable.menu3);
                        menuButtons.get(index).setBackgroundResource(R.drawable.menubnt3);
                    }
                    gallery_img.setCurrentItem(index,true);
                }
            });
            rel_layout.addView(readButton2);
            menuButtons.add(readButton2);
        }
        menuButtons.get(0).setBackgroundResource(R.drawable.menubnt0);


        gallery_img.setCurrentItem(0,false);
        gallery_img.setOffscreenPageLimit(6);
        //Log.Info ("a", "width " + this.img_big_sz_x_ + "   h: " + this.img_big_sz_y_);

        setContentView(rel_layout);

        //Intent intent_full_scroll = new Intent(this, PuzzleActivity.class );
        //this.startActivity(intent_full_scroll);
    }

    protected void onResume ()
    {
        SystemConfiguration.FIRST_TO_SECOND_LEVEL_ACTIVITY_FLAG = 0;
        super.onResume ();
    }

    /*public void onBackPressed ()
    {
        Intent intent_full_scroll = new Intent(this, PuzzleActivity.class );
        this.startActivity(intent_full_scroll);
    }*/
}
