package com.hcy.tomeetu.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message

import com.hcy.tomeetu.R
import com.hcy.tomeetu.common.BaseActivity
import com.hcy.tomeetu.utils.UIHelper

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }
        handler.postDelayed({
        UIHelper.startActivity(MainActivity::class.java)
        }, 3000)

    }
}
