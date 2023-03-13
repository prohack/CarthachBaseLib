package com.carthach.base.abs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carthach.base.http.ApiHttpException
import com.carthach.base.http.LoginInvalidException
import com.carthach.base.util.getStackTrace
import com.carthach.base.util.log
import com.carthach.base.util.showToast
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException


typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Complete = suspend () -> Unit
typealias Cancel = suspend () -> Unit

open class BaseViewModel : ViewModel() {

    val isShowLoading = MutableLiveData(false)
    val loginInvalid = MutableLiveData<Boolean>()

    protected fun launch(
        block: Block<Unit>,
        error: Error? = null,
        complete: Complete? = null,
        cancel: Cancel? = null,
        needChangeLoadingState: Boolean = true,
        needToast: Boolean = true,
    ) :Job{
        return viewModelScope.launch{
            try {
                block.invoke(this)

            }catch (e : Exception){
                if (needChangeLoadingState){
                    isShowLoading.value = false
                }
                if (e is CancellationException){
                    cancel?.invoke()
                }else {
                    error?.invoke(e)
                    onError(e,needToast)
                }
            }
        }
    }

    protected open fun onError(e: Exception, needToast: Boolean){
        when(e){
            is ApiHttpException -> if (needToast){
                e.message?.let {
                    ToastUtils.show(it)
                }
            }

            is LoginInvalidException -> {
                loginInvalid.value = true
                if (needToast) {
                    e.message?.let {
                        ToastUtils.show(it)
                    }
                }
            }

            is ConnectException , is SocketTimeoutException -> if (needToast){
                ToastUtils.show("network error") //todo invoke then handler it
            }
            else -> {
                if (needToast){
                    ToastUtils.show("error")
                }
                log("ViewModel Error", getStackTrace(e) ?: "error")
            }
        }
    }

}