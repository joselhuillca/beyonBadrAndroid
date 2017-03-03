package com.badr.nwes.beyondbadr.SmartPhone;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;

/**
 * Created by Citec on 16/12/2016.
 */

public class FragmentHome extends Fragment {
    LinearLayout main_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        main_layout = (LinearLayout)rootView.findViewById(R.id.fragment_home_linearL);
        TextView test = new TextView(this.getActivity());
        test.setText("Fragment Home!");
        main_layout.addView(test);

        return rootView;
    }

}
