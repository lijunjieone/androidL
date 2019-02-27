package com.y.b.tools;

import android.database.Cursor;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Copy a part of codes from IOUtils in common-io
 * @author John Lee
 * @version 2015-04-07
 */
public final class IOUtils {
    public static final int BUFFER = 4096;

    /**
     * 关于流
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    /**
     * 关闭Cursor
     * @param cursor
     */
    public static void closeQuietly(Cursor cursor) {
       if (null != cursor && !cursor.isClosed()) {
            cursor.close();
       }
    }

    /**
     * InputStream 转为OutputStream
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        final byte[] buffer = new byte[BUFFER];
        long count = 0L;
        int len;
        while ((len = input.read(buffer)) > 0) {
            output.write(buffer, 0, len);
            count += len;
        }
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * String 转为 OutputStream
     * @param data
     * @param output
     * @throws IOException
     */
    public static void write(String data, OutputStream output) throws IOException {
        output.write(data.getBytes("UTF-8"));
    }

    /**
     * InputStream转换为字节数组
     * @param in
     * @return
     */
    public static byte[] toByteArray(InputStream in) {
        if (in == null){
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            closeQuietly(in);
            closeQuietly(out);
        }
        return null;
    }

    /**
     * InputStream转换为String
     * @param ins
     * @return
     */
    public static String toString(InputStream ins) {
        ByteArrayOutputStream byteOus = null;
        try {
            byteOus = new ByteArrayOutputStream();
            byte[] tmp = new byte[1024];
            int size = 0;
            while ((size = ins.read(tmp)) != -1) {
                byteOus.write(tmp, 0, size);
            }
            byteOus.flush();
            return byteOus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeQuietly(byteOus);
            closeQuietly(ins);
        }
        return null;
    }

    /**
     * 将String转换成InputStream
     * @param in String 不能为null。
     * @return
     */
    public static InputStream stringToInputStream(String in){
        if (in == null){
            return null;
        }

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        return is;
    }

    /**
     * 数据解压缩
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decompressZip(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decompressZip(bais, baos);
        data = baos.toByteArray();

        baos.flush();
        baos.close();
        bais.close();

        return data;
    }

    /**
     * 压缩数据
     * @param data
     * @return
     * @throws IOException
*/
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 压缩
        compress(bais, baos);

        byte[] output = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return output;
    }

    /**
     * 数据压缩
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public static void compress(InputStream is, OutputStream os)
            throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        //gos.finish();//在4.4手机上必须要finish不能比flush先调用
        gos.flush();
        gos.close();
    }

    /**
     * 数据解压缩
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decompress(bais, baos);
        data = baos.toByteArray();

        baos.flush();
        baos.close();
        bais.close();

        return data;
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public static void decompress(InputStream is, OutputStream os)
            throws IOException {
        GZIPInputStream gis = new GZIPInputStream(is);
        try {
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = gis.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        }finally {
            closeQuietly(gis);
        }

//        gis.close();
    }

    /**
     * 数据解压缩
     * @param is
     * @param os
     * @throws IOException
     */
    public static void decompressZip(InputStream is, OutputStream os)
            throws IOException {
        InflaterInputStream zis = new InflaterInputStream(is);

        try {

            int count;
            byte data[] = new byte[BUFFER];
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        }finally {

            closeQuietly(zis);
        }

//        zis.close();
    }
}
