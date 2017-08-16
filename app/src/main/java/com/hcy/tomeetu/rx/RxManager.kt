package com.hcy.tomeetu.rx


import com.hcy.mylibrary.KLog

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Administrator on 2016/7/21 0021.
 */
class RxManager {


    private val compositeSubscription = CompositeSubscription()

    fun add(s: Subscription): RxManager {
        compositeSubscription.add(s)
        return this
    }

    fun clear() {
        KLog.d("clear compositeSubscription")
        compositeSubscription.clear()
    }

    /**
     * can not use
     */
    fun destory() {
        KLog.d("destory compositeSubscription")
        compositeSubscription.clear()
        compositeSubscription.unsubscribe()
    }

    fun remove(subscription: Subscription?) {
        compositeSubscription.remove(subscription)
    }

    internal fun hasSubscription(): Boolean {
        return compositeSubscription.hasSubscriptions()
    }


}
