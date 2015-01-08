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
package com.ozcanlab.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A persistent service that maintains a card on the Glass timeline. When turned
 * "on", this service pushes sensor data from a variety of sources onto this
 * card.
 *
 * @author betker
 *
 */
public class DispatcherService extends Service {

    

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
       super.onStartCommand(intent, flags, startid);
       return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //no bindings here
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
