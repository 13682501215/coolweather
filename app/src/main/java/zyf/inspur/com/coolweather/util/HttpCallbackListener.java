package zyf.inspur.com.coolweather.util;

import java.io.InputStream;

/**
 * Created by zyf on 2016/8/24.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
