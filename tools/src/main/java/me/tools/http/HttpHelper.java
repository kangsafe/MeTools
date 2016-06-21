package me.tools.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/21.
 */
public class HttpHelper {

    public static String getHttpArg(Map<String, String> map, boolean isSort) {
        String httpArg = "";
        if (map != null) {
            List<Map.Entry<String, String>> infoIds =
                    new ArrayList<Map.Entry<String, String>>(map.entrySet());
            if (isSort) {
                Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        //return (o2.getValue() - o1.getValue());
                        return (o1.getKey()).toString().compareTo(o2.getKey());
                    }
                });
            }
            //排序后
            for (int i = 0; i < infoIds.size(); i++) {
                httpArg.concat(String.format("%s=%s&", infoIds.get(i).getKey(), infoIds.get(i).getValue()));
                //            String id = infoIds.get(i).toString();
                //            System.out.println(id);
            }
            httpArg = httpArg.substring(0, httpArg.length() - 1);
        }
        return httpArg;
    }

    public static String getHttpArg(Map<String, String> map) {
        return getHttpArg(map, false);
    }


    /**
     * @param httpUrl :请求接口
     * @param map     :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, Map<String, String> map, boolean isSort, String httpMethod) {
        String httpArg = getHttpArg(map, isSort);
        if (!httpArg.isEmpty() && httpMethod.equals("GET")) {
            httpUrl.concat("?").concat(httpArg);
        }
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("apikey", MyApplication.key);
            connection.setDoOutput(true);
            if (!httpArg.isEmpty() && httpMethod.equals("POST")) {
                connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
            }
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param httpUrl :请求接口
     * @param map     :参数
     * @return 返回结果
     */
    public static String get(String httpUrl, Map<String, String> map) {
        return request(httpUrl, map, false, "GET");
    }

    /**
     * @param httpUrl :请求接口
     * @param map     :参数
     * @return 返回结果
     */
    public static String get(String httpUrl, Map<String, String> map, boolean isSort) {
        return request(httpUrl, map, isSort, "GET");
    }

    public static String post(String httpUrl, Map<String, String> map, boolean isSort) {
        return request(httpUrl, map, isSort, "POST");
    }


    public static String post(String httpUrl, Map<String, String> map) {
        return request(httpUrl, map, false, "POST");
    }
}
