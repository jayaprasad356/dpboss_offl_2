package com.betplay.dpboss_off;

import android.content.Context;

public class constant {
    // DO NOT EDIT
    static String prefs = "codegente";


    ///// CONFIGURATION /////

    // API FOLDER URL
    //static String prefix = "http://matkaguru99.com/Balaji_Games/api/";
    static String prefix = "https://smsattaadmin.dpbossmatka.info/api/";

    // APK DOWNLOAD LINK
    //static String link = "http://matkaguru99.com/Balaji_Games/";
    static String link = "https://smsattaadmin.dpbossmatka.info/";

    // PROJECT ROOT URL
    static String project_root = "https://smsattaadmin.dpbossmatka.info/";

    static final String UPI_ID_VAL = "Merchant80388.augp@aubank";

    // MIN AMOUNT ALLOWED IN TOTAL FOR BETTING
    static int min_total = 1;

    // MAX AMOUNT ALLOWED IN TOTAL FOR BETTING
    static int max_total = 10000;

    // MIN AMOUNT ALLOWED FOR SINGLE BET
    static int min_single = 1;

    // MAX AMOUNT ALLOWED FOR SINGLE BET
    static int max_single = 10000;

    // MIN DEPOSIT AMOUNT THROUGH PAYMENT GATEWAY
    static int min_deposit = 500;

    static public String getWhatsapp(Context context){

        String number = context.getSharedPreferences(constant.prefs,Context.MODE_PRIVATE).getString("whatsapp","");
        if (number.equals("")){
            return "";
        }
        if (number.contains("+91")){
            return  "http://wa.me/"+context.getSharedPreferences(constant.prefs,Context.MODE_PRIVATE).getString("whatsapp",null);
        } else {
            return  "http://wa.me/+91"+context.getSharedPreferences(constant.prefs,Context.MODE_PRIVATE).getString("whatsapp",null);
        }

    }

}
