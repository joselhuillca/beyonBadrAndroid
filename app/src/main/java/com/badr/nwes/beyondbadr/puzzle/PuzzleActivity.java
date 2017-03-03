package com.badr.nwes.beyondbadr.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.FirstLevelActivity;
import com.badr.nwes.beyondbadr.FirstLevelTabActivitySmartPhone;
import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import  com.badr.nwes.beyondbadr.data.*;
import com.microsoft.xbox.toolkit.SoundManager;
import com.microsoft.xbox.toolkit.ThreadManager;


public class PuzzleActivity extends Activity implements IFinalizeActivity, View.OnClickListener {

    public static PuzzleActivity Instance;
    public  SnakeGridView malla;
    public  GridItemAdapter img_adap;
    public boolean is_smartphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int widthInDp = metrics.widthPixels;
        int heightInDp = metrics.heightPixels;
        Instance = this;
        float scaleFactor  =  this.getResources().getDisplayMetrics().density;

        SystemConfiguration.setWidthPixel (widthInDp);
        SystemConfiguration.setHeigthPixel (heightInDp);
        SystemConfiguration.setDensity (scaleFactor);

        ThreadManager.UIThread = Thread.currentThread();
        ThreadManager.Handler = new Handler();

        SoundManager.getInstance().setEnabled(true);
        SoundManager.getInstance().loadSound( R.raw.sndbuttonselectandroid );
        SoundManager.getInstance().loadSound( R.raw.sndbuttonbackandroid );
        SoundManager.getInstance().loadSound( R.raw.avatartransition );

        if (SystemConfiguration.getMinDpi() > 600) {
            //Device is a 7" tablet
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            is_smartphone = false;
        }
        else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            is_smartphone = true;
        }


        int img_big_sz_x_ = this.getWindowManager().getDefaultDisplay().getWidth();

        int img_big_sz_y_ = this.getWindowManager().getDefaultDisplay().getHeight();

        //SystemConfiguration.DIMENSION_DESING_WIDTH = img_big_sz_x_;
        //SystemConfiguration.DIMENSION_DESING_HEIGHT = img_big_sz_y_;


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GridDataFrame sdsource = new GridDataFrame();
        NSFrame base_screen_desc = null;
        base_screen_desc = sdsource.getBaseScreenNSFrame();

        NSFrame grid_desc = sdsource.getGridNSFrame();
        NSFrame logo_desc = sdsource.getLogoNSFrame();
        NSFrame map_desc = sdsource.getMapNSFrame();
        NSFrame textl_desc = sdsource.getTextLeftNSFrame();
        NSFrame textr_desc = sdsource.getTextRightNSFrame();
        NSFrame textlugar_desc = sdsource.getTextLugarNSFrame();

        int grid_x = (grid_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int grid_y = (grid_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);
        int grid_w = (grid_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int grid_h = (grid_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int logo_x = (logo_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int logo_y = (logo_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);

        int logo_w = (logo_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int logo_h = (logo_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int map_x = (map_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int map_y = (map_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);
        int map_w = (map_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int map_h = (map_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int textl_x = (textl_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int textl_y = (textl_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);
        int textl_w = (textl_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int textl_h = (textl_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int textr_x = (textr_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int textr_y = (textr_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);
        int textr_w = (textr_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int textr_h = (textr_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int textlugar_x = (textlugar_desc.X * (img_big_sz_x_ - 1)) / (base_screen_desc.Width - 1);
        int textlugar_y = (textlugar_desc.Y * (img_big_sz_y_ - 1)) / (base_screen_desc.Height - 1);
        int textlugar_w = (textlugar_desc.Width * img_big_sz_x_) / (base_screen_desc.Width);
        int textlugar_h = (textlugar_desc.Height * img_big_sz_y_) / (base_screen_desc.Height);

        int aux_textlugar_h = 0;
        int buttonRead_y = SystemConfiguration.getHeight (20);
        if(is_smartphone){//Si es un SmartPhone
            map_x = 0 - SystemConfiguration.getWidth(100);
            map_y += SystemConfiguration.getHeight(50);
            //Log.d("character: ",String.format("%d, %d",base_screen_desc.Width,map_y));
            textlugar_y = map_y + SystemConfiguration.getHeight(80);;
            aux_textlugar_h += SystemConfiguration.getHeight(100);
            textl_x = textlugar_x;
            textl_y = textlugar_y + aux_textlugar_h;
            textl_w = SystemConfiguration.getHeight(300);
            textl_h = SystemConfiguration.getHeight(300);

            buttonRead_y = -SystemConfiguration.getHeight (50);
        }

        RelativeLayout rel_layout = new RelativeLayout(this);
        rel_layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        ImageView grid_image = new ImageView(this);
        grid_image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        grid_image.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView border_theme = new ImageView(this);
        border_theme.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        border_theme.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView map_theme = new ImageView(this);
        map_theme.setLayoutParams(new RelativeLayout.LayoutParams(map_w, map_h));

        ImageView logo_theme = new ImageView(this);
        logo_theme.setLayoutParams(new RelativeLayout.LayoutParams(logo_w, logo_h));

        ImageView image_bg = new ImageView(this);
        image_bg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        TextView textl_theme = new TextView(this);
        textl_theme.setLayoutParams(new RelativeLayout.LayoutParams(textl_w, textl_h));
        textl_theme.setTextSize (TypedValue.COMPLEX_UNIT_PX, SystemConfiguration.getHeight (26) );
        textl_theme.setTextColor (Color.parseColor ("#E5E5E5"));

        TextView textr_theme = new TextView(this);
        textr_theme.setLayoutParams(new RelativeLayout.LayoutParams(textr_w, textr_h));
        textr_theme.setTextSize (TypedValue.COMPLEX_UNIT_PX, SystemConfiguration.getHeight (26) );
        textr_theme.setTextColor (Color.parseColor ("#E5E5E5"));

        //LinearLayout lin_texlugar_theme = new LinearLayout(this);
        //lin_texlugar_theme.LayoutParameters = new R.LayoutParams(textlugar_w, textlugar_h);


        TextView textlugar_theme = new TextView(this);
        textlugar_theme.setLayoutParams(new RelativeLayout.LayoutParams(textlugar_w, textlugar_h+aux_textlugar_h));
        textlugar_theme.setTextSize (TypedValue.COMPLEX_UNIT_PX, SystemConfiguration.getHeight (22) );
        textlugar_theme.setTextColor (Color.parseColor ("#E5E5E5"));


         img_adap = new GridItemAdapter(this, this, grid_w, grid_h, border_theme, map_theme, logo_theme, image_bg, grid_image, textl_theme, textr_theme, textlugar_theme);

         malla = new SnakeGridView(this, grid_w, grid_h, img_adap);


        malla.setLayoutParams(new GridView.LayoutParams(grid_w, grid_h));

         malla.setAdapter(img_adap);


        //for (int i = 0; i < 5; i++)
        //    img_adap.PanAction( i  );

        malla.setNumColumns(SystemConfiguration.GRID_COLUMNS);

        malla.setX(grid_x);
        malla.setY(grid_y);

        map_theme.setX(map_x);
        map_theme.setY(map_y);

        logo_theme.setX(logo_x);
        logo_theme.setY(logo_y);

        textl_theme.setX(textl_x);
        textl_theme.setY(textl_y);

        textr_theme.setX(textr_x);
        textr_theme.setY(textr_y);
        //Log.d("character: ",textl_theme.getText().toString());

        textlugar_theme.setX(textlugar_x);
        textlugar_theme.setY(textlugar_y);
        // Create your application here
        rel_layout.addView(image_bg);
        rel_layout.addView(grid_image);

        rel_layout.addView(border_theme);
        rel_layout.addView(map_theme);//character
        rel_layout.addView(logo_theme);
        rel_layout.addView(malla);
        rel_layout.addView(textl_theme);//Description1.1 character  small
        rel_layout.addView(textr_theme);//Description2 character  full
        rel_layout.addView(textlugar_theme);//Description1 character  small


        Button readButton = new Button(this);
        readButton.setLayoutParams(new RelativeLayout.LayoutParams(textlugar_w*2/3, (int)(textlugar_h*1.7f)));

        readButton.setText("");
        readButton.setBackgroundResource( R.drawable.comic_button);
        readButton.setX(   SystemConfiguration.getHeight (20) );
        readButton.setY(textr_y + buttonRead_y );
        rel_layout.addView(readButton);

        readButton.setOnClickListener(this);
        //android:id="@+id/button1"
       // android:layout_width="wrap_content"
        //android:layout_height="wrap_content"
        //android:text="Button"
        //android:background="@drawable/one" />


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        rel_layout.setBackground( SystemConfiguration.getDrawableResource( this, R.drawable.background) );


        setContentView(rel_layout);

        //setContentView(R.layout.activity_main);

        malla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ThreadManager.UIThreadPostDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_adap.PanAction(position);
                    }
                }, id * 100);
            }
        });

    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Boton read!", Toast.LENGTH_LONG).show();
        if(!is_smartphone) {
            Intent intent_full_scroll = new Intent(this, FirstLevelActivity.class);
            ThreadManager.UIThreadPost(new Runnable() {
                public void run() {
                    SoundManager.getInstance().playSound(R.raw.sndbuttonselectandroid);
                }
            });
            this.startActivity(intent_full_scroll);
        }
        else{
            Intent intent_full_scroll_smatphone = new Intent(this, FirstLevelTabActivitySmartPhone.class);
            ThreadManager.UIThreadPost(new Runnable() {
                public void run() {
                    SoundManager.getInstance().playSound(R.raw.sndbuttonselectandroid);
                }
            });
            this.startActivity(intent_full_scroll_smatphone);
            //Toast.makeText(this, "Smartphone", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        ThreadManager.UIThreadPostDelayed(new Runnable() {
            @Override
            public void run() {
                int []index  = new int[10];
                index [0] = 0; index [1] = 1;
                index [2] = 8;  index [3] = 9;
                index [4] = 16;  index [5] = 17;
                index [6] = 24;  index [7] = 25;
                index [8] = 26;  index [9] = 27;

                int id = 0;
                for (int i  : index ) {
                    malla.performItemClick(malla.getAdapter().getView(i, null, null), i, id);
                    id++;
                }
            }
        }, 1000)  ;
        super.onResume();
    }

    @Override
    public void finalizeNow() {

        this.onBackPressed();
    }

    @Override
    public void readyToFinalize() {

    }
}

