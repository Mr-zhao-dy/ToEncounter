package com.hcy.tomeetu.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cfzx.mvp.presenter.BasePresenter;
import com.cfzx.mvp.presenter.loader.PresenterFactory;
import com.cfzx.mvp.presenter.loader.PresenterLoader;
import com.hcy.mylibrary.KLog;
import com.hcy.tomeetu.fragment.TFragment;
import com.hcy.tomeetu.utils.C;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2016/7/21 0021.
 */
public abstract class AppBaseFragment<P extends BasePresenter<V>, V> extends BaseFragment implements LoaderManager.LoaderCallbacks<P>, PresenterFactory {

    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);
    protected boolean mFirstStart;//Is this the first start of the fragment (after onCreate)
    protected boolean mViewReCreate = false; //rootViewWeakRef 是否重新创建了
    protected boolean isVisible;
    @Nullable
    protected P mBasePresenter;

    protected WeakReference<View> rootViewWeakRef;
    private int mUniqueLoaderIdentifier;//Unique identifier for the loader, persisted across re-creation
    private Unbinder unbinder;
    private Integer containerId;

    //lazy load tag
    private boolean isLazyLoaded = false;


    public AppBaseFragment() {
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.d(TAG, TAG + "=> onCreate_save:" + savedInstanceState);
        mFirstStart = savedInstanceState == null || savedInstanceState.getBoolean(C.SavedInstanceState.RECREATION_SAVED_STATE);
        mUniqueLoaderIdentifier = savedInstanceState == null ? AppBaseActivity.sViewCounter.incrementAndGet() : savedInstanceState.getInt(C.SavedInstanceState.LOADER_ID_SAVED_STATE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(mUniqueLoaderIdentifier, null, this).startLoading();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            setContainerId(container.getId() == -1 ? container.hashCode() : container.getId());//默认设置
        }
        if (rootViewWeakRef != null && rootViewWeakRef.get() != null) {
            View oldParent = (View) rootViewWeakRef.get().getParent();
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(rootViewWeakRef.get());
            }
        } else {
            if (!mFirstStart) mViewReCreate = true;
            rootViewWeakRef = new WeakReference(inflater.inflate(getLayoutId(), container, false));
        }
        if (savedInstanceState != null)
            onRestartInstance(savedInstanceState);
        unbinder = ButterKnife.bind(this, rootViewWeakRef.get());
        return rootViewWeakRef.get();
    }

    protected abstract int getLayoutId();

    protected abstract void initWidget(View rootView);

    protected void onRestartInstance(Bundle bundle) {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化化一次
        if (mFirstStart || mViewReCreate) {
            initWidget(rootViewWeakRef.get());
        }
    }

    private void doStart() {
        KLog.d(TAG, TAG + ": doStart", "mFirstStart :" + mFirstStart, "mUniqueLoaderIdentifier :" + mUniqueLoaderIdentifier, "instance = " + this);
        mBasePresenter.onViewAttached((V) this);
        mBasePresenter.onStart(mFirstStart);
        if (mFirstStart || mViewReCreate) {
            initData();
        }
        if (!isLazyLoaded && isVisible) {
            lazyLoad();
            isLazyLoaded = true;
        }
        mFirstStart = false;
        mViewReCreate = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBasePresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBasePresenter != null) {
            mBasePresenter.onResume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        if (!isVisible || isLazyLoaded || mBasePresenter == null) {
            //presenter 可能没有初始化 ,放入dostart 中执行lazy
            return;
        }
        lazyLoad();
        isLazyLoaded = true;
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }

    protected abstract void initData();


    @Override
    public void onPause() {
        super.onPause();
        if (mBasePresenter != null) {
            mBasePresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBasePresenter != null) {
            mBasePresenter.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mBasePresenter != null) {
            mBasePresenter.onViewDetached();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootViewWeakRef = null;


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(C.SavedInstanceState.RECREATION_SAVED_STATE, true);
        outState.putInt(C.SavedInstanceState.LOADER_ID_SAVED_STATE, mUniqueLoaderIdentifier);
    }


    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getViewContext(), this);
    }

    /**
     * fragment 会回调两次
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        //fragment中会赋值两次，可以设置flag。
        KLog.d(TAG, TAG + "onLoadFinished");
        mBasePresenter = data;
        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mBasePresenter = null;
    }


    /**
     * fragment management
     */
    public TFragment addTFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addTFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addTFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

}
