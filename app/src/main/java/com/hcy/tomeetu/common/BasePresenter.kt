package com.cfzx.mvp.presenter

import com.google.gson.JsonObject
import rx.Observable

/**
 * Created by Administrator on 2016/7/21 0021.
 */
interface BasePresenter<V> {


    /**
     * Called when the view is attached to the presenter. Presenters should normally not use this
     * method since it's only used to link the view to the presenter which is done by the BasePresenter.
     * after activity's or fragment's onStart

     * @param view the view
     */
    fun onViewAttached(view: V)

    /**
     * Called every time the view starts, the view is guarantee to be not null starting at this
     * method, until [.onStop] is called.
     * after [.onViewAttached]

     * @param firstStart true if it's the first start of this presenter, only once in the presenter lifetime
     */
    fun onStart(firstStart: Boolean)

    /**
     * do not call manually, it will call if presenter is  firstStart
     */
    fun onFirstLoad()

    fun onResume()
    fun onPause()


    /**
     * Called every time the view stops. After this method, the view will be null until next
     * [.onStart] call.
     */
    fun onStop()

    /**
     * Called when the view is detached from the presenter. Presenters should normally not use this
     * method since it's only used to unlink the view from the presenter which is done by the BasePresenter.
     */
    fun onViewDetached()

    /**
     * Called when the presenter is definitely destroyed, you should use this method only to release
     * any resource used by the presenter (cancelCurrentRequest HTTP requests, close database connection...).
     */
    fun onPresenterDestroyed()


    //region 封装的接口
  //  fun removeCache(vararg keys: String, isRegex: Boolean = false) = CacheLoader.removeCacheLike(*keys, isRegex = isRegex) //发布,修改成功后移除相关缓存

    /**
     * 加载更多的通用接口,presenter 继承 com.cfzx.mvp.presenter.AbstractRefreshLoadMorePresenterImpl，并实现view的loadComplete方法
     */
    interface LoadMorePresenter {
        /**
         * 加载数据

         * @param page 从1开始递增
         */
        fun loadData4Page(page: Int)


        /**
         * 处理加载更多
         */
        fun onLoadMore()

        /**
         * 是否需要加载下一页

         * @return
         */
        fun hasLoadNext(): Boolean

    }

    interface RefreshPresenter {
        /**
         * 下拉刷新开始时调用
         */
        fun onRefresh()
    }

    interface LastCityIdPresenter {
        val lastKnowCityId: Observable<String>
       // fun getCityEventById(cityCode: String): Observable<CityEvent>
    }

    interface SearchPresenter<V> {
        /**
         * 加载搜索数据

         * @param page 从1开始递增
         */

        fun loadSearch4Page(page: Int)
    }

    /***
     * 实现通用的列表presenter

     * @param
     */
    interface BaseRefreshLoadMorePresenter<V> : BasePresenter<V>, LoadMorePresenter, RefreshPresenter, LastCityIdPresenter

    interface AppDataTypeListPresenter<V> : BaseRefreshLoadMorePresenter<V>, SearchPresenter<V>

    /***
     * 实现通用的动态标签presenter
     */
    interface DynamicMenusPresenter {
        /**
         * 初始化过滤标签
         */
        fun initDropFilterMenus()

        /**
         * 取消请求，切换过滤标签时使用
         */
        fun cancelCurrentRequest()
    }


    interface BaseMultiPresenter<V> {
        val otherPresenters: List<BasePresenter<V>>
        fun findPresenterByClazz(x: Class<*>): BasePresenter<V>? = otherPresenters.find { it.javaClass == x }

    }

    interface ValidateNumPresenter {
      //  fun requestValidateNumber(phone: String): Observable<JsonObject> = BaseCacheRequestImpl.getInstance(C.NAPI.SMS).requestFor(arrayMapOf("phone" to phone))
    }

    //endregion

    interface BaseDetailPresenter<V> : BasePresenter<V>, RefreshPresenter {

        fun loadDetail()

        fun cancelRequest()
        /**
         * 举报
         */
        fun errorReport()

        /**
         * 举报
         */
        fun startReport()

        /**
         * 分享
         */
        fun startShare()

        /**
         * 预加载评论
         */
        fun preLoadComment()

        /**
         * 关注
         */
        fun FocusUser()

        fun getAllFocus()

        fun communication()
        /**
         * 收藏action
         */
        fun startCollect()

        /**
         * 打电话
         */
        fun startCall(phones: List<String>)

        fun delete()
        /**
         * 导航
         */
        fun navigation()
    }


    /**
     * 发布基类
     */
    interface BasePublishPresenter<V> : BasePresenter<V> {

        // fun initPublishParams(provider: DataProvider.Providers, subType: DataProvider.SubType?)
        fun beginPublish() //厂房的发布

        fun uploadImage(strings: List<String>): Observable<List<String>>
        fun cancelRequest()

        //   fun startToUpdate(id: String, image: ArrayList<PlantDetailBean.RoundsBean>)
        fun updatePublish()

        /**
         * 推广信息支付
         */
        fun toWXPay()
    }


    //viewpager + fragment 懒加载数据使用
    interface LazyLoaderPresenter {
        fun lazyLoad()
    }

    interface ConfigPresenter<V> {
        fun getConfig()
        fun getConfigBean()
    }
}
