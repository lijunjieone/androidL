package com.y.t.ui.main.home.behavior.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.y.b.tools.Log;
import com.y.t.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewBehavior extends CoordinatorLayout.Behavior<View>{

    private static final String TAG = "TopViewBehavior";
    private float deltaY;

    public MainViewBehavior() {
    }

    public MainViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean flag = (dependency.getId() == R.id.top_container);
        Log.e(TAG,"flag:"+flag+",child.id:"+child.getId());
        return flag;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        int height = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.top_container);
        Log.e(TAG,"main.id:"+R.id.main_container+",child.id:"+child.getId()+",height:"+height);
        child.setTranslationY(height);
        return super.onLayoutChild(parent, child, layoutDirection);
    }



    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算列表y坐标，最小为0
        float y = dependency.getHeight() + dependency.getTranslationY();
        Log.e(TAG,"main.method:onDependentViewChanged,child.id:"+child.getId()+",dependency.id:"+dependency.getId()+",y:"+y+",de.height:"+dependency.getHeight()+",tranY:"+dependency.getTranslationY());
        if (y < 0) {
            y = 0;
        }
        child.setY(y);
        return true;
    }}
