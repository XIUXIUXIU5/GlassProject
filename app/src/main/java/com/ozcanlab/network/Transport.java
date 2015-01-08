/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.ozcanlab.rdt.R;
import com.ozcanlab.thrift.RdtReader;
import com.ozcanlab.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import static com.ozcanlab.utils.Constants.*;

/**
 *
 * @author Romain
 */
public final class Transport {

    public RdtReader.Iface client = null;
    private TTransport transport = null;
    private static Transport instance = null;
    private static Context ctx = null;
    private static String path = null;

    public static Context getCtx() {
        return ctx;
    }

    public void establishConnexion() throws TTransportException, IOException, NetworkErrorException {
        if (path == null) {
            path = FileUtils.copyRawToInternal(Transport.ctx, "cert.bks", R.raw.cert);
        }
        File f = new File(path);
        if (f.exists()) {
            if (InetAddress.getByName(ADDRESS).isReachable(5000)) {
                //try {
                    TSSLTransportFactory.TSSLTransportParameters params = new TSSLTransportFactory.TSSLTransportParameters();
                    params.setTrustStore(f.getAbsolutePath(), "client", "X509", "BKS");
                    this.transport = TSSLTransportFactory.getClientSocket(ADDRESS, PORT, 20000, params); // , 20000, params
                    if (!this.transport.isOpen()) {
                        this.transport.open();
                    }
                //} catch (TTransportException ex) {
                //    Toast.makeText(ctx, "Could not connect to the server", Toast.LENGTH_LONG).show();
                //    Log.d(APP_TAG, "Ex = ", ex);
                //}
            } else {
                Log.e(APP_TAG, "Can't reach : " + ADDRESS);
                throw new NetworkErrorException("Failed to connect to devpc02");
            }
        } else {
            Log.e(APP_TAG, "Can't find file : " + f.getName());
        }
    }

    public synchronized Transport connect() throws TTransportException, IOException, NetworkErrorException {
        if (this.transport == null) {
            this.establishConnexion();
        }
        return this;
    }

    public synchronized TTransport getTransport() {
        return this.transport;
    }

    /**
     * Use it to connect to the server and do function call. By default, it will
     * reconnect the client using a cookie.
     *
     * @return the client
     */
    public synchronized RdtReader.Iface getClient() {
        if (this.client == null) {
            this.client = new RdtReader.Client(new TBinaryProtocol(this.getTransport()));
        }
        return this.client;
    }

    private Transport() {
    }

    public static Transport getInstance(Context ctx) {
        Transport.ctx = ctx;
        if (instance == null) {
            instance = new Transport();
        }
        return instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
        // that'll teach 'em
    }

    public synchronized void closeConnexion() {
        try {
            this.transport.close();
            this.transport = null;
            this.client = null;
        } catch (NullPointerException ex) {
        }
    }
}