package com.carthach.base.util

import java.io.PrintWriter
import java.io.StringWriter

fun getStackTrace(t: Throwable): String? {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    return pw.use { pw ->
        t.printStackTrace(pw)
        sw.toString()
    }
}