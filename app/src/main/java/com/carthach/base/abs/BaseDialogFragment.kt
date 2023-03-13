package com.carthach.base.abs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.carthach.base.util.canClick
import java.lang.ref.WeakReference

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() , View.OnClickListener{

    protected lateinit var mContext: Context

    protected open lateinit var mViewBinding: VB

    protected abstract fun initViewBinding(): VB

    protected abstract fun getDialogStyle(): Int

    protected abstract fun canCancel(): Boolean

    protected abstract fun setWindowAttributes(window: Window?)

    protected abstract fun init()

    protected var dismissListener: (() -> Unit?)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mContext = WeakReference(activity).get()!!
        mViewBinding = initViewBinding()
        val dialog = Dialog(mContext, getDialogStyle())
        dialog.setContentView(mViewBinding.root)
        dialog.setCancelable(canCancel())
        dialog.setCanceledOnTouchOutside(canCancel())
        setWindowAttributes(dialog.window)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun isShow(): Boolean {
        return if (this.isDetached || this.isHidden || this.dialog == null) {
            false
        } else this.dialog!!.isShowing
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded && null == manager.findFragmentByTag(tag)) {
            val transaction = manager.beginTransaction()
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    override fun dismiss() {
        onDismiss(this.dialog!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissListener?.invoke()
    }

    override fun onClick(v: View?) {
        if (!canClick) return
    }

    fun setOnDismissListener(listener: () -> Unit) {
        dismissListener = listener
    }

}