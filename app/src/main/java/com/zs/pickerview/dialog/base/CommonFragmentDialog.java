package com.zs.pickerview.dialog.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zs.pickerview.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by godyhm on 2019-05-16
 */
@SuppressLint("ValidFragment")
public class CommonFragmentDialog extends BaseFragmentDialog {

    public static final int BUTTON_LEFT = BUTTON_NEGATIVE;

    public static final int BUTTON_RIGHT = BUTTON_POSITIVE;

    private TextView mTitleView;

    private TextView mContentView;

    private ViewGroup mContentContainer;

    private ViewGroup mActionContainer;

    private TextView mButtonLeft;

    private TextView mButtonRight;

    private View mContentDivider;

    private View mButtonDivider;

    private final Builder mBuilder;

    private CommonFragmentDialog(Builder builder) {
        super(builder.mFragmentActivity);
        mBuilder = builder;
    }

    private void setCommonTextView(TextView textView, String text, int textRes, int textColor, int textSize) {
        if (textView == null) {
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else if (textRes > 0) {
            textView.setText(textRes);
        } else {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        if (textColor > 0) {
            textView.setTextColor(textColor);
        }
        if (textSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private void setContentView(String content, int contentRes, int contentColor, int contentSize) {
        setCommonTextView(mContentView, content, contentRes, contentColor, contentSize);
    }

    private void setLeftButton(final String label, int labelRes, int color, int size, final DialogInterface.OnClickListener listener) {
        if (mButtonLeft == null) {
            return;
        }
        setCommonTextView(mButtonLeft, label, labelRes, color, size);
        mButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(getDialog(), BUTTON_LEFT);
                }
            }
        });
    }


    private void setRightButton(final String label, int labelRes, int color, int size, final DialogInterface.OnClickListener listener) {
        if (mButtonRight == null) {
            return;
        }
        setCommonTextView(mButtonRight, label, labelRes, color, size);
        mButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(getDialog(), BUTTON_RIGHT);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_simple_dialog;
    }

    @Override
    protected void initViews(View contentView) {
        mTitleView = contentView.findViewById(R.id.tv_title);
        mContentView = contentView.findViewById(R.id.tv_content);
        mContentContainer = contentView.findViewById(R.id.layout_content_container);
        mButtonLeft = contentView.findViewById(R.id.tv_left);
        mButtonRight = contentView.findViewById(R.id.tv_right);

        mActionContainer = contentView.findViewById(R.id.action_container);
        mContentDivider = contentView.findViewById(R.id.view_content_divider);
        mButtonDivider = contentView.findViewById(R.id.button_divider);
        setView(mBuilder);
        if (mBuilder.mDialogBgResId > 0) {
            contentView.setBackgroundResource(mBuilder.mDialogBgResId);
        }
    }

    private void setView(Builder builder) {
        //set title
        setCommonTextView(mTitleView, builder.mTitle,
                builder.mTitleRes, builder.mTitleColor, builder.mTitleSize);
        //set content
        if (!TextUtils.isEmpty(builder.mContent) || builder.mContentRes > 0) {
            setContentView(builder.mContent, builder.mContentRes,
                    builder.mContentColor, builder.mContentSize);
        } else if (builder.mContentView != null) {//custom contentView
            mContentView.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.VISIBLE);
            ViewGroup parentViewGroup = (ViewGroup) builder.mContentView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(builder.mContentView);
            }
            mContentContainer.addView(builder.mContentView);
        }

        setLeftButton(builder.mLabelLeft, builder.mLabelLeftRes, builder.mLabelLeftColor,
                builder.mLabelLeftSize, builder.mLeftButtonListener);
        setRightButton(builder.mLabelRight, builder.mLabelRightRes, builder.mLabelRightColor,
                builder.mLabelRightSize, builder.mRightButtonListener);

        if (mButtonLeft.getVisibility() == View.GONE && mButtonRight.getVisibility() == View.GONE) {
            mActionContainer.setVisibility(View.GONE);
            mContentDivider.setVisibility(View.GONE);
        } else {
            mActionContainer.setVisibility(View.VISIBLE);
        }

        if (builder.mShowContentDivider) {
            mContentDivider.setVisibility(View.VISIBLE);
            if (builder.mContentDividerColor > 0) {
                mContentDivider.setBackgroundColor(builder.mContentDividerColor);
            }
        } else {
            mContentDivider.setVisibility(View.GONE);
        }

        if (builder.mShowButtonDivider) {
            mButtonDivider.setVisibility(View.VISIBLE);
            if (builder.mButtonDividerColor > 0) {
                mButtonDivider.setBackgroundColor(builder.mButtonDividerColor);
            }
        } else {
            mButtonDivider.setVisibility(View.GONE);
        }

        getDialog().setCanceledOnTouchOutside(builder.mCancelOutSide);
        if (builder.mDismissListener != null) {
            getDialog().setOnDismissListener(builder.mDismissListener);
        }
        if (builder.mCancelListener != null) {
            getDialog().setOnCancelListener(builder.mCancelListener);
        }
    }


    public static class Builder {
        /**
         * title text
         */
        private String mTitle;
        /**
         * title resource id
         */
        private int mTitleRes;

        /**
         * the color of the text
         */
        private int mTitleColor;

        /**
         * the size of title in pixel
         */
        private int mTitleSize;

        /**
         * the dialog content
         */
        private String mContent;
        /**
         * the dialog res
         */
        private int mContentRes;

        /**
         * color of the content
         */
        private int mContentColor;

        /**
         * text size of content in pixel
         */
        private int mContentSize;

        /**
         * the label of left button
         */
        private String mLabelLeft;
        /**
         * the label resource of left button
         */
        private int mLabelLeftRes;

        /**
         * the color of label left
         */
        private int mLabelLeftColor;

        /**
         * the text size of label left
         */
        private int mLabelLeftSize;
        /**
         * the listener of left button
         */
        private DialogInterface.OnClickListener mLeftButtonListener;

        /**
         * the label of right button
         */
        private String mLabelRight;

        /**
         * the label resource of Right button
         */
        private int mLabelRightRes;

        /**
         * the color of label Right
         */
        private int mLabelRightColor;

        /**
         * the text size of label Right
         */
        private int mLabelRightSize;

        /**
         * the listener of Right button
         */
        private DialogInterface.OnClickListener mRightButtonListener;

        /**
         * the custom view of dialog, if provided,
         * the view will be the view of the dialog content
         */
        private View mContentView;

        /**
         * if exit when back pressed
         */
        private boolean mCancelable = false;

        /**
         * if cancel when touch outsize the dialog
         */
        private boolean mCancelOutSide = false;
        /**
         * show a divider between two action button or not, default will show
         */
        private boolean mShowButtonDivider = true;

        /**
         * the divider color between two action button
         */
        private int mButtonDividerColor;

        /**
         * show the divider below the content view or not, default will show
         */
        private boolean mShowContentDivider = true;

        /**
         * the color of a divider view under content view
         */
        private int mContentDividerColor;

        /**
         * drawable resource id of dialog background
         */
        private int mDialogBgResId;

        private DialogInterface.OnDismissListener mDismissListener;

        private DialogInterface.OnCancelListener mCancelListener;

        private FragmentActivity mFragmentActivity;

        public Builder(Context context) {
            if (!(context instanceof FragmentActivity)) {
                throw new IllegalArgumentException(
                        "dialog fragment only works with fragment environment! ");
            }
            mFragmentActivity = (FragmentActivity) context;
        }

        public Builder(Fragment fragment) {
            mFragmentActivity = fragment.getActivity();
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setTitleRes(int mTitleRes) {
            this.mTitleRes = mTitleRes;
            return this;
        }

        public Builder setTitleColor(int mTitleColor) {
            this.mTitleColor = mTitleColor;
            return this;
        }

        public Builder setTitleSize(int mTitleSize) {
            this.mTitleSize = mTitleSize;
            return this;
        }

        public Builder setContent(String mContent) {
            this.mContent = mContent;
            return this;
        }

        public Builder setContentRes(int mContentRes) {
            this.mContentRes = mContentRes;
            return this;
        }

        public Builder setContentColor(int mContentColor) {
            this.mContentColor = mContentColor;
            return this;
        }

        public Builder setContentSize(int mContentSize) {
            this.mContentSize = mContentSize;
            return this;
        }

        public Builder setLabelLeft(String mLabelLeft) {
            this.mLabelLeft = mLabelLeft;
            return this;
        }

        public Builder setLabelLeftRes(int mLabelLeftRes) {
            this.mLabelLeftRes = mLabelLeftRes;
            return this;
        }

        public Builder setLabelLeftColor(int mLabelLeftColor) {
            this.mLabelLeftColor = mLabelLeftColor;
            return this;
        }

        public Builder setLabelLeftSize(int mLabelLeftSize) {
            this.mLabelLeftSize = mLabelLeftSize;
            return this;
        }

        public Builder setLabelLeft(int color, int size) {
            if (color > 0) {
                mLabelLeftColor = color;
            }

            if (size > 0) {
                mLabelLeftSize = size;
            }
            return this;
        }

        public Builder setLeftButtonListener(DialogInterface.OnClickListener mLeftButtonListener) {
            this.mLeftButtonListener = mLeftButtonListener;
            return this;
        }

        public Builder setLeftButton(String labelLeft, DialogInterface.OnClickListener mLeftButtonListener) {
            mLabelLeft = labelLeft;
            this.mLeftButtonListener = mLeftButtonListener;
            return this;
        }

        public Builder setLeftButton(int labelLeftRes, DialogInterface.OnClickListener mLeftButtonListener) {
            mLabelLeftRes = labelLeftRes;
            this.mLeftButtonListener = mLeftButtonListener;
            return this;
        }


        public Builder setLabelRight(String mLabelRight) {
            this.mLabelRight = mLabelRight;
            return this;
        }

        public Builder setLabelRightRes(int mLabelRightRes) {
            this.mLabelRightRes = mLabelRightRes;
            return this;
        }

        public Builder setLabelRightColor(int mLabelRightColor) {
            this.mLabelRightColor = mLabelRightColor;
            return this;
        }

        public Builder setLableRightSize(int mLableRightSize) {
            this.mLabelRightSize = mLableRightSize;
            return this;
        }

        public Builder setLabelRight(int color, int size) {
            if (color > 0) {
                mLabelRightColor = color;
            }

            if (size > 0) {
                mLabelRightSize = size;
            }
            return this;
        }

        public Builder setRightButtonListener(DialogInterface.OnClickListener mRightButtonListener) {
            this.mRightButtonListener = mRightButtonListener;
            return this;
        }

        public Builder setRightButton(String labelRight, DialogInterface.OnClickListener mRightButtonListener) {
            mLabelRight = labelRight;
            this.mRightButtonListener = mRightButtonListener;
            return this;
        }


        public Builder setRightButton(int labelRightRes, DialogInterface.OnClickListener mRightButtonListener) {
            mLabelRightRes = labelRightRes;
            this.mRightButtonListener = mRightButtonListener;
            return this;
        }

        public Builder setContentView(View mContentView) {
            this.mContentView = mContentView;
            return this;
        }

        public Builder setShowButtonDivider(boolean mShowButtonDivider) {
            this.mShowButtonDivider = mShowButtonDivider;
            return this;
        }

        public Builder setButtonDividerColor(int mButtonDividerColor) {
            this.mButtonDividerColor = mButtonDividerColor;
            return this;
        }

        public Builder setShowContentDivider(boolean mShowContentDivider) {
            this.mShowContentDivider = mShowContentDivider;
            return this;
        }

        public Builder setContentDividerColor(int mContentDividerColor) {
            this.mContentDividerColor = mContentDividerColor;
            return this;
        }

        public Builder setCancelable(boolean isCancelable) {
            mCancelable = isCancelable;
            return this;
        }

        public Builder setCancelOutSize(boolean isCancelOutSide) {
            mCancelOutSide = isCancelOutSide;
            return this;
        }

        public Builder setDismissListener(DialogInterface.OnDismissListener dismissListener) {
            mDismissListener = dismissListener;
            return this;
        }

        public Builder setCancelListener(DialogInterface.OnCancelListener mCancelListener) {
            this.mCancelListener = mCancelListener;
            return this;
        }

        public Builder setDialogBgResId(int mDialogBgResId) {
            this.mDialogBgResId = mDialogBgResId;
            return this;
        }

        public CommonFragmentDialog build() {
            final CommonFragmentDialog dialog =
                    new CommonFragmentDialog(this);
            dialog.setCancelable(mCancelable);
            return dialog;
        }
    }
}
