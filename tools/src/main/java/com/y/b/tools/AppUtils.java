package com.y.b.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.content.FileProvider;

public class AppUtils {
    public static void installApk(Context context, String file) {
        openFile(context,file,"application/vnd.android.package-archive");
    }



    public static boolean checkMimeTypeIntent(Activity context,String url, String mimetype) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url),mimetype);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ResolveInfo info =context
                .getPackageManager()
                .resolveActivity(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        if (info != null) {
            ComponentName myName = ((Activity)context).getComponentName();
            if (!myName.getPackageName().equals(
                    info.activityInfo.packageName)) {
                return true;
            }
        }
        return false;

    }


    public static File getDownloadFile() {
        File mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");
        if(!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }

        return mDownloadDir;
    }

    public static void openImage(Context context,String file) {
        openFile(context,file,"image/*");
    }

    public static void openFile(Context context,String file) {
        if(!file.startsWith("/")) {
            file = getDownloadFile()+"/"+file;
        }
        if(file.endsWith(".apk")) {
            AppUtils.installApk(context,file);
        }else if(file.endsWith("png") || file.endsWith("jpg") || file.endsWith("jpeg")) {
            AppUtils.openImage(context,file);
        }else {
            AppUtils.openFile(context,file,"*/*");
        }
    }
    public static void openFile(Context context,String file,String mimetype) {

//        File f = new File(WEBVIEW_CACHE_PIC);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File apkFile = null;
        if(file.startsWith("/")) {
            apkFile =new File(file);
        }else {
            apkFile = new File(getDownloadFile().getAbsolutePath()+file);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", apkFile);
            install.setDataAndType(contentUri, mimetype);
        } else {
            install.setDataAndType(Uri.fromFile(apkFile), mimetype);
        }

        context.startActivity(install);
    }

    private  static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getDefaultSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }
    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context),getDefaultSharedPreferencesMode());
    }


    public static DisplayMetrics getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm=new DisplayMetrics();
        display.getMetrics(dm);
        return dm;
    }

    public static String getDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(timestamp);
        return format.format(d);
    }


    public static int getStatusHeight(Context context) {
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight1;

    }
}
