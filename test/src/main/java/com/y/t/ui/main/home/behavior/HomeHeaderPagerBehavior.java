package com.y.t.ui.main.home.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;


import com.y.b.tools.AppContext;
import com.y.t.BuildConfig;
import com.y.t.ui.main.home.behavior.helper.ViewOffsetBehavior;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


/**
 * 可滚动的新闻列表的头部
 * <p/>
 * Created by chensuilun on 16/7/24.
 */
public class HomeHeaderPagerBehavior extends ViewOffsetBehavior {
    private static final String TAG = "UcNewsHeaderPager";
    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;
    public static final int STATE_SCROLLING = 2;
    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;

    private int mCurState = STATE_OPENED;
    private OnPagerStateListener mPagerStateListener;

    private OverScroller mOverScroller;
    private int mMinTouchSlop = ViewConfiguration.get(AppContext.getAppContext()).getScaledTouchSlop();

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;


    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public HomeHeaderPagerBehavior() {
        init();
    }


    public HomeHeaderPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams params) {
        super.onAttachedToLayoutParams(params);
    }

    private void init() {
        mOverScroller = new OverScroller(AppContext.getAppContext());
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<CoordinatorLayout>(parent);
        mChild = new WeakReference<View>(child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStartNestedScroll: ");
        }
        return !isClosed();
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        //dy>0 scroll up;dy<0,scroll down

        float halfOfDis = dy / 4.0f;
        if (!canScroll(child, halfOfDis)) {
            // 到达边界
            Log.d(TAG, "边界 TransY =" + (halfOfDis > 0 ? getHeaderOffsetRange() : 0));
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
        } else {
            // 在中间的状态
            Log.d(TAG, "中间状态 transY =" + (child.getTranslationY() - halfOfDis));
            child.setTranslationY(child.getTranslationY() - halfOfDis);
            if (mPagerStateListener != null) {
                dispatchState(STATE_SCROLLING);
            }
        }
        //consumed all scroll behavior after we started Nested Scrolling
        consumed[1] = dy;
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        // consumed the flinging behavior until Closed
        Log.e(TAG,"onNestedPreFling " + velocityY);
        // 当前状态在 open 的时候，只有手指向上（velocityY > 0） fling 才会去动画
        if (velocityY > 0 || mCurState == STATE_SCROLLING) {
            handleActionFling(coordinatorLayout, child);
        }
        return !isClosed(child);
    }


    private boolean isClosed(View child) {
        boolean isClosed = child.getTranslationY() == getHeaderOffsetRange();
        return isClosed;
    }

    private boolean isOpened(View child) {
        return child.getTranslationY() == 0;
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }


    public boolean isOpened() {
        return mCurState == STATE_OPENED;
    }

    private String toState() {
        switch (mCurState) {
            case STATE_CLOSED:
                return "closed";
            case STATE_OPENED:
                return "open";
            case STATE_SCROLLING:
                return "scrolling";
        }
        return "error";
    }


    private void dispatchState(int newState) {
        if (mPagerStateListener!=null) {
            mCurState = newState;
            if (mCurState == STATE_OPENED) {
                mPagerStateListener.onPagerOpened();
            } else if (mCurState == STATE_CLOSED) {
                mPagerStateListener.onPagerClosed();
            } else {
                mPagerStateListener.onPageScrolling();
            }
        }
        Log.i(TAG, "----> dispatchState = " + toState());
    }

    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        return pendingTranslationY >= getHeaderOffsetRange() && pendingTranslationY <= 0;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent ev) {

        Log.e(TAG,"onInterceptTouchEvent:ev="+ev.toString());
        Log.i(TAG, " isClosing = " + mIsCloseAnimationRunning);

        if (ev.getAction() == MotionEvent.ACTION_DOWN && !mOverScroller.isFinished()) {
            if (mIsCloseAnimationRunning) {
                // 在执行关闭动画的时候，由于有延时，可以提前发送一个关闭的消息，新闻这时候可以获取消息滚动
                dispatchState(STATE_CLOSED);
            } else {
                // 当在执行打开的动画的时候，新的消息又过来了，这时候，取消动画
                mOverScroller.abortAnimation();
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_UP && !isClosed() && !isOpened()) {
            handleActionUp(parent, child);
            //return true;
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public void handleActionUp() {
        CoordinatorLayout parent = mParent.get();
        View child = mChild.get();
        if (parent != null && child != null) {
            handleActionUp(parent, child);
        }
    }


    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleActionUp: ");
        }
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(parent, child);
        if (child.getTranslationY() < getHeaderOffsetRange() / 10.0f) {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        } else {
            mFlingRunnable.scrollToOpen(DURATION_SHORT);
        }

    }

    private void handleActionFling(CoordinatorLayout parent, final View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(parent, child);
        if (!isClosed()) {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        }
    }

    private void onFlingFinished(CoordinatorLayout coordinatorLayout, View layout) {
        if (isClosed(layout)) {
            dispatchState(STATE_CLOSED);
        } else if (isOpened(layout)) {
            dispatchState(STATE_OPENED);
        }
        mIsCloseAnimationRunning = false;
    }

    public void openPager() {
        openPager(DURATION_LONG);
    }

    /**
     * @param duration open animation duration
     */
    public void openPager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (child != null) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child, false);
            mFlingRunnable.scrollToOpen(duration);
        }
    }

    public void closePager() {
        closePager(DURATION_LONG);
    }

    /**
     * @param duration close animation duration
     */
    public void closePager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (!isClosed()) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child, true);
            mFlingRunnable.scrollToClosed(duration);
        }
    }


    private FlingRunnable mFlingRunnable;
    private boolean mIsCloseAnimationRunning = false;

    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation is of the
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
            mIsCloseAnimationRunning = false;
        }

        FlingRunnable(CoordinatorLayout parent, View layout, boolean isClosing) {
            mParent = parent;
            mLayout = layout;
            mIsCloseAnimationRunning = isClosing;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            float dy = getHeaderOffsetRange() - curTranslationY;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "scrollToClosed:offest:" + getHeaderOffsetRange());
                Log.d(TAG, "scrollToClosed: cur0:" + curTranslationY + ",end0:" + dy);
                Log.d(TAG, "scrollToClosed: cur:" + Math.round(curTranslationY) + ",end:" + Math.round(dy));
                Log.d(TAG, "scrollToClosed: cur1:" + (int) (curTranslationY) + ",end:" + (int) dy);
            }
            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy + 0.1f), duration);
            start(true);
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
            start(false);
        }

        private void start(boolean isClosing) {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout, isClosing);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                onFlingFinished(mParent, mLayout);
            }
        }


        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    if (BuildConfig.DEBUG) {
                       Log.d(TAG, "run: " + mOverScroller.getCurrY() + " isClosing = " + mIsCloseAnimationRunning);
                    }
                    if(mPagerStateListener != null && !mIsCloseAnimationRunning) {
                        dispatchState(STATE_SCROLLING);
                    }
                    ViewCompat.setTranslationY(mLayout, mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }

    /**
     * callback for HeaderPager 's state
     */
    public interface OnPagerStateListener {
        /**
         * do callback when pager closed
         */
        void onPagerClosed();

        /**
         * do callback when pager opened
         */
        void onPagerOpened();


        void onPageScrolling();
    }

}
