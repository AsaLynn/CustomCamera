package com.water.camera.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.water.camera.CameraInterface;

/**
 * =====================================
 * 创建日期：2017/9/8
 * 描    述：
 * =====================================
 */
public interface State {

    void start(SurfaceHolder holder, float screenProp);

    void stop();

    void focus(float x, float y, CameraInterface.FocusCallback callback);

    void onSwitch(SurfaceHolder holder, float screenProp);

    void restart();

    void capture();

    void capture(Boolean previewEnabled);

    void record(Surface surface, float screenProp);

    void stopRecord(boolean isShort, long time);

    void cancel(SurfaceHolder holder, float screenProp);

    void confirm();

    void zoom(float zoom, int type);

    void flash(String mode);
}
