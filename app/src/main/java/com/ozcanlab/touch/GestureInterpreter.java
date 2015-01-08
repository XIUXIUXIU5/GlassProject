// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.ozcanlab.touch;


import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import static com.ozcanlab.utils.Constants.*;

/**
 * Processes touch events and scrolls the screen in manual mode.
 *
 * @author Romain Caire
 */
public class GestureInterpreter extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
   
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(APP_TAG, "Double tap event e = " + e.toString());
        return super.onDoubleTapEvent(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e); //To change body of generated methods, choose Tools | Templates.
        Log.d(APP_TAG, "On long press");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(APP_TAG, "On Scroll");
        return super.onScroll(e1, e2, distanceX, distanceY); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e); //To change body of generated methods, choose Tools | Templates.
        Log.d(APP_TAG, "On show press");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(APP_TAG, "Tap down");
        return super.onDown(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(APP_TAG, "Flinging " + velocityX + ", " + velocityY);
        if (velocityX < -3500) {
            Log.d(APP_TAG, "Fling Right");
        } else if (velocityX > 3500) {
            Log.d(APP_TAG, "Fling Left");
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(APP_TAG, "Tap up");
        // Bring up the controls
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(APP_TAG, "Double tap");
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(APP_TAG, "Confirmed single tap");
        return true;
    }
}