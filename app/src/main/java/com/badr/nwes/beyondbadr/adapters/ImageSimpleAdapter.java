package com.badr.nwes.beyondbadr.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.telerik.widget.list.ListViewAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

/**
 * Created by Citec on 26/11/2016.
 */
public class ImageSimpleAdapter extends ListViewAdapter {
    public ImageSimpleAdapter(List items) {
        super(items);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.imagesimple_list_item, parent, false);
        return new ImageSimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ImageSimpleViewHolder viewHolder = (ImageSimpleViewHolder)holder;
        ImageSimple img_simple = (ImageSimple)getItems().get(position);

        viewHolder.image_Simple.setScaleType(ImageView.ScaleType.FIT_START);

        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource(img_simple.getName(), 0,0, img_simple.getContext());
        //tamaño original de la imagen
        int w = input_image_bitmap.getWidth();
        int h = input_image_bitmap.getHeight();
        //Log.d("Size Width2: ",String.format("%d",w));
        //tamaño normalizado
        double normalized = normalizarImage(w,img_simple.getSize_width())/100.0;
        w = (int)(normalized*w);
        h = (int)(normalized*h);
        //Log.d("Size Width2: ",String.format("%d",h));
        //Bitmap scaled = Bitmap.createScaledBitmap(input_image_bitmap, (int)(w*img_simple.getScaled()), (int)(h*img_simple.getScaled()), true);
        Bitmap scaled = Bitmap.createScaledBitmap(input_image_bitmap, (int)(w*img_simple.getScaled()), (int)(h*img_simple.getScaled()), true);

        viewHolder.image_Simple.setImageBitmap(scaled);
    }

    public static class ImageSimpleViewHolder extends ListViewHolder {

        ImageView image_Simple;

        public ImageSimpleViewHolder(View itemView) {
            super(itemView);

            image_Simple = (ImageView)itemView.findViewById(R.id.imageSimple);
        }
    }

    public double normalizarImage(int w_norma,int w){
        return (w*100.0)/(w_norma*1.0);
    }
}
