package com.y.b.tools;


import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;



/**
 * 日志类。在应用中禁止使用系统自带的log类。
 * 目前这个类最大的作用就是在release版本上，不打印日志。
 */
final public class Log {

    public static final String LOG="log";

	//打包时自动修改
	public  static boolean flag = true;
	

    public static final int VERBOSE = android.util.Log.VERBOSE;

    public static final int DEBUG = android.util.Log.DEBUG;

    public static final int INFO = android.util.Log.INFO;

    public static final int WARN = android.util.Log.WARN;

    public static final int ERROR = android.util.Log.ERROR;

    public static final int ASSERT = android.util.Log.ASSERT;
    
    public static final boolean enableFileLog = false;

    public static boolean isLoggable(String tag, int level){
    	return android.util.Log.isLoggable(tag, level);
    }
	
	   /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
    	if(flag) {
    		android.util.Log.v(tag, msg);
    	}
    	
    }
    
    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
    	if(flag) {
    		android.util.Log.v(tag, msg ,tr);
    	}
    }
    

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if(flag) {
        	android.util.Log.d( tag, msg);
        }
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if(flag) {
        	android.util.Log.d( tag, msg ,tr);
        }
    }
    


    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if(flag) { 
        	android.util.Log.i( tag,msg);
        }
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if(flag) {
        	android.util.Log.i( tag, msg,tr);
        }
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fi(String tag, String msg){
    	if(flag){
    		android.util.Log.i(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fi(String path, String tag, String msg){
    	if(flag){
    		android.util.Log.i(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fi(String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.i(tag, msg,tr);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fi(String path, String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.i(tag, msg,tr);
    	}
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if(flag) {
        	android.util.Log.w( tag, msg);
        }
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        if(flag) {
        	android.util.Log.w(tag, msg ,tr);
        }
    }

         
    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static void w(String tag, Throwable tr) {
        if(flag) {
        	android.util.Log.w( tag,tr);
        }
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fw(String tag, String msg){
    	if(flag){
    		android.util.Log.w(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fw(String path, String tag, String msg){
    	if(flag){
    		android.util.Log.w(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fw(String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.w(tag, msg,tr);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fw(String path, String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.w(tag, msg,tr);
    	}
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if(flag) {
        	android.util.Log.e( tag, msg);
        }
    }

    /**
     * Send a {@link #ERROR} log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static void e(String tag, Throwable tr) {
        if(flag) {
            android.util.Log.e(tag, "", tr);
        }
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
    	  if(flag) {
    		  android.util.Log.e( tag, msg ,tr);
    	  }
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fe(String tag, String msg){
    	if(flag){
    		android.util.Log.e(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fe(String path, String tag, String msg){
    	if(flag){
    		android.util.Log.e(tag, msg);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fe(String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.e(tag, msg,tr);
    	}
    }
    
    /**
     * normal log and file log simultaneously
     * @param tag
     * @param msg
     */
    public static void fe(String path, String tag, String msg, Throwable tr){
    	if(flag){
    		android.util.Log.e(tag, msg,tr);
    	}
    }


}
