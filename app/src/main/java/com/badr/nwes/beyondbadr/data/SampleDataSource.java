package com.badr.nwes.beyondbadr.data;


import android.content.Context;
import android.content.res.AssetManager;

import com.badr.nwes.beyondbadr.R;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by Kelvin on 2016-10-03.
 */
public class SampleDataSource
{

    private CharacterCollection AllGroups;

    public ArrayList<String> getBackGrounds()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++) {
            ret.add(AllGroups.get(i).ImagePath);
        }
        return ret;
    }

    public ArrayList<String> getTitles()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Name);
        }
        return ret;
    }

    public ArrayList<String> getSubTitles()
    {

        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Personality);
        }
        return ret;
    }

    public ArrayList<String> getThemes()
    {

        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            //ret.add(AllGroups.get(i).UniqueId);
            ret.add("amarillo");
        }
        return ret;
    }



    public ArrayList<String> getLocalidad()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Name);
        }
        return ret;

    }
    public ArrayList<String> getUbicacion()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Age + "");
        }
        return ret;
    }

    public ArrayList<String> getClima()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Height + "");
        }
        return ret;
    }

    public ArrayList<String> getAltitud()
    {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < AllGroups.size(); i++)
        {
            ret.add(AllGroups.get(i).Weight + "");
        }
        return ret;
    }


    public SampleDataSource(Context context)  {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CharacterCollection> jsonAdapter = moshi.adapter(CharacterCollection.class);

        AssetManager assetManager = context.getAssets();
        String path = "badr_characters.json";
        CharacterCollection collection = null;
        try {
            InputStream is = assetManager.open(path);

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer);

            collection = jsonAdapter.fromJson(json);

        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }

        Random rand = new Random();
        List<CharacterInfo> listOfCharacters = new ArrayList<CharacterInfo>(28);

        for (int i = 0; i < collection.AllCharacters.size(); i++){
            listOfCharacters.add(collection.AllCharacters.get(i));
        }
        for (int i = 0; i < collection.AllCharacters.size(); i++){
            listOfCharacters.add(collection.AllCharacters.get(i));
        }

        this.AllGroups = new CharacterCollection(listOfCharacters);

    }
}
