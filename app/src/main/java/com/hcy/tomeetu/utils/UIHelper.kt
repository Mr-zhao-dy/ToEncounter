package com.hcy.tomeetu.utils

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hcy.mylibrary.KLog

/**
 * by y on 2016/4/29.
 */
class UIHelper {

    //检测键盘的状态
    private val isKeyboardShown: Boolean
        get() {
            val softKeyboardHeight = 100
            val r = Rect()
            val decorView = AppManager.currentActivity().window.decorView
            decorView.getWindowVisibleDisplayFrame(r)
            val dm = decorView.resources.displayMetrics
            val heightDiff = decorView.bottom - r.bottom
            return heightDiff > softKeyboardHeight * dm.density
        }

    companion object {
        @JvmStatic fun startActivity(clz: Class<*>) {
            val intent = Intent(UIUtils.getContext(), clz)
            AppManager.currentActivity().startActivity(intent)
        }


        @JvmStatic fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
            val intent = Intent(UIUtils.getContext(), clz)
            bundle?.let { intent.putExtras(it) }
            AppManager.currentActivity().startActivity(intent)
        }

        @JvmStatic fun startActivityForResult(clz: Class<*>, requestCode: Int) {
            val intent = Intent(UIUtils.getContext(), clz)
            AppManager.currentActivity().startActivityForResult(intent, requestCode)
        }


        /**
         * 优化activity的背景过度绘制(activity存在背景色)
         * 在合适的时候调用，建议放在super.onCreate(saveInstanceState)代码下面一行

         * @param activity
         */
        /*
    public static void $optimizeBackgroundOverdraw(Activity activity) {
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }*/

        // 收起软键盘
        fun closeSyskeyBroad() {
            try {
                val inputMethodManager = UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(UIUtils.getActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                KLog.w("closeSyskeyBroad", "关闭输入法异常 , 忽略")
            }

        }

        //检测键盘的状态
        fun syskeyBroadStatus(): Boolean {
            val imm = UIUtils.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.isActive
        }

        fun showSoftKeyboard(view: View) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            if (!view.isFocused) view.requestFocus()

            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
        }
    }


}
