package com.badr.nwes.beyondbadr.TabSmartphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SystemConfiguration;
import com.badr.nwes.beyondbadr.adapters.GeneralImageItemDataAdapter;
import com.badr.nwes.beyondbadr.adapters.ImageItemData;
import com.badr.nwes.beyondbadr.data.BookDataSource;
import com.badr.nwes.beyondbadr.data.ChapterDataSource;
import com.badr.nwes.beyondbadr.data.PListLoader;
import com.badr.nwes.beyondbadr.data.SectionDataSource;
import com.telerik.widget.list.RadListView;
import com.telerik.widget.list.SlideItemAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Citec on 28/11/2016.
 */

public class TabMore extends Fragment {

    public static final BookDataSource book = new BookDataSource();
    private int num_of_chapters;
    private int[]init_pos_chapter_;
    private int[]end_pos_chapter_;
    private int temp_n_childs=0;
    private int scale_dx_=0;
    int size_widht;
    int size_height;
    int numeroThumbalis_inScreen;

    private static final int PAGE_COUNT = 5;
    private RadListView firstList_characters;
    private RadListView secondtList_comic;
    private RadListView ThirdList_Art;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_more, container, false);
        context = this.getActivity();
        Bitmap input_image_bitmap = SystemConfiguration.decodeSampledBitmapFromResource("chapter2.jpg", 0,0,context);
        rootView.setBackground(new BitmapDrawable(input_image_bitmap));

        //SET - RADLIST --------------------------------------------------------------
        LinearLayoutManager lmngr = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        this.firstList_characters = (RadListView) rootView.findViewById(R.id.lvFirst);
        SlideItemAnimator slideAnimator = new SlideItemAnimator();
        slideAnimator.setAnimateInDirection(SlideItemAnimator.DIRECTION_TOP);
        this.firstList_characters.setItemAnimator(slideAnimator);

        this.firstList_characters.setLayoutManager(lmngr);

        //-------
        lmngr = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        this.secondtList_comic = (RadListView) rootView.findViewById(R.id.lvSecond);
        this.secondtList_comic.setLayoutManager(lmngr);
        slideAnimator = new SlideItemAnimator();
        slideAnimator.setAnimateInDirection(SlideItemAnimator.DIRECTION_TOP);
        this.secondtList_comic.setItemAnimator(slideAnimator);

        //------
        lmngr = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        this.ThirdList_Art = (RadListView) rootView.findViewById(R.id.lvThrid);
        this.ThirdList_Art.setLayoutManager(lmngr);
        slideAnimator = new SlideItemAnimator();
        slideAnimator.setAnimateInDirection(SlideItemAnimator.DIRECTION_TOP);
        this.ThirdList_Art.setItemAnimator(slideAnimator);

        //FIN  SET -RADLIST --------------------------------------------------------

        //IMPORTANTE init_data antes del adapter
        init_data();

        //ADAPTADORES ----------------------------------------------------------------
        GeneralImageItemDataAdapter adapter = new GeneralImageItemDataAdapter(
                getListOfItemData(1),
                TabMore.this.getActivity(),
                R.layout.general_image_item_data);
        firstList_characters.setAdapter(adapter);

        //------
        adapter = new GeneralImageItemDataAdapter(
                getListOfItemData(2),
                TabMore.this.getActivity(),
                R.layout.general_image_item_data);
        secondtList_comic.setAdapter(adapter);

        //------
        adapter = new GeneralImageItemDataAdapter(
                getListOfItemData(3),
                TabMore.this.getActivity(),
                R.layout.general_image_item_data);
        ThirdList_Art.setAdapter(adapter);

        //FIN ADAPTADORES ------------------------------------------------

        /*Triangle drawable = new Triangle();
        drawable.setColor(getResources().getColor(R.color.listViewHorizontalLayoutHeaderColor));
        ImageView pointerView = (ImageView)rootView.findViewById(R.id.tabPointer);
        pointerView.setImageDrawable(drawable);*/

        return rootView;
    }

    public void init_data(){
        PListLoader loader = new PListLoader(context);

        ArrayList<ChapterDataSource> chapters = new ArrayList<ChapterDataSource>();

        //Log.Info("cargado de chapters: " , "chapter: " + i );
        for (int i = 0; i < 6; i++) {
            ChapterDataSource chapter = loader.LoadChapterDataSource("", "chapter" + (i + 1) + ".xml");
            chapters.add(chapter);
            Log.d("chapter",String.format("%d",chapter.Sections.size()));
        }

        book.Chapters = chapters;

        num_of_chapters = book.Chapters.size();
        init_pos_chapter_ = new int[num_of_chapters];
        end_pos_chapter_ = new int[num_of_chapters];


        numeroThumbalis_inScreen = 2;
        scale_dx_ = (int) ( (1.0 * size_widht) / numeroThumbalis_inScreen );

        size_widht = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();
        size_height = this.getActivity().getWindowManager().getDefaultDisplay().getHeight()/  SystemConfiguration.SCREEN_HEIGHT_DIVIDER;
    }

    private List<ImageItemData> getListOfItemData(int ind_chapter) {
        List<ImageItemData> imagesItems = new ArrayList<>();
        if (ind_chapter>num_of_chapters)return null;

        ChapterDataSource ch = book.Chapters.get(ind_chapter);
        int num_of_sections = ch.Sections.size();
        if(ind_chapter > 0) init_pos_chapter_[ind_chapter] = end_pos_chapter_[ind_chapter - 1];
        if(ind_chapter > 0) end_pos_chapter_[ind_chapter] = end_pos_chapter_[ind_chapter - 1];

        int cont = 0;
        for(int ind_section = 0 ; ind_section <num_of_sections; ind_section++) {
            SectionDataSource sec = ch.Sections.get(ind_section);
            int number_r =sec.Pages.size();
            end_pos_chapter_[ind_chapter] += (number_r * scale_dx_);
            for (int ind_image = 0; ind_image <number_r ;  ind_image++) {
                imagesItems.add(new ImageItemData("name", "descrip", context, sec,ind_chapter,ind_section, ind_image, size_widht, size_height, numeroThumbalis_inScreen,end_pos_chapter_,number_r,cont));
                cont++;
            }
        }

        return imagesItems;
    }

}
