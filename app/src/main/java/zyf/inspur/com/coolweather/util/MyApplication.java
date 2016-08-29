package zyf.inspur.com.coolweather.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by zyf on 2016/8/25.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
