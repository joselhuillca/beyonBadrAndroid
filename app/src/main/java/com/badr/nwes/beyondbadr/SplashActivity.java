package com.badr.nwes.beyondbadr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.SmartPhone.NavigatorBadrSmartphone;
import com.badr.nwes.beyondbadr.puzzle.PuzzleActivity;

/**
 * Created by Kelvin on 2016-10-05.
 */

public class SplashActivity extends Activity {

    public boolean is_smartphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View decorView = getWindow().getDecorView();
         int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);

        if (SystemConfiguration.getMinDpi() > 600) {
            //Device is a 7" tablet
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            is_smartphone = false;
            Intent intent = new Intent(this, PuzzleActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            is_smartphone = true;
            Intent intent = new Intent(this, NavigatorBadrSmartphone.class);
            startActivity(intent);
            finish();
        }


    }
}
