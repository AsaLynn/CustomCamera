package com.ai.camera

import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 *  Created by zxn on 2021/5/12.
 */
class CameraFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()
    }

    /**
     * 拍照标记
     */
    private var isTakePhoto = false

    /**
     * 相机类
     */
    private var mCamera: Camera? = null

    /**
     * 拍摄按钮视图
     */
    private val mPhotoLayout: RelativeLayout? = null

    /**
     * 确定按钮视图
     */
    private val mConfirmLayout: RelativeLayout? = null

    /**
     * 图片流暂存
     */
    private var imageData: ByteArray? = null

    /**
     * 聚焦视图
     */
    private var mOverCameraView: OverCameraView? = null

    /**
     * 相机预览
     */
    private val mPreviewLayout: FrameLayout? = null

    /**
     * 蒙版类型
     */
    private val mMongolianLayerType: MongolianLayerType = MongolianLayerType.IDCARD_POSITIVE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onResume() {
        super.onResume()

        val preview = CameraPreview(activity, mCamera)
        mOverCameraView = OverCameraView(activity)
        mPreviewLayout?.addView(preview)
        mPreviewLayout?.addView(mOverCameraView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnCancel.setOnClickListener {
            activity?.finish()
        }
        bntTakePhoto.setOnClickListener {
            if (!isTakePhoto) {
                takePhoto()
            }
        }

        if (mMongolianLayerType != MongolianLayerType.PASSPORT_ENTRY_AND_EXIT) {
            Glide.with(context).load(getMaskImage()).into(mMaskImage)
        } else {
            mMaskImage.setVisibility(View.GONE)
            mPassportEntryAndExitImage.setVisibility(View.VISIBLE)
        }
    }

    /**
     * 注释：拍照并保存图片到相册
     * 时间：2019/3/1 0001 15:37
     * 作者：郭翰林
     */
    private fun takePhoto() {
        isTakePhoto = true
        //调用相机拍照
        mCamera?.takePicture(null, null, null, { data: ByteArray, camera1: Camera? ->
            data?.let {
                mCamera?.apply {
                    lock()
                    stopPreview()
                    release()
                }
            }
            //视图动画
            mPhotoLayout?.setVisibility(View.GONE)
            mConfirmLayout?.setVisibility(View.VISIBLE)
            AnimSpring.getInstance(mConfirmLayout).startRotateAnim(120f, 360f)
            imageData = data
            //停止预览
            mCamera?.stopPreview()
        })
    }

    /**
     * 注释：获取蒙版图片
     */
    private fun getMaskImage(): Int {
        when (mMongolianLayerType) {
            MongolianLayerType.BANK_CARD -> {
                return R.mipmap.bank_card
            }
            MongolianLayerType.HK_MACAO_TAIWAN_PASSES_POSITIVE -> {
                return R.mipmap.hk_macao_taiwan_passes_positive
            }
            MongolianLayerType.HK_MACAO_TAIWAN_PASSES_NEGATIVE -> {
                return R.mipmap.hk_macao_taiwan_passes_negative
            }
            MongolianLayerType.IDCARD_POSITIVE -> {
                return R.mipmap.idcard_positive
            }
            MongolianLayerType.IDCARD_NEGATIVE -> {
                return R.mipmap.idcard_negative
            }
            MongolianLayerType.PASSPORT_PERSON_INFO -> {
                return R.mipmap.passport_person_info
            }
            else -> return 0
        }
    }

}