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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.List;

import static com.ozcanlab.utils.Constants.APP_TAG;

//import com.google.glass.location.GlassLocationManager;

/**
 *
 * @author SaqibIsGreat
 */
public class PlantImagerActivity extends Activity {

    private boolean Indoor;
    private int numOfPictureTaken = 0;

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
//    private Criteria cisWhitePictureriteria = new Criteria();
    private String RdtType = "";
    private float valueALS = 0;
    
    private LocationManager locationManager;
    private Location lastKnownLocation;

    private String firstPhotoPath;
    private String secondPhotoPath;
    private String mPhotoPath;

    public final static String INDOOR_MESSAGE = "com.ozcanlab.rdt.INDOOR_MESSAGE";
    public final static String FIRST_PHOTO_PATH = "com.ozcanlab.rdt.FIRST_PHOTO_PATH";
    public final static String SECOND_PHOTO_PATH = "com.ozcanlab.rdt.SECOND_PHOTO_PATH";
    public final static String LOCATION_MESSAGE = "com.ozcanlab.rdt.LOCATION_MESSAGE";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //get intent(indoor)
        Intent intent = getIntent();
        Indoor = intent.getBooleanExtra(StartPlant.EXTRA_MESSAGE,false);


        /* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        setContentView(R.layout.main);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // ToDo add your GUI initialization code here  


        //open the camera
        (new OpenCameraTask()).execute();
        //Toast.makeText(PlantImagerActivity.this, "Center device in blue rect in red mode", Toast.LENGTH_LONG).show();


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
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        List<String> providers = locationManager.getProviders(criteria,
                true /* enabledOnly */);
        
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 1000,
                    0, locationListener);
        }
    }
    
//

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                sensorManager.registerListener(mySensorListener, light, SensorManager.SENSOR_DELAY_NORMAL);

                //take the picture
                mCamera.takePicture(null, null, null, new MyPictureCallBackJPG());
                return false;
        }
        return super.onKeyDown(keyCode, event); //To change body of generated methods, choose Tools | Templates.
    }



    //maybe can be deleted since we are controlling the camera directly
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FROM_CAMERA:
                    // Stop listening for ALS updates.
                    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                    sensorManager.unregisterListener(mySensorListener);
                   // processPictureWhenReady(data.getStringExtra("picture_file_path"));
                   // (new UploadTask()).execute(data.getStringExtra("picture_file_path"));
                    break;
                default:
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
   */
    /**
     * Process picture - from example GDK
     *
     * @param //picturePath
     */
    //@shaolei
    // processPictureWhenReady and onActivityResult are all GDK method that use an intent to capture
    // the image without directly using the Camera object.
    // but to create the overlay we need to have control the camera so these two method won't be used

    //todo:implement the method to save the photos
    /*
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
 */
    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        releaseCameraAndPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //To change body of generated methods, choose Tools | Templates.
        releaseCameraAndPreview();
    }

    @Override
    protected void onDestroy() {
        // In case we missed it !
        super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void finish() {
        super.finish(); //To change body of generated methods, choose Tools | Templates.

        releaseCameraAndPreview();
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
                CameraUtils.setCameraDisplayOrientation(PlantImagerActivity.this, cameraId, mCamera);
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
                    CameraPreview cameraPreview = new CameraPreview(PlantImagerActivity.this, mCamera);
                    cameraPreview.setId(R.id.camera_preview);
                    frameLayout.addView(cameraPreview, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                }
                // Overlay

                if (frameLayout.findViewById(R.id.camera_rdt_overlay) == null) {

                    if (numOfPictureTaken == 0){
                        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(PlantImagerActivity.this,"PlantImagerRed");
                        cameraSurfaceView.setId(R.id.camera_rdt_overlay);
                        frameLayout.addView(cameraSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        cameraSurfaceView.setZOrderMediaOverlay(true);
                        cameraSurfaceView.setZOrderOnTop(true);
                    }
                    else {
                        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(PlantImagerActivity.this, "PlantImagerWhite");
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
                
                TextView t = new TextView(PlantImagerActivity.this);
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
//                            CameraActivity.this.releaseCameraAndPreview();
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

            t = new TextView(PlantImagerActivity.this);
            t.setText("Waiting for Camera");
            t.setGravity(Gravity.CENTER);
            LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            t.setLayoutParams(layoutParams);
            t.setTextSize(50);
            t.setTextColor(Color.WHITE);

            frameLayout.addView(t);
        }

        private String getName() {
            Account[] accounts = AccountManager.get(PlantImagerActivity.this).getAccounts();
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

                    RdtReader.Iface client = Transport.getInstance(PlantImagerActivity.this).connect().getClient();
                    rtrn = client.transferPicture(ByteBuffer.wrap(ba), thriftParams);
                }
            } catch (NetworkErrorException ex) {
                //Toast.makeText(CameraActivity.this, "Please connect to Internet", Toast.LENGTH_LONG).show();
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -1;
            } catch (TTransportException ex) {
                //Toast.makeText(CameraActivity.this, "Server Error, Retry", Toast.LENGTH_LONG).show();
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -2;
            } catch (Exception ex) {
                Log.e(APP_TAG, "Exception", ex);
                rtrn = -3;
            } finally {
                Transport.getInstance(PlantImagerActivity.this).closeConnexion();
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
//                            CameraActivity.this.releaseCameraAndPreview();
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
//                            CameraActivity.this.releaseCameraAndPreview();
//                            (new OpenCameraTask()).execute();
//                        }
                    }
                }, 500);
            }
        }
    }


    //new MyPictureCallBackJPG ( take two photos without uploading the photo)


    private class MyPictureCallBackJPG implements Camera.PictureCallback {


        public void onPictureTaken(byte[] data, Camera camera) {

            //save the photo and record the photo path
            SaveImageTask saveImage = new SaveImageTask();
            saveImage.execute();
            while (saveImage.isLocked())
                continue;

            //reset the camera and take another photo
            if (numOfPictureTaken == 1) {
                releaseCameraAndPreview();
                (new OpenCameraTask()).execute();
            }

            //invoke the confirmation activity
            else {
                Intent intent = new Intent(PlantImagerActivity.this, ConfirmPlantActivity.class);
                intent.putExtra(INDOOR_MESSAGE, Indoor);
                intent.putExtra(FIRST_PHOTO_PATH, firstPhotoPath);
                intent.putExtra(SECOND_PHOTO_PATH, secondPhotoPath);
                intent.putExtra(LOCATION_MESSAGE, lastKnownLocation);
                startActivity(intent);
            }
        }
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        public boolean locked;

        public boolean isLocked(){
            return locked;
        }

        @Override
        protected void onPreExecute (){
            super.onPreExecute();
            locked = true;

        }

        @Override
        protected void onPostExecute(Void v)
        {
            numOfPictureTaken++;
           locked = false;
            super.onPostExecute(null);
        }
        @Override
        protected Void doInBackground(byte[]... data) {

            FileOutputStream outStream = null;

            // Write to external storage
            try {
                //File exStorage = Environment.getExternalStorageDirectory();
                //File dir = new File (exStorage.getAbsolutePath());
                //dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());

                File outFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator +"DCIM/Camera/"+ fileName);

               // File outFile = new File(exStorage, fileName);


                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);

                outStream.flush();
                outStream.close();

               // Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
                if (numOfPictureTaken == 0)
                    firstPhotoPath = new String(outFile.getAbsolutePath());

                else if (numOfPictureTaken == 1)
                {
                    secondPhotoPath = new String(outFile.getAbsolutePath());
                }

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }



    //rewrite this method above to take 2 photos and then invoke the confirmPlantActitviy

//    public native boolean imageProcessing();
//
//    static {
//        System.loadLibrary("image-proc");
//    }
    //This is a Code Cimetery ! Just in case we need it one day ... Not in use right now.
    // Old code used with "take picture" method

    /*
    private class MyPictureCallBackJPG implements Camera.PictureCallback {

        private String getFolder() {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures";
        }

        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream os = null;
            Long date = (new Date()).getTime();
            String path_image = getFolder() + File.separator + RdtType + "_" + date + ".jpg";
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
                    String s = "Value for " + RdtType + "_" + date + ".jpg = " + valueALS + "\n";
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
                    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                    sensorManager.unregisterListener(mySensorListener);
                    (new UploadTask()).execute(path_image);
                } catch (IOException ex) {
                    Log.d(APP_TAG, "Error... IO Exception", ex);
                } catch (NullPointerException ex) {
                    Log.d(APP_TAG, "Null pointer exception", ex);
                }
            }
        }

    }
    */
}
