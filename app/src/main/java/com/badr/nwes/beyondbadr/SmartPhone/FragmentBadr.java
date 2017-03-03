package com.badr.nwes.beyondbadr.SmartPhone;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.adapters.GeneralImageItemDataAdapter;
import com.telerik.widget.list.RadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Citec on 16/12/2016.
 */

public class FragmentBadr extends Fragment {

    private LinearLayout main_layout;
    private Context context;
    private LinearLayout layout_about;
    private LinearLayout layout_sinopsys;
    private LinearLayout layout_credits;
    private LinearLayout layout_comic;
    private LinearLayout layout_ratecomic;
    private LinearLayout layout_characters;

    private String TAG = FragmentArt.class.getSimpleName();
    private String jsonComicpreview;
    private String jsonCharacteres;
    public RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_badr,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Init(rootView);
        Fill_About();
        Fill_Sinopsys();
        Fill_Credits();

        //init_data();//Para elRadList de Comic y Characters
        Fill_Comic();
        Fill_RateComic();
        Fill_Characters();

        return rootView;
    }

    //Inicializamos todas las variables de la clase
    private void Init(View rootview){
        context = this.getActivity();
        main_layout = (LinearLayout)rootview.findViewById(R.id.mainLayout_badr);

        layout_about = new LinearLayout(context);
        layout_about.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_about.setOrientation(LinearLayout.HORIZONTAL);
        layout_about.setWeightSum(1);
        //layout_about.setBackgroundColor(Color.YELLOW);

        layout_sinopsys = new LinearLayout(context);
        layout_sinopsys.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_sinopsys.setOrientation(LinearLayout.VERTICAL);

        layout_credits = new LinearLayout(context);
        layout_credits.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_credits.setOrientation(LinearLayout.HORIZONTAL);
        layout_credits.setWeightSum(1);

        layout_comic = new LinearLayout(context);
        layout_comic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_comic.setOrientation(LinearLayout.VERTICAL);

        layout_ratecomic = new LinearLayout(context);
        layout_ratecomic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_ratecomic.setOrientation(LinearLayout.VERTICAL);

        layout_characters = new LinearLayout(context);
        layout_characters.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_characters.setOrientation(LinearLayout.VERTICAL);

        //Añadimos los views
        main_layout.addView(layout_about);
        main_layout.addView(layout_sinopsys);
        main_layout.addView(layout_credits);
        main_layout.addView(layout_comic);
        main_layout.addView(layout_ratecomic);
        main_layout.addView(layout_characters);

        //Inicializamos SystemConfiguration
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int widthInDp = metrics.widthPixels;
        int heightInDp = metrics.heightPixels;
        SystemConfiguration.setWidthPixel (widthInDp);
        SystemConfiguration.setHeigthPixel (heightInDp);
    }

    //Llenamos la parte de About
    private void Fill_About(){
        //Imagen ------------------------------------------------------
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout_img_about = new LinearLayout(context);
        float wImage = 0.48f;
        Params.weight = wImage;
        layout_img_about.setLayoutParams(Params);
        layout_img_about.setOrientation(LinearLayout.VERTICAL);

        ImageView img_about = new ImageView(context);
        img_about.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource("levelzero/images/imageAbout.jpg", 0,0, context);
        float aspecRatio = (float) input_image_bitmap.getHeight() / (float) input_image_bitmap.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float mImageWidth = displayMetrics.widthPixels*wImage;
        int mImageHeight = Math.round(mImageWidth*aspecRatio);
        Bitmap mBitmapScale = Bitmap.createScaledBitmap(input_image_bitmap,(int)mImageWidth,mImageHeight,false);
        img_about.setImageBitmap(mBitmapScale);

        layout_img_about.addView(img_about);

        layout_about.addView(layout_img_about);

        //TEXTO DEBAJO DE LA IMAGEN
        TextView content_imageBottom = new TextView(context);
        content_imageBottom.setText("By: Northern World Entertainment Software Inc.\n" +
                "\n" +
                "Released: October 5, 2016\n" +
                "Age Rating: 17+ Only\n" +
                "Pages: 25");
        content_imageBottom.setTextColor(Color.BLACK);
        int padding_txt = SystemConfiguration.getWidth(14);
        content_imageBottom.setPadding(0,padding_txt,0,padding_txt);
        content_imageBottom.setTextSize(SystemConfiguration.getWidth(11));

        layout_img_about.addView(content_imageBottom);

        //Espaciador - Auxiliar -------------------------------------------------
        LinearLayout.LayoutParams Params_aux = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout_espaciador = new LinearLayout(context);
        Params_aux.weight = 0.03f;
        layout_espaciador.setLayoutParams(Params_aux);
        layout_espaciador.setOrientation(LinearLayout.VERTICAL);

        layout_about.addView(layout_espaciador);

        //Contenido a la DERECHA-------------------------------------------------------------
        int padding2 = SystemConfiguration.getWidth(16);

        LinearLayout.LayoutParams Params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout_contenido_about = new LinearLayout(context);
        Params2.weight = 0.51f;
        layout_contenido_about.setLayoutParams(Params2);
        layout_contenido_about.setOrientation(LinearLayout.VERTICAL);
        //layout_contenido_about.setBackgroundColor(Color.GREEN);

        TextView title_about = new TextView(context);
        title_about.setText("The Battle of Badr\n Beyond Badr: Episode 0");
        title_about.setTypeface(null, Typeface.BOLD);
        title_about.setPadding(0,0,0,padding2);
        title_about.setTextColor(Color.BLACK);
        title_about.setTextSize(SystemConfiguration.getWidth(18));

        TextView content_about = new TextView(context);
        content_about.setText("Lorem Ipsum es simplemente el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años...");
        content_about.setTextColor(Color.BLACK);
        content_about.setTextSize(SystemConfiguration.getWidth(12));

        layout_contenido_about.addView(title_about);
        layout_contenido_about.addView(content_about);

        layout_about.addView(layout_contenido_about);
        //-----------
    }

    //Llenamos la parte de Sinopsys
    private void Fill_Sinopsys(){
        TextView title_sinopsys = new TextView(context);
        title_sinopsys.setText("SINOPSYS");
        title_sinopsys.setText("Sinopsys");
        int padding_top = SystemConfiguration.getWidth(26);
        int padding_bottom = SystemConfiguration.getWidth(16);
        title_sinopsys.setPadding(0,padding_top,0,padding_bottom);
        title_sinopsys.setTypeface(null, Typeface.BOLD);
        title_sinopsys.setTextColor(Color.BLACK);
        title_sinopsys.setTextSize(SystemConfiguration.getWidth(16));

        TextView content_sinopsys = new TextView(context);
        content_sinopsys.setText("Lorem Ipsum es simplemente el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años...");
        content_sinopsys.setTextColor(Color.BLACK);
        content_sinopsys.setTextSize(SystemConfiguration.getWidth(12));

        layout_sinopsys.addView(title_sinopsys);
        layout_sinopsys.addView(content_sinopsys);
    }

    //Llenamos la parte de los Creditos
    private void Fill_Credits(){
        TextView title_credits = new TextView(context);
        title_credits.setText("Credits");
        title_credits.setTypeface(null, Typeface.BOLD);
        title_credits.setTextColor(Color.BLACK);
        title_credits.setTextSize(SystemConfiguration.getWidth(16));
        int padding_ = SystemConfiguration.getWidth(16);
        title_credits.setPadding(0,padding_,0,padding_);

        //Layout LEFT
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout_left = new LinearLayout(context);
        Params.weight = 0.5f;
        layout_left.setLayoutParams(Params);
        layout_left.setOrientation(LinearLayout.VERTICAL);

        layout_left.addView(title_credits);

        TextView content_left = new TextView(context);
        content_left.setText(Html.fromHtml("Northern World Entertainment Software Inc." +
                "<p><strong>Pencils:</strong>" +
                "<br>Kaimer Dalmos</p>" +
                "<p><strong>Colored by:</strong>" +
                "<br>Erly Almanza</p>"));
        content_left.setTextColor(Color.BLACK);
        content_left.setTextSize(SystemConfiguration.getWidth(12));

        layout_left.addView(content_left);

        //Layout RIGHT
        LinearLayout layout_right = new LinearLayout(context);
        Params.weight = 0.5f;
        layout_right.setLayoutParams(Params);
        layout_right.setOrientation(LinearLayout.VERTICAL);

        TextView content_right = new TextView(context);
        content_right.setText(Html.fromHtml("<p><br><br><br><br><br><br><strong>Edited by:</strong>" +
                "<br>Kelvin Ali</p>" +
                "<p><strong>Genres:</strong>" +
                "<br>Historical" +
                "<br>Religious" +
                "<br>Video Games</p>"));
        content_right.setTextColor(Color.BLACK);
        content_right.setTextSize(SystemConfiguration.getWidth(12));

        layout_right.addView(content_right);

        layout_credits.addView(layout_left);
        layout_credits.addView(layout_right);

    }

    //Llenamos la parte de Comic Book
    private void Fill_Comic(){
        TextView title_comic = new TextView(context);
        title_comic.setText("Comic Book");
        title_comic.setTypeface(null, Typeface.BOLD);
        title_comic.setTextColor(Color.BLACK);
        title_comic.setTextSize(SystemConfiguration.getWidth(16));
        int padding_top = SystemConfiguration.getWidth(18);
        title_comic.setPadding(0,padding_top,0,0);

        LinearLayoutManager lmngr = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RadListView listView_comic = new RadListView(context);
        listView_comic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        listView_comic.setLayoutManager(lmngr);

        //Trabajando con json
        jsonComicpreview = SystemConfiguration.setJsonString("badr_comicpreview.json",context);
        List<Image> images = new ArrayList<>();
        try {
            images = fetchImages(jsonComicpreview);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adapters ---------------------
        GeneralImageItemDataAdapter adapter = new GeneralImageItemDataAdapter(
                images,
                FragmentBadr.this.getActivity(),
                R.layout.general_image_item_data);
        listView_comic.setAdapter(adapter);

        layout_comic.addView(title_comic);
        layout_comic.addView(listView_comic);
    }

    private void Fill_RateComic(){
        TextView title_ratecomic = new TextView(context);
        title_ratecomic.setText("Rate this Book");
        int padding_top = SystemConfiguration.getWidth(26);
        int padding_bottom = SystemConfiguration.getWidth(16);
        title_ratecomic.setPadding(0,padding_top,0,padding_bottom);
        title_ratecomic.setTypeface(null, Typeface.BOLD);
        title_ratecomic.setTextColor(Color.BLACK);
        title_ratecomic.setTextSize(SystemConfiguration.getWidth(16));

        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout_rate = new LinearLayout(context);
        layout_rate.setLayoutParams(Params);
        layout_rate.setOrientation(LinearLayout.VERTICAL);
        layout_rate.setGravity(Gravity.CENTER);

        ratingBar = new RatingBar(getActivity());
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1.0f);

        layout_rate.addView(ratingBar);

        layout_ratecomic.addView(title_ratecomic);
        layout_ratecomic.addView(layout_rate);
    }

    //Llenamos la parte de Characters
    private void Fill_Characters(){
        TextView title_characters = new TextView(context);
        title_characters.setText("Chatacters Scroll");
        int padding_top = SystemConfiguration.getWidth(26);
        int padding_bottom = SystemConfiguration.getWidth(16);
        title_characters.setPadding(0,padding_top,0,padding_bottom);
        title_characters.setTypeface(null, Typeface.BOLD);
        title_characters.setTextColor(Color.BLACK);
        title_characters.setTextSize(SystemConfiguration.getWidth(16));

        LinearLayoutManager lmngr = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RadListView listView_characters = new RadListView(context);
        listView_characters.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        listView_characters.setLayoutManager(lmngr);

        //Trabajando con json
        jsonCharacteres = SystemConfiguration.setJsonString("badr_characters2.json",context);
        List<Image> images = new ArrayList<>();
        try {
            images = fetchImages(jsonCharacteres);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adapters ---------------------
        GeneralImageItemDataAdapter adapter = new GeneralImageItemDataAdapter(
                images,
                FragmentBadr.this.getActivity(),
                R.layout.general_image_item_data);
        listView_characters.setAdapter(adapter);

        layout_characters.addView(title_characters);
        layout_characters.addView(listView_characters);
    }

    // -------------------------------- INICIO - Llenando - ADAPTERS ----------------------------------

    //Para el Comic Preview y los characteres
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
                image.setMcontext(getActivity());
                image.setPosition(i);

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

}
