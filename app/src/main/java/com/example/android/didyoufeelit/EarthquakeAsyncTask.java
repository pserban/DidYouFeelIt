package com.example.android.didyoufeelit;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class EarthquakeAsyncTask extends AsyncTask<String, Void, Event> {

    private final static String LOG_TAG = EarthquakeAsyncTask.class.getSimpleName();

    private final WeakReference<MainActivity> activityReference;

    public EarthquakeAsyncTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected Event doInBackground(String... urls) {
        // Don't perform the request if there are no URLs,
        // or the first URL is null.
        if (urls.length < 1 || urls[0] == null || urls[0].isEmpty()) {
            return null;
        }

        Event earthquake = Utils.fetchEarthquakeData(urls[0]);

        return earthquake;
    }

    @Override
    protected  void onPostExecute(Event event) {
        super.onPostExecute(event);

        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (event == null) {
            return;
        }

        activity.updateUI(event);
    }
}
