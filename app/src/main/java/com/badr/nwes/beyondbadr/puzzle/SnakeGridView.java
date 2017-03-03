package com.badr.nwes.beyondbadr.puzzle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.GridView;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.microsoft.xbox.toolkit.SoundManager;
import com.microsoft.xbox.toolkit.ThreadManager;

/**
 * Created by Kelvin on 2016-10-03.
 */
class SnakeGridView extends GridView {

    private int width;
    private int height;
    private int divWidth;
    private int divHeight;

    GridItemAdapter imageAdapter;

    GestureDetector gestureDetector;

    PuzzleActivity context;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        PuzzleActivity context;

        public GestureListener(PuzzleActivity context) {
            this.context = context;
        }

        @Override
        public void onLongPress(MotionEvent e) {
           // context.finalizeNow();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //context.finalizeNow();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            imageAdapter.PanAction(e.getActionIndex());
            return super.onSingleTapUp(e);
        }
    }


    public SnakeGridView(PuzzleActivity context, int width, int height, GridItemAdapter ref_image_adapter) {
        super(context);
        this.width = width;
        this.height = height;
        divWidth = this.width / SystemConfiguration.GRID_COLUMNS;
        divHeight = this.height / SystemConfiguration.GRID_ROWS;
        imageAdapter = ref_image_adapter;
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureListener(context));

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            //Log.Info("info", "snakegridview: " + ev.GetX() + " " + ev.GetY());
            int position = (int) Math.floor(ev.getX() / divWidth) + (int) Math.floor(ev.getY() / divHeight) * SystemConfiguration.GRID_COLUMNS;
            //Log.Info("info", "snakegridview: " + (ev.GetX() / divWidth) + " " + (ev.GetY() / divHeight) + " -> " + position);
            if (position < imageAdapter.getCount() && position >= 0) {
              //  imageAdapter.PutThemeAt(position);
                imageAdapter.PanAction(position);

            }

        }   else if (action == MotionEvent.ACTION_DOWN) {
            int position = (int) Math.floor(ev.getX() / divWidth) + (int) Math.floor(ev.getY() / divHeight) * SystemConfiguration.GRID_COLUMNS;
            if (position < imageAdapter.getCount() && position >= 0) {

                 imageAdapter.PanAction(position);

            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            int position = (int) Math.floor(ev.getX() / divWidth) + (int) Math.floor(ev.getY() / divHeight) * SystemConfiguration.GRID_COLUMNS;
            //Log.Info("info", "snakegridview: " + (ev.GetX() / divWidth) + " " + (ev.GetY() / divHeight) + " -> " + position);
            if (position < imageAdapter.getCount() && position >= 0) {
                imageAdapter.PutColorAtImage(position);
                imageAdapter.PutThemeAt(position);
            }
        }
        /*else if (action == MotionEvent.ACTION_DOWN) {
            int position = (int) Math.floor(ev.getX() / divWidth) + (int) Math.floor(ev.getY() / divHeight) * SystemConfiguration.GRID_COLUMNS;
            //Log.Info("info", "snakegridview: " + (ev.GetX() / divWidth) + " " + (ev.GetY() / divHeight) + " -> " + position);
            if (position < imageAdapter.getCount() && position >= 0)
                imageAdapter.PutThemeAt(position);
        }*/
        gestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }


}


