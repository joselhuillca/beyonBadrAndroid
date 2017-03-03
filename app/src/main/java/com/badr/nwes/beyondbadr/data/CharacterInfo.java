package com.badr.nwes.beyondbadr.data;

/**
 * Created by Kelvin on 2016-10-05.
 */
public class CharacterInfo
{
    public CharacterInfo(String uniqueId, String title, int age, float height, float weight, String subtitle, String imagePath, String description)
    {
        this.UniqueId = uniqueId;
        this.Name = title;
        Age = age;
        Height = height;
        Weight = weight;
        this.Personality = subtitle;
        this.GroupName = description;
        this.ImagePath = imagePath;
      //  this.ResourceID = 0;
    }

    //public  int ResourceID;

    public final String UniqueId;

    public final String Name;

    public final int Age;

    public final float Height;

    public final float Weight;

    public final String Personality;

    public final String GroupName;

    public final String ImagePath;


  /*  "UniqueId": "1",
            "Name": "Abu Bakr As-Siddiq",
            "Personality": "Muslim",
            "ImagePath": "levelzero/images/01.jpg",
            "Age": 51,
            "Height": 1.84,
            "Weight": 90,
            "Personality": "Truthful, honorable, faithful, veracious, extraordinary memory, courageous, brave, humble and feared warrior due to his strategies and lethal skills with multiple weapons. Abu Bakr As-Siddiq was chosen to become the first caliph in Islam after the death of Prophet Muhammad(SAW) in 634 CE!"
*/
}
