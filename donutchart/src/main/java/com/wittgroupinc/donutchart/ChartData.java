package com.wittgroupinc.donutchart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.io.Serializable;

/**
 * Created by Pawan on 16-03-2016.
 */
public class ChartData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static RectF mRectF = new RectF();

    private final Path mPath = new Path();
    private final Paint mPaint = new Paint();
    private float sectorValue;

    double startTime;
    double stopTime;

    double currentAngle;
    double currentSweep;

    double innerRadiusRatio;

    float width;
    float height;

    double buildAmount;

    public ChartData(int color, float sectorValue, double innerRadiusRatio) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        this.sectorValue = sectorValue;
        this.innerRadiusRatio = innerRadiusRatio;
    }


    public void buildShape(double amount) {
        float midX, midY, radius, innerRadius;
        midX = width / 2;
        midY = height / 2;
        if (midX < midY) {
            radius = midX;
        } else {
            radius = midY;
        }
        innerRadius = (float)(radius * innerRadiusRatio);
        mPath.reset();
        mRectF.set(midX - radius, midY - radius, midX + radius, midY + radius);
        double relativeSweep = currentSweep * amount;
        createArc(mPath, mRectF, relativeSweep, currentAngle, relativeSweep);
        mRectF.set(midX - innerRadius, midY - innerRadius, midX + innerRadius, midY + innerRadius);
        createArc(mPath, mRectF, relativeSweep, currentAngle + relativeSweep, -relativeSweep);
        mPath.close();
    }

    private void createArc(Path p, RectF mRectF, double currentSweep, double startAngle, double sweepAngle) {
        if (currentSweep == 360) {
            p.addArc(mRectF, (float)startAngle, (float)sweepAngle);
        } else {
            p.arcTo(mRectF, (float)startAngle, (float)sweepAngle);
        }
    }

    public void setTime(double startTime, double stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public void setAngles( double currentAngle, double currentSweep) {
        this.currentAngle = currentAngle;
        this.currentSweep = currentSweep;
    }
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getSectorValue() {
        return sectorValue;
    }

    public void setSectorValue(float sectorValue) {
        this.sectorValue = sectorValue;
    }

    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha & 0xFF);
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    public void setAmount(double amount) {
        if (buildAmount == amount) return;
        buildAmount = amount;

        buildShape(amount);
        int alpha = (int) (amount * 255);
        setAlpha(alpha);
    }

}
