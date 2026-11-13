package com.russia.launcher.ui.activity

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.russia.game.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private var permissionsGranded = false
    private var animationEnded = false
    // var gpuDetected             = false

    private val REQUEST_ID = 228
    private val permissionList = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        //Manifest.permission.POST_NOTIFICATIONS // 13+ bug
    )

    private lateinit var binding: ActivitySplashBinding

    private val isOnline: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseApp.getApps(applicationContext).isNotEmpty()) {
            FirebaseCrashlytics.getInstance().deleteUnsentReports()
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        binding.lottieLogo.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animationEnded = true
                startIfReady()
            }
        })

        if (!isOnline) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ошибка!")
                .setMessage("Нет соединения с интернетом")
                .setPositiveButton("Закрыть") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                    finishAffinity()
                }
            runOnUiThread { builder.create().show() }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_ID)
        } else {
            permissionsGranded = true
            startIfReady()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_ID) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranded = true
                    startIfReady()
                }
            }
        }
    }

    fun startIfReady() {
        if (permissionsGranded && animationEnded) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

}

//class GpuInfoActivity(activity: SplashActivity) {
//
//    private var mGlSurfaceView: GLSurfaceView? = null
//    private val mGlRenderer: Renderer = object : Renderer {
//        override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
//            MainUtils.usselesTex.remove(".dxt")
////            val glExtensions = gl?.glGetString(GL10.GL_EXTENSIONS)
////            if (glExtensions!!.contains("GL_IMG_texture_compression_pvrtc")) {
////                MainUtils.usselesTex.remove(".pvr")
////            } else if (glExtensions.contains("GL_EXT_texture_compression_dxt1") || glExtensions.contains("GL_EXT_texture_compression_s3tc") || glExtensions.contains("GL_AMD_compressed_ATC_texture")) {
////                MainUtils.usselesTex.remove(".dxt")
////            } else {
////                MainUtils.usselesTex.remove(".etc")
////            }
////
//            activity.gpuDetected = true
//            activity.startIfReady()
//        }
//
//        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
//            // TODO Auto-generated method stub
//        }
//
//        override fun onDrawFrame(gl: GL10) {
//            // TODO Auto-generated method stub
//        }
//    }
//
//    init {
//        mGlSurfaceView = GLSurfaceView(activity)
//        mGlSurfaceView!!.setRenderer(mGlRenderer)
//        activity.findViewById<FrameLayout>(R.id.activitySplash).addView(mGlSurfaceView)
//        mGlSurfaceView!!.layoutParams.width = 1
//        mGlSurfaceView!!.layoutParams.height = 1
//    }
//}