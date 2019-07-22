package com.example.typeevaluatordemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class MyView extends View {
    public static final float RADIUS = 70f;
    private Point currentPoint;
    private Paint mPaint,p;
    private float randomHeigh;
    private float randomWith;
    private Random random = new Random();
    private boolean a = false;
    private float oldK;
    private float rangeHeight = 1418;//(100~1418)
    private float rangeWith = 940;//(100~940)
    private static int UP = 1, DOWN = 2, RIGHT = 3, LEFT = 4;
    private static int RANGE_X_RIGHT = 840;
    private static int RANGE_X_LEFT = 0;
    private static int RANGE_Y_UP = 1318;
    private static int RANGE_Y_DOWN = 0;
    private static float SPEED = 1;
    Point start = new Point(110, 100);
    Point end, start1;
    ValueAnimator animator;
    int[] colors={Color.BLUE,Color.BLACK,Color.GREEN,Color.GRAY,Color.YELLOW,Color.RED,Color.CYAN};
    int index=0;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        p=new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLUE);
        mPaint.setStrokeWidth(2.5f);
        randomHeigh = random.nextInt(1319) + 100;
        randomWith = random.nextInt(841) + 100;
        if (a == false) {
            end = new Point(randomWith, rangeHeight);
            a = true;
        } else {
            end = new Point(rangeWith, randomHeigh);
            a = false;
        }
        oldK = calculateK(start, end);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(30, 30, 1010, 30, p);
        canvas.drawLine(30, 30, 30, 1488, p);
        canvas.drawLine(30, 1488, 1010, 1488, p);
        canvas.drawLine(1010, 30, 1010, 1488, p);

        if (currentPoint == null) {
            currentPoint = new Point(110, 100);
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, RADIUS, mPaint);


            Log.d("SAG", "onDraw: " + oldK);
            animator = ValueAnimator.ofObject(new PointEvaluator(), start, end);
            animator.setInterpolator(new LinearInterpolator());
            // time=calculateDistance(start,end)/SPEED;
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentPoint = (Point) animation.getAnimatedValue();
                    invalidate();
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("NEWK", "-------------------------------------------------------------------------------------------------------------------------");
                    start1 = new Point(end);
                    Log.d("NEWK", "oldEnd  " + end.getX() + "  " + end.getY());
                    Log.d("NEWK", "oldk: " + oldK);
                    float newK = (-1) * oldK;
                    Log.d("NEWK", "newk: " + newK);
                    int flag;
                    if (start1.getX() == 100) {
                        flag = LEFT;
                    } else if (start1.getX() == rangeWith) {
                        flag = RIGHT;
                    } else if (start1.getY() == rangeHeight) {
                        flag = DOWN;
                    } else {
                        flag = UP;
                    }
                    mPaint.setColor(colors[index%7]);
                    index++;
                    calculateXY(start1, newK, flag, end);
                    Log.d("NEWK", "start: " + oldK + "  " + start1.getX() + "  " + start1.getY());
                    Log.d("NEWK", "end: " + oldK + "  " + end.getX() + "  " + end.getY());
                    oldK = calculateK(start1, end);
                    Log.d("NEWK", "NewoldK: " + oldK);
                    ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), start1, end);
                    float time=calculateDistance(start,end)/SPEED;
                    animator.setDuration(2000);
                    animator.addListener(this);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            currentPoint = (Point) animation.getAnimatedValue();
                            if (currentPoint.getX() + 70 > 1010) {
                                Log.d("NEWK", "error: +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            }
                            invalidate();
                        }
                    });
                    animator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, RADIUS, mPaint);
        }
    }

    private float calculateK(Point start, Point end) {
        float k = ((rangeHeight - end.getY()) - (rangeHeight - start.getY())) / ((end.getX() - 100) - (start.getX() - 100));
        Log.d("SAG", "calculateK: " + ((end.getX() - 100) - (start.getX() - 100)));
        return k;
    }

    //计算XY坐标
    private void calculateXY(Point start, float k, int flag, Point end) {
        float y = (rangeHeight - start.getY());
        float x = start.getX() - 100;
        if (flag == DOWN) {
            Log.d("NEWK", "DOWN: ");

            if (k > 0) {
                Log.d("NEWK", "k>0: ");

                float x1 = ((RANGE_Y_UP - y) / k) + x;
                Log.d("NEWK", "x1: " + x1);

                float y1 = k * (RANGE_X_RIGHT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                if (x1 <= RANGE_X_RIGHT) {
                    end.setX(x1 + 100);
                } else {
                    end.setX(rangeWith);
                }
                if (y1 <= RANGE_Y_UP) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(100);
                }
            } else {
                Log.d("NEWK", "k<0: ");

                float x1 = ((RANGE_Y_UP - y) / k) + x;
                Log.d("NEWK", "x1: " + x1);

                float y1 = k * (RANGE_X_LEFT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                if (x1 >= RANGE_X_LEFT) {
                    end.setX(x1 + 100);
                } else {
                    end.setX(100);
                }
                if (y1 <= RANGE_Y_UP) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(100);
                }
            }
        } else if (flag == UP) {
            Log.d("NEWK", "UP: ");

            if (k > 0) {
                Log.d("NEWK", "k>0: ");

                float x2 = ((RANGE_Y_DOWN - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                float y1 = k * (RANGE_X_LEFT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                if (x2 >= RANGE_X_LEFT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(100);
                }
                if (y1 >= RANGE_Y_DOWN) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(rangeHeight);
                }
            } else {
                Log.d("NEWK", "k<0: ");

                float x2 = ((RANGE_Y_DOWN - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                float y1 = k * (RANGE_X_RIGHT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                if (x2 <= RANGE_X_RIGHT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(rangeWith);
                }
                if (y1 >= RANGE_Y_DOWN) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(rangeHeight);
                }
            }
        } else if (flag == RIGHT) {
            Log.d("NEWK", "RIGHT: ");

            if (k > 0) {
                Log.d("NEWK", "k>0: ");

                float y1 = k * (RANGE_X_LEFT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                float x2 = ((RANGE_Y_DOWN - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                if (y1 >= RANGE_Y_DOWN) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(rangeHeight);
                }
                if (x2 >= RANGE_X_LEFT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(100);
                }
            } else {
                Log.d("NEWK", "k<0: ");

                float y1 = k * (RANGE_X_LEFT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                float x2 = ((RANGE_Y_UP - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                if (y1 <= RANGE_Y_UP) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(100);
                }
                if (x2 >= RANGE_X_LEFT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(100);
                }
            }
        } else {
            Log.d("NEWK", "LEFT: ");

            if (k > 0) {
                Log.d("NEWK", "k>0: ");

                float y1 = k * (RANGE_X_RIGHT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                float x2 = ((RANGE_Y_UP - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                if (y1 <= RANGE_Y_UP) {
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(100);
                }
                if (x2 <= RANGE_X_RIGHT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(rangeWith);
                }
            } else {
                Log.d("NEWK", "k<0: ");

                float y1 = k * (RANGE_X_RIGHT - x) + y;
                Log.d("NEWK", "y1: " + y1);

                float x2 = ((RANGE_Y_DOWN - y) / k) + x;
                Log.d("NEWK", "x2: " + x2);

                if (y1 >= RANGE_Y_DOWN) {
                    Log.d("NEWK", "1: ");
                    end.setY(rangeHeight - y1);
                } else {
                    end.setY(rangeHeight);
                }
                if (x2 <= RANGE_X_RIGHT) {
                    end.setX(x2 + 100);
                } else {
                    end.setX(rangeWith);
                }
            }
        }
    }

    //计算距离
    private float calculateDistance(Point start, Point end) {
        return (float) Math.sqrt((end.getX()-start.getX())*(end.getX()-start.getX())+(end.getY()-start.getY())*(end.getY()-start.getY()));
    }
}
