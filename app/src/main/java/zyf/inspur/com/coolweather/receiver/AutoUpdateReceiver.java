package zyf.inspur.com.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import zyf.inspur.com.coolweather.service.AutoUpdateService;

/**
 * Created by zyf on 2016/8/30.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context, AutoUpdateService.class);
        context.startService(intent1);
    }
}
