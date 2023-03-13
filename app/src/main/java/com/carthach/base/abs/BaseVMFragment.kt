package com.carthach.base.abs

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseVMFragment<VB: ViewBinding, VM: BaseViewModel> : BaseFragment<VB>(){

    protected open lateinit var mViewModel: VM

    protected abstract fun initViewModel(): Class<VM>
    protected abstract fun initObserve()
    protected abstract fun loginInvalid()

    override fun init() {
        mViewModel = ViewModelProvider(this).get(initViewModel())
        observe()
    }

    private fun observe() {
        mViewModel.apply {
            isShowLoading.observe(this@BaseVMFragment) {
                if (it) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            loginInvalid.observe(this@BaseVMFragment) {
                if (it) {
                    loginInvalid()
                }
            }
        }
        initObserve()
    }

    open fun showLoading() {

    }

    open fun dismissLoading() {

    }

}