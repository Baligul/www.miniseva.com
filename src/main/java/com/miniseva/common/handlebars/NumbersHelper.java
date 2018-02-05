package com.miniseva.common.handlebars;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

@HandlebarsHelper
public class NumbersHelper {
    public NumbersHelper() {
    }

    public static String numberValueAttribute(Integer val) {
        if (val != null && val >= 0)
            return "value=\"" + val + "\"";
        else
            return "";
    }

//    public static CharSequence exists(Integer num) {
//        if (num != null)
//            return "true";
//        else
//            return "false";
//    }
//
//    public static CharSequence exists(String num) {
//        if (num != null && num.equals(""))
//            return "true";
//        else
//            return "false";
//    }
}
