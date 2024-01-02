package com.huishun.plugin.overlay

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import java.io.Serializable

data class UrlInfo(
    val url: String,
    val name: String,
    val query: String?,
    val latitude: Double?,
    val longitude: Double?,
    val displacement: Double?
) : Serializable

@CapacitorPlugin(name = "Overlay")
class OverlayPlugin : Plugin() {
    private val implementation = Overlay()

    @RequiresApi(Build.VERSION_CODES.O)
    @PluginMethod
    fun close(call: PluginCall) {
        WindowView.window.close()
        val ret = JSObject()
        call.resolve(ret)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @PluginMethod
    fun open(call: PluginCall) {
        val values = call.getArray("values")
        val ret = JSObject()

        checkOverlayPermission()
        startService(values)

        ret.put("value", implementation.echo(Build.VERSION.SDK_INT.toString()))
        call.resolve(ret)
    }

    // method for starting the service
    @RequiresApi(Build.VERSION_CODES.M)
    fun startService(values: JSArray) {
        val myIntent = Intent(this.context, OverlayService::class.java)
        myIntent.putExtra("values", values.toString())
        if (Settings.canDrawOverlays(this.context)) {
            startForegroundService(this.context, myIntent)
        }
    }

    // method to ask user to grant the Overlay permission
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this.context)) {
            // send user to the device settings
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(this.context, myIntent, null)
        }
    }
}
