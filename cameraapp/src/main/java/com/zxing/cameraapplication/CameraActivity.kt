package com.zxing.cameraapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.water.camera.CameraView
import com.water.camera.listener.ErrorListener
import com.water.camera.listener.OnCameraListener
import com.water.camera.util.DeviceUtil
import com.water.camera.util.FileUtil
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

/**
 * 自定义相机页面.
 */
class CameraActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraActivity"

        @JvmStatic
        fun jumpTo(context: Context, requestCode: Int = 0) {
            val intent = Intent(context, CameraActivity::class.java)
            if (requestCode == 0) {
                context.startActivity(intent)
            } else {
                (context as Activity).startActivityForResult(
                    intent, requestCode
                )
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_camera)

        cameraView.run {
            //设置视频保存路径
            setSaveVideoPath(Environment.getExternalStorageDirectory().path + File.separator + "JCamera")
            setFeatures(CameraView.BUTTON_STATE_ONLY_CAPTURE)
            setTip("")
            setErrorLisenter(object : ErrorListener {
                override fun onError() {
                    //错误监听
                    Log.i("CJT", "camera error")
                    val intent = Intent()
                    setResult(103, intent)
                    finish()
                }

                override fun AudioPermissionError() {
                    Toast.makeText(this@CameraActivity, "给点录音权限可以?", Toast.LENGTH_SHORT).show()
                }
            })

            setOnCameraListener(object :
                OnCameraListener {
                override fun captureSuccess(bitmap: Bitmap) {
                    Log.i(TAG, "captureSuccess: ")
                    val path = FileUtil.saveBitmap("JCamera", bitmap)
                    val intent = Intent()
                    intent.putExtra("path", path)
                    setResult(101, intent)
                    finish()
                }

                override fun recordSuccess(url: String, firstFrame: Bitmap) {
                    //获取视频路径
                    val path = FileUtil.saveBitmap("JCamera", firstFrame)
                    Log.i("CJT", "url = $url, Bitmap = $path")
                    val intent = Intent()
                    intent.putExtra("path", path)
                    setResult(102, intent)
                    finish()
                }
            })
            setLeftClickListener { finish() }
            setRightClickListener {
                Toast.makeText(
                    this@CameraActivity,
                    "Right",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        Log.i("CJT", DeviceUtil.getDeviceModel())
    }

    override fun onStart() {
        super.onStart()
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }

    override fun onResume() {
        super.onResume()
        cameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraView.onPause()
    }
}