package com.zs.pickerview.dialog.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zs.pickerview.R;


/**
 * Created by godyhm on 2019-05-16
 */
public abstract class BaseFragmentDialog extends AppCompatDialogFragment {
    private static final float DEFAULT_WIDTH_PERCENT = 0.75f;

    /**
     * dialog with
     */
    private int mWidth;

    /**
     * dialog height
     */
    private int mHeight;

    /**
     * the animation of showing and dismissing the dialog
     */
    private int mAnimation;

    /**
     * the background resource of the dialog
     */
    private int mBackgroundRes;

    /**
     * the position of the dialog
     */
    private int mGravity = Gravity.CENTER;

    /**
     * the dialog of fragment manager
     */
    private FragmentManager mManager;

    /**
     * the view of the whole dialog
     */
    protected ViewGroup mDialogView;

    BaseFragmentDialog(FragmentActivity activity) {
        mManager = activity.getSupportFragmentManager();
        mWidth = activity.getResources().getDisplayMetrics().widthPixels;
        mWidth = (int) (mWidth * DEFAULT_WIDTH_PERCENT);
        mHeight = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BaseDialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(getLayoutId(), container, false);
        if (view instanceof ViewGroup) {
            mDialogView = (ViewGroup) view;
        }
        initViews(view);
        return view;
    }

    public ViewGroup getDialogView() {
        return mDialogView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View contentView);

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window == null) {
            return;
        }
        final WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = mGravity;
        if (mWidth >= WindowManager.LayoutParams.WRAP_CONTENT) {
            wlp.width = mWidth;
        }
        if (mHeight >= WindowManager.LayoutParams.WRAP_CONTENT) {
            wlp.height = mHeight;
        }
        if (mAnimation > 0) {
            wlp.windowAnimations = mAnimation;
        }
        if (mBackgroundRes > 0) {
            window.setBackgroundDrawableResource(mBackgroundRes);
        }

        window.setAttributes(wlp);
    }

    /**
     * show dialog at top
     */
    public BaseFragmentDialog setShowAtTop() {
        mGravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mAnimation = R.style.top_slide_animation;
        return this;
    }

    /**
     * show dialog at bottom
     */
    public BaseFragmentDialog setShowAtBottom() {
        mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mAnimation = R.style.bottom_slide_animation;
        return this;
    }

    /**
     * set the animation of the dialog
     */
    public BaseFragmentDialog setAnim(int animRes) {
        mAnimation = animRes;
        return this;
    }

    /**
     * set the back ground drawable resource
     */
    public BaseFragmentDialog setBackgroundRes(int backgroundRes) {
        mBackgroundRes = backgroundRes;
        return this;
    }

    /**
     * show the dialog with spec size, you can pass
     * WindowManager.LayoutParams.WRAP_CONTENT
     * or WindowManager.LayoutParams.MATCH_PARENT as well
     */
    public void showBySize(int width, int height) {
        mWidth = width;
        mHeight = height;
        show();
    }

    /**
     * show the dialog  with  MATCH_PARENT width
     */
    public void showMatchWidth() {
        mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        show();
    }

    /**
     * show the dialog with spec percent of screen with,
     * as spec percent of screen height
     */
    public void showByPercent(
            float widthPercent, float heightPercent) {
        mWidth = (int) (mWidth * widthPercent);
        mHeight = (int) (mHeight * heightPercent);
        show();
    }

    public void show() {
        show("");
    }

    public void show(String tag) {
        show(mManager, tag);
    }
}
