package com.carthach.base.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.carthach.base.abs.BaseApplication
import java.util.*

object ActivityUtil {

    private val mActivitys = Stack<Activity>()

    fun init(context: Application) {
        mActivitys.clear()
        context.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                mActivitys.add(p0)
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                mActivitys.remove(p0)
            }
        })
    }

    fun finishActivity(vararg cls: Class<out Activity?>) {
        if (!cls.isNullOrEmpty()) {
            mActivitys.forEach { activiy ->
                if (cls.contains(activiy::class.java)) {
                    activiy.finish()
                }
            }
        }
    }

    fun getLauncherActivity(pkg: String): String? {
        if (pkg.isNullOrEmpty()) {
            return ""
        }
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pkg)
        val pm: PackageManager = BaseApplication.instance.packageManager
        val info = pm.queryIntentActivities(intent, 0)
        return if (info == null || info.size == 0) {
            ""
        } else info[0].activityInfo.name
    }

    fun getActivityList():List<Activity>{
        return mActivitys
    }

//    private val mActivityList = LinkedList<Activity>()

//    fun getActivityList(): List<Activity> {
//        if (!mActivityList.isEmpty()) {
//            return LinkedList(mActivityList)
//        }
//        val reflectActivities: List<Activity>? = getActivitiesByReflect()
//        if (reflectActivities != null) {
//            mActivityList.addAll(reflectActivities)
//        }
//        return LinkedList(mActivityList)
//    }
//
//    private fun getActivitiesByReflect(): List<Activity>? {
//        val list = LinkedList<Activity>()
//        var topActivity: Activity? = null
//        try {
//            val activityThread: Any? = getActivityThread()
//            val mActivitiesField = activityThread?.javaClass?.getDeclaredField("mActivities")
//            mActivitiesField?.isAccessible = true
//            val mActivities = mActivitiesField?.get(activityThread) as? Map<*, *>
//                ?: return list
//            val binder_activityClientRecord_map = mActivities as Map<Any, Any>
//            for (activityRecord in binder_activityClientRecord_map.values) {
//                val activityClientRecordClass: Class<*> = activityRecord.javaClass
//                val activityField = activityClientRecordClass.getDeclaredField("activity")
//                activityField.isAccessible = true
//                val activity = activityField[activityRecord] as Activity
//                if (topActivity == null) {
//                    val pausedField = activityClientRecordClass.getDeclaredField("paused")
//                    pausedField.isAccessible = true
//                    if (!pausedField.getBoolean(activityRecord)) {
//                        topActivity = activity
//                    } else {
//                        list.add(activity)
//                    }
//                } else {
//                    list.add(activity)
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("UtilsActivityLifecycle", "getActivitiesByReflect: " + e.message)
//        }
//        if (topActivity != null) {
//            list.addFirst(topActivity)
//        }
//        return list
//    }
//
//    private fun getActivityThread(): Any? {
//        val activityThread: Any? = getActivityThreadInActivityThreadStaticField()
//        return activityThread ?: getActivityThreadInActivityThreadStaticMethod()
//    }
//
//    private fun getActivityThreadInActivityThreadStaticField(): Any? {
//        return try {
//            val activityThreadClass = Class.forName("android.app.ActivityThread")
//            val sCurrentActivityThreadField =
//                activityThreadClass.getDeclaredField("sCurrentActivityThread")
//            sCurrentActivityThreadField.isAccessible = true
//            sCurrentActivityThreadField[null]
//        } catch (e: java.lang.Exception) {
//            Log.e("UtilsActivityLifecycle",
//                "getActivityThreadInActivityThreadStaticField: " + e.message)
//            null
//        }
//    }
//
//    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
//        return try {
//            val activityThreadClass = Class.forName("android.app.ActivityThread")
//            activityThreadClass.getMethod("currentActivityThread").invoke(null)
//        } catch (e: java.lang.Exception) {
//            Log.e("UtilsActivityLifecycle",
//                "getActivityThreadInActivityThreadStaticMethod: " + e.message)
//            null
//        }
//    }

}