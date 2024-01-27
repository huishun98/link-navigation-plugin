package com.huishun.plugin.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
class WindowView( // declaring required variables
    private val context: Context,
    private val data: ArrayList<UrlInfo>,
    private val packageName: String
) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var window: WindowView
    }

    private val mView: View
    private var mParams: WindowManager.LayoutParams
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater
    private var nameTextView: TextView
    private var distanceTextView: TextView
    private var mapBtn: ImageButton
    private var i = -1

    init {
        // set the layout parameters of the window
        val metrics = context.resources.displayMetrics
        mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
            // than filling the screen
            (metrics.widthPixels * 0.95f).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,  // Display it on top of other application windows
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
            // through any transparent parts
            PixelFormat.TRANSLUCENT
        )
        mParams.gravity = Gravity.BOTTOM
        mParams.x = 0
        mParams.y = 0

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.popup_window, null)

        mView.findViewById<View>(R.id.window_close).setOnClickListener { i = -1; openApp() } // close()
        mView.findViewById<View>(R.id.window_next).setOnClickListener { next() }
        mView.findViewById<View>(R.id.window_previous).setOnClickListener { previous() }
        mView.findViewById<TextView>(R.id.exit_btn).setOnClickListener { close() }
        mView.findViewById<ImageButton>(R.id.edit_btn).setOnClickListener { openApp() }
        mapBtn = mView.findViewById(R.id.map_btn)
        mapBtn.setOnClickListener { openMap() }
        distanceTextView = mView.findViewById(R.id.distance_text)
        nameTextView = mView.findViewById(R.id.name_text)

        val viewTouchListener = object: View.OnTouchListener {
            val updatedFloatWindowLayoutParam = mParams
            var x = 0.0; var y = 0.0; var px = 0.0; var py = 0.0

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = updatedFloatWindowLayoutParam.x.toDouble()
                        y = updatedFloatWindowLayoutParam.y.toDouble()
                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        updateWindowPosition((x + event.rawX - px).toInt(), (y - event.rawY + py).toInt())
                    }
                }
                return false
            }
        }
        mView.setOnTouchListener(viewTouchListener)

        mWindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private fun updateWindowPosition(x: Int, y: Int) {
        val updatedFloatWindowLayoutParam = mParams
        updatedFloatWindowLayoutParam.x = x
        updatedFloatWindowLayoutParam.y = y
        mWindowManager.updateViewLayout(mView, updatedFloatWindowLayoutParam)
    }

    private fun openMap() {
        val info = data[i]
        val q = Uri.encode(info.query)
        val gmmIntentUri = Uri.parse("geo:${info.latitude},${info.longitude}?q=$q")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Install a map app to use this feature", Toast.LENGTH_SHORT).show()
        }
    }

    fun open() {
        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.windowToken == null && mView.parent == null) {
                mWindowManager.addView(mView, mParams)
                next()
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    private fun next() {
        i += 1
        if (i >= data.size) return openApp() // close()
        openUrl()
    }

    private fun previous() {
        i -= 1
        Log.d("NarieInfo", "prev")
        if (i < 0) return openApp() // close()
        openUrl()
    }

    @SuppressLint("SetTextI18n")
    private fun openUrl() {
        val info = data[i]
        val notALoc = info.latitude == null && info.longitude == null
        mapBtn.visibility = if (notALoc) View.GONE else View.VISIBLE
        nameTextView.text = info.name
        distanceTextView.text = if (info.displacement == null) "" else "~${info.displacement.toInt()}m away"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(info.url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun openApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent) //null pointer check in case package name was not found
        }
    }

    fun getActiveEntry(): UrlInfo? {
        return if (i >= 0 && i < data.size) data[i] else null
    }

    fun isActive(): Boolean {
        return mView.isAttachedToWindow
    }

    fun close() {
        try {
            // remove the view from the window
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(mView)
            // invalidate the view
            mView.invalidate()
            // remove all views
            (mView.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}
