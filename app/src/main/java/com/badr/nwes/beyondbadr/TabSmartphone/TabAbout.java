package com.badr.nwes.beyondbadr.TabSmartphone;

/**
 * Created by Citec on 28/11/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.adapters.ImageSimple;
import com.badr.nwes.beyondbadr.adapters.ImageSimpleAdapter;
import com.telerik.widget.list.RadListView;

import java.util.ArrayList;
import java.util.List;

public class TabAbout extends  Fragment {

    private Context context;
    private RelativeLayout main_layout;
    private RadListView list_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getActivity();
        init();

        ImageSimpleAdapter listViewAdapter = new ImageSimpleAdapter(getListOfImageSimple());
        list_view.setAdapter(listViewAdapter);

        return main_layout;
    }

    public void init(){
        main_layout = new RelativeLayout(context);
        main_layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource("phoneChapter1/fondo.png", 0,0,context);
        main_layout.setBackground(new BitmapDrawable(input_image_bitmap));

        list_view = new RadListView(context);
        list_view.setLayoutParams(new RadListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        main_layout.addView(list_view);
    }

    private List<ImageSimple> getListOfImageSimple() {
        List<ImageSimple> img_simple = new ArrayList<>();

        int size_widht = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();

        img_simple.add(new ImageSimple("phoneChapter1/primero.png", "description",(int)(size_widht),1.0,context));
        img_simple.add(new ImageSimple("phoneChapter1/segundo.png", "description",(int)(size_widht),1.0,context));
        img_simple.add(new ImageSimple("phoneChapter1/tercero_escudo.png", "description",(int)(size_widht),1.0,context));
        return img_simple;
    }
}
