package com.miniseva.configuration;

public class Configuration {
    // Pagination: Number of items to display per page
    public static int PAGE_SIZE = 30;

    // Forms

    public static final String ACTION_PUBLISH = "publish";
    public static final String ACTION_UNPUBLISH = "unpublish";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_EDIT_HTML = "edit html";
    public static final String ACTION_PREVIEW = "preview";
    public static final String ACTION_ADD_EXISTING = "add existing";
    public static final String ACTION_ADD_NEW = "add new";

    // URLs
    // ====

    public static final String URL_ADMIN_HOME = "/admin";

    // Accounts

    public static final String URL_ADMIN_ACCOUNTS = "/admin/accounts";

    // Signup

    public static final String URL_SIGNUP = "/signup";
}