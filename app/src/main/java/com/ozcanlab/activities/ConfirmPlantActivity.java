package com.ozcanlab.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.ozcanlab.rdt.R;

public class ConfirmPlantActivity extends Activity {

    private boolean Indoor;
    private Location lastLocation;
    private String firstPhotoPath;
    private String secondPhotoPath;
    private ImageView myImage1;
    private ImageView myImage2;
    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Indoor = intent.getBooleanExtra(PlantImagerActivity.INDOOR_MESSAGE,false);
        firstPhotoPath = intent.getStringExtra(PlantImagerActivity.FIRST_PHOTO_PATH);
        secondPhotoPath = intent.getStringExtra(PlantImagerActivity.SECOND_PHOTO_PATH);
       // lastLocation = intent.

        setContentView(R.layout.activity_confirm_plant);

        BitmapFactory.Options options;
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap1 = BitmapFactory.decodeFile(firstPhotoPath,options);
        myImage1 = (ImageView) findViewById(R.id.image_1);
        myImage1.setImageBitmap(bitmap1);

        Bitmap bitmap2 = BitmapFactory.decodeFile(secondPhotoPath,options);
        myImage2 = (ImageView) findViewById(R.id.image_2);
        myImage2.setImageBitmap(bitmap2);


        TextView IndoorTextView = (TextView) findViewById(R.id.Indoor);

        if (Indoor == true)
        {
            IndoorTextView.setText("Indoor");
        }
        else
        {
            IndoorTextView.setText("Outdoor");
        }
        mGestureDetector = createGestureDetector(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_plant, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private GestureDetector createGestureDetector(final Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    Intent menuIntent = new Intent(ConfirmPlantActivity.this, PlantMenuActivity.class);
                   // menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                           // Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(menuIntent);
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe

                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe

                    return true;
                }
                return false;
            }
        });


        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });

        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return false;
            }
        });
        return gestureDetector;
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

}
