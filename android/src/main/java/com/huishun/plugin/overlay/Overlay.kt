package com.huishun.plugin.overlay

import android.util.Log

class Overlay {
    fun echo(value: String?): String? {
        Log.i("Echo", value!!)
        return value
    }
}
