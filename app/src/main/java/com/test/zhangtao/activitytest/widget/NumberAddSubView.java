package com.test.zhangtao.activitytest.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/10/2.
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener
{
    public static final int DEFUALT_MAX = 1000;
    private LayoutInflater mInflater;
    private Button mBtnAdd;
    private TextView mTextNumber;
    private Button mBtnSub;

    private int value;
    private int maxValue = DEFUALT_MAX;
    private int minValue;
    private OnButtonClickListener listener;

    public void setOnButtonClickListener(OnButtonClickListener listener)
    {
        this.listener = listener;
    }

    public NumberAddSubView(Context context)
    {
        this(context , null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs)
    {
        this(context, attrs , 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);

        initView();

        if (attrs != null)
        {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context ,
                    attrs , R.styleable.NumberAddSubView , defStyleAttr , 0);

            int val = a.getInt(R.styleable.NumberAddSubView_value , 0);
            setValue(val);
            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue , 0);
            setMinValue(minVal);
            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue , 0);
            if (maxVal != 0)
                setMaxValue(maxVal);

            Drawable drawableBtnAdd = a.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable drawableBtnSub = a.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable drawableTextView = a.getDrawable(R.styleable.NumberAddSubView_textViewBackground);
            setTextViewBackground(drawableTextView);
            setButtonAddBackground(drawableBtnAdd);
            setButtonSubBackground(drawableBtnSub);

            a.recycle();
        }
    }

    private void initView()
    {
        View view = mInflater.inflate(R.layout.weight_number_add_sub , this , true);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);
        mTextNumber = (TextView) view.findViewById(R.id.textNumber);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    public int getValue()
    {
        String val = mTextNumber.getText().toString();
        if (val != null && !"".equals(val))
            this.value = Integer.parseInt(val);
        return value;
    }

    public void setValue(int value)
    {
        mTextNumber.setText(value + "");
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btn_add)
        {
            numAdd();
            if (listener != null)
            {
                listener.onButtonAddClick(view , value);
            }
        }
        else if (view.getId() == R.id.btn_sub)
        {
            numSub();
            if (listener != null)
            {
                listener.onButtonSubCLick(view , value);
            }
        }
    }

    public void setTextViewBackground(Drawable drawable)
    {
        mTextNumber.setBackgroundDrawable(drawable);
    }

    public void setButtonAddBackground(Drawable drawable)
    {
        mBtnAdd.setBackgroundDrawable(drawable);
    }

    public void setButtonSubBackground(Drawable drawable)
    {
        mBtnSub.setBackgroundDrawable(drawable);
    }

    private void numAdd()
    {
        if (value < maxValue)
            value = value + 1;
        mTextNumber.setText(value + "");
    }

    private void numSub()
    {
        if (value > minValue)
            value = value - 1;
        mTextNumber.setText(value + "");
    }

    public interface OnButtonClickListener
    {
        void onButtonAddClick(View view , int value);
        void onButtonSubCLick(View view , int value);
    }
}
