package com.example.emy.sunshine;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> AA;
    public MainActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            FetchWeatherTask task = new FetchWeatherTask();
            task.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecast = {
                "Today_Sunny_88/63",
                "Tomorrow_foggy_70/46",
                "Weds_Cloudy_72/63",
                "Thurs_Rainy_64/51",
                "Fri_Foggy_70/46",
                "Sat_Sunny_76/83"};

        ArrayList<String> weekforecast = new ArrayList<String>(Arrays.asList(forecast));

         AA = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekforecast);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setAdapter(AA);

        return rootView;


    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

             if(params.length ==0){
                 return null;
             }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastjsonstr = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=866de16d866f5a88ac0bd08119eaf596");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    stringBuffer.append(line + "");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }

                forecastjsonstr = stringBuffer.toString();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error Clasing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null){
                AA.clear();
                for(String dayforecast : result){
                    AA.add(dayforecast);
                }

            }
            super.onPostExecute(result);
        }
    }




}