/*
	GlassClient.java
	Author(s): Jordi A. Burbano
	19.02.2015
	Usage: java GlassClient <host name> <port number>
*/
package com.ozcanlab.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GlassClient {

    private Socket socket = null;
    private BufferedOutputStream bos = null;

    // to be sent to server
    private ArrayList<File> files = null;
    private ArrayList<String> names = null;

    private String lightingCondition;

    public GlassClient(String hostName, int portNo, String file1, String file2, String lightingCondition) {
        try {
            socket = new Socket(hostName, portNo);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }

        this.files = new ArrayList<File>();
        this.files.add(new File(file1));
        this.files.add(new File(file2));

        this.lightingCondition = lightingCondition;
    }

    public GlassClient(String hostName, int portNo, String lightingCondition) {
        try {
            socket = new Socket(hostName, portNo);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }

        this.lightingCondition = lightingCondition;
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

            // // send number of files
            // dos.writeInt(this.files.size());
            // dos.flush();

            // // send filenames
            // for (int i=0; i < this.files.size(); i++) {

            // dos.writeUTF(this.files.get(i).getName());
            // dos.flush();

            // }//end for

            // int n=0;
            // byte[] buffer = new byte[4096];

            // // send each file
            // for (int i=0; i < this.files.size(); i++) {
            // // send file size
            // dos.writeLong(this.files.get(i).length());
            // dos.flush();

            // // write file
            // FileInputStream fis = new FileInputStream(this.files.get(i));

            // while ( (n = fis.read(buffer)) != -1 ) {
            // dos.write(buffer, 0, n);
            // dos.flush();
            // }//end while

            // }//end for

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



    public static void main(String[] args) throws IOException {

        // if (args.length != 2) {
        // System.err.println(
        // "Usage: java GlassClient <host name> <port number>");
        // System.exit(1);
        // }

        // String hostName = args[0];
        // int portNo = Integer.parseInt(args[1]);

        // String file1 = "C:\\Users\\Jordi\\nonDB\\glassServer\\images\\6\\20140811-Outside-Leaf2-RED  (1).jpg";
        // String file2 = "C:\\Users\\Jordi\\nonDB\\glassServer\\images\\6\\20140811-Outside-Leaf2-WHITE (1).jpg";
        // String lightingCondition = "OUTDOORS";

        // GlassClient gclient = new GlassClient(hostName, portNo, file1, file2, lightingCondition);
        // String finalResult = gclient.send();
        // System.out.println(finalResult);
        // gclient.closeSocket();

    }//end main()

}//end class GlassClient