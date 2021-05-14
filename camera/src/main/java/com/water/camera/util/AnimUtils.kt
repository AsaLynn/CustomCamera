package com.water.camera.util

import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.water.camera.R

/**
 * Created by zxn on 2021/5/14.
 */
object AnimUtils {

    fun playAnimation(ivAwaiting: View, ivCameraBorder: View, isPlay: Boolean) {
        if (isPlay) {
            if (ivAwaiting.animation != null) {
                ivAwaiting.animation.reset()
                ivAwaiting.animation.start()
            } else {
                ivAwaiting.startAnimation(
                    AnimationUtils.loadAnimation(
                        ivAwaiting.context,
                        R.anim.anim_camera_lens
                    ).apply {
                        interpolator = LinearInterpolator()
                    })
            }
            if (ivCameraBorder.animation != null) {
                ivCameraBorder.animation.reset()
                ivCameraBorder.animation.start()
            }else{
                ivCameraBorder.startAnimation(
                    AnimationUtils.loadAnimation(
                        ivCameraBorder.context,
                        R.anim.anim_camera_border
                    ).apply {
                        interpolator = LinearInterpolator()
                    })
            }
        } else {
            ivAwaiting.clearAnimation()
            ivCameraBorder.clearAnimation()
        }
    }

}