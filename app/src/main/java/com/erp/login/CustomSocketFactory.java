package com.erp.login;

import android.content.Context;


import org.apache.http.conn.scheme.HostNameResolver;

import org.apache.http.conn.ssl.SSLSocketFactory;


import java.io.IOException;

import java.net.Socket;

import java.security.KeyManagementException;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;




public class CustomSocketFactory extends SSLSocketFactory {



    Context context;

     SSLContext Cur_SSL_Context = SSLContext.getInstance("TLSv1.2");



    public CustomSocketFactory ()
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException
    {
        super(null, null, null, null, null, (HostNameResolver) null);
        Cur_SSL_Context.init(null, new TrustManager[] {(TrustManager) new X509_Trust_Manager()}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException
    {

        System.out.println("SocketCreation"+enableTLSOnSocket(Cur_SSL_Context.getSocketFactory().createSocket(socket, host, port, autoClose)));
        return enableTLSOnSocket(Cur_SSL_Context.getSocketFactory().createSocket(socket, host, port, autoClose));


    }

    @Override
    public Socket createSocket() throws IOException
    {
        System.out.println("SocketCreationonlyIO"+enableTLSOnSocket(Cur_SSL_Context.getSocketFactory().createSocket()));

        return enableTLSOnSocket(Cur_SSL_Context.getSocketFactory().createSocket());
    }


    private Socket enableTLSOnSocket(Socket socket) {
        if (socket != null && (socket instanceof SSLSocket)) { // skip the fix if server doesn't provide there TLS version
         //  ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2"});

             ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.2"});

           // ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.1"});

        }
   //     System.out.println("isTLSServerEnabled((SSLSocket) socket    :   "+isTLSServerEnabled((SSLSocket) socket));

        System.out.println("ENABLING:"+socket);

        return socket;
    }

    private static boolean isTLSServerEnabled(SSLSocket sslSocket) {
        System.out.println("__prova__ :: " + sslSocket.getSupportedProtocols().toString());
        for (String protocol : sslSocket.getSupportedProtocols()) {
            if (protocol.equals("TLSv1.1") || protocol.equals("TLSv1.2")) {
                return true;
            }
        }
        return false;
    }
}

 class X509_Trust_Manager implements X509TrustManager
{

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}

