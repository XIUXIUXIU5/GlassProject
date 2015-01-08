/*
 Youtube Feed - YouTube Atom Feed for Google Glass
 Copyright (C) 2013 James Betker

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ozcanlab.startup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
//import com.google.android.glass.timeline.TimelineManager;
//import com.google.glass.location.GlassLocationManager;
import com.ozcanlab.activities.ImagerActivity;

import static com.ozcanlab.utils.Constants.*;
import com.ozcanlab.utils.TimelineUtils;

/**
 * The StartupActivity for this app is simply a launcher for the backing
 * service. It has no UI.
 *
 * @author betker
 */


/*
public class StartupActivity extends Activity {

    private static final String LIVE_CARD_TAG = "rdtonglass";
//    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GlassLocationManager.init(this);
        
//        mTimelineManager = TimelineManager.from(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getIntent();
        
        if (mLiveCard == null) {
//            mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_TAG);
            mLiveCard.setDirectRenderingEnabled(true).getSurfaceHolder();
            mLiveCard.publish(PublishMode.REVEAL);
        }

        //Log.v(APP_TAG, "Service onStartCommand intent=" + (intent == null ? "null" : intent.toString()));
        //Log.v(APP_TAG, "Intent data URI: " + (intent == null ? "null" : intent.getDataString()));
        
        try {
            if (intent != null && intent.getData() != null) {
                Log.v(APP_TAG, "Intent data : " + intent.getData().getPath()); 
                if (intent.getDataString().contains("process")) {
                    doProcess(intent.getData().getPathSegments().get(0));
                }
            } else {
                // FIXME: no home card initialized
                //TimelineUtils.initHomeCard(this);
            }   
        } finally {
            // Finishing this activity
            finish();
        }
    }

    void doProcess(String type) {
        Log.d(APP_TAG, "Processing phase");
        Intent cameraIntent = new Intent(getBaseContext(), ImagerActivity.class);
        cameraIntent.setAction(Intent.ACTION_VIEW);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cameraIntent.putExtra("RdtType", type);
        startActivity(cameraIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
*/