package com.badr.nwes.beyondbadr.data;

import java.util.List;

/**
 * Created by Kelvin on 2016-10-05.
 */

public final class CharacterCollection {

    public final List<CharacterInfo> AllCharacters;


    public CharacterCollection(List<CharacterInfo> allCharacters) {
        AllCharacters = allCharacters;
    }

    public int size () {
        return AllCharacters.size();
    }
    public CharacterInfo get (int index) {
        return AllCharacters.get(index);
    }
}
