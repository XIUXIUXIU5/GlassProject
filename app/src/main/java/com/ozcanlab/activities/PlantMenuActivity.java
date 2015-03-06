/*Author(s): Lei Shao
* show menu for selecting send after confirmation page*/

package com.ozcanlab.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ozcanlab.rdt.R;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class PlantMenuActivity extends Activity {

    private boolean mStop;
    private boolean mSend;

    private String Indoor;
    private Location lastLocation;
    private String firstPhotoPath;
    private String secondPhotoPath;

    public final static String RESULT_MESSAGE = "com.ozcanlab.rdt.RESULT_MESSAGE";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_plant_menu, menu);

        Intent intent = getIntent();
        Indoor = intent.getStringExtra(PlantImagerActivity.INDOOR_MESSAGE);
        firstPhotoPath = intent.getStringExtra(PlantImagerActivity.FIRST_PHOTO_PATH);
        secondPhotoPath = intent.getStringExtra(PlantImagerActivity.SECOND_PHOTO_PATH);
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

            GlassClient test = new GlassClient();
            test.execute();
            Toast.makeText(PlantMenuActivity.this, "sending the picture", Toast.LENGTH_SHORT).show();

            while(test.isLocked())
            {
                continue;
            }


            String result = test.result;



            Intent intent = new Intent(PlantMenuActivity.this, PlantResultMainActivity.class);
            intent.putExtra(RESULT_MESSAGE,result);
             startActivity(intent);

        }



        //stop the service
        else if (mStop) {


            startActivity(new Intent(PlantMenuActivity.this, StartPlant.class));
            mStop = false;
        }

    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // perform the actions selected,then finish the Activity.
        performActionsIfConnected();
        finish();
    }


    public class GlassClient extends AsyncTask<Void, Void, Void> {

        private Socket socket = null;
        private BufferedOutputStream bos = null;

        // to be sent to server
        private ArrayList<File> files = null;
        private ArrayList<String> names = null;

        private String lightingCondition;

        public String result;
        public boolean locked;

        public boolean isLocked(){
            return locked;
        }

         protected void onPreExecute() {
             super.onPreExecute();
             locked = true;

        }



        protected Void doInBackground(Void... params) {
            try {
                socket = new Socket("devpc02.ee.ucla.edu", 8045);
            } catch (Exception e) {
                System.err.println(e);
                System.exit(-1);
            }

            this.files = new ArrayList<File>();
            this.files.add(new File(firstPhotoPath));
            this.files.add(new File(secondPhotoPath));

            this.lightingCondition = Indoor;

            this.result = this.send();
             return null;
        }



        protected void onPostExecute(Void v) {
            super.onPostExecute(null);
            this.closeSocket();
            this.locked = false;
            return;
        }




        public Socket getSocket() {
            return this.socket;
        }

        public void closeSocket() {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.err.println("GlassClient failed to close socket");
                System.exit(-1);
            }
        }

        public String send() {
            String finalResult = "";
            try {
                // data stream between client and server
                DataInputStream dis = new DataInputStream(this.getSocket().getInputStream());
                DataOutputStream dos = new DataOutputStream(this.getSocket().getOutputStream());

                // send number of files
                dos.writeInt(this.files.size());
                dos.flush();

                // send filenames
                for (int i=0; i < this.files.size(); i++) {

                    dos.writeUTF(this.files.get(i).getName());
                    dos.flush();

                }//end for

                int n=0;
                byte[] buffer = new byte[4096];

                // send each file
                for (int i=0; i < this.files.size(); i++) {
                    // send file size
                    dos.writeLong(this.files.get(i).length());
                    dos.flush();

                    // write file
                    FileInputStream fis = new FileInputStream(this.files.get(i));

                    while ( (n = fis.read(buffer)) != -1 ) {
                        dos.write(buffer, 0, n);
                        dos.flush();
                    }//end while

                }//end for

                // send params
                dos.writeUTF(this.lightingCondition);
                dos.flush();

                // receive finalResult
                finalResult = dis.readUTF();
                //System.out.println("final result: " + finalResult);

                // close dos
                dos.close();
            } catch (Exception e) {
                System.err.println(e);
                System.exit(-1);
            }
            return finalResult;
        }



    }//end class GlassClient

}

