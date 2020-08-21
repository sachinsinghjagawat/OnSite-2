package com.example.onsite2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class MyClockCanvas extends View {

    Paint paint;
    int padding , padding2 , extraPadding , extraPadding2 , x , y ;
    float minX , minY, secX , secY;
    int minText , secText;
    Rect rect = new Rect();
    Rect rectReset ;
    Rect rectPause;

    int minPassed = 0;
    int secPassed = 0;

    boolean running = false;

    Handler secHandler = new Handler();
    Runnable secRun;

    public MyClockCanvas(Context context) {
        super(context);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        x = displayMetrics.widthPixels / 2 ;
        y = displayMetrics.heightPixels / 2;

        extraPadding = 48;
        extraPadding2 = 30;
        padding = 60;
        padding2 = 100;
        minText = 30;
        secText = 50;
        minX = x ;
        minY = (y - ((x - padding)/2)) - ((((x - padding)/2) - padding2 - extraPadding2)) ;
        secX = x;
        secY = y - ((x - padding- extraPadding));

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);

    }

    public MyClockCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPauseButton(canvas);
        drawResetButton(canvas);
        drawSecondClock(canvas);
        drawSecondHand(canvas);
        drawSecondNumber(canvas);
        drawSecondCenter(canvas);
        drawMinuteClock(canvas);
        drawMinuteHand(canvas);
        drawMinuteNumber(canvas);
        drawMinuteCenter(canvas);


        postInvalidate();
    }

    private void drawResetButton(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        paint.setColor(getResources().getColor(R.color.grey));
        paint.setAntiAlias(true);

        double angle = (Math.PI * 50)/30;

        int xTemp = (int) (x + ((x - padding- extraPadding)*Math.sin(angle)));
        int yTemp = (int) (y - ((x - padding- extraPadding)*Math.cos(angle)));

        canvas.drawRect(xTemp , yTemp - 250 , xTemp + 50,  yTemp , paint);
        canvas.drawRect(xTemp - 30 , yTemp - 220 , xTemp + 50 + 30 ,  yTemp - 250 , paint);

        rectReset = new Rect(xTemp - 30 , yTemp - 250 , xTemp + 80 , yTemp);

    }

    private void drawPauseButton(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        paint.setColor(getResources().getColor(R.color.grey));
        paint.setAntiAlias(true);

        canvas.drawRect( x - 30 , y - (x - padding) - 150 , x + 30 , y - (x - padding) + 150 , paint );
        canvas.drawRect( x - 90 , y - (x - padding) - 180 , x + 90 , y - (x - padding) - 150 , paint );

        rectPause = new Rect(x - 90 , y - (x - padding) - 180 , x + 90 , y - (x - padding) + 150 );

    }

    private void drawMinuteCenter(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        canvas.drawCircle(x , (y - ((x - padding)/2)), 10 , paint);
    }

    private void drawMinuteNumber(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        paint.setTextSize(minText);

        for (int i=5 ; i <61 ; i+=5){
            String temp = i + "" ;
            paint.getTextBounds(temp , 0 , temp.length() , rect);
            double angle = (Math.PI * i)/30;
            int xTemp = (int) (x + ((((x - padding)/2) - padding2 - extraPadding2)*Math.sin(angle)) -(rect.width()/2));
            int yTemp = (int) ((y - ((x - padding)/2)) - ((((x - padding)/2) - padding2 - extraPadding2)*Math.cos(angle)) +(rect.height()/2) );
            canvas.drawText(temp , xTemp , yTemp , paint);
        }

    }

    private void drawMinuteHand(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        canvas.drawLine(x , (y - ((x - padding)/2)), minX , minY , paint );
    }

    private void drawMinuteClock(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        canvas.drawCircle(x , (y - ((x - padding)/2)) , (((x - padding)/2) - padding2) , paint );
        invalidate();
    }

    private void drawSecondCenter(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        canvas.drawCircle(x , y, 20 , paint);
    }

    private void drawSecondNumber(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        paint.setTextSize(secText);

        for (int i=5 ; i <61 ; i+=5){
            String temp = i + "" ;
            paint.getTextBounds(temp , 0 , temp.length() , rect);
            double angle = (Math.PI * i)/30;
            int xTemp = (int) (x + ((x - padding- extraPadding)*Math.sin(angle)) -(rect.width()/2));
            int yTemp = (int) (y - ((x - padding- extraPadding)*Math.cos(angle)) +(rect.height()/2) );
            canvas.drawText(temp , xTemp , yTemp , paint);
        }
        
    }

    private void drawSecondHand(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        canvas.drawLine(x , y, secX , secY , paint );
        invalidate();
    }

    private void drawSecondClock(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(50f);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        canvas.drawCircle(x , y , (x - padding) + 20 , paint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50f);
        paint.setColor(getResources().getColor(R.color.brown));
        paint.setAntiAlias(true);

        canvas.drawCircle(x , y , (x - padding) + 20 , paint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        canvas.drawCircle(x , y , (x - padding) + 40 , paint);
    }

    private void timeRotation() {

        secRun = new Runnable() {
            @Override
            public void run() {
                float angle = (float) ((Math.PI / 30)*minPassed);
                minX = (float) (x + ((((x - padding)/2) - padding2 - extraPadding2)*Math.sin(angle)));
                minY = (float) ((y - ((x - padding)/2)) - ((((x - padding)/2) - padding2 - extraPadding2)*Math.cos(angle)) );

                int var = (minPassed + 1)*60 - secPassed;
                if (var == 0){
                    minPassed++;
                }

                float angle2 = (float) ((Math.PI / 30)*secPassed);
                secX = (float) (x + ((x - padding- extraPadding)*Math.sin(angle2)));
                secY = (float) (y - ((x - padding- extraPadding)*Math.cos(angle2)) );
                secPassed++;
                secHandler.postDelayed(this , 1000);
            }
        };
        secHandler.post(secRun);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int xCor= (int) event.getX();
        int yCor = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (rectPause.contains(xCor , yCor)){
                    if (running == true){
                        secHandler.removeCallbacks(secRun);
                        running = false;
                        return true;
                    }else {
                        timeRotation();
                        running = true;
                        return true;
                    }
                }
                if (rectReset.contains(xCor , yCor)){
                    if (running) {
                        minX = x;
                        minY = (y - ((x - padding) / 2)) - ((((x - padding) / 2) - padding2 - extraPadding2));
                        secX = x;
                        secY = y - ((x - padding - extraPadding));
                        secHandler.removeCallbacks(secRun);
                        minPassed = 0;
                        secPassed = 0;
                        running = false;
                        return true;
                    }else {
                        if (secPassed != 0){
                            minX = x;
                            minY = (y - ((x - padding) / 2)) - ((((x - padding) / 2) - padding2 - extraPadding2));
                            secX = x;
                            secY = y - ((x - padding - extraPadding));
                            secHandler.removeCallbacks(secRun);
                            minPassed = 0;
                            secPassed = 0;
                            running = false;
                            return true;
                        }else {
                            minX = x;
                            minY = (y - ((x - padding) / 2)) - ((((x - padding) / 2) - padding2 - extraPadding2));
                            secX = x;
                            secY = y - ((x - padding - extraPadding));
                            secHandler.removeCallbacks(secRun);
                            minPassed = 0;
                            secPassed = 0;
                            running = true;
                            timeRotation();
                            return true;
                        }
                    }

                }
            }
        }

                return false;
    }
}
