package com.cfzx.mvp.view

import android.content.Context
import android.os.Bundle
import android.support.v4.util.ArrayMap
import com.afollestad.materialdialogs.MaterialDialog
import com.hcy.tomeetu.R

import rx.Observable
import java.lang.ref.WeakReference

/**
 * Created by Administrator on 2016/7/21 0021.
 */
interface BaseView {

    companion object {
        var place_holder_loading: WeakReference<MaterialDialog?>? = null
    }

    fun getViewContext(): Context

    fun showLoading(): Unit {
        place_holder_loading = WeakReference(MaterialDialog.Builder(getViewContext()).content(R.string.loading).progress(true, 0).show())
    }

    fun dismissLoading(): Unit {
        place_holder_loading?.let {
            it.get()?.dismiss()
            it.clear()
        }
        place_holder_loading = null
    }

    fun <T> showContent(data: T?): Unit = TODO(" no impl")

    fun showError(e: Throwable?): Unit = TODO(" no impl")

    interface BaseListView : BaseView {
        fun <T> showContents(datas: List<T>?)

        fun loadComplete()

    }

    interface BaseListWithRefreshView : BaseListView {


        /**
         * 请求参数
         * @param page
         * *
         * @return
         */
        fun getRequestParams(page: Int): Observable<Map<String, Any>>


        /**
         * 允许刷新
         * @param b
         */
        fun enableRefresh(b: Boolean)

        /**
         * 重置列表
         */
        fun resetList()


    }

    interface AppDataTypeListView : BaseListWithRefreshView



    interface SearchView {
        /**
         * 列表搜索请求参数
         * @param page
         * *
         * @return
         */
        fun getSearchParams(page: Int): Observable<Map<String, Any>>
    }


    interface DynamicMenusView {


    }






}
