package com.example.analogclockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class AnalogClockView extends View {
    private int hours=0;
    private int minutes=0;
    private int seconds=0;

    private static final int BASE_WIDTH = 600;
    private static final int BASE_HEIGHT = 600;
    private Paint paint;
    private int width=0;
    private int height=0;
    private boolean isInit;

    public AnalogClockView(Context context) {
        super(context);
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        this.hours = (int) hour;
        this.minutes = c.get(Calendar.MINUTE);
        this.seconds = c.get(Calendar.SECOND);
  }

    public AnalogClockView(Context context,int hours,int minutes,int seconds) {
        super(context);
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public AnalogClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(attrs != null){
            TypedArray typedArray = context.
                    obtainStyledAttributes(attrs, R.styleable.AnalogClockView);
            Calendar c = Calendar.getInstance();
            float hour = c.get(Calendar.HOUR_OF_DAY);
            hour = hour > 12 ? hour - 12 : hour;
            hours = typedArray.getInt(R.styleable.AnalogClockView_hours, (int) hour);
            minutes = typedArray.getInt(R.styleable.AnalogClockView_minutes,c.get(Calendar.MINUTE));
            seconds = typedArray.getInt(R.styleable.AnalogClockView_sec,c.get(Calendar.SECOND));


            typedArray.recycle();
        }
    }


    private void initClockDrawValues(){
        height = getHeight();
        width =  getWidth();
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        isInit = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
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
        }
        setMeasuredDimension(w, h);

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



    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClockDrawValues();
        }

        canvas.drawColor(Color.BLACK);

        canvas.drawCircle(width / 2, height / 2, width / 2 - 10, paint);

        canvas.save();

        canvas.rotate(seconds * 6, width/2, height/2);

        canvas.drawLine(width/2, height/2, width/2, height/2 - width/2 + 10, paint);

        canvas.restore();

        canvas.save();

        canvas.rotate(minutes * 6 + seconds / 10, width/2, height/2);

        canvas.drawLine(width/2, height/2, width/2 , height/2 - width/2 + width/8, paint);

        canvas.restore();

        canvas.save();

        canvas.rotate(hours * 30  + minutes / 2, width/2, height/2);

        canvas.drawLine(width/2, height/2, width/2 , height/2 - width/2 + width/4, paint);

        canvas.restore();



        //   drawHands(canvas);

        seconds++;
        if (seconds>59)
        {
            minutes++;
            seconds=0;
        }
        if (minutes>59){
            minutes = 0;
            hours++;
        }
   /*     if (hours>12){
            hours = 1;
        }*/
        postInvalidateDelayed(1000);

    }


}
