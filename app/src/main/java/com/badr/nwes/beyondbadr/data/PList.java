package com.badr.nwes.beyondbadr.data;

import org.core4j.xml.XDocument;
import org.core4j.xml.XElement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kelvin on 2016-10-07.
 */
public class PList extends HashMap<String, Object>
{

    public PList()
    {
    }

    public PList(String content)
    {
        if (content.length() > 10)
        {
            Load(content);
        }
        else
        {
            this.put("array", new ArrayList<Object>());
        }
    }



    private void Load(String content)
    {
        XDocument doc = XDocument.parse(content);

        XElement plist = doc.element("plist");
        XElement dict = plist.element("dict");
        if (dict == null)
        {
            dict = plist.element("array");

             org.core4j.Enumerable<XElement> dictElements = dict.elements();
            ArrayList<Object> list = new ArrayList<Object>();
            for (XElement e : dictElements)
            {
                Object one = ParseValue(e);
                list.add(one);
            }
            this.put("array", list);
        }
        else
        {
            org.core4j.Enumerable<XElement> dictElements = dict.elements();
            Parse(this, dictElements);
        }

    }

    private Object ParseValue(XElement val)
    {
        if (val.getName().getLocalName().equals("string"))
                return val.getValue();
        else if (val.getName().getLocalName().equals("integer"))
                return Integer.parseInt(val.getValue());
        else if (val.getName().getLocalName().equals("real"))
                return Float.parseFloat(val.getValue());
        else if (val.getName().getLocalName().equals("true"))
                return true;
        else if (val.getName().getLocalName().equals("false"))
                return false;
        else if (val.getName().getLocalName().equals("dict")) {
            PList plist = new PList();
            Parse(plist, val.elements());
            return plist;
        }
        else if (val.getName().getLocalName().equals("array")) {
            ArrayList<Object> list = ParseArray(val.elements());
            return list;
        }
        else
            throw new IllegalArgumentException("Unsupported");

    }
    private ArrayList<Object> ParseArray(Iterable<XElement> elements)
    {
        ArrayList<Object> list = new ArrayList<Object>();
        for (XElement e : elements)
        {
            Object one = ParseValue(e);
            list.add(one);
        }

        return list;
    }

    private void Parse(PList dict,  org.core4j.Enumerable<XElement> elements)
    {
        for (int i = 0; i < elements.count(); i += 2)
        {
            XElement key = elements.elementAt(i);
            XElement val = elements.elementAt(i + 1);

            dict.put(key.getValue(), ParseValue(val));
        }
    }
}
