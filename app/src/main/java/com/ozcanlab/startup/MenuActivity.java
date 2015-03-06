/**
 * @author LeiShao
 * creat the menu with options:RDT Leaf Mercury Imager Stop
 */
package com.ozcanlab.startup;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.ozcanlab.activities.CameraActivity;
import com.ozcanlab.activities.ImagerMenuActivity;
import com.ozcanlab.activities.StartPlant;
import com.ozcanlab.rdt.R;


public class MenuActivity extends Activity {

    // Requested actions.
    private boolean mStop;
    private boolean mStartRDT;
    private boolean mStartLeaf;
    private boolean mStartMercury;
    private boolean mStartImager;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                mStop = true;
                return true;

            case R.id.RDT:
                mStartRDT = true;
                return true;

            case R.id.Leaf:
                mStartLeaf = true;
                return true;

            case R.id.Mercury:
                mStartMercury = true;
                return true;

            case R.id.Imager:
                mStartImager = true;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //not sure about the camera activity for now, so the activity opened might not be right
    private void performActionsIfConnected() {
        //open the corresponding activity
        if (mStartImager) {
            mStartImager = false;
            startActivity(new Intent(MenuActivity.this, ImagerMenuActivity.class));
        }


        else if (mStartRDT) {
            mStartRDT = false;
            startActivity(new Intent(MenuActivity.this, CameraActivity.class));
        }

        else if (mStartLeaf) {
            mStartLeaf = false;
/*
            GlassClient test = new GlassClient("devpc02.ee.ucla.edu", 8045,true);
            test.send();
            if(test.getResult())
            test.closeSocket();

*/
            startActivity(new Intent(MenuActivity.this, StartPlant.class));

        }

        else if (mStartMercury)
        {
            mStartMercury = false;
         //   startActivity(new Intent(MenuActivity.this, CameraActivity.class));

        }

        //stop the service
        else if (mStop) {
            mStop = false;
            stopService(new Intent(MenuActivity.this, StartupService.class));
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
