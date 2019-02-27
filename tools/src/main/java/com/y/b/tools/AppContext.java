package com.y.b.tools;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;


import com.y.b.tools.reflect.Reflect;

import java.util.List;

/**
 * 运行时 工具类
 * Created by Frey on 15/11/6.
 */
public class AppContext {

    private static final Object mSync = new Object();
    /**
     * Application实例
     */
    private static Application APP_INSTANCE;
    private static Handler mUiHandler;

    /**
     * 获取一个绑定主进程MessageQueue的handler
     * @return
     */
    public static Handler getUiHandler() {
        synchronized (mSync) {
            if (mUiHandler == null) {
                mUiHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mUiHandler;
    }

    /**
     * 获取application context
     *
     * 返回值可能为null
     *
     * @return
     */
    public static Context getAppContext() {
        Context context = null;
        Application app = getApplication();
        if (app != null) {
            context = app.getApplicationContext();
        }
        return context;
    }

    /**
     * 取得Application实例
     * 返回值可能为null
     *
     * @return
     */
    public static Application getApplication() {
        if (APP_INSTANCE == null) {
            synchronized (mSync) {
                if (APP_INSTANCE == null) {
                    try {
                        APP_INSTANCE = Reflect.on("android.app.ActivityThread").call("currentActivityThread").call("getApplication").get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return APP_INSTANCE;
    }

    /**
     * 在主进程中运行一个任务
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        getUiHandler().post(runnable);
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            Application app = getApplication();
            if (app != null) {
                info = app.getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    /**
     * 根据id获取color
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        Context context = getAppContext();
        if (context != null) {
            return getAppContext().getResources().getColor(colorId);
        } else {
            if (Log.flag) {
                throw new NullPointerException("AppContext retrieve appcontext is null.");
            } else {
                return Color.BLACK;
            }
        }
    }

    public static int getDimension(int dimensId) {
        Context context = getAppContext();
        if (context != null) {
            return (int)getAppContext().getResources().getDimension(dimensId);
        } else {
            if (Log.flag) {
                throw new NullPointerException("AppContext retrieve appcontext is null.");
            } else {
                return -2; // -2为自适应大小
            }
        }
    }

    public static Drawable getDrawable(int drawableId) {
        Context context = getAppContext();
        if (context != null) {
            return getAppContext().getResources().getDrawable(drawableId);
        } else {
            if (Log.flag) {
                throw new NullPointerException("AppContext retrieve appcontext is null.");
            } else {
                return null;
            }
        }
    }

    /**
     * 根据id获取string
     *
     * @param stringId
     * @return
     */
    public static String getString(int stringId, Object... formatArgs) {
        Context context = getAppContext();
        if (context != null) {
            return getAppContext().getString(stringId, formatArgs);
        } else {
            if (Log.flag) {
                throw new NullPointerException("AppContext retrieve appcontext is null.");
            } else {
                return "";
            }
        }
    }

    public static String getString(int stringId) {
        Context context = getAppContext();
        if (context != null) {
            return getAppContext().getString(stringId);
        } else {
            if (Log.flag) {
                throw new NullPointerException("AppContext retrieve appcontext is null.");
            } else {
                return "";
            }
        }
    }

    public static List<ResolveInfo> getIntentAvailableAppInfo(Intent intent) {
        final PackageManager packageManager = getAppContext().getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo;
    }

    public static boolean isIntentAvailable(Intent intent) {
        return getIntentAvailableAppCount(intent) > 0;
    }

    public static int getIntentAvailableAppCount(Intent intent) {
        return getIntentAvailableAppInfo(intent).size();
    }


}
