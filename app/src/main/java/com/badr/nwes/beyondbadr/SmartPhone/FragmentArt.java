package com.badr.nwes.beyondbadr.SmartPhone;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.activity.SlideshowDialogFragment;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.adapter.GalleryAdapter;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.badr.nwes.beyondbadr.SystemConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Citec on 16/12/2016.
 */

public class FragmentArt extends Fragment{

    private String TAG = FragmentArt.class.getSimpleName();
    private String jsonBuffer ;
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    private FragmentActivity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_art,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = (FragmentActivity) getActivity();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(getActivity());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentManager fm = context.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        jsonBuffer = SystemConfiguration.setJsonString("badr_wallpapers.json",context);

        try {
            fetchImages();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    private void fetchImages() throws JSONException {

        JSONArray response = new JSONArray(jsonBuffer);

        images.clear();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject object = response.getJSONObject(i);
                Image image = new Image();
                image.setName(object.getString("name"));

                JSONObject url = object.getJSONObject("url");
                image.setSmall(url.getString("small"));
                image.setMedium(url.getString("medium"));
                image.setLarge(url.getString("large"));
                image.setTimestamp(object.getString("timestamp"));

                images.add(image);

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        mAdapter.notifyDataSetChanged();
    }

}
