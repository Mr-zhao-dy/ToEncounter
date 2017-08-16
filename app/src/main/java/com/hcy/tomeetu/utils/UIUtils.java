package com.hcy.tomeetu.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;


import com.hcy.tomeetu.common.AppContext;

import java.io.File;


/**
 * by y on 2016/4/28.
 */
@SuppressWarnings("ALL")
public class UIUtils {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Context getContext() {
        return AppContext.get();
    }

    public static Activity getActivity() {
        return AppManager.INSTANCE.currentActivity();
    }

    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getResources().getColor(id, null);
        }
        return getContext().getResources().getColor(id);
    }

    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    public static TypedArray getTypedArray(int id) {
        return getContext().getResources().obtainTypedArray(id);
    }

    public static Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + AppContext.get().getPackageName() + FOREWARD_SLASH + resourceId);
    }

    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
