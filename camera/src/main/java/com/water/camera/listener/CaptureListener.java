package com.water.camera.listener;

public interface CaptureListener {
    /**
     * 拍照
     */
    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();
}
