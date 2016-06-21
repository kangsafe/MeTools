package me.tools.encoder;
/**
 * Copyright (C) 2010 The Android Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * @author long
 *
 */
public class Base64Helper {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();

    /**
     * toBase64:(将图片转成base64 字符串).
     *
     * @param bitmap 图片Bitmap
     * @return
     * @author kangsafe@163.com
     * @since JDK 1.6
     */
    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return toBase64(baos);
    }

    /**
     * toBase64:(将图片转成base64 字符串).
     *
     * @param input 字节数组输出流
     * @return
     * @author kangsafe@163.com
     * @since JDK 1.6
     */
    public static String toBase64(ByteArrayOutputStream input) {
        return encodeToString(input.toByteArray());
    }

    /**
     * encodeFile:(将文件转成base64 字符串).
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    @NonNull
    public static String encodeFile(String path) throws IOException {
        File file = new File(path);
        return encodeFile(file);
    }

    /**
     * encodeFile:(将文件转成base64 字符串).
     *
     * @param file 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    @NonNull
    public static String encodeFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return encodeFile(fileInputStream);
    }

    /**
     * encodeFile:(将文件转成base64 字符串).
     *
     * @param fileInputStream 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    @NonNull
    public static String encodeFile(FileInputStream fileInputStream) throws IOException {
        byte[] buffer = new byte[fileInputStream.available()];
        fileInputStream.read(buffer);
        fileInputStream.close();
        return encodeToString(buffer);
    }

    public static String encodeToString(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * decodeFile:(将base64字符解码保存文件).
     *
     * @param base64Code 编码后的字串
     * @param savePath   文件保存路径
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static void decodeFile(String base64Code, String savePath) throws Exception {
        //byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = decodeFile(base64Code);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    public static byte[] decodeFile(String base64Code) {
        return Base64.decode(base64Code, Base64.NO_WRAP);
    }

    public static Bitmap decodeFile(byte[] input) {
        return BitmapFactory.decodeByteArray(input, 0, input.length);
    }

    public static Bitmap fromBase64(String base64Code) {
        return decodeFile(decodeFile(base64Code));
    }

    @NonNull
    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }

    public static byte[] decode(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err.println("Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18)
                    + (decode(s.charAt(i + 1)) << 12)
                    + (decode(s.charAt(i + 2)) << 6)
                    + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }
}