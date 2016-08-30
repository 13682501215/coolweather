package zyf.inspur.com.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zyf.inspur.com.coolweather.R;
import zyf.inspur.com.coolweather.model.City;
import zyf.inspur.com.coolweather.util.HttpCallbackListener;
import zyf.inspur.com.coolweather.util.HttpUtil;
import zyf.inspur.com.coolweather.util.LogUtil;
import zyf.inspur.com.coolweather.util.Utility;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.weather_layout);
        //        初始化各个控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        switchCity= (Button) findViewById(R.id.switch_city);
        refreshWeather= (Button) findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        String countyCode = getIntent().getStringExtra("county_code");
        LogUtil.log(LogUtil.TAG, "WeatherActivity onCreate() countyCode is " + countyCode, LogUtil.DEBUG);
        if (!TextUtils.isEmpty(countyCode)) {
//            有县级代号就去查询天气
            publishText.setText("同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
//            没有县级代号就直接显示本地天气
            showWeather();
        }
    }

    /**
     * Mehtod: showWeather <br>
     * Description: 从SharePreference文件中读取存储的天气信息，并显示到界面上 <br>
     * Creator: zyf <br>
     * Date: 2016/8/29 16:45
     */
    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name", ""));
        temp1Text.setText(preferences.getString("temp1", ""));
        temp2Text.setText(preferences.getString("temp2", ""));
        weatherDespText.setText(preferences.getString("weather_desp", ""));
        publishText.setText("今天" + preferences.getString("publish_time", "") + "发布");
        currentDateText.setText(preferences.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    /**
     * Mehtod: queryWeatherCode <br>
     * Description: 查询县级代号对应的天气代号 <br>
     * Creator: zyf <br>
     * Date: 2016/8/29 16:28
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * Mehtod: queryFromServer <br>
     * Description: 根据传入的地址和类型去向服务器查询天气代号或者天气信息 <br>
     * Creator: zyf <br>
     * Date: 2016/8/29 16:31
     */
    private void queryFromServer(final String address, final String type) {
        LogUtil.log(LogUtil.TAG, "WeatherActivity queryFromServer() address is:\n" + address + "\ntype is " + type);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.log(LogUtil.TAG, "WeatherActivity queryFromServer onFinish() type is " + type);
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(type)) {
//                       从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
//                    处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败。。。");
                    }
                });
            }
        });
    }

    /**
     * Mehtod:queryWeatherInfo <br>
     * Description: 查询天气代号所对应的天气 <br>
     * Creator: zyf <br>
     * Date: 2016/8/29 16:42
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.switch_city:
                Intent intent=new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中。。。");
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode=preferences.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
