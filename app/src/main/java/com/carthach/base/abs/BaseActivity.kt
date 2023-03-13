package com.carthach.base.abs

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.carthach.base.util.canClick
import com.carthach.base.util.changeStatusBar
import com.hjq.toast.ToastUtils

/**
 * 基础Activity, 需要传入viewBinding
 */
abstract class BaseActivity<VB:ViewBinding> :AppCompatActivity(), View.OnClickListener{

    protected open lateinit var mViewBinding: VB
    protected abstract fun initViewBinding() : VB
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = initViewBinding()
        setContentView(mViewBinding.root)
        changeStatusBar(this, translucentStatusBar = translucentStatusBar(), statusBarSetLightMode = statusBarSetLightMode())
        init()

    }

    open fun translucentStatusBar(): Boolean {
        return true
    }

    open fun statusBarSetLightMode(): Boolean {
        return false
    }

    protected fun showToast(str: String?) {
        str?.let {
            ToastUtils.show(it)
        }
    }

    override fun onClick(v: View?) {
        if (!canClick) return
    }


}