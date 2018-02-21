package com.kaerenabo.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleManager {

    public static Context updateResources(Context context) {
        String language = Utils.getStringFromPref(context, Constant.PREF_LOCALE);
        if(language== null)
            language= "en";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Context context){
        String language = Utils.getStringFromPref(context, Constant.PREF_LOCALE);
        if(language== null)
            language= "en";
        Locale locale = new Locale(language);
        return locale;
    }
}