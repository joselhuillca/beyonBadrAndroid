package com.badr.nwes.beyondbadr.SmartPhone;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.adapters.ImageSimple;
import com.badr.nwes.beyondbadr.adapters.ImageSimpleAdapter;
import com.telerik.widget.list.RadListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Citec on 16/12/2016.
 */

public class FragmentCredits extends Fragment {

    private Context context;
    private RelativeLayout main_layout;
    private RadListView list_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_credits,container,false);

        Init(rootView);
        ImageSimpleAdapter listViewAdapter = new ImageSimpleAdapter(getListOfImageSimple());
        list_view.setAdapter(listViewAdapter);

        return rootView;
    }

    public void Init(View rootview){
        context = this.getActivity();

        main_layout = (RelativeLayout)rootview.findViewById(R.id.mainLayout_credits);

        list_view = (RadListView)rootview.findViewById(R.id.listView_credits);
    }

    private List<ImageSimple> getListOfImageSimple() {
        List<ImageSimple> img_simple = new ArrayList<>();

        int size_widht = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //Log.d("Size Width: ",String.format("%d",size_widht));

        //img_simple.add(new ImageSimple("phoneChapter1/primero.png", "description",(int)(size_widht),1.0,context));
        img_simple.add(new ImageSimple("phoneChapter1/segundo.png", "description",(int)(size_widht),1.0,context));
        img_simple.add(new ImageSimple("phoneChapter1/tercero_escudo.png", "description",(int)(size_widht),1.0,context));
        return img_simple;
    }
}
