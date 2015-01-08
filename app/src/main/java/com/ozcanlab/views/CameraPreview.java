/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.views;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

import static com.ozcanlab.utils.Constants.*;
import java.util.List;

/**
 *
 * @author Romain
 */
/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera = null;
    /* Thread */
    private int width = 0;
    private int height = 0;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        this.mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                //mCamera.startPreview();
            }
        } catch (IOException ex) {
            Log.e(APP_TAG, "Error in Surface Created", ex);
        } catch (RuntimeException ex) {
            // The program has crash ...
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.cancelAutoFocus();
            }
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 0;
        int desiredHeight = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        // TODO Auto-generated method stub
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.cancelAutoFocus();
            }
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            if (mCamera != null) {
                Camera.Parameters p = mCamera.getParameters();
                if (p != null) {
                    List<Size> sizes = p.getSupportedPreviewSizes();
                    Size optimalSize = getOptimalPreviewSize(sizes, w, h);
                    p.setPreviewSize(optimalSize.width, optimalSize.height);
                    mCamera.setParameters(p);
                }
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Log.d(APP_TAG, "Error starting camera preview: ", e);
        }
    }
}
