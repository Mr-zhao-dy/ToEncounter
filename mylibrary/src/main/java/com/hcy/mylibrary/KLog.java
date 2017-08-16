package com.hcy.mylibrary;



import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.hcy.mylibrary.klog.BaseLog;
import com.hcy.mylibrary.klog.FileLog;
import com.hcy.mylibrary.klog.JsonLog;
import com.hcy.mylibrary.klog.XmlLog;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is a Log tool，with this you can the following
 * <ol>
 * <li>use KLog.d(),you could print whether the method execute,and the default tag is current class's name</li>
 * <li>use KLog.d(msg),you could print log as before,and you could location the method with a click in Android Studio Logcat</li>
 * <li>use KLog.json(),you could print json string with well format automatic</li>
 * </ol>
 *
 * @author zhaokaiqiang
 *         github https://github.com/ZhaoKaiQiang/KLog
 *         15/11/17 扩展功能，添加对文件的支持
 *         15/11/18 扩展功能，增加对XML的支持，修复BUG
 *         15/12/8  扩展功能，添加对任意参数的支持
 *         15/12/11 扩展功能，增加对无限长字符串支持
 *         16/6/13  扩展功能，添加对自定义全局Tag的支持
 */
public class KLog {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    public static final int JSON_INDENT = 4;
    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;
    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "KLog";
    private static  String SUFFIX = ".java";
    private static final int JSON = 0x7;
    private static final int XML = 0x8;

    private static final   int STACK_TRACE_INDEX = 5;

    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void init(boolean isShowLog, @Nullable String tag) {
        IS_SHOW_LOG = isShowLog;
        mGlobalTag = tag;
        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(XML, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(XML, tag, xml);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagStr, Object... objects) {

        if (!IS_SHOW_LOG) {
            return;
        }
        if (objects.length == 0){
            objects = new Object[1];
            objects[0] = tagStr;
            tagStr = null;
        }
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseLog.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonLog.printJson(tag, msg, headString);
                break;
            case XML:
                XmlLog.printXml(tag, msg, headString);
                break;
        }
    }


    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {

        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];

        StackTraceElement callElement = null;
        for (int i = STACK_TRACE_INDEX + 1; i < stackTrace.length; i++) {
            StackTraceElement traceElement = stackTrace[i];
            if (traceElement.getClassName().contains("cfzx")) {
                callElement = traceElement;
                break;
            }
        }
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        //tag
        StringBuilder tagBuilder = new StringBuilder();
        tagBuilder.append("[");
        String tag = tagStr == null ? getClazzName(targetElement) : tagStr;
        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tagBuilder.append(TAG_DEFAULT);
        } else if (!mIsGlobalTagEmpty) {
            tagBuilder.append(mGlobalTag);
            tagBuilder.append("/");
        }
        tagBuilder.append(tag);
        tagBuilder.append("]");

        String headString = getStackInfo(targetElement, callElement);

        return new String[]{tagBuilder.toString(), msg, headString};
    }

    @NonNull
    private static String getClazzName(StackTraceElement stack) {
        String className = stack.getClassName();
        String fileName = stack.getFileName();
        if (!TextUtils.isEmpty(className) && !TextUtils.isEmpty(fileName)){

            String[] fileNameInfo = fileName.split("\\.");
            String[] classNameInfo = className.split("\\.");

            SUFFIX = "."+ fileNameInfo[fileNameInfo.length - 1];
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
            }
            if (className.contains("$")) {
                className = className.split("\\$")[0] + SUFFIX;
            }
        }

        return className;
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(getObjString(object)).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            return objects.length == 0 ? NULL : getObjString(objects);
        }
    }

    private static String getObjString(Object object) {
        StringBuilder builder = new StringBuilder();

        if (object instanceof Collection) { //collections
            builder.append("{");
            Iterator iterator = ((Collection) object).iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                builder.append(getObjString(next));
                builder.append(",");
            }
            if (builder.lastIndexOf(",") != -1) {
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append("}");
            return builder.toString();
        } else if (object.getClass().isArray()) {
            builder.append("{");
            try {
                Object[] objects = (Object[]) object;
                for (Object o : objects) {
                    builder.append(getObjString(o));
                    builder.append(",");
                }
                if (builder.lastIndexOf(",") != -1) {
                    builder.deleteCharAt(builder.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //基本类型
                return (object).toString();
            }
            builder.append("}");
            return builder.toString();
        } else {
            return object.toString();
        }

    }

    private static String getStackInfo(StackTraceElement targetElement, StackTraceElement callElement) {
        if (callElement != null) {
            return String.format("(%s:%s)#%s[(%s:%s)#%s@%s]", getClazzName(callElement), getLineNumber(callElement), getMethodName(callElement),
                    getClazzName(targetElement), getLineNumber(targetElement), getMethodName(targetElement),
                    Thread.currentThread().getName());
        } else {
            return String.format("[(%s:%s)#%s@%s]", getClazzName(targetElement), getLineNumber(targetElement), getMethodName(targetElement),
                    Thread.currentThread().getName());
        }


    }

    @NonNull
    private static String getMethodName(StackTraceElement targetElement) {
        String methodName = targetElement.getMethodName();
        return methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    private static int getLineNumber(StackTraceElement targetElement) {
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }
        return lineNumber;
    }

}
