package com.water.camera.state;

import android.graphics.Bitmap;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.water.camera.CameraInterface;
import com.water.camera.CameraView;
import com.water.camera.util.LogUtil;

/**
 * =====================================
 * 描    述：空闲状态
 * =====================================
 */
class PreviewState implements State {
    public static final String TAG = "PreviewState";

    private CameraMachine machine;

    PreviewState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void start(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
    }

    @Override
    public void stop() {
        CameraInterface.getInstance().doStopPreview();
    }


    @Override
    public void focus(float x, float y, CameraInterface.FocusCallback callback) {
        LogUtil.i("preview state foucs");
        if (machine.getView().handlerFocus(x, y)) {
            CameraInterface.getInstance().handleFocus(machine.getContext(), x, y, callback);
        }
    }

    @Override
    public void onSwitch(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().switchCamera(holder, screenProp);
    }

    @Override
    public void restart() {

    }

    @Override
    public void capture() {
        CameraInterface.getInstance().takePicture((bitmap, isVertical) -> {
            LogUtil.i("capture");
            machine.getView().showPicture(bitmap, isVertical);
            machine.setState(machine.getBorrowPictureState());
        });
    }

    @Override
    public void capture(Boolean previewEnabled) {
        LogUtil.i("capture,previewEnabled:" + previewEnabled);
        if (previewEnabled) {
            capture();
        } else {
            CameraInterface.getInstance().takePicture((bitmap, isVertical) -> {
                LogUtil.i("capture,previewEnabled:" + false + isVertical + "bitmap:" + bitmap);
                machine.getView().takePicture(bitmap);
                machine.getView().confirmState(CameraView.TYPE_PICTURE);
                machine.setState(machine.getPreviewState());
            });
        }
    }

    @Override
    public void record(Surface surface, float screenProp) {
        CameraInterface.getInstance().startRecord(surface, screenProp, null);
    }

    @Override
    public void stopRecord(final boolean isShort, long time) {
        CameraInterface.getInstance().stopRecord(isShort, new CameraInterface.StopRecordCallback() {
            @Override
            public void recordResult(String url, Bitmap firstFrame) {
                if (isShort) {
                    machine.getView().resetState(CameraView.TYPE_SHORT);
                } else {
                    machine.getView().playVideo(firstFrame, url);
                    machine.setState(machine.getBorrowVideoState());
                }
            }
        });
    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        LogUtil.i("浏览状态下,没有 cancle 事件");
    }

    @Override
    public void confirm() {
        LogUtil.i("浏览状态下,没有 confirm 事件");
    }

    @Override
    public void zoom(float zoom, int type) {
        LogUtil.i(TAG, "zoom");
        CameraInterface.getInstance().setZoom(zoom, type);
    }

    @Override
    public void flash(String mode) {
        CameraInterface.getInstance().setFlashMode(mode);
    }
}
