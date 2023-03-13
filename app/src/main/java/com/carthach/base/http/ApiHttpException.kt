package com.carthach.base.http

class ApiHttpException (val code:Int, override val message:String?, val data:String? = null) :Exception()