package com.badr.nwes.beyondbadr.data;

/**
 * Created by Kelvin on 2016-10-05.
 */
public class NSFrame {
    public int Width;
    public int Height;
    public int X;
    public int Y;

    public NSFrame ()
    {
        this(0,0,0,0);
    }

    public NSFrame(int x, int y, int w, int h) {
        X = x;
        Y = y;
        Width = w;
        Height = h;
    }
}
