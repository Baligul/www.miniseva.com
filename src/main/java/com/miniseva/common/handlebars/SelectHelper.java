package com.miniseva.common.handlebars;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

@HandlebarsHelper
public class SelectHelper {
    public SelectHelper() {
    }

    public static String isSelected(String optionValue, String selectedValue) {
        if (optionValue != null && optionValue.equals(selectedValue))
            return "selected";
        else
            return "";
    }

    public static String isSelectedLong(Long optionValue, Long selectedValue) {
        if (optionValue != null && optionValue.equals(selectedValue))
            return "selected";
        else
            return "";
    }

    public static String isSelectedInt(Integer optionValue, Integer selectedValue) {
        if (optionValue != null && optionValue.equals(selectedValue))
            return "selected";
        else
            return "";
    }

    // I am unable to get the int variation of this method to work
    //    public static String isSelectedInt(int optionValue, int selectedValue) {
    //        if (optionValue == selectedValue)
    //            return "selected";
    //        else
    //            return "";
    //    }
}
