package com.badr.nwes.beyondbadr;

import com.badr.nwes.beyondbadr.SystemConfiguration;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class ProportionCalculator
{
    public static void CalculeImageBoundaries(float width_border, float height_border, float[] result){
        if(result == null || result.length != 4) {
            result = null;
            result = new float[4];
        }
        float horizontal_prop = width_border * SystemConfiguration.BACKGROUND_BORDER_HOR_PROPORTION;
        result[0] = horizontal_prop * SystemConfiguration.OFFSET_CONTENT_HOR_TOP_PROPORTION;
        result[2] = horizontal_prop * (1.0f - SystemConfiguration.OFFSET_CONTENT_HOR_TOP_PROPORTION);
        float vertical_prop = (height_border * SystemConfiguration.BACKGROUND_BORDER_VER_PROPORTION);
        result[1] = vertical_prop * SystemConfiguration.OFFSET_CONTENT_VER_TOP_PROPORTION;
        result[3] = vertical_prop * (1.0f - SystemConfiguration.OFFSET_CONTENT_VER_TOP_PROPORTION);
    }
    public static void CalculeBackgroundBoundaries(float image_width, float image_height, float[] result){
        if(result == null || result.length != 4) {
            result = null;
            result = new float[4];
        }
        float cx1 = SystemConfiguration.BACKGROUND_BORDER_HOR_PROPORTION * SystemConfiguration.OFFSET_CONTENT_HOR_TOP_PROPORTION;
        float cx2 = SystemConfiguration.BACKGROUND_BORDER_HOR_PROPORTION * (1.0f - SystemConfiguration.OFFSET_CONTENT_HOR_TOP_PROPORTION);
        result[0] = (image_width * cx1 ) / (cx1 * cx2 - (cx1 - 1.0f) * (cx2 - 1.0f) );
        result[2] = (image_width * (cx1 - 1.0f) ) / (cx1 * cx2 - (cx1 - 1.0f) * (cx2 - 1.0f) );

        float cy1 = SystemConfiguration.BACKGROUND_BORDER_VER_PROPORTION * SystemConfiguration.OFFSET_CONTENT_VER_TOP_PROPORTION;
        float cy2 = SystemConfiguration.BACKGROUND_BORDER_VER_PROPORTION * (1.0f - SystemConfiguration.OFFSET_CONTENT_VER_TOP_PROPORTION);
        result[1] = (image_height * cy1 ) / (cy1 * cy2 - (cy1 - 1.0f) * (cy2 - 1.0f) );
        result[3] = (image_height * (cy1 - 1.0f) ) / (cy1 * cy2 - (cy1 - 1.0f) * (cy2 - 1.0f) );
    }
}
