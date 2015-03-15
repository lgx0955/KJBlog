package org.kymjs.blog.api;

import org.kymjs.blog.AppConfig;
import org.kymjs.blog.bean.WeatherForecast;
import org.kymjs.blog.bean.WeatherToday;
import org.kymjs.blog.utils.JsonParser;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.KJLoger;

/**
 * Created by lody  on 2015/3/14.
 *
 * 单元测试:PASS
 */
public class WeatherApi {
    private static final String TAG = WeatherApi.class.getSimpleName();
    /** 今日天气 */
    private static final String WEATHER_TODAY_SUFFIX_URL = "/data/2.5/weather";
    /**天气预告*/
    private static final String WEATHER_FORECAST_SUFFIX_URL = "/data/2.5/forecast/daily";


    /**Base URL*/
    public static String base_url = "http://api.openweathermap.org";

    public static void getTodayWeather(final String city,String lang, final WeatherCallBack.TodayWeatherCallBack callBack){

        HttpConfig config = new HttpConfig();
        config.maxRetries = 4;// 出错重连次数

        HttpParams params = new HttpParams();
        params.put("q",city);
        params.put("appid", AppConfig.APPID);
        params.put("lang",lang);
        params.put("units","metric");

        final KJHttp http = new KJHttp(config);

        http.get(getAbsoluteUrl(WEATHER_TODAY_SUFFIX_URL), params, new HttpCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                callBack.onfailture(strMsg);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                KJLoger.debug(content);
                WeatherToday today = JsonParser.fromJsonObject(content, WeatherToday.class);
                callBack.onTodayWeatherSearchSuccess(today);
            }
        });
    }

    public static void getWeatherForecast(String city,String lang,int cnt,  final WeatherCallBack.WeatherrForecastCallBack callBack){

        HttpConfig config = new HttpConfig();
        config.maxRetries = 4;// 出错重连次数

        HttpParams params = new HttpParams();
        params.put("q",city);
        params.put("appid",AppConfig.APPID);
        params.put("lang",lang);
        params.put("units","metric");
        params.put("cnt",String.valueOf(cnt));

        final KJHttp http = new KJHttp(config);

        http.get(getAbsoluteUrl(WEATHER_FORECAST_SUFFIX_URL), params, new HttpCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                callBack.onfailture(strMsg);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                KJLoger.debug(content);
                WeatherForecast forecast = JsonParser.fromJsonObject(content, WeatherForecast.class);
                callBack.onWeatherrForecastSearchSuccess(forecast);

            }
        });
    }



    /**
     *
     * @param relativeUrl
     * @return 完整http路径
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        if(relativeUrl.startsWith("http")){
            return relativeUrl;
        }
        String url = base_url + relativeUrl;
        KJLoger.debug(url);
        return url;
    }

}
