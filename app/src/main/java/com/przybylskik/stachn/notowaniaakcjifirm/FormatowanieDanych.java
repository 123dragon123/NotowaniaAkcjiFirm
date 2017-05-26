package com.przybylskik.stachn.notowaniaakcjifirm;

import java.util.Vector;

public class FormatowanieDanych //Ta klasa pozwala podzielić dane wierszu tabeli na osobne parametry
{
    public Vector<String> sformatowac(String dane)
    {
        Vector<String> sformatowane = new Vector<>();
        String line;
        int pos = 0;
        for(int i=0; i<dane.length(); i++)
        {
            if(dane.charAt(i)==',')
            {
                line = dane.substring(pos, i);
                sformatowane.add(line);
                pos = i+1;
            }
        }
        return  sformatowane;
    }
}
