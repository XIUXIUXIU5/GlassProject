/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ozcanlab.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

import java.util.List;

/**
 *
 * @author SaqibIsGreat
 */
public class CameraUtils {

    public static Camera.Size getBestSize(List<Camera.Size> cs) {
        Camera.Size result = null;
        int bestSurface = 0, currentSurface;

        for (Camera.Size size : cs) {
            currentSurface = size.width * size.height;
            if (bestSurface < currentSurface) {
                bestSurface = currentSurface;
                result = size;
            }
        }

        return result;
    }

    public static Camera.Size getSizeAround(int aroundSize, List<Camera.Size> cs) {
        Camera.Size result = null;
        int currentSurface, lastSurface = Integer.MAX_VALUE;

        for (Camera.Size size : cs) {
            currentSurface = size.width * size.height;
            if (aroundSize >= currentSurface & lastSurface >= aroundSize) {
                return size;
            }
            lastSurface = currentSurface;
        }

        return result;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        
        camera.setDisplayOrientation(result);
    }
    
    public static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result=null;
        float dr = Float.MAX_VALUE;
        float ratio = (float)width/(float)height;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            float r = (float)size.width/(float)size.height;
            if( Math.abs(r - ratio) < dr && size.width <= width && size.height <= height ) {
                dr = Math.abs(r - ratio);
                result = size;
            }
        }

        return result;
    }
}
