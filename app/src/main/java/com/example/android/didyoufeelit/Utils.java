package com.example.android.didyoufeelit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static Event fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error closing input stream", ex);
        }

        Event earthquake = extractFeatureFromJSON(jsonResponse);

        return earthquake;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "Error with creating URL: ", ex);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream intputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                intputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(intputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", ex);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (intputStream != null) {
                intputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Event extractFeatureFromJSON(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        try {
            JSONObject baseJSONResponse = new JSONObject(earthquakeJSON);
            JSONArray featureArray = baseJSONResponse.getJSONArray("features");

            if (featureArray.length() > 0) {
                JSONObject firstFeature = featureArray.getJSONObject(0);
                JSONObject properties = firstFeature.getJSONObject("properties");

                String title = properties.getString("title");
                String numberOfPeople = properties.getString("felt");
                String perceivedStrength = properties.getString("cdi");

                return new Event(title, numberOfPeople, perceivedStrength);
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", ex);
        }
        return null;
    }
}
