/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ozcanlab.rdt.R;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rom@in
 */
public class CameraSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private DrawThread drawThread;
    private static Bitmap overlay;
    private int width = 640;
    private int height = 360;
    private Paint circlePaint;

    public CameraSurfaceView(Context context, String overlayType) {
        super(context);

        this.circlePaint = new Paint();
        this.circlePaint.setColor(Color.RED);
        this.circlePaint.setStyle(Style.FILL);

        if (overlayType == "RDT")
            overlay = BitmapFactory.decodeResource(getResources(), R.drawable.overlay);
        else if (overlayType == "Imager")
            overlay = BitmapFactory.decodeResource(getResources(), R.drawable.rect);
        else if(overlayType == "Circle")
            overlay = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        else if (overlayType == "PlantImagerRed")
        	overlay = BitmapFactory.decodeResource(getResources(), R.drawable.rect_red);
        else if (overlayType == "PlantImagerWhite")
        	overlay = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
        else
            overlay = BitmapFactory.decodeResource(getResources(), R.drawable.overlay);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        drawThread = new DrawThread(mHolder);

        // Flag must be cleared otherwise onDraw will not be called after
        // invalidating the view.
        this.setWillNotDraw(false);
        setZOrderMediaOverlay(true);
        setZOrderOnTop(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        /* Start UI Thread */
        overlay = Bitmap.createScaledBitmap(overlay, width, height, false);
        drawThread.setRunning(true);
        drawThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.setRunning(false);
        while (true) {
            try {
                drawThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 640;
        int desiredHeight = 360;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            // Be whatever you want
            width = desiredWidth;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            // Be whatever you want
            height = desiredHeight;
        }
        
        // MUST CALL THIS

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(overlay, 0, 0, circlePaint);
        
    }

    protected class DrawThread extends Thread {

        private final SurfaceHolder surfaceHolder;
        private boolean isRunning;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            isRunning = false;
        }

        public void setRunning(boolean run) {
            isRunning = run;
        }

        @Override
        public void run() {
            Canvas c;

            while (isRunning) {
                    c = null;
                    synchronized (surfaceHolder) {
                        try {
                            c = surfaceHolder.lockCanvas(null);
                            postInvalidate();

                        } catch (Exception e) {
                        } finally {
                            // do this in a finally so that if an exception is
                            // thrown
                            // during the above, we don't leave the Surface in
                            // an
                            // inconsistent state
                            if (c != null) {
                                surfaceHolder.unlockCanvasAndPost(c);
                            }
                        }
                    }
                    
                try {
                    Thread.sleep(60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CameraSurfaceView.class.getName()).log( Level.SEVERE, null, ex);
                }
            }
        }
    }
}
