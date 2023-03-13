package com.carthach.base.abs

import android.app.Application
import com.hjq.toast.ToastUtils
import com.carthach.base.util.ActivityUtil

open class BaseApplication:Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance =this

        ToastUtils.init(this)
        ActivityUtil.init(this)
    }
}