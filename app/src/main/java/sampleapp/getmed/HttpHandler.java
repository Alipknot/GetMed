package sampleapp.getmed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Nicolas on 23-04-2016.
 */
public class HttpHandler {
    Context mcontext;
    // fetch data


    public HttpHandler(Context mcontext) {
        this.mcontext = mcontext;
    }

    public String get(String geturl) {//metodo get para utilizar mysql
        ConnectivityManager connMgr = (ConnectivityManager)
                mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())

        {
            try {

                BufferedReader inputStream = null;

                URL myurl = new URL(geturl);

                URLConnection dc = myurl.openConnection();
                dc.setConnectTimeout(5000);
                dc.setReadTimeout(15000);

                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));

                // read the JSON results into a string
                String result = inputStream.readLine();
                return result;
            } catch (Exception e) {
                Log.d("error", e.getMessage());
                return "ERROR";
            }
        } else {
            // display error
            return "null";
        }


    }

}
