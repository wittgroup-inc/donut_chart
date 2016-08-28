package com.wittgroup.donutchart.donutchart;

import android.graphics.Path;
import android.graphics.Region;

import java.io.Serializable;

/**
 * Created by RIG on 16-03-2016.
 */
public class ChartData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Path mPath = new Path();
    private final Region mRegion = new Region();
    private Float y_values, x_values, size, left, top, right, bottom, data, mValue;
    private String cordinate;


    private int color;

    public ChartData() {
    }

    public ChartData(int color) {
        this.color = color;
    }

    public ChartData(Float y_values, Float x_values) {
        this.y_values = y_values;
        this.x_values = x_values;
    }

    public ChartData(Float y_values, Float x_values, Float size) {
        this.y_values = y_values;
        this.x_values = x_values;
        this.size = size;
    }

    protected ChartData(Float left, Float top, Float right, Float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    protected ChartData(Float y_axis, Float x_axis, Float size, String cordinate) {
        this.y_values = y_axis;
        this.x_values = x_axis;
        this.size = size;
        this.cordinate = cordinate;
    }

    public ChartData(Float val) {
        this.data = val;
    }

    public Float getY_values() {
        return y_values;
    }

    public void setY_values(Float y_values) {
        this.y_values = y_values;
    }

    public Float getX_values() {
        return x_values;
    }

    public void setX_values(Float x_values) {
        this.x_values = x_values;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getTop() {
        return top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getRight() {
        return right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    public Float getBottom() {
        return bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
    }

    public Float getValue() {
        return this.data;
    }

    public void setValue(Float data) {
        this.data = data;
    }

    public String getCordinate() {
        return this.cordinate;
    }

    public void setCordinate(String cordinate) {
        this.cordinate = cordinate;
    }

    public Path getPath() {
        return mPath;
    }

    public Region getRegion() {
        return mRegion;
    }

    public float getSectorValue() {
        return mValue;
    }

    public void setSectorValue(float value) {
        mValue = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
