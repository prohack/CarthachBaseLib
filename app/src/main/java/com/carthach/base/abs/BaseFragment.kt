package com.carthach.base.abs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.carthach.base.util.canClick
import com.hjq.toast.ToastUtils

abstract class BaseFragment<VB :ViewBinding> :Fragment(),View.OnClickListener{

    protected open val mViewBinding: VB by lazy { initViewBinding() }
    protected abstract fun initViewBinding() : VB
    protected abstract fun init()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mViewBinding.root
    }


    override fun onClick(p0: View?) {
        if (!canClick) return
    }

    protected fun showToast(str: String?){
        str?.let {
            ToastUtils.show(it)
        }
    }



}