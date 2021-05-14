# CameraView（1.0.0）  

## 使用方法
- Gradle依赖：  implementation 'com.github.AsaLynn:CustomCamera :1.0.0'

这是一个模仿微信拍照的Android开源控件
- 点击拍照

- 10s的视频大概1.9M左右

- 长按录视频（视频长度可设置）

- 长按录视频的时候，手指上滑可以放大视频

- 录制完视频可以浏览并且重复播放

- 前后摄像头的切换

- 可以设置小视频保存路径

## 示例截图

![image](https://github.com/CJT2325/CameraView/blob/master/assets/screenshot_0.jpg)
![image](https://github.com/CJT2325/CameraView/blob/master/assets/screenshot_1.jpg)
![image](https://github.com/CJT2325/CameraView/blob/master/assets/screenshot_2.jpg)

## GIF图

![image](https://github.com/CJT2325/CameraView/blob/master/assets/video.gif)

## 使用步骤（Android Studio）

**添加下列代码到 module gradle**

> 最新版本（1.1.9）更新内容：
```gradle
implementation 'com.github.AsaLynn:CustomCamera :1.0.0'
//添加闪关灯,自定义左右按钮图标资源
```
**如果获取依赖失败则添加下列代码到 project gradle**
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://www.jitpack.io" }
    }
}
```

### 旧版本
```gradle
implementation 'cjt.library.wheel:camera:1.0.0'
//修复BUG
```

```
## 布局文件中添加
```xml
//1.0.0+
<com.cjt2325.cameralibrary.JCameraView
    android:id="@+id/jcameraview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:duration_max="10000"
    app:iconMargin="20dp"
    app:iconSize="30dp"
    app:iconSrc="@drawable/ic_camera_enhance_black_24dp"/>
```
### （1.0.0+）
属性 | 属性说明
---|---
iconSize | 右上角切换摄像头按钮的大小
iconMargin | 右上角切换摄像头按钮到上、右边距
iconSrc | 右上角切换摄像头按钮图片
iconLeft | 左边按钮图片资源（1.1.9+）
iconRight | 右边按钮图片资源（1.1.9+）
duration_max | 设置最长录像时间（毫秒）

### AndroidManifest.xml中添加权限
```xml
<uses-permission android:name="android.permission.FLASHLIGHT" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
```

### LICENSE
Copyright 2017 CJT2325

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
