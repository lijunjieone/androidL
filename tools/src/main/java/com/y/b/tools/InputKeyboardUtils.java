package com.y.b.tools;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.y.b.tools.reflect.Reflect;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Frey on 15/11/6.
 * 软键盘相关的操作类
 */
public class InputKeyboardUtils {
    public static final String LOG_TAG = "InputKeyboardUtils";

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    public static void toggleInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showInput(View v, int flags) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, flags);
    }

    /**
     * 使用该方法启动软键盘，按home键后，无法关闭，还有很多其他情景，以后原则上禁止使用。
     * 推荐使用showInput(v,0)自己主动传递参数
     * @param v
     */
    @Deprecated
    public static void showInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public static void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
    }

    public static void hideInput(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = act.getCurrentFocus();
        if (focus != null) {
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 这里在Fragment上获取到的并不准确
     * @param activity
     * @return
     */
    public static boolean isShowInput(Activity activity) {
        boolean isShow = false;
        //判断隐藏软键盘是否弹出
        if (activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            //隐藏软键盘
            isShow = true;
        }
        return isShow;
    }

    /**
     * 是否是搜狗输入法。
     * @param context
     * @return
     */
    public static boolean isSouGou(Context context){
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            String curId = Reflect.on(imm).get("mCurId");
            if (curId != null && curId.equals("com.sohu.inputmethod.sogou/.SogouIME")){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前的软键盘信息。
     * @param context
     * @return
     */
    public static InputMethodInfo getCurrentInputMethodInfo(Context context){
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            String curId = Reflect.on(imm).get("mCurId");
            if (TextUtils.isEmpty(curId)){
                return null;
            }

            List<InputMethodInfo> list = imm.getEnabledInputMethodList();
            for (InputMethodInfo info :list) {
                if (info.getId().equals(curId)){
                    Log.i("InputKeyboardUtils ",  info.toString());
                    return  info;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解决软键盘引发的内存泄露
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//                        if (QLog.isColorLevel()) {
//                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
//                        }
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }
}
