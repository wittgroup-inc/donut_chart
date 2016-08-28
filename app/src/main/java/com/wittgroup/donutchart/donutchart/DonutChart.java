package com.wittgroup.donutchart.donutchart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;


import com.wittgroup.donutchart.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by RIG on 16-03-2016.
 */

public class DonutChart extends View {
    private int mInnerCircleRatio;

    private ArrayList<ChartDataHolder> mSlices = new ArrayList<>();
    private RectF mRectF = new RectF();
    double time = Float.POSITIVE_INFINITY;

    static final double DEGREES = 360;
    static final double BUFFERANGLE = 2;

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
        mInnerCircleRatio = a.getInt(R.styleable.DonutChart_pieInnerCircleRatio, 0);
    }

    public void prepareSlices() {
        double currentAngle = 270;
        double currentSweep = 0;
        float totalValue = 0;
        for (ChartDataHolder slice : mSlices) {
            totalValue += slice.getSectorValue();
        }
        double totalSweep = DEGREES;
        totalSweep -= (mSlices.size() * BUFFERANGLE);
        double totalTime = 0;
        for (ChartDataHolder slice : mSlices) {
            double currentTime = (slice.getSectorValue() / totalValue);
            currentSweep = currentTime * totalSweep;
            slice.init((float) currentAngle, (float) currentSweep, slice);
            slice.setTime(totalTime, totalTime + currentTime);
            totalTime += currentTime;
            currentAngle += currentSweep;
            currentAngle += BUFFERANGLE;

        }
    }

    public void onDraw(Canvas canvas) {
        for (ChartDataHolder slice : mSlices) {
            if (slice.stopTime < time) {
                canvas.drawPath(slice.path, slice.paint);
            } else {
                if (slice.startTime > time) {
                    //It's in the middle of this element and should be animated drawn partway.
                }
            }
        }
    }

    public void setInnerCircleRatio(int innerCircleRatio) {
        mInnerCircleRatio = innerCircleRatio;
        postInvalidate();
    }

    public void addSector(ChartData sector) {
        mSlices.add(new ChartDataHolder(sector));
        postInvalidate();
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

    public class ChartDataHolder {
        float sectorValue;
        Path path;
        Paint paint;
        Region region;
        int color;

        double startTime;
        double stopTime;
        //I didn't have this class.


        public ChartDataHolder() {
            region = new Region();
            path = new Path();
            paint = new Paint();
            paint.setAntiAlias(true);
        }

        public ChartDataHolder(ChartData chartData) {
            region = chartData.getRegion();
            path = chartData.getPath();
            color = chartData.getColor();
            paint = new Paint();
            sectorValue = chartData.getSectorValue();
            paint.setAntiAlias(true);
        }

        public float getSectorValue() {
            return sectorValue;
        }

        public void setSectorValue(float sectorValue) {
            this.sectorValue = sectorValue;
        }

        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
        }

        public Path getPath() {
            return path;
        }

        public int getColor() {
            return color;

        }

        public void setColor(int color) {
            this.color = color;
        }

        public void init(float currentAngle, float currentSweep, ChartDataHolder slice) {
            float midX, midY, radius, innerRadius;
            midX = getWidth() / 2;
            midY = getHeight() / 2;
            if (midX < midY) {
                radius = midX;
            } else {
                radius = midY;
            }
            innerRadius = radius * mInnerCircleRatio / 255;
            path.reset();
            //int color = getColor();
            Random rnd = new Random();
            int color = 0;
            if (slice.getColor() != 0) {
                color = Color.argb(Color.alpha(slice.getColor()), Color.red(slice.getColor()), Color.green(slice.getColor()), Color.blue(slice.getColor()));
            } else {
                color = Color.argb(255, (int) slice.getSectorValue(), rnd.nextInt(256), rnd.nextInt(256));
            }
            paint.setColor(color);
            mRectF.set(midX - radius, midY - radius, midX + radius, midY + radius);
            createArc(path, mRectF, currentSweep, currentAngle, currentSweep);
            mRectF.set(midX - innerRadius, midY - innerRadius, midX + innerRadius, midY + innerRadius);
            createArc(path, mRectF, currentSweep, currentAngle + currentSweep, -currentSweep);
            path.close();
            // Create selection region
            Region r = getRegion();
            r.set((int) (midX - radius),
                    (int) (midY - radius),
                    (int) (midX + radius),
                    (int) (midY + radius));
        }


        private void createArc(Path p, RectF mRectF, float currentSweep, float startAngle, float sweepAngle) {
            if (currentSweep == 360) {
                p.addArc(mRectF, startAngle, sweepAngle);
            } else {
                p.arcTo(mRectF, startAngle, sweepAngle);
            }
        }

        public void setTime(double startTime, double stopTime) {
            this.startTime = startTime;
            this.stopTime = stopTime;
        }

    }


    ValueAnimator animator;

    public void animate_time() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(5000); //5000 = 5 seconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                time = ((Float) (animation.getAnimatedValue())).floatValue();
                //  invalidate();
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
                time = Float.POSITIVE_INFINITY;
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                time = Float.POSITIVE_INFINITY;
            }
        });
        animator.start();
    }

    public void commit() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                prepareSlices();
                //animate_time();
                //invalidate();
                return false;
            }
        });

    }
}