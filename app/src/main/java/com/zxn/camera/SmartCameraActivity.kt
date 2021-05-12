package com.zxn.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ai.camera.CameraFragment

/**
 *  智能相机.
 *  Created by zxn on 2021/5/12.
 */
class SmartCameraActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun jumpTo(context: Context) {
            context.startActivity(Intent(context, SmartCameraActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, CameraFragment.newInstance())
            .commitAllowingStateLoss()
    }
}