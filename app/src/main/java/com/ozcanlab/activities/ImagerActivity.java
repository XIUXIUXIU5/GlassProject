/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ozcanlab.network.Transport;
import com.ozcanlab.rdt.R;
import com.ozcanlab.thrift.Params;
import com.ozcanlab.thrift.RdtReader;
import com.ozcanlab.utils.CameraUtils;
import com.ozcanlab.utils.FileUtils;
import com.ozcanlab.views.CameraPreview;
import com.ozcanlab.views.CameraSurfaceView;

import org.apache.thrift.transport.TTransportException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ozcanlab.utils.Constants.APP_TAG;

//import com.google.glass.location.GlassLocationManager;

/**
 *
 * @author SaqibIsGreat
 */
public class ImagerActivity extends Activity {

//    private GestureDetector gestureInterpreter;
    private Camera mCamera = null;
    private Context selfActivity = this;
    //private CameraView cameraView = null;
    //private CameraSurfaceView cameraSurfaceView = null;
    private static final int cameraId = 0;
    private static final int FROM_CAMERA = 1584;
    private MySensorListener mySensorListener = new MySensorListener();
    // GPS Stuffs
//    private MyLocationListener locationListener = null;
//    private LocationManager locationManager = null;
//    private Criteria criteria = new Criteria();
    private String RdtType = "";
    private float valueALS = 0;
    
    private LocationManager locationManager;
    private Location lastKnownLocation;

    private boolean mStartHorRect;
    private boolean mStartVertRect;
    private boolean mStartSquare;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        //selfActivity = this;

        /* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */

        Intent intent = getIntent();
        mStartHorRect = intent.getBooleanExtra(ImagerMenuActivity.HORRECT_MESSAGE,false);
        mStartSquare = intent.getBooleanExtra(ImagerMenuActivity.SQUARE_MESSAGE,false);
        mStartVertRect = intent.getBooleanExtra(ImagerMenuActivity.VERRECT_MESSAGE,false);

         setContentView(R.layout.main);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // ToDo add your GUI initialization code here  

        //if (getIntent() != null) {
        //    RdtType = getIntent().getStringExtra("RdtType");
        //}

        (new OpenCameraTask()).execute();
        //cameraView = new CameraView(ImagerActivity.this);
        //cameraSurfaceView = new CameraSurfaceView(ImagerActivity.this);
        //initViews();

        //imageProcessing();
        
        // GPS Stuffs
        //GlassLocationManager.init(this);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastKnownLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        //Criteria criteria = new Criteria();
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //List<String> providers = locationManager.getProviders(criteria,
        //        true /* enabledOnly */);
        
        //for (String provider : providers) {
        //    locationManager.requestLocationUpdates(provider, 1000,
        //            0, locationListener);
        //}
    }
    
//    private void initViews()
//    {
//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_framelayout);
//        frameLayout.removeAllViews();
//
//        // Camera Preview
//        if (frameLayout.findViewById(R.id.camera_preview) == null) {
//            cameraView.setId(R.id.camera_preview);
//            frameLayout.addView(cameraView, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
//            //CameraPreview cameraPreview = new CameraPreview(ImagerActivity.this, mCamera);
//            //cameraPreview.setId(R.id.camera_preview);
//            //frameLayout.addView(cameraPreview, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
//        }
//
//        // Overlay
//        if (frameLayout.findViewById(R.id.camera_rdt_overlay) == null) {
//            CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(ImagerActivity.this);
//            cameraSurfaceView.setId(R.id.camera_rdt_overlay);
//            frameLayout.addView(cameraSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            cameraSurfaceView.setZOrderMediaOverlay(true);
//            cameraSurfaceView.setZOrderOnTop(true);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        
//        if (cameraView != null) {
//            cameraView.releaseCamera();
//        }
//        
//        initViews();
        
        releaseCameraAndPreview();
//        if (mCamera == null) {
//            (new OpenCameraTask()).execute();
//        } else {
//            try {
//                mCamera.reconnect();
//            } catch (IOException ex) {
//                Log.e(getString(R.string.app_name), "failed to open Camera (reconnect)", ex);
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause(); //To change body of generated methods, choose Tools | Templates.
        
        releaseCameraAndPreview();
        
//        if (cameraView != null) {
//            cameraView.releaseCamera();
//        }
    }

    @Override
    protected void onDestroy() {
        // In case we missed it !
        //releaseCameraAndPreview();
        super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed(); //To change body of generated methods, choose Tools | Templates.
//        releaseCameraAndPreview();
//        finish();
//    }

    @Override
    public void finish() {
        super.finish(); //To change body of generated methods, choose Tools | Templates.
        
        //if (cameraView != null) {
        //    cameraView.releaseCamera();
        //}
        releaseCameraAndPreview();
        //moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                // ALS Sensor
                //SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                //Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                //sensorManager.registerListener(mySensorListener, light, SensorManager.SENSOR_DELAY_NORMAL);

//                if (cameraView != null) {
//                    cameraView.releaseCamera();
//                }

                mCamera.takePicture(null, null, null, new MyPictureCallBackJPG());
                //releaseCameraAndPreview();
                //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(takePictureIntent, FROM_CAMERA);
                return false;
        }
        return super.onKeyDown(keyCode, event); //To change body of generated methods, choose Tools | Templates.
    }
    
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (KeyEvent.KEYCODE_BACK) {
//            case 4:
//                releaseCameraAndPreview();
//                ImagerActivity.this.finish();
//                return true;
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FROM_CAMERA:
                    // Stop listening for ALS updates.
                    //SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                    //sensorManager.unregisterListener(mySensorListener);
                    
                    //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(takePictureIntent, FROM_CAMERA);
                    //processPictureWhenReady(data.getStringExtra("picture_file_path"));
                    //(new UploadTask()).execute(data.getStringExtra("picture_file_path"));
                    
                    if (mCamera == null) {
                        (new OpenCameraTask()).execute();
                    } else {
                        ImagerActivity.this.releaseCameraAndPreview();
                        (new OpenCameraTask()).execute();
                    }
                    break;
                default:
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
   
    /**
     * Process picture - from example GDK
     *
     * @param picturePath
     */
    private void processPictureWhenReady(final String picturePath) {
        final File pictureFile = new File(picturePath);

        if (pictureFile.exists()) {
            // The picture is ready; process it.
            (new UploadTask()).execute(picturePath);
        } else {
            // The file does not exist yet. Before starting the file observer, you
            // can update your UI to let the user know that the application is
            // waiting for the picture (for example, by displaying the thumbnail
            // image and a progress indicator).

            final File parentDirectory = pictureFile.getParentFile();
            FileObserver observer = new FileObserver(parentDirectory.getPath()) {
                // Protect against additional pending events after CLOSE_WRITE is
                // handled.
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path) {
                    if (!isFileWritten) {
                        // For safety, make sure that the file that was created in
                        // the directory is actually the one that we're expecting.
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile.equals(pictureFile));

                        if (isFileWritten) {
                            stopWatching();

                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }
            };
            observer.startWatching();
        }
    }
 
    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            //mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    // Light sensor listener
    class MySensorListener implements SensorEventListener {

        private int accuracy;
        private int changeCount = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            this.accuracy = accuracy;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (this.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                return;
            }

            switch (event.sensor.getType()) {
                case Sensor.TYPE_LIGHT:
                    valueALS = (valueALS * changeCount + event.values[0]) / (changeCount + 1);
                    changeCount++;
                    break;
            }
        }
    }

    // Open the Camera in Background
    private class OpenCameraTask extends AsyncTask<Void, Void, Boolean> {

        private void initCamera() {

            mCamera.setDisplayOrientation(90);

            Camera.Size r;
            Camera.Parameters params = mCamera.getParameters();
            if (params != null) {

                // Set camera orientation
                CameraUtils.setCameraDisplayOrientation(ImagerActivity.this, cameraId, mCamera);
                // Preview size
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Log.d(APP_TAG, "Size = " + size.toString());

                r = CameraUtils.getBestPreviewSize(size.x, size.y, params);
                params.setPreviewSize(r.width, r.height);
                //Log.d(APP_TAG, "Preview size = " + r.width + "*" + r.height);

                // Google XE10 Fix
                // TODO : Remove this hack
                params.setPreviewFpsRange(30000, 30000);

                // Picture size
                r = CameraUtils.getBestSize(params.getSupportedPictureSizes());
                params.setPictureSize(r.width, r.height);
                //Log.d(APP_TAG, "Picture size = " + r.width + "*" + r.height);
                //params.setWhiteBalance("auto");

                if (params.getSupportedPreviewFormats().contains(ImageFormat.YUY2)) {
                    params.setPreviewFormat(ImageFormat.YUY2);
                }

                if (params.getSupportedSceneModes().contains(Camera.Parameters.SCENE_MODE_BARCODE)) {
                    params.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);
                }

                params.setJpegThumbnailQuality(100);
                params.setJpegQuality(100);
                if (params.getMaxNumMeteringAreas() > 0) { // check that metering areas are supported
                    // Log.d(APP_TAG, "Setting meteringAreas ! #" + params.getMaxNumMeteringAreas());
                    List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                    meteringAreas.add(new Camera.Area(new Rect(-50, -50, 50, 50), 1000)); // set weight to 60%
                    params.setMeteringAreas(meteringAreas);
                }

                if (params.getMaxNumFocusAreas() > 0) {
                    // Log.d(APP_TAG, "Setting focusAreas ! #" + params.getMaxNumMeteringAreas());
                    List<Camera.Area> focusArea = new ArrayList<Camera.Area>();
                    focusArea.add(new Camera.Area(new Rect(-50, -50, 50, 50), 1000)); // set weight to 60%
                    params.setFocusAreas(focusArea);
                }

                mCamera.setFaceDetectionListener(null);
                mCamera.setParameters(params);
            }
        }

        private boolean safeCameraOpen(int id) {
            boolean qOpened = false;

            releaseCameraAndPreview();
            int count = 0;
            while (count < 8 && mCamera == null) {
                try {
                    mCamera = Camera.open(id);
                    qOpened = (mCamera != null);
                } catch (Exception e) {
                    Log.e(getString(R.string.app_name), "Count " + count + ": Failed to open Camera", e);
                    //releaseCameraAndPreview();
                }
                
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    Log.e(getString(R.string.app_name), "Count " + count + ": Thread sleep failed", e);
                }
                count++;
            }

            return qOpened;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (mCamera == null) {
                if (safeCameraOpen(cameraId)) {
                    initCamera();
                    Log.d(APP_TAG, "Camera Set !");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success && mCamera != null) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_framelayout);
                frameLayout.removeAllViews();

                // Camera Preview
                if (frameLayout.findViewById(R.id.camera_preview) == null) {
                    CameraPreview cameraPreview = new CameraPreview(ImagerActivity.this, mCamera);
                    cameraPreview.setId(R.id.camera_preview);
                    frameLayout.addView(cameraPreview, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                }

                // Overlay
                if (frameLayout.findViewById(R.id.camera_rdt_overlay) == null) {
                    if(mStartVertRect == true) {
                        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(ImagerActivity.this, "RDT");
                        cameraSurfaceView.setId(R.id.camera_rdt_overlay);
                        frameLayout.addView(cameraSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        cameraSurfaceView.setZOrderMediaOverlay(true);
                        cameraSurfaceView.setZOrderOnTop(true);
                    }
                    else if (mStartHorRect == true)
                    {
                        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(ImagerActivity.this, "Imager");
                        cameraSurfaceView.setId(R.id.camera_rdt_overlay);
                        frameLayout.addView(cameraSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        cameraSurfaceView.setZOrderMediaOverlay(true);
                        cameraSurfaceView.setZOrderOnTop(true);
                    }

                    else if(mStartSquare == true)
                    {
                        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(ImagerActivity.this, "Circle");
                        cameraSurfaceView.setId(R.id.camera_rdt_overlay);
                        frameLayout.addView(cameraSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        cameraSurfaceView.setZOrderMediaOverlay(true);
                        cameraSurfaceView.setZOrderOnTop(true);
                    }
                }
            }
            else {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_framelayout);
                frameLayout.removeAllViews();
                
                TextView t = new TextView(ImagerActivity.this);
                t.setText("Unable to open Camera! Closing...");
                t.setGravity(Gravity.CENTER);
                LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                t.setLayoutParams(layoutParams);
                t.setTextSize(50);
                t.setTextColor(Color.WHITE);

                frameLayout.addView(t);
                
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)selfActivity).finish();
                        // change your text here
//                        if (mCamera == null) {
//                            (new OpenCameraTask()).execute();
//                        } else {
//                            ImagerActivity.this.releaseCameraAndPreview();
//                            (new OpenCameraTask()).execute();
//                        }
                        
                    }
                }, 2000);
            }
        }
    }
    // Upload the Picture 

    private class UploadTask extends AsyncTask<String, Integer, Integer> {

        InputStream inputStream = null;
        TextView t;

        @Override
        protected void onPreExecute() {
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_framelayout);
            frameLayout.removeAllViews();

            t = new TextView(ImagerActivity.this);
            t.setText("Waiting for Camera");
            t.setGravity(Gravity.CENTER);
            LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            t.setLayoutParams(layoutParams);
            t.setTextSize(50);
            t.setTextColor(Color.WHITE);

            frameLayout.addView(t);
        }

        private String getName() {
            Account[] accounts = AccountManager.get(ImagerActivity.this).getAccounts();
            for (Account account : accounts) {
                if (account.type.equals("com.google")) {
                    return account.name;
                }
            }
            return "";
        }

        @Override
        protected Integer doInBackground(String... params) {
            
            int rtrn = 0;
            
            try {
                if (!params[0].isEmpty()) {
                    Log.d(APP_TAG, "Sending Picture : " + params[0]);
                    File f = new File(params[0]);

                    int count = 0;

                    do {
                        // Wait for the picture to be written by gcam
                        publishProgress(++count);
                        Thread.sleep(250);

                    } while (!f.exists() && count < 40);

                    // Get Picture
                    inputStream = new FileInputStream(f);
                    byte[] ba = FileUtils.readFully(inputStream);

                    // Get Params
                    Params thriftParams = new Params();
                    thriftParams.als_value = valueALS;
                    thriftParams.google_account = getName();
                    thriftParams.picture_ext = "jpg";
                    thriftParams.rdt_type = RdtType;
                    //Location loc = GlassLocationManager.getInstance().getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    // FIXME: possible memory leak - lastKnownLocation changed in different thread
                    if (lastKnownLocation != null) {
                        thriftParams.location_long = lastKnownLocation.getLongitude();
                        thriftParams.location_lat = lastKnownLocation.getLatitude();
                    }

                    Log.d(APP_TAG, "Thrift Params = " + thriftParams);

                    RdtReader.Iface client = Transport.getInstance(ImagerActivity.this).connect().getClient();
                    rtrn = client.transferPicture(ByteBuffer.wrap(ba), thriftParams);
                }
            } catch (NetworkErrorException ex) {
                //Toast.makeText(ImagerActivity.this, "Please connect to Internet", Toast.LENGTH_LONG).show();
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -1;
            } catch (TTransportException ex) {
                //Toast.makeText(ImagerActivity.this, "Server Error, Retry", Toast.LENGTH_LONG).show();
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -2;
            } catch (Exception ex) {
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -3;
            } finally {
                Transport.getInstance(ImagerActivity.this).closeConnexion();
            }
            return rtrn;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values); //To change body of generated methods, choose Tools | Templates.
            String s = "Uploading\n";
            Log.d(APP_TAG, "Count = " + values[0]);
            for (int i = 0; i < values[0]; i++) {
                if (i % 3 == 0) {
                    s += ".";
                }
            }

            t.setText(s);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > -1) {
                t.setText("Upload success. Result will arrive shortly.");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)selfActivity).finish();
                        // change your text here
//                        if (mCamera == null) {
//                            (new OpenCameraTask()).execute();
//                        } else {
//                            ImagerActivity.this.releaseCameraAndPreview();
//                            (new OpenCameraTask()).execute();
//                        }
                        
                    }
                }, 500);
            } else {
                switch (result) {
                    case -1:
                        t.setText("No connection. Please connect to Internet.");
                        break;
                    case -2:
                        t.setText("Upload failed. Please try again later.");
                        break;
                    case -3:
                        t.setText("Oops. Something broke. Try again later.");
                        break;
                    default:
                        t.setText("Unknown error. Please try again later.");
                        break;
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)selfActivity).finish();
//                        // change your text here
//                        if (mCamera == null) {
//                            (new OpenCameraTask()).execute();
//                        } else {
//                            ImagerActivity.this.releaseCameraAndPreview();
//                            (new OpenCameraTask()).execute();
//                        }
                    }
                }, 500);
            }
        }
    }

//    public native boolean imageProcessing();
//
//    static {
//        System.loadLibrary("image-proc");
//    }
    //This is a Code Cimetery ! Just in case we need it one day ... Not in use right now.
    // Old code used with "take picture" method
    private class MyPictureCallBackJPG implements Camera.PictureCallback {

        private String getFolder() {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures";
        }

        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream os = null;
            Long date = (new Date()).getTime();
            String path_image = getFolder() + File.separator + date + ".jpg";
            try {
                Log.d(APP_TAG, "Picture taken ... Uri = ");
                Log.d(APP_TAG, path_image);
                os = new FileOutputStream(path_image);
                Log.d(APP_TAG, "Created stream");
                os.write(data);
                Log.d(APP_TAG, "Wrote data");
                os.flush();
                Log.d(APP_TAG, "Flushed");
                os.close();
                Log.d(APP_TAG, "Closed");
                Log.d(APP_TAG, "ALS Value");
                Log.d(APP_TAG, String.valueOf(valueALS));

                if (valueALS > 0) {
                    os = new FileOutputStream(getFolder() + File.separator + "ALSvalues.txt", true);
                    String s = "Value for " + date + ".jpg = " + valueALS + "\n";
                    os.write(s.getBytes());
                    os.flush();
                    os.close();
                } else {
                    Log.d(APP_TAG, "ALS Value not found");
                }

            } catch (FileNotFoundException ex) {
                Log.d(APP_TAG, "Error... File not found", ex);
            } catch (IOException ex) {
                Log.d(APP_TAG, "Error... IO Exception", ex);
            } catch (NullPointerException ex) {
                Log.d(APP_TAG, "Null pointer exception", ex);
            } catch (Exception ex) {
                Log.d(APP_TAG, "Unknown exception", ex);
            } finally {
                try {
                    os.close();
                    //SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                    //sensorManager.unregisterListener(mySensorListener);
                    //(new UploadTask()).execute(path_image);
                    
                    //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(takePictureIntent, FROM_CAMERA);
                    
                    if (mCamera == null) {
                        (new OpenCameraTask()).execute();
                    } else {
                        ImagerActivity.this.releaseCameraAndPreview();
                        (new OpenCameraTask()).execute();
                    }
                } catch (IOException ex) {
                    Log.d(APP_TAG, "Error... IO Exception", ex);
                } catch (NullPointerException ex) {
                    Log.d(APP_TAG, "Null pointer exception", ex);
                }
            }
        }
    }
}
