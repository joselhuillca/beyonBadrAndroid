package com.badr.nwes.beyondbadr.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.badr.nwes.beyondbadr.ProportionCalculator;
import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.activity.SlideShowActivity;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.activity.SlideshowDialogFragment;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.reader.content.StackBorderLoader;
import com.telerik.widget.list.ListViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * Created by Citec on 26/11/2016.
 */
public class GeneralImageItemDataHolder extends ListViewHolder {

    public LinearLayout itemImage;
    public ImageView imageView;
    public Image entity;
    //public ArrayList<Image> images;
    //public List<Image> imagesList;

    public GeneralImageItemDataHolder(View itemView) {
        super(itemView);

        this.itemImage = (LinearLayout) itemView.findViewById(R.id.imageView);
        //this.itemImage.setLayoutParams(new LinearLayout.LayoutParams(150,-1));
        this.imageView = (ImageView) itemView.findViewById(R.id.imgView);

        //this.images = new ArrayList<>();
        //this.imagesList = new ArrayList<Image>();

        this.itemImage.setGravity(Gravity.CENTER_HORIZONTAL);
    }


    public void bind(final Image entity) {
        this.entity = entity;

        StackBorderLoader stack_border_bitmap_gen = new StackBorderLoader(entity.getMcontext());

        //fillListImages(entity.getImages());
        //this.imagesList = entity.getImages();

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(entity.getMedium(), 0,0, entity.getMcontext());
        Bitmap copy_border = stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas comboImage = new Canvas(copy_border);
        // Then draw the second on top of that

        float [] boundaries = new float[4];

        ProportionCalculator.CalculeImageBoundaries(copy_border.getWidth(), copy_border.getHeight(), boundaries);
        comboImage.drawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight()), new RectF(boundaries[0], boundaries[1], copy_border.getWidth() - boundaries[2], copy_border.getHeight() - boundaries[3]), null);

       // Display display = getWindowManager().getDefaultDisplay();
       // Log.e("", "" + display.getHeight() + " " + display.getWidth());
        // android:background="#40000000"

        Bitmap scaled = Bitmap.createScaledBitmap(copy_border, input_image_bitmap.getWidth(), input_image_bitmap.getHeight(), true);

        //Log.d("Image Position ",String.format("%d, contador: %d, name: %s",image.position,image.contador,entity.getMedium()));


        this.imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println(String.format("position image: %d",entity.getPosition()));
                Bundle bundle = new Bundle();
                bundle.putString("images", entity.getFileJSON());
                bundle.putInt("position", entity.getPosition());

                /*FragmentManager fm = ((FragmentActivity)entity.getMcontext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/

                Intent intent = new Intent(entity.getMcontext(), SlideShowActivity.class);
                intent.putExtras(bundle); //Put your id to your next Intent
                v.getContext().startActivity(intent);
            }
        });

        this.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.imageView.setImageBitmap(scaled);

    }

    //Convertimos un List<> a ArrayList<>
    /*public void fillListImages(List<Image> imgs){
        images.clear();
        images.addAll(imgs);
    }*/


}
