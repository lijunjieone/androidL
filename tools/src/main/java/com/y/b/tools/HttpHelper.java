package com.y.b.tools;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by lijunjie on 15/10/8.
 * 网络操作类
 */
public class HttpHelper {

    public static String getBodyString(String url) {
        Request request = new Request.Builder().url(url).build();
        return getBodyString(request, CommonHttpClient.getMxHttpClient(url));
    }

    public static String getBodyString(String url, HashMap<String, String> heads) {
        Request.Builder builder = new Request.Builder().url(url);
        appendHeaders(builder, heads);
        Request request = builder.build();

        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        return getBodyString(request, client);
    }

    private static String getBodyString(Request request, OkHttpClient client){
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getBodyBytes(String url) {
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param url
     * @param retryCount
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String getBodyString(String url, int retryCount) throws IOException {
        Response response = getResponse(url, retryCount, null);

        if (response != null){
            return getBodyString(response);
        }

        return null;
    }

    public static String getBodyString(Response response) {
        if(response != null && response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static InputStream getBodyInputStream(String url) {
        Response response = getResponse(url);
        if(response != null && response.isSuccessful()) {
            if (response.body() != null){
                return response.body().byteStream();
            }
        }

        return null;
    }

    public static InputStream getBodyInputStream(Response response) {
        if(response != null && response.isSuccessful()) {
            if (response.body() != null){
                return response.body().byteStream();
            }
        }

        return null;
    }

    public static Response getResponse(String url) {
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        try {
            Request request = new Request.Builder().url(url.toString()).build();
            return client.newCall(request).execute();
        } catch (UnknownHostException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.lang.IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Response getResponse(String url, Map<String, String> heads){
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        Response response = null;
        try {
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            appendHeaders(builder, heads);
            Request request = builder.build();
            response = client.newCall(request).execute();
            return response;
        } catch (UnknownHostException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.lang.IllegalArgumentException e){
            e.printStackTrace();
        }

        return response;
    }

    public static Response getResponse(String url, String contentType) {
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        Request request = new Request.Builder().url(url.toString()).addHeader("Content-Type", contentType).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * send a http GET request
     * @param url
     * @param retryCount 连结失败时的重试次数
     * ＠param successCodes 认为成功的状态码数组，例如200,206,也可以组合各种异常状态码，如：404,
     * 如果为null,则只要获得服务器的返回信息就认为成功
     * @return HttpResponse
     * @throws Exception
     */
    public static Response getResponse(String url, int retryCount, int... successCodes) throws IOException {
        Response response = null;
        boolean succeed = false;
        // 对数组排序，以便在后面查找相应代码
        if(successCodes != null){
            Arrays.sort(successCodes);
        }

        do {
            OkHttpClient client=CommonHttpClient.getMxHttpClient(url);
            Request request=new Request.Builder().url(url).build();
            try {
                response=client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                if (retryCount <= 0) {
                    throw e;
                }
            } catch (Exception e){
                e.printStackTrace();
                if (retryCount <= 0) {
                    throw e;
                }
            }


            if (response != null) {
                if(successCodes != null){
                    succeed = Arrays.binarySearch(successCodes, response.code()) >= 0;
                }else{
                    succeed = true;
                }
            }

            if(succeed){
                break;
            }
        }while(!succeed && retryCount-- > 0);

        return response;
    }

    public static Response postResponse(String url, Map<String,String> params) {
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> iterator=params.keySet().iterator();
        while (iterator.hasNext()) {
            String key=iterator.next();
            String value=params.get(key);
            builder.add(key,value);
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response postResponse(Request request, OkHttpClient client){
        Response response = null;

        try {
            response = client.newCall(request).execute();
            return response;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    public static Response postResponseByException(String url, String contentType, byte[] data) throws IOException {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, data);
        Request request = new Request.Builder().url(url.toString()).post(requestBody).build();

        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        return client.newCall(request).execute();
    }
    public static Response postResponseByException(String url, String contentType, String body) throws IOException {
        return postResponseByException(url,contentType,body.getBytes());
    }

    public static Response postResponse(String url, String contentType, String body) {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, body);

        Request request = new Request.Builder().url(url.toString()).post(requestBody).build();
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);

        return postResponse(request, client);
    }

    public static Response postResponse(String url, String contentType, HashMap<String, String> heads, String body) {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, body);

        Request.Builder builder = new Request.Builder().url(url);
        appendHeaders(builder, heads);
        Request request = builder.post(requestBody).build();
        return postResponse(request, CommonHttpClient.getMxHttpClient(url));
    }

    public static Response postResponse(String url, String contentType, HashMap<String, String> heads, byte[] data) {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, data);

        Request.Builder builder = new Request.Builder().url(url);
        appendHeaders(builder, heads);
        Request request = builder.post(requestBody).build();

        return postResponse(request, CommonHttpClient.getMxHttpClient(url));
    }

    public static Response postResponse(String url, String contentType, byte[] data) {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, data);
        Request request = new Request.Builder().url(url.toString()).post(requestBody).build();

        OkHttpClient client = CommonHttpClient.getMxHttpClient(url);
        return postResponse(request, client);
    }

    public static Response postResponse(URL url, String contentType, byte[] data) {
        final MediaType MEDIA_TYPE = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, data);

        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient client = CommonHttpClient.getMxHttpClient(url.toString());
        return postResponse(request, client);
    }

    /**
     * 下载一个请求结果，到一个文件中
     * @param url
     * @param saveAs
     * @return
     */
    public static boolean downloadFile(String url, final String saveAs)  {
        return FileUtils.downFile(url, saveAs);
    }


    private static Headers getHeaders(String url) {
        Headers headers = null;
        Request request = new Request.Builder().url(url).head().build();
        try {
            Response response = CommonHttpClient.getMxHttpClient(url).newCall(request).execute();
            headers = response.headers();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    /**
     * 获取内容长度
     * @param url
     * @return
     */
    public static long getContentLength(String url) {
        long length = 0L;
        Headers headers = getHeaders(url);
        if (headers != null) {
            String contentLength = headers.get("Content-Length");
            if (!TextUtils.isEmpty(contentLength)) {
                length = Long.parseLong(contentLength);
            }
        }
        return length;
    }

    /**
     * 获取内容长度
     * @param headers
     * @return
     */
    public static long getContentLength(Headers headers) {
        long length = 0L;
        if (headers != null) {
            String contentLength = headers.get("Content-Length");
            if (!TextUtils.isEmpty(contentLength)) {
                length = Long.parseLong(contentLength);
            }
        }
        return length;
    }

    /**
     * 添加头部
     * @param builder
     * @param heads
     */
    private static void appendHeaders(Request.Builder builder, Map<String, String> heads){
        if (heads == null){
            return;
        }

        Iterator<String> iterator = heads.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.addHeader(key, heads.get(key));
        }
    }

    /**
     * 将请求转换为Curl格式
     * @param url
     * @param body
     * @param param
     * @return
     */
    public static String formatHttpToCurl(String url, String body, HashMap<String, String> param){
        return formatHttpToCurl(url, body, param, "");
    }

    /**
     * 将请求转换为Curl请求
     * @param url
     * @param body
     * @param params
     * @param contentType
     * @return
     */
    public static String formatHttpToCurl(String url, String body, HashMap<String, String> params, String contentType){
        String curl = "curl ";
        if (params != null){
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key=iterator.next();
                String value=params.get(key);
                curl += " -H '"+key+":"+value+"' ";
            }
        }

        if (!TextUtils.isEmpty(contentType)){
            curl += " -H 'Content-Type:"+contentType+"' ";
        }

        if (!TextUtils.isEmpty(body)){
            curl += " -d '"+body+"' ";
        }

        curl += " '"+url+"'";

        return curl;
    }

}
