package com.badr.nwes.beyondbadr.puzzle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.IFinalizeActivity;
import com.badr.nwes.beyondbadr.R;
 import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.SampleDataSource;
import com.microsoft.xbox.toolkit.SoundManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Kelvin on 2016-10-03.
 */
class GridItemAdapter extends BaseAdapter {

    class ImageAlphaAnimation {

        private ImageView image;

        private Runnable runnableAnimation;

        public ImageAlphaAnimation(ImageView image_ref){
            image = image_ref;

            long tick = System.currentTimeMillis();

            final long startTime = tick;
            final long duration = SystemConfiguration.SCALE_ANIMATION_MILLISECONDS ;

            runnableAnimation = new Runnable() {
                @Override
                public void run() {
                    long tick = System.currentTimeMillis();
                    float t = (float) ( (tick) - startTime) /  (2 * duration);
                    t = t > 1.0f ? 1.0f : t;
                    if (t < 1f) {
                        image.post(runnableAnimation);
                        image.setImageAlpha( (int)Math.floor((1 - t) * 255));
                    }else{
                        image.setImageAlpha(0);
                    }
                }
            };
            image.postDelayed(runnableAnimation, 0);


        }
    }


    Context context;

    int screenWidth;
    int screenHeight;

    private Bitmap white_item_bitmap_;
    private Bitmap []item_bitmaps_;
    private ArrayList<Integer> item_indexes_;
    private ArrayList<String> image_bg_paths_;

    private ArrayList<String> localidades_;
    private ArrayList<String> ubicaciones_;
    private ArrayList<String> climas_;
    private ArrayList<String> altitudes_;
    private ArrayList<String> titulos_;
    private ArrayList<String> subtitulos_;

    private IFinalizeActivity mFinalizer;
    private int width_;
    private int height_;
    private ImageView[] imagesCollection;
    private int num_images_ ;
    private ImageView border_theme_;
    private ImageView map_theme_;
    private ImageView image_bg_;
    private TextView textl_theme_;
    private TextView textr_theme_;
    private TextView textlugar_theme_;
    private int current_index_;

    private String black_grid_image_path_ = "levelzero/black_grid.png";
    private String logo_img_path = "levelzero/logo_trc.png";

    private String [] item_paths_ = {
            "levelzero/item_amarillo.png",
            "levelzero/item_azul.png",
            "levelzero/item_rojo.png",
            "levelzero/item_verde.png",
            "levelzero/item_violeta.png"
    };


    private String [] border_paths_ = {
            "levelzero/amarillo.png",
            "levelzero/azul.png",
            "levelzero/rojo.png",
            "levelzero/verde.png",
            "levelzero/violeta.png"
    };

    private String [] map_paths_ = {
            "levelzero/mapa_amarillo.png",
            "levelzero/mapa_azul.png",
            "levelzero/mapa_rojo.png",
            "levelzero/mapa_violeta.png", // TODO falta mapa verde
            "levelzero/mapa_violeta.png"
    };

    public GridItemAdapter(Context c, IFinalizeActivity finalizer, int width, int height,
                           ImageView border_theme,
                           ImageView map_theme,
                           ImageView logo_img,
                           ImageView image_bg,
                           ImageView black_grid_image,
                           TextView textl_theme,
                           TextView textr_theme,
                           TextView textlugar_theme )
    {

        textl_theme_ = textl_theme;
        textr_theme_ = textr_theme;
        textlugar_theme_ = textlugar_theme;
        mFinalizer = finalizer;
        border_theme_ = border_theme;
        map_theme_ = map_theme;
        image_bg_ = image_bg;
        context = c;
        width_ = width;
        height_ = height;
        current_index_ = 0;

        screenWidth = SystemConfiguration.DIMENSION_DESING_WIDTH;
        screenHeight = SystemConfiguration.DIMENSION_DESING_HEIGHT;

        SampleDataSource sample_images = new SampleDataSource(context);

        item_indexes_ = new ArrayList<Integer>();
        ArrayList<String> item_descs = sample_images.getThemes();
        ubicaciones_ = sample_images.getUbicacion();
        altitudes_ = sample_images.getAltitud();
        climas_ = sample_images.getClima();
        localidades_ = sample_images.getLocalidad();
        titulos_ = sample_images.getTitles();
        subtitulos_ = sample_images.getSubTitles();
        image_bg_paths_ = sample_images.getBackGrounds();
        num_images_ = item_descs.size();
        imagesCollection = new ImageView[num_images_];
        item_bitmaps_ = new Bitmap [num_images_];

        for(int index = 0; index < num_images_ ; ++index){
            if(item_descs.get(index).equals("amarillo")) item_indexes_.add(0);
            else if(item_descs.get(index).equals("azul")) item_indexes_.add(1);
            else if(item_descs.get(index).equals("rojo")) item_indexes_.add(2);
            else if(item_descs.get(index).equals("verde")) item_indexes_.add(3);
            else item_indexes_.add(4);
        }

        white_item_bitmap_= decodeSampledBitmapFromResource("levelzero/item_blanco.png", width_ / SystemConfiguration.GRID_COLUMNS, height_ / SystemConfiguration.GRID_ROWS);

        PutThemeAt(0);


        //Bitmap bgrid_bitmap = decodeSampledBitmapFromResource(black_grid_image_path_, screenWidth, screenHeight);

        black_grid_image.setBackground(SystemConfiguration.getDrawableResource( context, R.drawable.black_grid));

        logo_img.setBackground(SystemConfiguration.getDrawableResource( context, R.drawable.logo_trc));


    }

        private static final float BITMAP_SCALE = 0.5f;
        private static final float BLUR_RADIUS = 10f;

        @SuppressLint("NewApi")
        public   Bitmap blur(Context context, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height,
                    false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }


    public void PutColorAtImage(int position){
       // if(current_index_ == position)
         //   return;

        imagesCollection[position].setImageAlpha(200);
        Bitmap copy_white = white_item_bitmap_.copy(Bitmap.Config.ARGB_8888, true);
        //if(current_index_ != -1){
        imagesCollection[current_index_].setImageAlpha(250);
        imagesCollection[current_index_].setImageBitmap(copy_white);

        ImageAlphaAnimation img_anim = new ImageAlphaAnimation(imagesCollection[current_index_]);
        //}

        imagesCollection[position].setImageBitmap(item_bitmaps_[position]);


        Bitmap border_bitmap_ = decodeSampledBitmapFromResource(image_bg_paths_.get(position), width_ / SystemConfiguration.GRID_COLUMNS, height_ / SystemConfiguration.GRID_ROWS) ;

        int currentBitmapWidth = border_bitmap_.getWidth();
        int currentBitmapHeight = border_bitmap_.getHeight();

        int ivWidth = image_bg_.getWidth();
        int ivHeight = image_bg_.getHeight() ;
        int newWidth = ivWidth;

        int newHeight = (int)Math.floor ((double)currentBitmapHeight * ((double)newWidth / (double)currentBitmapWidth));

        final Bitmap newbitMap = Bitmap.createScaledBitmap (border_bitmap_, newWidth, ivHeight, true);


        Runnable img_anim2 = new Runnable() {
            @Override
            public void run() {

                Bitmap blurredBitmap = blur( context, newbitMap);

                image_bg_.setImageBitmap (blurredBitmap);

            }
        };
        image_bg_.postDelayed(img_anim2, 0);



        //	image_bg_.SetImageBitmap(border_bitmap_);


        //border_bitmap_.Recycle();
        image_bg_.setScaleType (ImageView.ScaleType.FIT_CENTER);
        current_index_ = position;
    }


    public void PanAction(int position){
        if(current_index_ == position)
            return;

        //if (imagesCollection[position] == null)
            //imagesCollection[position] = getView(position,    ,null)

        imagesCollection[position].setImageAlpha(200);
        Bitmap copy_white = white_item_bitmap_.copy(Bitmap.Config.ARGB_8888, true);
        //if(current_index_ != -1){
        imagesCollection[current_index_].setImageAlpha(250);
        imagesCollection[current_index_].setImageBitmap(copy_white);

        ImageAlphaAnimation img_anim = new ImageAlphaAnimation(imagesCollection[current_index_]);
        //}

        imagesCollection[position].setImageBitmap(item_bitmaps_[position]);

       // ImageAlphaAnimation  img_anim2 = new ImageAlphaAnimation(item_bitmaps_[position]);

        current_index_ = position;

    }

    public void PutThemeAt(final int position){
        //position++;
        textl_theme_.setText(  Html.fromHtml("<p><big><b>AGE</b></big><br/>" +
                ubicaciones_.get(position) + "<br/><br/>" +
                "<big><b>HEIGHT</b></big><br/>" +
                "" + climas_.get(position) + "<br/><br/>" +
                "<big><b>WEIGHT</b></big><br/>" +
                "" + altitudes_.get(position) + "</p>") );


        textr_theme_.setText ( Html.fromHtml("<p> <big> <b> " + titulos_.get(position) + "</b> </big> <br/>" +
                subtitulos_.get(position) + "</p>") );
        //textlugar_theme_.Text = ;
        //textlugar_theme_.TextFormatted = Android.Text.Html.FromHtml("<font color='Yellow', align=\"right\" >" + localidades_[item_indexes_.get(position)] +  " </font>.");
        textlugar_theme_.setText(  Html.fromHtml("<p> <big> <b> <font color='yellow'>" + localidades_.get(position) + " </font> </b> </big> <br/>"
                + " <big><b> MUSLIN </b></big> </p>"  ));
        //textlugar_theme_.TextFormatted = Android.Text.Html.FromHtml(("<p align=center> <b> "
        //                        + "Hi!" + " <br/> <h1>"+"aaaaaaaaaaaaaaaaaaaaathis is line two"+" </h1> <br/>"
        //                      + "I am
        // ';'
        // \fine" + "  </b> </p>"));
        //textlugar_theme_.TextFormatted = Android.Text.Html.FromHtml(" <p>This is a html-formatted string with <b>bold</b> and <i>italic</i> text</p> <p>This is another paragraph of the same string.</p> ");
        textlugar_theme_.setGravity(  Gravity.RIGHT );
        textlugar_theme_.setTypeface(  Typeface.MONOSPACE);
        textr_theme_.setGravity(  Gravity.RIGHT );
        textr_theme_.setTypeface(  Typeface.MONOSPACE);
        textl_theme_.setGravity(  Gravity.RIGHT );
        textl_theme_.setTypeface(  Typeface.MONOSPACE);

        /*if ( border_theme_.getDrawable() == null ) {
            Bitmap border_bitmap_ = decodeSampledBitmapFromResource(border_paths_[item_indexes_.get(position)], screenWidth, screenHeight);
            border_theme_.setImageBitmap(border_bitmap_);
        }*/
         border_theme_.setBackground( SystemConfiguration.getDrawableResource( context, R.drawable.marco) );

        //imagesCollection[position].setImageBitmap(item_bitmaps_[position]);


        Runnable img_anim2 = new Runnable() {
            @Override
            public void run() {

                InputStream is = context.getResources().openRawResource(mAvatarIds[position]);
                Bitmap imageBitmap = BitmapFactory.decodeStream(is);

               // Bitmap blurredBitmap = blur( context, imageBitmap);

                map_theme_.setImageBitmap(imageBitmap );

                SoundManager.getInstance().playSound(R.raw.sndbuttonselectandroid);


            }
        };
        map_theme_.postDelayed(img_anim2, 0);
        //}

    }


    public   int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        AssetManager assetManager = context.getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr, null, options);
        /*options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeStream(istr, null, options); */
        return bitmap;
    }

    @Override
    public int getCount() {
        return num_images_;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };

    private Integer[] mAvatarIds = {
            R.drawable.avatar_1, R.drawable.avatar_2,
            R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6,
            R.drawable.avatar_7, R.drawable.avatar_8,
            R.drawable.avatar_9, R.drawable.avatar_10,
            R.drawable.avatar_11, R.drawable.avatar_12,
            R.drawable.avatar_13, R.drawable.avatar_14,
            R.drawable.avatar_1, R.drawable.avatar_2,
            R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6,
            R.drawable.avatar_7, R.drawable.avatar_8,
            R.drawable.avatar_9, R.drawable.avatar_10,
            R.drawable.avatar_11, R.drawable.avatar_12,
            R.drawable.avatar_13, R.drawable.avatar_14
    };


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ImageView imageView;
        //if (convertView == null) {  // if it's not recycled, initialize some attributes
        if(imagesCollection[position] == null){
            convertView = new ImageView(context);
            imagesCollection[position] = (ImageView)convertView;
			    /*imagesCollection[position].Click += delegate {
					mFinalizer.FinalizeNow();
			};*/
            imagesCollection[position].setLayoutParams( new GridView.LayoutParams(width_ / SystemConfiguration.GRID_COLUMNS, height_ / SystemConfiguration.GRID_ROWS) ) ;
            imagesCollection[position].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagesCollection[position].setPadding(2, 2, 2, 2);

            //item_bitmaps_[position] = decodeSampledBitmapFromResource(item_paths_[ item_indexes_.get(position)] , width_ / SystemConfiguration.GRID_COLUMNS, height_ / SystemConfiguration.GRID_ROWS);
            //imagesCollection[position].setImageBitmap(item_bitmaps_[position]);

            imagesCollection[position].setImageResource(mThumbIds[position]);
            InputStream is = context.getResources().openRawResource(mThumbIds[position]);
            Bitmap imageBitmap = BitmapFactory.decodeStream(is);
            item_bitmaps_[position] = imageBitmap;

            if(position != 0){
                imagesCollection[position].setImageAlpha(0);
                //Log.Info("", "GridItemAdapter(getview): position = " + position + " " + (int)imagesCollection[position].Tag);
            }



        }
        return imagesCollection[position];
    }
}
