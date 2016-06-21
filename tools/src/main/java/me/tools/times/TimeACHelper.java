package me.tools.times;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2016/6/21 0021.
 */
public class TimeACHelper {
    /**
     * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 1.创建一个URL对象
     * 2.通过URL对象，创建一个HttpURLConnection对象 3.得到InputStram 4.从InputStream当中读取数据
     *
     * @param urlStr
     * @return
     * @author long
     */
    public static String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            // 创建一个URL对象
            URL url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /* @author long
    * @param args
    */
    public static void main(String[] args) {

        String url = "http://www.time.ac.cn/stime.asp";
        String content = download(url);
        Pattern p1 = Pattern.compile("hrs\\s+=\\s+\\d{1,2}");
        Pattern p2 = Pattern.compile("min\\s+=\\s+\\d{1,2}");
        Pattern p3 = Pattern.compile("sec\\s+=\\s+\\d{1,2}");
        Matcher m1 = p1.matcher(content);
        Matcher m2 = p2.matcher(content);
        Matcher m3 = p3.matcher(content);
        if (m1.find()) {
            System.out.println("the hour is---->"
                    + m1.group().replace("hrs = ", ""));
        }
        if (m2.find()) {
            System.out.println("the hour is---->"
                    + m2.group().replace("min = ", ""));
        }
        if (m3.find()) {
            System.out.println("the hour is---->"
                    + m3.group().replace("sec = ", ""));
        }
    }

}
