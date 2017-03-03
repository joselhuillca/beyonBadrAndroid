package com.badr.nwes.beyondbadr.adapters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.badr.nwes.beyondbadr.ProportionCalculator;
import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.reader.content.CustomImageView;
import com.badr.nwes.beyondbadr.reader.content.StackBorderLoader;
import com.telerik.widget.list.ListViewHolder;


/**
 * Created by Citec on 26/11/2016.
 */
public class GeneralImageItemDataHolder extends ListViewHolder {

    public LinearLayout itemImage;
    public Image entity;

    public GeneralImageItemDataHolder(View itemView) {
        super(itemView);

        this.itemImage = (LinearLayout) itemView.findViewById(R.id.imageView);

        this.itemImage.setGravity(Gravity.CENTER_HORIZONTAL);
    }


    public void bind(Image entity) {
        this.entity = entity;

        StackBorderLoader stack_border_bitmap_gen = new StackBorderLoader(entity.getMcontext());

        CustomImageView image = new CustomImageView(entity.getMcontext());

        image.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT));

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(entity.getMedium(), 0,0, entity.getMcontext());
        Bitmap copy_border = stack_border_bitmap_gen.GetStackBorderBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas comboImage = new Canvas(copy_border);
        // Then draw the second on top of that

        float [] boundaries = new float[4];

        ProportionCalculator.CalculeImageBoundaries(copy_border.getWidth(), copy_border.getHeight(), boundaries);
        comboImage.drawBitmap(input_image_bitmap, new Rect(0,0, input_image_bitmap.getWidth(), input_image_bitmap.getHeight()), new RectF(boundaries[0], boundaries[1], copy_border.getWidth() - boundaries[2], copy_border.getHeight() - boundaries[3]), null);

        Bitmap scaled = Bitmap.createScaledBitmap(copy_border, input_image_bitmap.getWidth(), input_image_bitmap.getWidth(), true);

        image.setImageBitmap(scaled);

        this.itemImage.addView(image);
    }


}
