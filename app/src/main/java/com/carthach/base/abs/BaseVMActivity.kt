package com.carthach.base.abs

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseVMActivity<VB: ViewBinding,VM: BaseViewModel> :BaseActivity<VB>() {
    protected open lateinit var mViewModel: VM

    protected abstract fun initViewModel(): Class<VM>
    protected abstract fun initObserve()
    protected abstract fun loginInvalid()
    protected abstract fun showLoading()
    protected abstract fun dismissLoading()

    override fun init() {
        mViewModel = ViewModelProvider(this)[initViewModel()]
        observe()
    }

    private fun observe() {
        mViewModel.apply {
            isShowLoading.observe(this@BaseVMActivity) {
                if (it) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            loginInvalid.observe(this@BaseVMActivity) {
                if (it) {
                    loginInvalid()
                }
            }
        }

        initObserve()
    }


}