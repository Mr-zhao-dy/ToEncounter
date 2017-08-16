package com.hcy.tomeetu.common;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hcy.mylibrary.KLog;
import com.hcy.tomeetu.BuildConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;


/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class AppContext extends Application {
    private static AppContext instance;
    //private static Account account = new Account();
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull() || json.getAsString().length() == 0) {
                return null;
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }).serializeNulls() .disableHtmlEscaping().create();
    //private RefWatcher refWatcher;

    public static AppContext get() {
        return instance;
    }

    public static Gson gson() {
        return gson;
    }

//    public static Account account() {
//        if (!account.isLogin()) {
//            //崩溃后或者account被可能的回收后可以重新获取
//            Account acc = ((Account) CacheLoader.getAcache().getAsObject(C.Account.LOGIN_ACCOUNT));
//            if (acc != null)
//                account = acc;
//        }
//        return account;
//    }

//    public static void setAccount(Account account) {
//        CacheLoader.getAcache().put(C.Account.LOGIN_ACCOUNT, account);
//        AppContext.account = account;
//    }


    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            MultiDex.install(this);
//        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ConfigInit();

        try {
            Bundle metaData = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            String[] sinaWeibo = metaData.getString("SinaWeibo").split("#");
            String[] weiXin = metaData.getString("WeChat").split("#");
            String[] qqZone = metaData.getString("QQZone").split("#");

//            PlatformConfig.setWeixin(weiXin[0], weiXin[1]);
//            PlatformConfig.setSinaWeibo(sinaWeibo[0], sinaWeibo[1]);
//            Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
//            PlatformConfig.setQQZone(qqZone[0], qqZone[1]);
//
////            Config.DEBUG = true;
//
//            MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, metaData.getString("UMENG_APPKEY"),
//                    metaData.getString("UMENG_CHANNEL"), MobclickAgent.EScenarioType.E_UM_NORMAL));

        } catch (Exception e) {
            e.printStackTrace();
        }

        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                e.printStackTrace();
                 KLog.e(e);
            }
        });
    }

//    public RefWatcher getRefWatcher() {
//        return refWatcher;
//    }

    private void ConfigInit() {

      //  refWatcher = LeakCanary.install(get());
      //  BlockCanary.install(this, new AppBlockCanaryContext()).start();
        KLog.init(BuildConfig.DEBUG, "cfzx");  //log // FIXME: 2016/11/2 0002
        if (BuildConfig.DEBUG) {
            KLog.d("getChannelName  = " + BuildConfig.channel);
            KLog.d("sha1  = " + sHA1(this));
            KLog.d("deviceInfo  = " + getDeviceInfo(this));

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        } else {
            KLog.init(BuildConfig.API_Log, "cfzx");  //log // FIXME: 2016/11/2 0002
        }
      //  YXCache.setContext(this);
      //  KLog.d("base url ; " + C.API.CFZX_BASE_HOST);




    }


}
