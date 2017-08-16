package com.hcy.tomeetu.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;


import com.hcy.mylibrary.KLog;
import com.hcy.tomeetu.BuildConfig;
import com.hcy.tomeetu.R;
import com.hcy.tomeetu.fragment.TFragment;
import com.hcy.tomeetu.rx.RxManager;
import com.hcy.tomeetu.utils.AppManager;
import com.hcy.tomeetu.utils.ReflectionUtil;
import com.jaeger.library.StatusBarUtil;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 * 日志记录及基础方法复用
 */
public class BaseActivity extends AppCompatActivity {

    public static String toOtherApp = null;

    public RxManager rxManager = new RxManager();
    protected String TAG;
    protected ToolBarOptions mToolBarOptions;
    private Toolbar mToolbar;

    private boolean destroyed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        TAG = this.getClass().getSimpleName();
        KLog.w(TAG, TAG + " : execute ..." + savedInstanceState);
        super.onCreate(savedInstanceState);
        setStatusBar();
        AppManager.INSTANCE.addActivity(this);
//        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
//        MobclickAgent.openActivityDurationTrack(BuildConfig.DEBUG);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.app_name;
        mToolBarOptions.navigateId = R.drawable.ic_arrow_back_white;
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
    }

    @Override
    protected void onStart() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onStart();
    }

    @Override
    protected void onResume() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onResume();
       // MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        KLog.w(this.getClass().getSimpleName() + " : execute ...");
        super.onPause();
       // MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onStop();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onPostResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onBackPressed();
    }

    @Override
    public void onStateNotSaved() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onStateNotSaved();
    }

    @Override
    protected void onResumeFragments() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onRestart();
    }

    @Override
    public void finish() {
        super.finish();
        KLog.w(TAG, TAG + " : execute ...");
    }

    @Override
    protected void onDestroy() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onDestroy();
        AppManager.INSTANCE.removeActivity(this);
//        AppContext.get().getRefWatcher().watch(this);
        destroyed = true;
        rxManager.destory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onTitleChanged(title, color);
    }

    @Override
    public void onContentChanged() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onContentChanged();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        KLog.w(TAG, TAG + " : execute ..." + fragment);
        super.onAttachFragment(fragment);
    }

    @Override
    public void onAttachedToWindow() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        KLog.w(TAG, TAG + " : execute ...");
        super.onDetachedFromWindow();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        KLog.w(TAG, TAG + " : execute ...");
        super.onSaveInstanceState(outState);
    }

    //region mToolbar 相关
    public void setToolBar(int toolBarId, ToolBarOptions options) {
        mToolbar = (Toolbar) findViewById(toolBarId);
        if (options.titleId != 0) {
            mToolbar.setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            mToolbar.setTitle(options.titleString);
        }
        if (options.logoId != 0) {
            mToolbar.setLogo(options.logoId);
        }
        setSupportActionBar(mToolbar);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (options.isNeedNavigate) {
            mToolbar.setNavigationIcon(options.navigateId);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigateUpClicked();
                }
            });
        }
    }

    public void setToolBar(int toolbarId, int titleId, int logoId) {
        mToolbar = (Toolbar) findViewById(toolbarId);
        mToolbar.setTitle(titleId);
        mToolbar.setLogo(logoId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(0);
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }
    //endregion


    //region fragment transaction

    public void onNavigateUpClicked() {
        onBackPressed();
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        if (transaction != null) {
            for (int i = 0; i < fragments.size(); i++) {
                // install
                TFragment fragment = fragments.get(i);
                if (fragment != null && new Integer(fragment.getContainerId()) != null) {
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
            }

            if (commit) {
                try {
                    transaction.commitAllowingStateLoss();
                } catch (Exception e) {

                }
            }
        }

        return fragments2;
    }

    public AppBaseFragment addFragment(AppBaseFragment fragment) {

        List<AppBaseFragment> fragments = new ArrayList<AppBaseFragment>(1);
        fragments.add(fragment);

        List<AppBaseFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<AppBaseFragment> addFragments(List<AppBaseFragment> fragments) {
        List<AppBaseFragment> fragments2 = new ArrayList<AppBaseFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        if (transaction != null) {
            for (int i = 0; i < fragments.size(); i++) {
                // install
                AppBaseFragment fragment = fragments.get(i);
                if (fragment != null && fragment.getContainerId() != null) {

                    int id = fragment.getContainerId();

                    // exists
                    AppBaseFragment fragment2 = (AppBaseFragment) fm.findFragmentById(id);

                    if (fragment2 == null) {
                        fragment2 = fragment;
                        transaction.replace(id, fragment);
                        commit = true;
                    }

                    fragments2.add(i, fragment2);
                }
            }

            if (commit) {
                try {
                    transaction.commitAllowingStateLoss();
                } catch (Exception e) {

                }
            }

        }
        return fragments2;
    }

    public AppBaseFragment switchContent(AppBaseFragment fragment) {
        return switchContent(fragment, false);
    }

    public AppBaseFragment switchContent(AppBaseFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null && fragment.getContainerId() != null) {
            fragmentTransaction.replace(fragment.getContainerId(), fragment);
            if (needAddToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            try {
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
            return fragment;
        } else {
            return null;
        }


    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                return onMenuKeyDown();

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    protected boolean onMenuKeyDown() {
        return false;
    }

    private void invokeFragmentManagerNoteStateNotSaved() {
        FragmentManager fm = getSupportFragmentManager();
        ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
    }


    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }
    //endregion trarans tr

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

    //other
    public Context getViewContext() {
        return this;
    }


    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }
}
