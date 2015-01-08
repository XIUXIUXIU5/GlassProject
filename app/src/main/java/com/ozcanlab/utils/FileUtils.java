/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Debug;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Romain
 */
public class FileUtils {

    public static String copyRawToInternal(Context ctx, String name, int id) throws IOException {

        //Open your local db as the input stream

        InputStream myInput = null;
        try {
            myInput = ctx.getResources().openRawResource(id);
        } catch (Exception ex) {
            Log.e("RDTGlass", "Exception", ex);
        }
        //Open the empty db as the output stream
        OutputStream myOutput = ctx.openFileOutput(name, Context.MODE_PRIVATE);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        return ctx.getFilesDir() + File.separator + name;
    }
    
    public static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
