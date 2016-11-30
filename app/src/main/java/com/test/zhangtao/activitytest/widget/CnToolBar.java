package com.test.zhangtao.activitytest.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/9/25.
 */
public class CnToolBar extends Toolbar
{
    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextView;
    private EditText mSearchView;
    private Button mRightButton;

    public CnToolBar(Context context)
    {
        this(context , null);
    }

    public CnToolBar(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs , 0);
    }

    public CnToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10 , 10);

        if (attrs != null)
        {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext() ,
                    attrs , R.styleable.CnToolBar , defStyleAttr , 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolBar_rightButtonIcon);
            if (rightIcon != null)
            {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowingSearchView = a.getBoolean(R.styleable.CnToolBar_isShowingSearchView , false);
            if (isShowingSearchView)
            {
                showSearchView();
                hideTitleView();
            }

            CharSequence rightButtonText = a.getText(R.styleable.CnToolBar_rightButtonText);
            if (rightButtonText != null)
            {
                setRightButtonText(rightButtonText);
            }

            a.recycle();
        }
    }

    private void initView()
    {
        if (mView == null)
        {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar , null);

            mTextView = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchView);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,
                    ViewGroup.LayoutParams.WRAP_CONTENT , Gravity.CENTER_HORIZONTAL);

            addView(mView , lp);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon)
    {
        if (mRightButton != null)
        {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonIcon(int icon)
    {
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonOnClickListener(OnClickListener li)
    {
        mRightButton.setOnClickListener(li);
    }

    public void setRightButtonText(CharSequence text)
    {
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id)
    {
        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton()
    {
        return this.mRightButton;
    }

    @Override
    public void setTitle(int resId)
    {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title)
    {
        initView();
        if (mTextView != null)
        {
            mTextView.setText(title);
            showTitleView();
        }
    }

    private void hideTitleView()
    {
        if (mTextView != null)
            mTextView.setVisibility(GONE);
    }

    private void showTitleView()
    {
        if (mTextView != null)
            mTextView.setVisibility(VISIBLE);
    }

    public void hideSearchView()
    {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    private void showSearchView()
    {
        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);
    }
}
