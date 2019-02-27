package com.y.t.ui.main.home.behavior.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.y.b.tools.Log;
import com.y.t.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ScrollerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopViewBehavior extends CoordinatorLayout.Behavior<View>{

    private static final String TAG = "TopViewBehavior";
    private float deltaY;
    private ScrollerCompat scrollCompat;

    public TopViewBehavior() {
    }

    public TopViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollCompat = ScrollerCompat.create(context,sQuinticInterpolator);
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        boolean flag = (dependency.getId() == R.id.main_container);
//        Log.e(TAG,"flag:"+flag+",child.id:"+child.getId());
        return false;
    }


    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        Log.e(TAG,"id:"+R.id.top_container+",child.id:"+child.getId());
//        child.setTranslationY(600);
        return false;
    }


    // 界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    // 列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    // 列表上一个全部可见的item位置
    private int lastPosition = -1;


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        Log.e(TAG,"top.method:onStartNestedScroll,child.id:"+child.getId()+",ev:"+ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downReach = false;
                upReach = false;
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e(TAG,"top.method:onStartNestedScroll,child.id:"+child.getId()+",target.id:"+target.getId());
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e(TAG,"top.method:onNestedPreScroll,child.id:"+child.getId()+",target.id:"+target.getId());
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (target instanceof RecyclerView) {
            RecyclerView list = (RecyclerView) target;
            // 列表第一个全部可见Item的位置
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }
            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy) && pos == 0) {
                float finalY = child.getTranslationY() - dy;
                if (finalY < -child.getHeight()) {
                    finalY = -child.getHeight();
                    upReach = true;
                } else if (finalY > 0) {
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy;
            }
            lastPosition = pos;
        }
        Log.e(TAG,"top.method:onNestedPreScroll,child.id:"+child.getId()+",target.id:"+target.getId()+",consumed1:"+consumed[1]);
    }

    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() == -child.getHeight() && !upReach) {
            return false;
        }

        if (downReach) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG,"top.method:onNestedFling,child.id:"+child.getId()+",target.id:"+target.getId());
//        if (velocityY < 0 && coordinatorLayout.getScrollY() >0) {
//            Log.i(TAG, "onNestedFling: " + velocityY);
////            resetType(FlingRunnable.SCROLL_CCONTINUE);
//            scrollCompat.fling(0, 0, 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
////            ViewCompat.postOnAnimation(dependencyView.get(), runnable);
//            return true;
//        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Log.e(TAG,"top.method:onNestedPreFling,child.id:"+child.getId()+",target.id:"+target.getId());
////        //上滑
//        if (velocityY > 0 ) {
////            resetType(FlingRunnable.PRE_FLING);
//            scrollCompat.fling(0, coordinatorLayout.getScrollY(), 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
////            ViewCompat.postOnAnimation(dependencyView.get(), runnable);
//            return true;
//
//        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


}
