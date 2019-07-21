package com.example.typeevaluatordemo;

import android.animation.TypeEvaluator;

import java.util.Random;

public class PointEvaluator implements TypeEvaluator {


    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point start = (Point) startValue;
        Point end = (Point) endValue;
        float x = start.getX() + fraction * (end.getX() - start.getX());
        float y = start.getY() + fraction * (end.getY() - start.getY());
        Point point = new Point(x, y);
        return point;
    }



}
