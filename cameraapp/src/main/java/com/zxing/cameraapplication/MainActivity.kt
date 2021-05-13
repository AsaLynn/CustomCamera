package com.zxing.cameraapplication

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.water.camera.util.DeviceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val GET_PERMISSION_REQUEST = 100 //权限申请自定义码

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener { v: View -> permissions }
        device!!.text = DeviceUtil.getDeviceInfo()
        //不具有获取权限，需要进行权限申请
    }

    /**
     * 获取权限
     */
    private val permissions: Unit
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivityForResult(
                        Intent(this@MainActivity, CameraActivity::class.java),
                        100
                    )
                } else {
                    //不具有获取权限，需要进行权限申请
                    ActivityCompat.requestPermissions(
                        this@MainActivity, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA
                        ), GET_PERMISSION_REQUEST
                    )
                }
            } else {
                CameraActivity.jumpTo(this@MainActivity, 101)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            Log.i("CJT", "picture")
            data?.let {
                photo.setImageBitmap(BitmapFactory.decodeFile(it.getStringExtra("path")))
            }
        }
        if (resultCode == 102) {
            Log.i("CJT", "video")
            val path = data!!.getStringExtra("path")
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show()
        }
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GET_PERMISSION_REQUEST) {
            var size = 0
            if (grantResults.size >= 1) {
                val writeResult = grantResults[0]
                //读写内存权限
                val writeGranted = writeResult == PackageManager.PERMISSION_GRANTED //读写内存权限
                if (!writeGranted) {
                    size++
                }
                //录音权限
                val recordPermissionResult = grantResults[1]
                val recordPermissionGranted =
                    recordPermissionResult == PackageManager.PERMISSION_GRANTED
                if (!recordPermissionGranted) {
                    size++
                }
                //相机权限
                val cameraPermissionResult = grantResults[2]
                val cameraPermissionGranted =
                    cameraPermissionResult == PackageManager.PERMISSION_GRANTED
                if (!cameraPermissionGranted) {
                    size++
                }
                if (size == 0) {
                    startActivityForResult(
                        Intent(this@MainActivity, CameraActivity::class.java),
                        100
                    )
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}