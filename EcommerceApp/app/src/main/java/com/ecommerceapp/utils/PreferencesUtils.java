package com.ecommerceapp.utils;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class PreferencesUtils {

    public static final String RESPONSE = "response";
    public static final String CATEGORIES_PRODUCTS = "categories_products";
    public static final String RANKINGS = "rankings";

    /**
     * Used to update dash board menu preferences.
     * @param prefStringName - attribute to update in dashboard preferences.
     * @param data - data in string format which will be updated.
     */
    public static void updateResponse(Context context, String prefStringName, JSONArray data){
        SharedPreferences mDashBoardPref = context.getSharedPreferences(RESPONSE, Context.MODE_PRIVATE);
        SharedPreferences.Editor dashBoardEditor = mDashBoardPref.edit();
        dashBoardEditor.putString(prefStringName, data.toString());
        dashBoardEditor.apply();
    }

    // Used to get Dashboard menus data
    public static String getResponse(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RESPONSE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
