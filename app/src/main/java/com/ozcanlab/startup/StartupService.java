/*Author(s): Lei Shao
* start app and create the live card*/
package com.ozcanlab.startup;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.ozcanlab.rdt.R;


/**
 * @author LeiShao
 * creat the live card
 */

public class StartupService extends Service {

    private static final String LIVE_CARD_TAG = "rdtonglass";

   private static final String rdtOnRunning = "ozcan lab apps";

    private LiveCard mLiveCard;

    private RemoteViews mLiveCardView;

    private final Handler mHandler = new Handler();
    private final UpdateLiveCardRunnable mUpdateLiveCardRunnable =
            new UpdateLiveCardRunnable();

    private static final long DELAY_MILLIS = 30000;


    public StartupService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {

            // Get an instance of a live card
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            // Inflate a layout into a remote view
            mLiveCardView = new RemoteViews(getPackageName(), R.layout.live_card);
            mLiveCard.setViews(mLiveCardView);

            //Set up initial RemoteViews values
            mLiveCardView.setTextViewText(R.id.live_card_text_view,rdtOnRunning);

            //mLiveCardView.

            // Set up the live card's action with a pending intent
            // to show a menu when tapped
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
          //  mLiveCard.setVoiceActionEnabled(true);
            mLiveCard.attach(this);

            // Publish the live card
            mLiveCard.publish(PublishMode.REVEAL);

            // Queue the update text runnable
            mHandler.post(mUpdateLiveCardRunnable);
        } else {
            mLiveCard.navigate();
        }

        // Return START_NOT_STICKY to prevent the system from restarting the service if it is killed

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

    /**
     * Runnable that updates live card contents
     */
    private class UpdateLiveCardRunnable implements Runnable{

        private boolean mIsStopped = false;

        /*
         * Updates the card every 30 seconds as a demonstration.
         */
        public void run(){
            if(!isStopped()){

                // Update the remote view.
                //if get the image
               // mLiveCardView.setTextViewText(R.id.live_card_text_view,"get result");

                //if other condition
                //set other text

                // Always call setViews() to update the live card's RemoteViews.
               // mLiveCard.setViews(mLiveCardView);

                // refresh the result every 30 seconds.
             //   mHandler.postDelayed(mUpdateLiveCardRunnable, DELAY_MILLIS);
            }
        }

        public boolean isStopped() {
            return mIsStopped;
        }

        public void setStop(boolean isStopped) {
            this.mIsStopped = isStopped;
        }
    }


}


