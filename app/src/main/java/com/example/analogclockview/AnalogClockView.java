package com.example.analogclockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class AnalogClockView extends View {

    private static final int BASE_WIDTH = 400 ;
    private static final int BASE_HEIGHT = 400;
    private Canvas canvas;
    private Paint paint;
    private int width=0;
    private int height=0;
    private int padding = 0;
    private int handTruncation, hourHandTruncation = 0;
    private int radius = 0;
    private boolean isInit;
    private Rect rect = new Rect();

    public AnalogClockView(Context context) {
        super(context);
        init(context,null);
    }

    public AnalogClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        height = getHeight();
        width = getWidth();
        padding = 50;
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int w;
        int h;

        if (widthMode == MeasureSpec.EXACTLY) {
            w = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            w = Math.min(BASE_WIDTH, widthSize);
        } else {
            w = BASE_WIDTH;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            h = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            h = Math.min(BASE_HEIGHT, heightSize);
        } else {
            h = BASE_HEIGHT;
        }
        int smaller = Math.min(w, h);
        w = smaller;
        h = smaller;
        if (!areAllEqual(getPaddingTop(), getPaddingBottom(), getPaddingLeft(), getPaddingRight())) {
            w = smaller - (getPaddingTop() + getPaddingBottom());
            h = smaller - (getPaddingLeft() + getPaddingRight());
        setMeasuredDimension(w, h);
        }
    }
    Rect backgroundBounds = new Rect();
    int textSize;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        backgroundBounds.left = getPaddingLeft();
        backgroundBounds.top = getPaddingTop();
        backgroundBounds.right = w - getPaddingRight();
        backgroundBounds.bottom = h - getPaddingBottom();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            init();
        }

        canvas.drawColor(Color.BLACK);
        drawCircle(canvas);
        drawHands(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }
    private void drawHand(Canvas canvas, double loc, boolean isHour) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHands(Canvas canvas) {
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas, (hour + c.get(Calendar.MINUTE) / 60) * 5f, true);
        drawHand(canvas, c.get(Calendar.MINUTE), false);
        drawHand(canvas, c.get(Calendar.SECOND), false);
    }
    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + padding - 10, paint);
    }
    private boolean areAllEqual(int... values) {
        if (values.length == 0) {
            return true;
        }
        int checkValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] != checkValue) {
                return false;
            }
        }
        return true;
    }
}
