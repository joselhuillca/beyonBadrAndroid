package com.badr.nwes.beyondbadr.SmartPhone.Glide.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.badr.nwes.beyondbadr.SystemConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlideShowActivity extends AppCompatActivity {

    private String TAG = SlideShowActivity.class.getSimpleName();
    private List<Image> images;
    private String fileJSON;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.fragment_image_slider);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        lblCount = (TextView) findViewById(R.id.lbl_count);
        lblTitle = (TextView) findViewById(R.id.title);
        lblDate = (TextView) findViewById(R.id.date);

        selectedPosition = getIntent().getIntExtra("position",-1);
        fileJSON = getIntent().getStringExtra("images");
        try {
            images = fetchImages(fileJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        images.clear();
        fileJSON = "";
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }



    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        Image image = images.get(position);
        lblTitle.setText(image.getName());
        lblDate.setText(image.getTimestamp());
    }

    private List<Image> fetchImages(String fileJson) throws JSONException {
        JSONArray response = new JSONArray(fileJson);
        List<Image> images = new ArrayList<>();

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
                image.setMcontext(this);
                image.setPosition(i);
                image.setFileJSON(fileJson);

                images.add(image);

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        //pasamos todas las imagenes
        for (int i = 0; i < response.length(); i++) {
            images.get(i).setImages(images);
        }
        return  images;
    }


    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image = images.get(position);

            //URL - Download Internet
            /*Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);
            */

            //Assets Local
            Bitmap bitmap_large = SystemConfiguration.decodeSampledBitmapFromResource(image.getLarge(), 0,0, getApplicationContext());
            imageViewPreview.setImageBitmap(bitmap_large);
            //imageViewPreview.setRotation(90.0f);
            imageViewPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
