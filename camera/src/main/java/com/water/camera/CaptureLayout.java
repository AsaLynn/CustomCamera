package com.water.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.camera.listener.CaptureListener;
import com.water.camera.listener.ClickListener;
import com.water.camera.listener.ReturnListener;
import com.water.camera.listener.TypeListener;
import com.water.camera.util.SizeUtils;


/**
 * =====================================
 * 描    述：集成各个控件的布局
 * =====================================
 */
public class CaptureLayout extends FrameLayout {

    private CaptureListener mCaptureListener;    //拍照按钮监听
    private TypeListener mTypeListener;          //拍照或录制后接结果按钮监听
    private ReturnListener returnListener;      //退出按钮监听
    private ClickListener leftClickListener;    //左边按钮监听
    private ClickListener rightClickListener;   //右边按钮监听
    private TextView tvLeftButton;

    public void setTypeListener(TypeListener listener) {
        this.mTypeListener = listener;
    }

    public void setCaptureListener(CaptureListener listener) {
        this.mCaptureListener = listener;
    }

    public void setReturnListener(ReturnListener returnListener) {
        this.returnListener = returnListener;
    }

    private CaptureButton btn_capture;      //拍照按钮
    private TypeButton btn_confirm;         //确认按钮
    private TypeButton btn_cancel;          //取消按钮
    private ReturnButton btn_return;        //返回按钮
    private ImageView iv_custom_left;            //左边自定义按钮
    private ImageView iv_custom_right;            //右边自定义按钮
    private TextView txt_tip;               //提示文本

    private int layout_width;
    private int layout_height;
    private int button_size;
    private int iconLeft = 0;
    private int iconRight = 0;

    private boolean isFirst = true;

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }
        button_size = (int) (layout_width / 4.5f);
        //button_size = (int) (SizeUtils.dp2px(getContext(),50));
        layout_height = button_size + (button_size / 5) * 2 + 100;

        initView();
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(layout_width, layout_height);
    }

    public void initEvent() {
        //默认为隐藏
        iv_custom_right.setVisibility(GONE);
        btn_cancel.setVisibility(GONE);
        btn_confirm.setVisibility(GONE);
    }

    public void startTypeBtnAnimator() {
        //拍照录制结果后的动画
        if (this.iconLeft != 0) {
            iv_custom_left.setVisibility(GONE);
        } else {
            btn_return.setVisibility(GONE);
        }
        if (tvLeftButton != null) {
            tvLeftButton.setVisibility(GONE);
        }

        if (this.iconRight != 0) {
            iv_custom_right.setVisibility(GONE);
        }

        btn_capture.setVisibility(GONE);
        btn_cancel.setVisibility(VISIBLE);
        btn_confirm.setVisibility(VISIBLE);
        btn_cancel.setClickable(false);
        btn_confirm.setClickable(false);
        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(btn_cancel, "translationX", layout_width / 4, 0);
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(btn_confirm, "translationX", -layout_width / 4, 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator_cancel, animator_confirm);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                btn_cancel.setClickable(true);
                btn_confirm.setClickable(true);
            }
        });
        set.setDuration(200);
        set.start();
    }

    private void initView() {
        setWillNotDraw(false);
        //拍照按钮
        btn_capture = new CaptureButton(getContext(), (int) (SizeUtils.dp2px(getContext(),50)));
        LayoutParams btn_capture_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_capture_param.gravity = Gravity.CENTER;
        btn_capture.setLayoutParams(btn_capture_param);
        btn_capture.setCaptureLisenter(new CaptureListener() {
            @Override
            public void takePictures() {
                if (mCaptureListener != null) {
                    mCaptureListener.takePictures();
                }
            }

            @Override
            public void recordShort(long time) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordShort(time);
                }
                startAlphaAnimation();
            }

            @Override
            public void recordStart() {
                if (mCaptureListener != null) {
                    mCaptureListener.recordStart();
                }
                startAlphaAnimation();
            }

            @Override
            public void recordEnd(long time) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordEnd(time);
                }
                startAlphaAnimation();
                startTypeBtnAnimator();
            }

            @Override
            public void recordZoom(float zoom) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordZoom(zoom);
                }
            }

            @Override
            public void recordError() {
                if (mCaptureListener != null) {
                    mCaptureListener.recordError();
                }
            }
        });

        //取消按钮
        btn_cancel = new TypeButton(getContext(), TypeButton.TYPE_CANCEL, button_size);
        final LayoutParams btn_cancel_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_cancel_param.gravity = Gravity.CENTER_VERTICAL;
        btn_cancel_param.setMargins((layout_width / 4) - button_size / 2, 0, 0, 0);
        btn_cancel.setLayoutParams(btn_cancel_param);
        btn_cancel.setOnClickListener(view -> {
            if (mTypeListener != null) {
                mTypeListener.cancel();
            }
            startAlphaAnimation();
        });

        //确认按钮
        btn_confirm = new TypeButton(getContext(), TypeButton.TYPE_CONFIRM, button_size);
        LayoutParams btn_confirm_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_confirm_param.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        btn_confirm_param.setMargins(0, 0, (layout_width / 4) - button_size / 2, 0);
        btn_confirm.setLayoutParams(btn_confirm_param);
        btn_confirm.setOnClickListener(view -> {
            if (mTypeListener != null) {
                mTypeListener.confirm();
            }
            startAlphaAnimation();
//                resetCaptureLayout();
        });

        //返回按钮
        btn_return = new ReturnButton(getContext(), (int) (button_size / 2.5f));
        LayoutParams btn_return_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn_return_param.gravity = Gravity.CENTER_VERTICAL;
        btn_return_param.setMargins(layout_width / 6, 0, 0, 0);
        btn_return.setLayoutParams(btn_return_param);
        btn_return.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });

        //addLeftCustomButton();

        //右边自定义按钮
        iv_custom_right = new ImageView(getContext());
        //LayoutParams iv_custom_param_right = new LayoutParams((int) (button_size / 2.5f), (int) (button_size / 2.5f));
        int w = getResources().getDimensionPixelSize(R.dimen.dp_32);
        int h = getResources().getDimensionPixelSize(R.dimen.dp_24);
        LayoutParams iv_custom_param_right = new LayoutParams(w, h);
        iv_custom_param_right.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        iv_custom_param_right.setMargins(0, 0, layout_width / 6, 0);
        iv_custom_right.setLayoutParams(iv_custom_param_right);
        iv_custom_right.setOnClickListener(v -> {
            if (rightClickListener != null) {
                rightClickListener.onClick();
            }
        });

        txt_tip = new TextView(getContext());
        LayoutParams txt_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txt_param.gravity = Gravity.CENTER_HORIZONTAL;
        txt_param.setMargins(0, 0, 0, 0);
        txt_tip.setText("轻触拍照，长按摄像");
        txt_tip.setTextColor(0xFFFFFFFF);
        txt_tip.setGravity(Gravity.CENTER);
        txt_tip.setLayoutParams(txt_param);

        this.addView(btn_capture);
        this.addView(btn_cancel);
        this.addView(btn_confirm);
        //this.addView(btn_return);
        //this.addView(iv_custom_left);
        this.addView(iv_custom_right);
        this.addView(txt_tip);

    }

    /**
     * 添加左边自定义按钮.
     */
    private void addLeftCustomButton() {
        if (leftButtonType == 0) {
            //removeView(iv_custom_left);
            iv_custom_left = new ImageView(getContext());
            LayoutParams iv_custom_param_left = new LayoutParams((int) (button_size / 2.5f), (int) (button_size / 2.5f));
            iv_custom_param_left.gravity = Gravity.CENTER_VERTICAL;
            iv_custom_param_left.setMargins(layout_width / 6, 0, 0, 0);
            iv_custom_left.setLayoutParams(iv_custom_param_left);
            iv_custom_left.setOnClickListener(v -> {
                if (leftClickListener != null) {
                    leftClickListener.onClick();
                }
            });
            addView(iv_custom_left);
        } else {
            tvLeftButton = new TextView(getContext());
            tvLeftButton.setText("取消");
            tvLeftButton.setTextColor(getResources().getColor(android.R.color.white));
            tvLeftButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LayoutParams iv_custom_param_left = new LayoutParams((int) (button_size / 2.5f), (int) (button_size / 2.5f));
            iv_custom_param_left.gravity = Gravity.CENTER_VERTICAL;
            iv_custom_param_left.setMargins(layout_width / 6, 0, 0, 0);
            tvLeftButton.setLayoutParams(iv_custom_param_left);
            tvLeftButton.setOnClickListener(v -> {
                if (leftClickListener != null) {
                    leftClickListener.onClick();
                }
            });
            addView(tvLeftButton);
        }
    }

    /**************************************************
     * 对外提供的API                      *
     **************************************************/
    public void resetCaptureLayout() {
        btn_capture.resetState();
        btn_cancel.setVisibility(GONE);
        btn_confirm.setVisibility(GONE);
        btn_capture.setVisibility(VISIBLE);
        if (leftButtonType == 0) {
            if (this.iconLeft != 0 && null != iv_custom_left) {
                iv_custom_left.setVisibility(VISIBLE);
            }
        } else if (leftButtonType == 1) {
            if (null != tvLeftButton) {
                tvLeftButton.setVisibility(VISIBLE);
            }
        } else {
            btn_return.setVisibility(VISIBLE);
        }
        if (this.iconRight != 0){
            iv_custom_right.setVisibility(VISIBLE);
        }
    }

    public void startAlphaAnimation() {
        if (isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 1f, 0f);
            animator_txt_tip.setDuration(500);
            animator_txt_tip.start();
            isFirst = false;
        }
    }

    public void setTextWithAnimation(String tip) {
        txt_tip.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 0f, 1f, 1f, 0f);
        animator_txt_tip.setDuration(2500);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        btn_capture.setDuration(duration);
    }

    public void setButtonFeatures(int state) {
        btn_capture.setButtonFeatures(state);
    }

    public void setTip(String tip) {
        txt_tip.setText(tip);
    }

    public void showTip() {
        txt_tip.setVisibility(VISIBLE);
    }

    public void setIconSrc(int iconLeft, int iconRight) {
        this.iconLeft = iconLeft;
        this.iconRight = iconRight;
        if (this.iconLeft != 0) {
            if (leftButtonType == 0 && iv_custom_left != null) {
                iv_custom_left.setImageResource(iconLeft);
                iv_custom_left.setVisibility(VISIBLE);
            }
            btn_return.setVisibility(GONE);
        } else {
            if (leftButtonType == 0 && iv_custom_left != null) {
                iv_custom_left.setVisibility(GONE);
            }
            btn_return.setVisibility(VISIBLE);
        }
        if (this.iconRight != 0) {
            iv_custom_right.setImageResource(iconRight);
            iv_custom_right.setVisibility(VISIBLE);
        } else {
            iv_custom_right.setVisibility(GONE);
        }
    }

    public void setLeftClickListener(ClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setRightClickListener(ClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    private int leftButtonType;

    public void setLeftButtonType(int type) {
        leftButtonType = type;
        addLeftCustomButton();

    }
}
