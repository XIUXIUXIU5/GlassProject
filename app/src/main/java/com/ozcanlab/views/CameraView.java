/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.views;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.ozcanlab.activities.ImagerActivity;
import com.ozcanlab.utils.CameraUtils;
import static com.ozcanlab.utils.Constants.APP_TAG;
import java.util.ArrayList;
import java.util.List;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder = null;
    private Camera camera = null;

    //@SuppressWarnings("deprecation")
    public CameraView(Context context) {
        super(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();

        // Set the Hotfix for Google Glass
        this.setCameraParameters(camera);

        // Show the Camera display
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            this.releaseCamera();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
// Start the preview for surfaceChanged
        if (camera != null) {
            camera.startPreview();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// Do not hold the camera during surfaceDestroyed - view should be gone
        this.releaseCamera();
    }

    /**
     * Important HotFix for Google Glass (post-XE11) update
     *
     * @param camera Object
     */
    public void setCameraParameters(Camera camera) {
        if (camera != null) {
//            camera.setDisplayOrientation(90);
//
//            Camera.Size r;
//            Camera.Parameters params = camera.getParameters();
//            if (params != null) {
//
//                // Set camera orientation
//                CameraUtils.setCameraDisplayOrientation(ImagerActivity.this, cameraId, mCamera);
//                // Preview size
//                Display display = getWindowManager().getDefaultDisplay();
//                Point size = new Point();
//                display.getSize(size);
//
//                Log.d(APP_TAG, "Size = " + size.toString());
//
//                r = CameraUtils.getBestPreviewSize(size.x, size.y, params);
//                params.setPreviewSize(r.width, r.height);
//                //Log.d(APP_TAG, "Preview size = " + r.width + "*" + r.height);
//
//                // Google XE10 Fix
//                // TODO : Remove this hack
//                //params.setPreviewFpsRange(30000, 30000);
//                // Picture size
//                r = CameraUtils.getBestSize(params.getSupportedPictureSizes());
//                params.setPictureSize(r.width, r.height);
//                //Log.d(APP_TAG, "Picture size = " + r.width + "*" + r.height);
//                //params.setWhiteBalance("auto");
//
//                if (params.getSupportedPreviewFormats().contains(ImageFormat.YUY2)) {
//                    params.setPreviewFormat(ImageFormat.YUY2);
//                }
//
//                if (params.getSupportedSceneModes().contains(Camera.Parameters.SCENE_MODE_BARCODE)) {
//                    params.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);
//                }
//
//                params.setJpegThumbnailQuality(100);
//                params.setJpegQuality(100);
//                if (params.getMaxNumMeteringAreas() > 0) { // check that metering areas are supported
//                    // Log.d(APP_TAG, "Setting meteringAreas ! #" + params.getMaxNumMeteringAreas());
//                    List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
//                    meteringAreas.add(new Camera.Area(new Rect(-50, -50, 50, 50), 1000)); // set weight to 60%
//                    params.setMeteringAreas(meteringAreas);
//                }
//
//                if (params.getMaxNumFocusAreas() > 0) {
//                    // Log.d(APP_TAG, "Setting focusAreas ! #" + params.getMaxNumMeteringAreas());
//                    List<Camera.Area> focusArea = new ArrayList<Camera.Area>();
//                    focusArea.add(new Camera.Area(new Rect(-50, -50, 50, 50), 1000)); // set weight to 60%
//                    params.setFocusAreas(focusArea);
//                }
//
//                mCamera.setFaceDetectionListener(null);
//                mCamera.setParameters(params);
//            }

            Parameters parameters = camera.getParameters();
            parameters.setPreviewFpsRange(30000, 30000);
            //params.setJpegThumbnailQuality(100);
            //params.setJpegQuality(100);
            camera.setParameters(parameters);
        }
    }

    /**
     * Release the camera from use
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
