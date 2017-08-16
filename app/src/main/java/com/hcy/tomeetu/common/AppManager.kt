package com.hcy.tomeetu.utils

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
object AppManager {

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        val activity = activityStack.last
        return activity
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.last
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        if (activityStack.remove(activity))
            activity.finish()
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }


    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack.forEach { it.finish() }
        activityStack.clear()
    }

    fun isExist(context: Context): Boolean {
        activityStack.filter { context === it }
        return false
    }

    fun exitExceptClass(cls: Class<*>) {
//        if (activity.javaClass != LoginActivity::class.java) activity.finish()
        activityStack.groupBy { it.javaClass == cls }.let {
            it[false]?.forEach { it.finish() }
            it[true]?.dropLast(1)?.forEach { it.finish() }
        }

    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        /*try {
            finishAllActivity()
            val activityMgr = AppContext.get().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(AppContext.get().packageName)
            System.gc()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
        }*/
    }

    fun ActivityStackSize(): Int {
        return activityStack.size
    }


    fun removeActivity(activity: Activity?) {
        activityStack.remove(activity)
    }


    val activityStack: LinkedList<Activity> by lazy {
        LinkedList<Activity>()
    }

    fun getActivity(cls: Class<*>): Activity? {
        return activityStack.find { it.javaClass == cls }
    }
}