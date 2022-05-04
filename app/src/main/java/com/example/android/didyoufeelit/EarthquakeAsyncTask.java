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
    protected Event doInBackground(String... strings) {
        Event earthquake = Utils.fetchEarthquakeData(strings[0]);

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
