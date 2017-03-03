package com.badr.nwes.beyondbadr.reader.content;

import com.badr.nwes.beyondbadr.data.BookDataSource;
import com.badr.nwes.beyondbadr.index.InfScrollPaged;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kelvin on 2016-10-14.
 */
public class DataSourceMapper{
    public static int[] DS_LISTA_CHAPTER = null;
    public static int[] DS_LISTA_SECTION = null;
    public static int[] DS_LISTA_IMAGE = null;
    public static Map<String, Integer> MAPPER_CSI;

    public static int NUM_OF_ELEMS = 0;
    public static void InitIndex (){
        MAPPER_CSI = new HashMap<String, Integer>();
        BookDataSource book =  InfScrollPaged.book;
        NUM_OF_ELEMS = 0;
        for (int chapter_ind = 0  ; chapter_ind < book.Chapters.size(); ++ chapter_ind)
            for ( int section_ind = 0; section_ind < book.Chapters.get(chapter_ind).Sections.size(); ++ section_ind)
                for ( int image_ind = 0; image_ind < book.Chapters.get(chapter_ind).Sections.get(section_ind).Pages.size(); ++ image_ind)
                    NUM_OF_ELEMS ++;

        DS_LISTA_CHAPTER= new int[NUM_OF_ELEMS];
        DS_LISTA_SECTION = new int[NUM_OF_ELEMS];
        DS_LISTA_IMAGE = new int[NUM_OF_ELEMS];
        NUM_OF_ELEMS = 0;
        String key_csi = "";
        for (int chapter_ind = 0  ; chapter_ind < book.Chapters.size(); ++ chapter_ind){
            for ( int section_ind = 0; section_ind < book.Chapters.get(chapter_ind).Sections.size(); ++ section_ind){
                for ( int image_ind = 0; image_ind < book.Chapters.get(chapter_ind).Sections.get(section_ind).Pages.size(); ++ image_ind){
                    key_csi = chapter_ind + "_" + section_ind + "_" + image_ind;
                    MAPPER_CSI.put(key_csi, NUM_OF_ELEMS);
                    DS_LISTA_CHAPTER[NUM_OF_ELEMS] = chapter_ind;
                    DS_LISTA_SECTION[NUM_OF_ELEMS] = section_ind;
                    DS_LISTA_IMAGE[NUM_OF_ELEMS++] = image_ind;
                }
            }
        }
        //Log.Info("cont" , "cont = " + NUM_OF_ELEMS);
    }
}
