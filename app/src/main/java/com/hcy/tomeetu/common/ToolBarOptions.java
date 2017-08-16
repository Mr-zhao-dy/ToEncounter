package com.hcy.tomeetu.common;


import com.hcy.tomeetu.R;

/**
 * Created by hzxuwen on 2016/6/16.
 */
public class ToolBarOptions {
    /**
     * toolbar的title资源id
     */
    public int titleId = 0;
    /**
     * toolbar的title
     */
    public String titleString;
    /**
     * toolbar的logo资源id
     */
    public int logoId = 0;
    /**
     * toolbar的返回按钮资源id，默认开启的资源nim_actionbar_dark_back_icon
     */
    public int navigateId = R.drawable.ic_arrow_back_white;
    /**
     * toolbar的返回按钮，默认开启
     */
    public boolean isNeedNavigate = false;
}
