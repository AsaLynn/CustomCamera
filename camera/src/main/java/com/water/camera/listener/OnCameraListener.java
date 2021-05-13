package com.water.camera.listener;

import android.graphics.Bitmap;

/**
 * =====================================
 * 描    述：
 * =====================================
 */
public interface OnCameraListener {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);

}
