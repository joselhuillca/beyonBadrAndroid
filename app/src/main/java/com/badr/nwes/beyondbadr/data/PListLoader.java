package com.badr.nwes.beyondbadr.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import org.core4j.xml.XDocument;
import org.core4j.xml.XElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Created by Kelvin on 2016-10-06.
 */

public class PListLoader {

    Context context;

    public PListLoader(Context c){
        context = c;
    }

    private String LoadContent (String path) {
        String ret = "";
        AssetManager assetManager = context.getAssets();
        CharacterCollection collection = null;
        try {
            InputStream is = assetManager.open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            ret = new String(buffer);

        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
        return ret;
    }

    private NSFrame StringToFrame(String frame)
    {
        Activity act = (Activity)((context instanceof Activity) ? context : null);

        NSFrame ret = new NSFrame();

        {
            //{{160,50}, {809,221}}
            frame = frame.replace(",", " ").replace("{", " ").replace("}", " ");
            ArrayList<String> values = new ArrayList<String>();
            String[] data = frame.split(" ");
            for (String v: data) {
                v = v.replaceAll(" ", "");
                if( v.length() > 0)
                    values.add(v);
            }

            float xfactor = act.getWindowManager().getDefaultDisplay().getWidth() / res_width;
            float yfactor = act.getWindowManager().getDefaultDisplay().getHeight() / res_heigth;

            ret.X = (int)(Double.parseDouble(values.get(0)) * xfactor);
            ret.Y = (int)(Double.parseDouble(values.get(1)) * yfactor);
            ret.Width = (int)(Double.parseDouble( values.get(2) ) * xfactor);
            ret.Height = (int)(Double.parseDouble( values.get(3) ) * yfactor);
        }


        return ret;
    }

    public  ChapterDataSource LoadChapterDataSource(String bookPath, String filepath)
    {
        ArrayList<SectionDataSource> sectionListDataSource = new ArrayList<SectionDataSource>();
        ChapterDataSource chapter = new ChapterDataSource();

        PList chapterIndex = new PList(LoadContent(bookPath + filepath));
        ArrayList<Object> chapterList =  (ArrayList<Object>) ( chapterIndex.get("chapter") ) ;
        for (Object chapterObj : chapterList)  {
            PList sectionPlist = (PList) chapterObj;
            String sectionName =  (String)sectionPlist.get("sectionName");
            ArrayList<Object> sectionList =  (ArrayList<Object>)sectionPlist.get("section");

            ArrayList<PageDataSource> pageList = new ArrayList<PageDataSource>();
            SectionDataSource section = new SectionDataSource();
            section.Title = "Section1";

            for (Object sectionObj: sectionList)
            {
                PList pagePlist = (PList) sectionObj;
                String contentDir =  (String)pagePlist.get("PageContentDir");
                contentDir += "/";
                String pagePlistFile = bookPath + contentDir + "index.xml";

                PageDataSource page = new PageDataSource();
                page.ThumbSource = bookPath + contentDir + "thumbnail.jpg";
                page.MediumSource  = bookPath + contentDir + "thumbnail.jpg";
                page.FullSource = bookPath + contentDir + "fullpage.jpg";
                pageList.add(page);
                //page = LoadPageDataSource(bookPath + contentDir, "index.xml");

                PList plistlayers = new PList( LoadContent(pagePlistFile) );
                ArrayList<Object> layers =  (ArrayList<Object>) plistlayers.get("array");
                ArrayList<NSPage> slides = new ArrayList<NSPage>();

                page.Slides = slides;

                if (layers != null)
                {
                    for (Object plistObj : layers)
                    {
                        if (plistObj instanceof String )
                            continue;
                        PList plist = (PList)plistObj;
                        NSPage slide = new NSPage();
                        slide.BackgroundFrame = StringToFrame(  (String) plist.get("frame"));
                        slide.BackgroundImage = bookPath + contentDir + plist.get("image");
                        slide.BackgroundImage = slide.BackgroundImage.replace(".png", ".jpg");

                        ArrayList<NSLayer> AllLayers = new ArrayList<NSLayer>();
                        slide.Layers = AllLayers;
                        if (plist.containsKey("children") == true)
                        {
                            ArrayList<Object> sub_layers =  (ArrayList<Object>)plist.get("children") ;
                            for (int j = 0; j < sub_layers.size(); j++)
                            {
                                PList child =  (PList)sub_layers.get(j);
                                NSLayer layer = new NSLayer();

                                layer.ThumbFrame = StringToFrame( (String) child.get("frame"));
                                layer.ThumbPath = bookPath + contentDir + (String) child.get("image");
                                layer.ThumbPath = layer.ThumbPath.replace(".png", ".jpg");

                                layer.Type = DataSourceType.IMAGE;

                                if (child.containsKey("glosary"))
                                {
                                    ArrayList<Object> items =  ( ArrayList<Object>)child.get("glosary");
                                    if (items.size() == 2)
                                    {
                                        ArrayList<Object> pglosary = (ArrayList<Object>) child.get("glosary") ;
                                        PList plistfull =  (PList)pglosary.get(0) ;
                                        layer.Largeframe = StringToFrame( (String)plistfull.get("frame"));
                                        layer.LargePath = bookPath + contentDir + plistfull.get("text");
                                        layer.LargePath = layer.LargePath.replace(".png", ".jpg");

                                        pglosary = (ArrayList<Object>)child.get("glosary");
                                        plistfull = (PList)pglosary.get(1);;
                                        layer.CroppedFrame = StringToFrame( (String)plistfull.get("frame"));


                                        if (  sectionList.size() <= 3 && pageList.size() < 4 )
                                        {
                                            PageDataSource tmpPage = new PageDataSource();
                                            tmpPage.FullSource = layer.LargePath;
                                            tmpPage.MediumSource = layer.LargePath;
                                            tmpPage.ThumbSource = layer.LargePath.replace("full.jpg", "full.jpg"); // was replaec thumb.jpg


                                            ArrayList<NSPage> tmpSlides = new ArrayList<NSPage>();
                                            NSPage slide_tmp = new NSPage();
                                            slide_tmp.BackgroundFrame = layer.Largeframe;
                                            slide_tmp.BackgroundImage = layer.LargePath;
                                            slide_tmp.Layers = new ArrayList<NSLayer>();
                                            tmpSlides.add(slide_tmp);

                                            tmpPage.Slides = tmpSlides;
                                            pageList.add(tmpPage);
                                        }
                                    }
                                    else if (items.size() == 1)
                                    {
                                        ArrayList<Object> pglosary = (ArrayList<Object>)child.get("glosary");

                                        PList plistfull = (PList) pglosary.get(0);;
                                        layer.Largeframe = StringToFrame( (String)plistfull.get("frame") );
                                        layer.LargePath =  (String)(plistfull.get("text"));

                                        if (layer.LargePath.contains("map") ) {
                                            layer.Type = DataSourceType.MAP;
                                        }
                                        else if (layer.LargePath.contains("video") ) {
                                            layer.Type = DataSourceType.VIDEO;
                                        }
                                        else if (layer.LargePath.contains("http"))
                                        {
                                            layer.Type = DataSourceType.VIEW360;
                                        }
                                    }
                                    else
                                    {
                                        layer.CroppedFrame = null;
                                        layer.Largeframe = null;
                                    }
                                    if (layer.Largeframe == null  && layer.CroppedFrame == null) {
                                        layer.Type = DataSourceType.BACKGROUND;
                                    }

                                    if (layer.CroppedFrame == null) {
                                        layer.Type = DataSourceType.TEXT;
                                    }

                                }

                                if (layer.ThumbFrame != null)
                                {
                                    layer.ThumbPath = layer.ThumbPath.replace(".png", ".jpg");
                                }
                                if (layer.LargePath != null)
                                {
                                    layer.LargePath = layer.LargePath.replace(".png", ".jpg");
                                }
                                if (layer.Largeframe == null && layer.CroppedFrame == null) {
                                    layer.ThumbPath = layer.ThumbPath.replace(".jpg", ".png");
                                }

                                AllLayers.add(layer);
                            }
                        }
                        slides.add(slide);
                    }
                }

                //page.ContentDir = bookPath + contentDir ;
                //page.ContentFile = "index.xml";
            }
            section.Pages = pageList;
            sectionListDataSource.add(section);
            //sectionListDataSource.Add(section);
        }

        chapter.Title = "Chapter 1";
        chapter.Sections = sectionListDataSource;
        return chapter;
    }


    static float res_width = 1600.0f;

    static float res_heigth = 900.0f;
}
