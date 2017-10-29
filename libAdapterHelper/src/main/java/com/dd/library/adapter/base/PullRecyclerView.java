package com.dd.library.adapter.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dd.library.adapter.base.refresh.AppBarStateChangeListener;
import com.dd.library.adapter.base.refresh.ArrowRefreshHeader;

/**
 * Created by 郑冬冬 on 2017/10/29.
 */

public class PullRecyclerView extends RecyclerView {

    private boolean isSupportPullToRefresh = false;

    private float mLastY = -1;
    private static final float DRAG_RATE = 3;
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private ArrowRefreshHeader mRefreshHeader;

    public PullRecyclerView(Context context) {
        super(context);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSupportPullToRefresh) {
            Adapter adapter = getAdapter();
            if (adapter != null && adapter instanceof BaseQuickAdapter) {
                BaseQuickAdapter wrapAdapter = (BaseQuickAdapter) adapter;
                if (mRefreshHeader == null) mRefreshHeader = wrapAdapter.getRefreshHeader();
                if (mRefreshHeader != null) {
                    if (mLastY == -1) {
                        mLastY = ev.getRawY();
                    }
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastY = ev.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            final float deltaY = ev.getRawY() - mLastY;
                            mLastY = ev.getRawY();
                            if (isOnTop() && wrapAdapter.isRefreshEnable() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                                mRefreshHeader.onMove(deltaY / DRAG_RATE);
                                if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                                    return false;
                                }
                            }
                            break;
                        default:
                            mLastY = -1; // reset
                            if (isOnTop() && wrapAdapter.isRefreshEnable() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                                if (mRefreshHeader.releaseAction()) {
                                    BaseQuickAdapter.RequestRefreshListener mRequestRefreshListener = wrapAdapter.getRequestRefreshListener();
                                    if (mRequestRefreshListener != null && !wrapAdapter.isLoading()) {
                                        wrapAdapter.setLoading();
                                        mRequestRefreshListener.onRefreshRequested();
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    private boolean isOnTop() {
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null && adapter instanceof BaseQuickAdapter) {
            isSupportPullToRefresh = true;
            mRefreshHeader = ((BaseQuickAdapter) adapter).getRefreshHeader();
        }
        super.setAdapter(adapter);
    }
}
