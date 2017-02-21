package com.sasha.cartest;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SortedService extends Service implements Runnable {
    private static String LOG_TAG = "SortedService";
    private static final String BROADCAST_CAR_CHANGE = "0";
    private static final String BROADCAST_PLAIN_CHANGE = "1";
    private static final String BROADCAST_SHIP_CHANGE = "2";

    private IBinder mBinder = new SortedBinder();
    private List<Car> lCar;
    private List<Plain> lPlain;
    private List<Ship> lShip;
    boolean isRunning, executing = false;
    ScheduledExecutorService scheduler;
    ScheduledFuture<?> handle;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
        isRunning = true;
        schedule(10);
    }

    private void schedule(int period) {
        if (handle != null) {
            handle.cancel(false);
            handle = null;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        handle = scheduler.scheduleWithFixedDelay(this, period, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.v(LOG_TAG, "in onDestroy");
    }

    @Override
    public void run() {
        int period = 1000;
        if (isRunning) {
            if (!executing) {
                lCar = new ArrayList<>();
                lPlain = new ArrayList<>();
                lShip = new ArrayList<>();
                new getSortedCarTask().execute(lCar);
                new getSortedPlainTask().execute(lPlain);
                new getSortedShipTask().execute(lShip);
            }
            schedule(period);
        }
    }

    public List<Car> getSortedCarList() {
        return lCar;
    }
    public List<Plain> getSortedPlainList() {
        return lPlain;
    }
    public List<Ship> getSortedShipList() {
        return lShip;
    }
    public class SortedBinder extends Binder {
        SortedService getService() {
            return SortedService.this;
        }
    }

    private class getSortedCarTask extends AsyncTask<List<Car>, Void, List<Car>> {

        @Override
        protected List doInBackground(List<Car>... params) {
            executing = true;
            lCar = new FirstSortedClass(params[0]).allSort();
            return lCar;
        }

        @Override
        protected void onPostExecute(List<Car> cars) {
            Intent intent = new Intent();
            intent.putExtra(BROADCAST_CAR_CHANGE, true);
            intent.setAction("SortedMainIntent");
            sendBroadcast(intent);
            executing = false;
        }
    }

    private class getSortedPlainTask extends AsyncTask<List<Plain>, Void, List<Plain>> {
        @Override
        protected List doInBackground(List<Plain>... params) {
            executing = true;
            lPlain = new SecondSortedClass(params[0]).allSort();
            return lPlain;
        }

        @Override
        protected void onPostExecute(List<Plain> plains) {
            Intent intent = new Intent();
            intent.putExtra(BROADCAST_PLAIN_CHANGE, true);
            intent.setAction("SortedMainIntent");
            sendBroadcast(intent);
            executing = false;
        }
    }
    private class getSortedShipTask extends AsyncTask<List<Ship>, Void, List<Ship>> {

        @Override
        protected List doInBackground(List<Ship>... params) {
            executing = true;
            lShip = new ThirdSortedClass(params[0]).allSort();
            return lShip;
        }

        @Override
        protected void onPostExecute(List<Ship> ships) {
            Intent intent = new Intent();
            intent.putExtra(BROADCAST_SHIP_CHANGE, true);
            intent.setAction("SortedMainIntent");
            sendBroadcast(intent);
            executing = false;
        }
    }
}