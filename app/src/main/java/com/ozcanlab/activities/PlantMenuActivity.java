package com.ozcanlab.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.ozcanlab.rdt.R;
import com.ozcanlab.startup.StartupService;

public class PlantMenuActivity extends Activity {

    private boolean mStop;
    private boolean mSend;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_plant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                mStop = true;
                return true;

            case R.id.Send:
                mSend = true;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //not sure about the camera activity for now, so the activity opened might not be right
    private void performActionsIfConnected() {
        //open the corresponding activity
        if (mSend) {
            //send the pictures
        }



        //stop the service
        else if (mStop) {
            mStop = false;

            startActivity(new Intent(PlantMenuActivity.this, StartPlant.class));
        }

    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // perform the actions selected,then finish the Activity.
        performActionsIfConnected();
        finish();
    }
}

