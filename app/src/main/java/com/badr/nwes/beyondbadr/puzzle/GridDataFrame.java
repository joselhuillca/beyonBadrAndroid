package com.badr.nwes.beyondbadr.puzzle;

import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.data.NSFrame;

/**
 * Created by Kelvin on 2016-10-19.
 */
class GridDataFrame {
    public NSFrame getBaseScreenNSFrame() {
        return baseScreenNSFrame;
    }


    public NSFrame getGridNSFrame() {
        return gridNSFrame;
    }

    public NSFrame getMapNSFrame() {
        return mapNSFrame;
    }

    public NSFrame getLogoNSFrame() {
        return logoNSFrame;
    }

    public NSFrame getTextLeftNSFrame() {
        return textLeftNSFrame;
    }

    public NSFrame getTextRightNSFrame() {
        return textRightNSFrame;
    }

    public NSFrame getTextLugarNSFrame() {
        return textLugarNSFrame;
    }

    private NSFrame baseScreenNSFrame;
     private NSFrame gridNSFrame;
    private NSFrame mapNSFrame;
    private NSFrame logoNSFrame;
    private NSFrame textLeftNSFrame;
    private NSFrame textRightNSFrame;
    private NSFrame textLugarNSFrame;

    public GridDataFrame() {

        if (SystemConfiguration.getMinDpi() > 600) {

            baseScreenNSFrame = new NSFrame(0, 0, 1600, 900);

            gridNSFrame = new NSFrame(472, 170, 1120, 592);

            mapNSFrame = new NSFrame(0, 176, (int) (343 * 1.5), (int) (357 * 2));

            logoNSFrame = new NSFrame(580, 0, 485, 117);

            textLeftNSFrame = new NSFrame(50, 530, 400, 240);

            textRightNSFrame = new NSFrame(280, 776, 1200, 124);

            textLugarNSFrame = new NSFrame(50, 436, 400, 60);
        }
        else {
            baseScreenNSFrame = new NSFrame(0, 0, 720, 1280);

            gridNSFrame = new NSFrame(5, 240, 705, 845);

            mapNSFrame = new NSFrame(290, 318, 524, 741);

            logoNSFrame = new NSFrame(130, 53, 461, 86);

            textLeftNSFrame = new NSFrame(0, 0, 0, 0);

            textRightNSFrame = new NSFrame(21, 1103, 677, 156);

            textLugarNSFrame = new NSFrame(373, 1040, 328, 50);

        }
    }
}
