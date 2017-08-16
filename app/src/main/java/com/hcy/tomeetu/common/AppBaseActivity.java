package com.hcy.tomeetu.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.FrameLayout;


import com.cfzx.mvp.presenter.BasePresenter;
import com.cfzx.mvp.presenter.loader.PresenterFactory;
import com.cfzx.mvp.presenter.loader.PresenterLoader;
import com.cfzx.mvp.view.BaseView;
import com.hcy.mylibrary.KLog;
import com.hcy.tomeetu.R;
import com.hcy.tomeetu.utils.C;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public abstract class AppBaseActivity<P extends BasePresenter<V>, V extends BaseView> extends BaseActivity implements LoaderManager.LoaderCallbacks<P>, PresenterFactory<P> {

    static final AtomicInteger sViewCounter = new AtomicInteger(Integer.MIN_VALUE);
    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);
    protected boolean mFirstStart;//Is this the first start of the activity (after onCreate)
    protected P mBasePresenter;

    @Nullable
    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout toolbarLayout;
    @Nullable
    @BindView(R.id.fl_head_container)
    protected FrameLayout headContainer;
    protected View mRootView;
    private int mUniqueLoaderIdentifier;//Unique identifier for the loader, persisted across re-creation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  UIHelper.$optimizeBackgroundOverdraw(this);
        KLog.d(TAG, TAG + "=> onCreate_save:" + savedInstanceState);
        mFirstStart = savedInstanceState == null || savedInstanceState.getBoolean(C.SavedInstanceState.RECREATION_SAVED_STATE);
        mUniqueLoaderIdentifier = savedInstanceState == null ? AppBaseActivity.sViewCounter.incrementAndGet() : savedInstanceState.getInt(C.SavedInstanceState.LOADER_ID_SAVED_STATE);
        mRootView = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(mRootView);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(mUniqueLoaderIdentifier, null, AppBaseActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBasePresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    protected void doStart() {
        KLog.d(TAG, TAG + "doStart", mFirstStart, mUniqueLoaderIdentifier);
        assert mBasePresenter != null;
        mBasePresenter.onViewAttached((V) this);
        mBasePresenter.onStart(mFirstStart);
        mFirstStart = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBasePresenter != null) mBasePresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBasePresenter != null) mBasePresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBasePresenter != null) {
            mBasePresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (mBasePresenter != null) {
            mBasePresenter.onViewDetached();
        }
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(C.SavedInstanceState.RECREATION_SAVED_STATE, true);
        outState.putInt(C.SavedInstanceState.LOADER_ID_SAVED_STATE, mUniqueLoaderIdentifier);
    }


    protected abstract int getLayoutId();

    /**
     * 不需要权限就忽略
     * 触发时机自己处理
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected abstract void requestPermission();


    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, this);
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mBasePresenter = data;
        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mBasePresenter = null;
    }

    @Nullable
    public CollapsingToolbarLayout getToolbarLayout() {
        return toolbarLayout;
    }


}
