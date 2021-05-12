package com.zxn.camera;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.camera.CameraActivity;
import com.ai.camera.MongolianLayerType;
import com.ai.camera.PermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_IMAGE_PATH = "imagePath";
    private ImageView resultImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultImg = findViewById(R.id.result);
        findViewById(R.id.btnTake).setOnClickListener(v -> openCamera());
    }

    /**
     * 打开相机.
     */
    private void openCamera() {
        PermissionUtils.applicationPermissions(this, new PermissionUtils.PermissionListener() {
            @Override
            public void onSuccess(Context context) {
                /*Intent intent = new Intent(activity, CameraActivity.class);
                intent.putExtra("MongolianLayerType", type);
                activity.startActivityForResult(intent, requestCode);*/
                SmartCameraActivity.jumpTo(MainActivity.this);
            }

            @Override
            public void onFailed(Context context) {
                if (AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.CAMERA)
                        && AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.STORAGE)) {
                    AndPermission.with(context).runtime().setting().start();
                }
                Toast.makeText(context, context.getString(com.ai.camera.R.string.permission_camra_storage), Toast.LENGTH_SHORT);
            }
        }, Permission.Group.STORAGE, Permission.Group.CAMERA);

    }

    /**
     * 注释：跳转拍照
     * 时间：2019/3/21 0021 9:13
     * 作者：郭翰林
     */
    public void gotoCamera(View view) {
        CameraActivity.startMe(this, 2005, MongolianLayerType.IDCARD_POSITIVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            resultImg.setImageURI(Uri.fromFile(new File(data.getStringExtra(KEY_IMAGE_PATH))));
        }
    }
}
