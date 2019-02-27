package com.y.b.tools;

import android.os.Build;
import android.webkit.WebSettings;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lijunjie on 15/9/28.
 */
public class CommonHttpClient {

    private static String mDefaultUa = null;

    private static OkHttpClient mHttpClient = null;
    private static OkHttpClient mHttpsClient = null;

    //进行一些公共的初始化操作
    private static void init(OkHttpClient.Builder builder ) {
//        if(mDefaultUa == null) {
//            mDefaultUa = getUserAgent();
//        }

//        builder.addInterceptor(new UserAgentInterceptor(mDefaultUa));
//        if (DebugUtils.isInDebug()){
//            builder.addInterceptor(new LoggingInterceptor());
//            builder.addInterceptor(Pandora.get().getInterceptor());
//        }
    }

//    private  static String getUserAgent() {
//        String userAgent = "";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            try {
//                userAgent = WebSettings.getDefaultUserAgent(AppContext.getAppContext());
//            } catch (Exception e) {
//                userAgent = System.getProperty("http.agent");
//            }
//        } else {
//            userAgent = System.getProperty("http.agent");
//        }
//        StringBuffer sb = new StringBuffer();
//        if (userAgent != null){
//            for (int i = 0, length = userAgent.length(); i < length; i++) {
//                char c = userAgent.charAt(i);
//                if (c <= '\u001f' || c >= '\u007f') {
//                    sb.append(String.format("\\u%04x", (int) c));
//                } else {
//                    sb.append(c);
//                }
//            }
//        }
//        return sb.toString();
//    }

    /**
     * 根据URL获取OkHttpClient
     * @param url
     * @return
     */
    public synchronized static OkHttpClient getMxHttpClient(String url) {
        return getMxHttpClient(url!=null&&url.indexOf("https")==0);
    }

    /**
     * 根据不同的Https获取不同的OkHttpClient
     * @param isHttps
     * @return
     */
    public synchronized static OkHttpClient getMxHttpClient(boolean isHttps) {
        if (!isHttps){
            if (mHttpClient == null){
                mHttpClient = CommonHttpClient.getOkHttpClient();
            }
            return mHttpClient;
        }else{
            if (mHttpsClient == null){
                mHttpsClient = CommonHttpClient.getUnsafeOkHttpClient();
            }
            return mHttpsClient;
        }
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 加入对cmwap的支持
//        if (NetworkUtils.isCmWapOK()) {
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
//            builder.proxy(proxy);
//        }

        builder.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
        builder.readTimeout(30, TimeUnit.SECONDS); // socketTimeout


        init(builder);
        return builder.build();
    }

    /**
    * 忽略SSL证书
    * @author wks
    * crate at 15/11/26 下午5:16
    */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            SSLSocketFactory sslSocketFactory = null;
            try {
                SSLContext sslContext= SSLContext.getInstance("SSL");
                sslContext.init(null, new X509TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            if (sslSocketFactory != null){
                builder.sslSocketFactory(sslSocketFactory, trustManager);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }

            // 加入对cmwap的支持
//            if (NetworkUtils.isCmWapOK()) {
//                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
//                builder.proxy(proxy);
//            }

            builder.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
            builder.readTimeout(30, TimeUnit.SECONDS); // socketTimeout

            init(builder);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

//class DefaultHeaderInterceptor implements Interceptor {
//
//    private static final String LOG_TAG ="DefaultHeader" ;
//    private void setDefaultHeader(Request request) {
////        request.newBuilder()
////                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
////                .addHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008101315 Ubuntu/8.10 (intrepid) Firefox/3.0.3")
////                .addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7")
////                .addHeader("Accept-Encoding", "gzip,deflate,sdch");
////        request.headers().newBuilder().build();
//
//    }
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Log.i(LOG_TAG,String.format("Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()));
//        setDefaultHeader(request);
//        Log.i(LOG_TAG, String.format("Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()));
//
//        Response response = chain.proceed(request);
//        return response;
//    }
//}

class LoggingInterceptor implements Interceptor {

    private static final String LOGTAG ="LoggingInterceptor" ;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        request.newBuilder().addHeader("User-Agent","Maxthon 5").build();

        long t1 = System.nanoTime();
        Log.i(LOGTAG,String.format("Sending request %s on \n %s %n %s ",
                request.url(), chain.connection(), request.headers(), request.body()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(LOGTAG, String.format("Received response in %.1fms%n%s with %s ", (t2 - t1) / 1e6d, response.request().url(), response.headers()));

        return response;
    }
}


class UserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public UserAgentInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}