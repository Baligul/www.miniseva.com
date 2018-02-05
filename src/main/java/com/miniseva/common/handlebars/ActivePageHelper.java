package com.miniseva.common.handlebars;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

@HandlebarsHelper
public class ActivePageHelper {
    public ActivePageHelper() {
    }

    public static String isActivePage(String navPage, String page) {
        if (navPage != null && navPage.equals(page))
            return "active";
        else
            return "";
    }
}
