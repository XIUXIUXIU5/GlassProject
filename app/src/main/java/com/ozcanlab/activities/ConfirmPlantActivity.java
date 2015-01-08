package com.ozcanlab.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozcanlab.rdt.R;

import java.io.File;

public class ConfirmPlantActivity extends Activity {

    private boolean Indoor;
    private Location lastLocation;
    private String firstPhotoPath;
    private String secondPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Indoor = intent.getBooleanExtra(PlantImagerActivity.INDOOR_MESSAGE,false);
        firstPhotoPath = intent.getStringExtra(PlantImagerActivity.FIRST_PHOTO_PATH);
        secondPhotoPath = intent.getStringExtra(PlantImagerActivity.SECOND_PHOTO_PATH);
       // lastLocation = intent.

        File imgFile1 = new  File(firstPhotoPath);
        Bitmap myBitmap1 = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
        ImageView myImage1 = (ImageView) findViewById(R.id.image_1);
        myImage1.setImageBitmap(myBitmap1);

        File imgFile2 = new  File(secondPhotoPath);
        Bitmap myBitmap2 = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
        ImageView myImage2 = (ImageView) findViewById(R.id.image_2);
        myImage2.setImageBitmap(myBitmap2);

        TextView IndoorTextView = (TextView) findViewById(R.id.Indoor);

        if (Indoor == true)
        {
            IndoorTextView.setText("Indoor");
        }
        else
        {
            IndoorTextView.setText("Outdoor");
        }
        setContentView(R.layout.activity_confirm_plant);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_plant, menu);
        return true;
    }

    //TODO:implement this method to send the photos
    public void sendMessage(View view){
       // Intent intent = new Intent(this, )
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
}
