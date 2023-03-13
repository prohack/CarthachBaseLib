package com.carthach.base.http

import java.lang.Exception

class LoginInvalidException (val code:Int, override val message:String?):Exception()