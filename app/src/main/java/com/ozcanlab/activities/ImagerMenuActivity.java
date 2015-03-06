/*Author(s): Lei Shao
* show menu for overlay options*/

 package com.ozcanlab.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.ozcanlab.rdt.R;

public class ImagerMenuActivity extends Activity {

    private boolean mStartHorRect;
    private boolean mStartVertRect;
    private boolean mStartSquare;

    public final static String HORRECT_MESSAGE = "com.ozcanlab.rdt.HORRECT_MESSAGE";
    public final static String VERRECT_MESSAGE = "com.ozcanlab.rdt.VERRECT_MESSAGE";
    public final static String SQUARE_MESSAGE = "com.ozcanlab.rdt.SQUARE_MESSAGE";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_imager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mStartHorRect:
                mStartHorRect = true;
                return true;

            case R.id.mStartSquare:
                mStartSquare = true;
                return true;

            case R.id.mStartVertRect:
                mStartVertRect = true;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //TODO:send the choice as extra
    //not sure about the camera activity for now, so the activity opened might not be right
    private void performActionsIfConnected() {
        //open the corresponding activity
        if (mStartSquare) {
            Intent intent = new Intent(ImagerMenuActivity.this, ImagerActivity.class);
            intent.putExtra(SQUARE_MESSAGE, mStartSquare);
            mStartSquare = false;
            startActivity(intent);
        }

        else if(mStartHorRect){
            Intent intent = new Intent(ImagerMenuActivity.this, ImagerActivity.class);
            intent.putExtra(HORRECT_MESSAGE, mStartHorRect);
            mStartHorRect = false;

            startActivity(intent);

        }

        else if (mStartVertRect) {
            Intent intent = new Intent(ImagerMenuActivity.this, ImagerActivity.class);
            intent.putExtra(VERRECT_MESSAGE, mStartVertRect);
            mStartVertRect = false;

            startActivity(intent);
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
