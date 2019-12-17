package com.erp.login;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;



/**
 * Created by Eswar on 23/07/16.
 */
public class HttpServiceImpl {

    Context context;
    ObjectMapper mapper;
    private HttpClient httpClient;
    HttpsURLConnection httpsURLConnection;
    SSLParameters sslParameters;
    private Object GooglePlay;

    public HttpServiceImpl() {


        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                    true);
        }
    }

    private String processEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                entity.getContent()));
        String line, result = "";
        while ((line = br.readLine()) != null)
            result += line;
        return result;
    }

    HttpContext httpContext;

    private String readPostResponse(String url, HttpEntity entity) throws Exception {

        String res = null;
        final SSLSocketFactory internalSSLSocketFactory;



        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);


        SchemeRegistry schReg = new SchemeRegistry();




        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", new CustomSocketFactory(), 443));



        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        httpClient = new DefaultHttpClient(conMgr, params);

        CookieStore cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60000);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 60000);

        HttpPost post = new HttpPost(url);
        HttpResponse response = null;

        post.setEntity(entity);
        response = httpClient.execute(post, httpContext);
        res = processEntity(response.getEntity());

        //provides String as response
        return res;
    }


    private String readJsonPostResponse(String url, String json) throws Exception {
        httpClient = new DefaultHttpClient();
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 600000);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 600000);
        HttpPost request = new HttpPost(url);
        String res = null;
        StringEntity entity = new StringEntity(json.toString());
        entity.setContentType("application/json;charset=UTF-8");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
        request.setHeader("Accept", "application/json");
        request.setEntity(entity);
        HttpResponse response = null;


        response = httpClient.execute(request);
        res = processEntity(response.getEntity());
        return res;
    }

    private String readResponseStream(String url) {
        String res = null;

        httpClient = new DefaultHttpClient();
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60000);
        HttpResponse response = null;
        try {
            System.out.println(url);
            response = httpClient.execute(new HttpGet(url));
            res = processEntity(response.getEntity());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public String verifyLogin(String user, String password, String apmcId, Context context) throws Exception {

        // context=this;


        String myurl = "enter your url";       //EXample  "https://www.icicibank.com/";




        List<NameValuePair> map = new ArrayList<>(3);

        map.add(new BasicNameValuePair("loginId", user));
        map.add(new BasicNameValuePair("password", password));
        map.add(new BasicNameValuePair("apmcId", apmcId));

        String response = readPostResponse(myurl,
                new UrlEncodedFormEntity(map));

        System.out.println("RESPONSE   " + response);

        return response;


    }


}







    /*fun Context.installTls12() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e:GooglePlayServicesRepairableException) {
            // Prompt the user to install/update/enable Google Play services.
            GoogleApiAvailability.getInstance()
                    .showErrorNotification(this, e.connectionStatusCode)
        } catch (e:GooglePlayServicesNotAvailableException) {
            // Indicates a non-recoverable error: let the user know.
        }
    }
*/




