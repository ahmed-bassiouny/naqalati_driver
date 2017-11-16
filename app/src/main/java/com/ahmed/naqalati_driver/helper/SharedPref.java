package com.ahmed.naqalati_driver.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bassiouny on 12/11/17.
 */

public class SharedPref {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String fullDataKey = "full_data";
    private static SharedPreferences sharedpreferences;

    private static void init(Context context){
        if(sharedpreferences==null)
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public static void setFullData(Context context,boolean userSetFullData){
        init(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(fullDataKey,userSetFullData);
        editor.apply();
    }
    public static Boolean isUserSetFullData(Context context){
        init(context);
        return sharedpreferences.getBoolean(fullDataKey,false);
    }
}
