package com.y.t.ui.main.home.behavior.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.y.b.tools.AppContext;
import com.y.t.R;

import androidx.appcompat.widget.ViewUtils;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by sun on 2018/1/25.
 * <p>
 * 用于做个性化数据的修改
 */

public class CustomBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    public CustomBehavior() {
    }

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    protected int getTitleHeight() {
        return AppContext.getAppContext().getResources().getDimensionPixelOffset(R.dimen.home_header_title_height);
    }


    protected int getHeaderOffsetRange() {
        return AppContext.getAppContext().getResources().getDimensionPixelOffset(R.dimen.home_header_pager_offset);
    }

    protected int getFinalHeight() {
        return AppContext.getAppContext().getResources().getDimensionPixelOffset(R.dimen.home_header_title_height) ;
    }


    protected boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.home_header_contaner;
    }
}
