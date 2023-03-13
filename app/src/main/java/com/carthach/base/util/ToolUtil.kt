package com.carthach.base.util

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.BuildConfig
import com.carthach.base.abs.BaseApplication
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

var lastClickTime: Long = 0
val clickTimeInterval: Long = 500
val canClick: Boolean
    get() {
        val time = System.currentTimeMillis()
        val canClick = time - lastClickTime >= clickTimeInterval
        lastClickTime = time
        return canClick
    }

fun changeStatusBar(
    activity: AppCompatActivity,
    translucentStatusBar: Boolean = true,
    statusBarSetLightMode: Boolean,
) {
    if (translucentStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var visibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!statusBarSetLightMode) {
                    visibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.decorView.systemUiVisibility = visibility
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!statusBarSetLightMode) {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }
}

fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager?.let {
        val outMetrics = DisplayMetrics()
        it.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    return 0
}

fun getScreenHeight(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager?.let {
        val outMetrics = DisplayMetrics()
        it.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }
    return 0
}

fun getScreenDensity(): Float {
    return Resources.getSystem().displayMetrics.density
}

fun getScreenDensityDpi(): Int {
    return Resources.getSystem().displayMetrics.densityDpi
}

fun goneView(vararg views: View?) {
    if (!views.isNullOrEmpty()) {
        for (view in views) {
            if (view != null && view.visibility != View.GONE) {
                view.visibility = View.GONE
            }
        }
    }
}

fun visibleView(vararg views: View?) {
    if (!views.isNullOrEmpty()) {
        for (view in views) {
            if (view != null && view.visibility != View.VISIBLE) {
                view.visibility = View.VISIBLE
            }
        }
    }
}

fun setTextsColor(@ColorRes colorId: Int, vararg views: TextView?) {
    if (!views.isNullOrEmpty()) {
        for (view in views) {
            view?.setTextColor(getCommonColor(colorId))
        }
    }
}

fun getCommonString(@StringRes resId: Int, vararg formatArgs: Any?): String? {
    return if (resId == 0) {
        ""
    } else BaseApplication.instance.resources.getString(resId, formatArgs)
}

fun showToast(msg: String?) {
    msg?.let {
        ToastUtils.show(msg)
    }
}

fun log(message: String) {
    log("log--->", message)
}

fun log(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun getCommonString(@StringRes resId: Int): String {
    return BaseApplication.instance.resources.getString(resId)
}

@JvmName("getCommonStringJvm")
fun getCommonStringJvm(@StringRes resId: Int, vararg formatArgs: String?): String {
    return if (resId == 0) {
        ""
    } else BaseApplication.instance.resources.getString(resId, formatArgs)
}

fun getCommonColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(BaseApplication.instance, colorId)
}

fun getCommonDrawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(BaseApplication.instance, drawableId)
}

fun getAppVersionCode(): Int {
    return getAppVersionCode(BaseApplication.instance.packageName)
}

fun getAppVersionName(): String? {
    return getAppVersionName(BaseApplication.instance.packageName)
}

fun getAppVersionCode(name: String?): Int {
    return try {
        val pm: PackageManager = BaseApplication.instance.packageManager
        val pi = pm.getPackageInfo(name!!, 0)
        pi?.versionCode ?: -1
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}

fun getAppVersionName(name: String?): String? {
    return try {
        val pm = BaseApplication.instance.packageManager
        val pi = pm.getPackageInfo(name!!, 0)
        if (pi == null) "" else pi.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

fun isMobileNo(phone: String?): Boolean {
    val regex = "^01[0,1,2,5]\\d{8}\$"
    return !phone.isNullOrEmpty() && phone!!.length == 11 && Pattern.matches(regex, phone)
}

fun isEmail(email: String?): Boolean {
    var regex =
        "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"
    return !email.isNullOrEmpty() && Pattern.matches(regex, email)
}

fun getModel(): String? {
    var model = Build.MODEL
    model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
    return model
}

fun unitFormat(i: Int): String? {
    return if (i in 0..9) "0$i" else i.toString()
}

fun getTime(time: Int): String? {
    var timeStr = ""
    var hour = 0
    var minute = 0
    var second = 0
    minute = time / 60
    if (minute < 60) {
        second = time % 60
        timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second)
    } else {
        hour = time / 3600
        minute = time % 3600 / 60
        second = time % 3600 % 60
        timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
    }
    return timeStr
}

fun timeMillisFormatDate(time: Long): String? {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(time))
}

fun SharedPreferences.Editor.removeValues(vararg keys: String?) {
    if (!keys.isNullOrEmpty()) {
        for (key in keys) {
            if (!key.isNullOrEmpty()) {
                remove(key)
            }
        }
        commit()
    }
}

fun FragmentActivity.countDown(
    time: Int = 5,
    start: ((scope: CoroutineScope) -> Unit)? = null,
    end: (() -> Unit)? = null,
    next: ((time: Int) -> Unit)? = null,
) {
    lifecycleScope.launch {
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            start?.let { it(this@launch) }

        }.onCompletion {
            if (!isFinishing) {
                end?.let { it.invoke() }
            }
        }.catch {
            log("")
        }.collect { t ->
            next?.let { it.invoke(t) }
        }

    }
}

fun Fragment.countDown(
    time: Int = 5,
    start: ((scope: CoroutineScope) -> Unit)? = null,
    end: (() -> Unit)? = null,
    next: ((time: Int) -> Unit)? = null,
) {
    lifecycleScope.launch {
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            start?.let { it(this@launch) }
        }.onCompletion {
            if (!isDetached) {
                end?.let { it.invoke() }
            }
        }.catch {
            log("")
        }.collect { t ->
            next?.let { it.invoke(t) }
        }

    }
}

fun <T> Collection<T>.toArrayList() = ArrayList<T>(this)

fun initNoList(list: List<String?>): String? {
    val builder = StringBuilder()
    for (i in list.indices) {
        if (i != 0) {
            builder.append(" ")
        }
        builder.append(list[i])
    }
    return builder.toString()
}

fun isMatch(regex: String?, input: CharSequence?): Boolean {
    return !input.isNullOrEmpty() && Pattern.matches(regex, input)
}

fun containsEmoji(str: String): Boolean {
    for (element in str) {
        if (isEmojiCharacter(element)) {
            return true
        }
    }
    return false
}

fun isEmojiCharacter(codePoint: Char): Boolean {
    val code = codePoint.code
    return !(code == 0x0 || code == 0x9 || code == 0xA || code == 0xD || code in 0x20..0xD7FF || code in 0xE000..0xFFFD || code in 0x10000..0x10FFFF)
}

fun requestFocus(view: View) {
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
}

fun getCurrentTimeZone(): String? {
    val tz = TimeZone.getDefault()
    return createGmtOffsetString(
        includeGmt = true,
        includeMinuteSeparator = true,
        offsetMillis = tz.rawOffset
    )
}

fun createGmtOffsetString(
    includeGmt: Boolean,
    includeMinuteSeparator: Boolean,
    offsetMillis: Int,
): String? {
    var offsetMinutes = offsetMillis / 60000
    var sign = '+'
    if (offsetMinutes < 0) {
        sign = '-'
        offsetMinutes = -offsetMinutes
    }
    val builder = java.lang.StringBuilder(9)
    if (includeGmt) {
        builder.append("GMT")
    }
    builder.append(sign)
    appendNumber(builder, 2, offsetMinutes / 60)
    if (includeMinuteSeparator) {
        builder.append(':')
    }
    appendNumber(builder, 2, offsetMinutes % 60)
    return builder.toString()
}

fun appendNumber(builder: java.lang.StringBuilder, count: Int, value: Int) {
    val string = value.toString()
    for (i in 0 until count - string.length) {
        builder.append('0')
    }
    builder.append(string)
}

fun mobileProtect(mobile: String?): String? {
    if (mobile.isNullOrEmpty()) {
        return ""
    }
    if (mobile.length < 10) {
        return mobile
    }
    return mobile.replaceRange(3, 7, "****")
}

fun copyText(text: CharSequence?) {
    val cm =
        BaseApplication.instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText(BaseApplication.instance.packageName, text))
}

fun getSystemLanguage(): Locale {
    return getLocal(Resources.getSystem().configuration)
}

private fun getLocal(configuration: Configuration): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0]
    } else {
        configuration.locale
    }
}

fun getLaunchAppIntent(pkgName: String): Intent? {
    val launcherActivity: String? = ActivityUtil.getLauncherActivity(pkgName)
    if (launcherActivity.isNullOrEmpty()) return null
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setClassName(pkgName, launcherActivity)
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

fun relaunchApp() {
    relaunchApp(false)
}

fun relaunchApp(isKillProcess: Boolean) {
    val intent: Intent? = getLaunchAppIntent(BaseApplication.instance.packageName)
    if (intent == null) {
        Log.e("AppUtils", "Didn't exist launcher activity.")
        return
    }
    intent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    BaseApplication.instance.startActivity(intent)
    if (!isKillProcess) return
    Process.killProcess(Process.myPid())
    System.exit(0)
}

fun div(v1: String?, v2: String?, scale: Int): String? {
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
}

fun mul(v1: String?, v2: String?): String? {
    var v1 = v1
    var v2 = v2
    if (TextUtils.isEmpty(v1)) v1 = "0"
    if (TextUtils.isEmpty(v2)) v2 = "0"
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.multiply(b2).stripTrailingZeros().toPlainString()
}

fun sub(v1: String?, v2: String?): String? {
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.subtract(b2).stripTrailingZeros().toPlainString()
}

fun add(v1: String?, v2: String?): String? {
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.add(b2).stripTrailingZeros().toPlainString()
}

/*
     * 将时间转换为时间戳
     */
@SuppressLint("SimpleDateFormat")
fun dateToStamp(s: String?): String? {
    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = simpleDateFormat.parse(s)
        val ts = date.time
        ts.toString()
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun millis2String(millis: Long, format: DateFormat): String? {
    return format.format(Date(millis))
}

fun StringToLong(num: String?): Long {
    var anInt: Long = 0
    anInt = try {
        num!!.toLong()
    } catch (e: Exception) {
        return 0
    }
    return anInt
}


