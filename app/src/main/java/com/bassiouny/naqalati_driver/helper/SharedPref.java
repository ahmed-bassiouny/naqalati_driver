package com.bassiouny.naqalati_driver.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bassiouny on 12/11/17.
 */

public class SharedPref {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String fullDataKey = "full_data";
    public static final String TokenKey = "token";
    public static final String UpdatedTokenKey = "updated_token";
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
    public static void setToken(Context context,String token){
        init(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TokenKey,token);
        editor.putBoolean(UpdatedTokenKey,false);
        editor.apply();
    }
    public static void setUpdatedToken(Context context){
        init(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(UpdatedTokenKey,true);
        editor.apply();
    }
    public static String getToken(Context context){
        init(context);
        return sharedpreferences.getString(TokenKey,"");
    }
    public static boolean updatedToken(Context context){
        init(context);
        return sharedpreferences.getBoolean(UpdatedTokenKey,false);
    }
}
