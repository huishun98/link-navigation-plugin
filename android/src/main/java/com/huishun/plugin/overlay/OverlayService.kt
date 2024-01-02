package com.huishun.plugin.overlay

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.json.JSONArray


class OverlayService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val values = intent?.getStringExtra("values")
            ?: return super.onStartCommand(intent, flags, startId)
        val packageName = intent.getStringExtra("package")
            ?: return super.onStartCommand(intent, flags, startId)

        val jsonArray = JSONArray(values)

        val data = ArrayList<UrlInfo>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val url = jsonObject.optString("url", "https://google.com")
            val name = jsonObject.optString("name", "")
            val query = jsonObject.optString("query", "")
            val longitude = jsonObject.optDouble("longitude", 0.0)
            val latitude = jsonObject.optDouble("latitude", 0.0)
            val displacement = jsonObject.optDouble("displacement", 0.0)
            data.add(UrlInfo(url, name, query, latitude, longitude, displacement))
        }

        startMyOwnForeground()
        WindowView.window = WindowView(this.applicationContext, data, packageName)
        WindowView.window.open()
        return super.onStartCommand(intent, flags, startId)
    }

    // for android version >=O we need to create
    // custom notification stating
    // foreground service is running
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val channelId = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Service running")
            .setContentText("Displaying over other apps") // important, otherwise the notification will show some default
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }
}
