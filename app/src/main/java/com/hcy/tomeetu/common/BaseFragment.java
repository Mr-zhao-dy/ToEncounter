package com.hcy.tomeetu.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcy.mylibrary.KLog;
import com.hcy.tomeetu.rx.RxManager;


/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BaseFragment extends Fragment {
    protected String TAG;
    protected BaseActivity activity;
    protected RxManager rxManager;
    @Override
    public void onAttach(Activity activity) {
        TAG = this.getClass().getSimpleName();
        KLog.w(TAG, TAG + " : execute ...activity =" + activity);
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        KLog.w(TAG, TAG + " : execute ... context :" + context);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        rxManager = new RxManager();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onStart();
    }

    @Override
    public void onResume() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onResume();
     //   MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        KLog.w(TAG, TAG + " : execute ... isVisibleToUser =" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPause() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onPause();
      //  MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onStop() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        KLog.w(TAG, TAG + " : execute ...");
      //  AppContext.get().getRefWatcher().watch(this);
        super.onDestroy();
        rxManager.clear();
        rxManager.destory();
    }

    @Override
    public void onDetach() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onLowMemory() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onViewStateRestored(savedInstanceState);
    }

    //region other
    public Context getViewContext() {
        if (activity == null) return AppContext.get();
        return activity;
    }
    //endregion
}
