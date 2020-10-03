package com.arkapp.carparknaviagation.utility.maps.route;

import android.os.AsyncTask;
import android.util.Log;

import com.arkapp.carparknaviagation.utility.HttpConnection;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;

/**
 * Created by Abdul Rehman on 12/24/2018.
 */
public class GetRouteTask extends AsyncTask<String, Void, String> {
    private int colourForPathPlot;
    private HomePageListener listener;

    public GetRouteTask(int color, HomePageListener listener) {
        colourForPathPlot = color;
        this.listener = listener;
    }
    //plotting the line for the tracking

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            HttpConnection http = new HttpConnection();
            data = http.readUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new ParseRouteTask(colourForPathPlot, listener).execute(result);
    }
}
