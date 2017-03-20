package com.badr.nwes.beyondbadr.SmartPhone;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.badr.nwes.beyondbadr.R;

import java.io.Serializable;

public class NavigatorBadrSmartphone extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int current_id_navigator;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_badr_smartphone);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //Mostramos un contenido inicial - Fragment Home
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame,new FragmentHome()).commit();
        current_id_navigator = 0;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigator_badr_smartphone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(id!=current_id_navigator) fm.beginTransaction().replace(R.id.content_frame,new FragmentHome()).commit();
            current_id_navigator = 0;
            toolbar.setTitle("Home");
        } else if (id == R.id.nav_badr) {
            if(id!=current_id_navigator) fm.beginTransaction().replace(R.id.content_frame,new FragmentBadr()).commit();
            current_id_navigator = 1;
            toolbar.setTitle("Battle of Badr");
        } else if (id == R.id.nav_art) {
            if(id!=current_id_navigator) fm.beginTransaction().replace(R.id.content_frame,new FragmentArt()).commit();
            current_id_navigator = 2;
            toolbar.setTitle("Exclusive Art");
        } else if (id == R.id.nav_credits) {
            if(id!=current_id_navigator) fm.beginTransaction().replace(R.id.content_frame,new FragmentCredits()).commit();
            current_id_navigator = 3;
            toolbar.setTitle("Credits");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
