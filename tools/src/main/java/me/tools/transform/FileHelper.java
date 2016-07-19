package me.tools.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 2016/6/21 0021.
 */
public class FileHelper {
    /**
     * toBitmap:(drawable转换为Bitmap)
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * toBitmap:(drawable转换为Bitmap)
     *
     * @param bitmap
     * @return
     */
    public static Drawable drawableToBitmap(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        return bd;
    }

    /**
     * toBitmap:(drawable转换为Bitmap)
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap drawableToBitmap(Context context, int resId) {
        Drawable d = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            d = context.getResources().getDrawable(resId, context.getTheme());
        } else {
            d = context.getResources().getDrawable(resId);
        }
        BitmapDrawable bd = (BitmapDrawable) d;
        return bd.getBitmap();
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String dateToString(long times, String formater) {
        SimpleDateFormat df = new SimpleDateFormat(formater);
        Date date = new Date(times);
        return df.format(date);
    }

    public static String dateToString(long times) {
        return dateToString(times, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 从resource的raw读取文件数据
     *
     * @param context
     * @param resId
     * @return
     **/
    public static String rawToString(Context context, int resId) {
        String res = "";
        try {

            //得到资源中的Raw数据流
            InputStream in = context.getResources().openRawResource(resId);

            //得到数据的大小
            int length = in.available();

            byte[] buffer = new byte[length];

            //读取数据
            in.read(buffer);

            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            res = bytesToString(buffer);

            //关闭
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 从resource的asset中读取文件数据
     *
     * @param context
     * @param fileName
     * @return
     **/
    public static String readFromAsset(Context context, String fileName) {
        String res = "";
        try {

            //得到资源中的asset数据流
            InputStream in = context.getResources().getAssets().open(fileName);

            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            res = bytesToString(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 读写/data/data/<应用程序名>目录上的文件:写入文件
     *
     * @param context
     * @param fileName 文件名
     * @param writestr 写入数据的字符串
     **/
    public void writeFileToAppDataPath(Context context, String fileName, String writestr) throws IOException {
        try {
            FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读写/data/data/<应用程序名>目录上的文件:读取文件
     *
     * @param context
     * @param fileName 文件名
     **/
    public String readFileFromAppDataPath(Context context, String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = bytesToString(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 读写SD卡中的文件。也就是/mnt/sdcard/目录下面的文件 ：
     *
     * @param fileName
     * @param write_str
     **/
    //写数据到SD中的文件
    public void writeFileSdcardFile(String fileName, String write_str) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读写SD卡中的文件。也就是/mnt/sdcard/目录下面的文件 ：
     *
     * @param fileName
     **/
    //读SD中的文件
    public String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = bytesToString(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //使用File类进行文件的读写：
    //读文件
    public String readSDFile(String fileName) throws IOException {
        String res = "";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        res = bytesToString(buffer);
        fis.close();
        return res;
    }

    //写文件
    public void writeSDFile(String fileName, String write_str) throws IOException {
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = write_str.getBytes();
        fos.write(bytes);
        fos.close();
    }

    /**
     * APK资源文件的大小不能超过1M，如果超过了怎么办？我们可以将这个数据再复制到data目录下，然后再使用。复制数据的代码如下：
     **/
    public boolean assetsCopyData(Context context, String strAssetsFilePath, String strDesFilePath) {
        boolean bIsSuc = true;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        File file = new File(strDesFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 766 " + file);
            } catch (IOException e) {
                bIsSuc = false;
            }

        } else {//存在
            return true;
        }

        try {
            inputStream = context.getAssets().open(strAssetsFilePath);
            outputStream = new FileOutputStream(file);

            int nLen = 0;

            byte[] buff = new byte[1024 * 1];
            while ((nLen = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, nLen);
            }

            //完成
        } catch (IOException e) {
            bIsSuc = false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                bIsSuc = false;
            }

        }

        return bIsSuc;
    }

    /**
     * 复制文件
     *
     * @param from
     * @param to
     * @return
     **/
    public static String copyFile(String from, String to) {
        try {
            File destFile = new File(to);

            if (destFile.exists()) {
                int suffix = 1;
                String fileName = getFileName(destFile.getName());
                String fileExt = getFileExtension(destFile.getName());
                String filePath = destFile.getParentFile().getAbsolutePath() + '/';

                String newFileName = filePath + fileName + '_' + suffix + '.'
                        + fileExt;
                while (new File(newFileName).exists()) {
                    newFileName = filePath + fileName + '_' + suffix + '.'
                            + fileExt;
                    suffix++;
                }
                to = newFileName;
            }

            FileInputStream original = new FileInputStream(from);
            FileOutputStream destination = new FileOutputStream(to);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = original.read(buffer)) > 0) {
                destination.write(buffer, 0, bytesRead);
            }

            original.close();
            destination.flush();
            destination.close();
        } catch (Exception e) {
            return null;
        }
        return to;
    }

    /**
     * Get file extension of the image file
     */
    public static String getFileExtension(String filename) {
        int dotposition = filename.lastIndexOf('.');
        return filename.substring(dotposition + 1, filename.length());
    }

    /**
     * Get the name of the image file
     */
    public static String getFileName(String filename) {
        int dotposition = filename.lastIndexOf('.');
        return filename.substring(0, dotposition);
    }

    /**
     * stringToBytes:   字符串转换为字节数组
     *
     * @param str 字符串
     * @return
     */
    @NonNull
    public static byte[] stringToBytes(String str) {
        return str.getBytes();
    }

    /**
     * bytesToString:   字符串转换为字节数组
     *
     * @param data 字节数组
     * @return
     */
    public static String bytesToString(byte[] data) {
        String res = "";
        try {
            res = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * bytesToBitmap:   字节数组转换为Bitmap
     *
     * @param data 字节数组
     * @return
     */
    public static Bitmap bytesToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * bitmapToBytes:   Bitmap转换为字节数组
     *
     * @param bitmap 字节数组
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
