package com.wittgroup.donutchart.donutchart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


import com.wittgroup.donutchart.R;

import java.util.ArrayList;

/**
 * Created by RIG on 16-03-2016.
 */

public class DonutChart extends View {
    private double mInnerCircleRatio;

    private ArrayList<ChartData> mSlices = new ArrayList<>();

    double time = 1;

    static final double DEGREES = 360;
    static final double BUFFERANGLE = 2;

    boolean dirty = true;

    public DonutChart(Context context) {
        this(context, null);
        init(context, null, 0);
    }

    public DonutChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DonutChart, 0, 0);
        mInnerCircleRatio = (double)a.getInt(R.styleable.DonutChart_pieInnerCircleRatio, 0);
        mInnerCircleRatio /= 255; //This line is silly, just use a double for that.
    }


    public void addSector(int color, float size) {
        mSlices.add(new ChartData(color,size,mInnerCircleRatio));
        dirty = true;
    }

    public void prepareSlices() {
        if (!dirty) return;
        dirty = false;
        double currentAngle = 270;
        double currentSweep = 0;
        float totalValue = 0;
        for (ChartData slice : mSlices) {
            totalValue += slice.getSectorValue();
        }
        double totalSweep = DEGREES;
        totalSweep -= (mSlices.size() * BUFFERANGLE);
        double totalTime = 0;
        for (ChartData slice : mSlices) {
            double currentTime = (slice.getSectorValue() / totalValue);
            currentSweep = currentTime * totalSweep;
            slice.setAngles((float)currentAngle,(float)currentSweep);
            slice.setTime(totalTime, totalTime + currentTime);
            slice.setSize(getWidth(),getHeight());
            slice.buildShape(1);
            totalTime += currentTime;
            currentAngle += currentSweep;
            currentAngle += BUFFERANGLE;

        }
    }

    public void onDraw(Canvas canvas) {
        prepareSlices();
        for (ChartData slice : mSlices) {
            if (slice.stopTime < time) {
                slice.setAlpha(255);
                slice.draw(canvas);

            } else {
                if (slice.startTime < time) {
                    double amount = (time - slice.startTime) / (slice.stopTime - slice.startTime);
                    slice.setAmount(amount);
                    slice.draw(canvas);
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int width = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        //int minh = (width -10)  + getPaddingBottom() + getPaddingTop();
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void clear() {
        if (!mSlices.isEmpty()) {
            mSlices.clear();
            postInvalidate();
        }
    }

    ValueAnimator animator;

    public void animate_time() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(5000); //5000 = 5 seconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                time = animation.getAnimatedFraction();
                //time = ((Float) (animation.getAnimatedValue())).floatValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator arg0) {
                time = 0;
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                time = 1;
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                time = 1;
            }
        });
        animator.start();
    }
}