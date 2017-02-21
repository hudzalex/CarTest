package com.sasha.cartest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SortedService mBoundService;
    private static final String BROADCAST_CAR_CHANGE="0";
    private static final String BROADCAST_PLAIN_CHANGE = "1";
    private static final String BROADCAST_SHIP_CHANGE = "2";
    boolean mServiceBound = false;
    private ListView resultListView;
    private List<Car> lCar;
    private List<Plain> lPlain;
    private List<Ship> lShip;
    private  List<String> mainArray=new ArrayList<>();
    SortedListAdapter  adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultListView=(ListView)findViewById(R.id.result_list_view);
        IntentFilter timerFilter = new IntentFilter("SortedMainIntent"); // Filter that gets stuff from the service
        registerReceiver(myReceiver, timerFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SortedService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SortedService.SortedBinder myBinder = (SortedService.SortedBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;

        }
    };

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle extras = intent.getExtras();
            if(extras.getBoolean(BROADCAST_CAR_CHANGE)){
                lCar=mBoundService.getSortedCarList();
                for (Car cars : lCar) {
                    mainArray.add(cars.getName());
                }
            }else if(extras.getBoolean(BROADCAST_PLAIN_CHANGE)){
                lPlain=mBoundService.getSortedPlainList();
                for (Plain plains : lPlain) {
                    mainArray.add(plains.getName());
                }

            }else if(extras.getBoolean(BROADCAST_SHIP_CHANGE)){
                lShip=mBoundService.getSortedShipList();
                for (Ship ships : lShip) {
                    mainArray.add(ships.getName());
                }
            }
            updateAdapter(mainArray);
        }
    };
    private class SortedListAdapter extends ArrayAdapter<String> {
        List <String> mItems;
        TextView txtView;
        Context context;
        int layoutResourceId;
        public SortedListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context=context;
            this.layoutResourceId=resource;
            mItems=objects;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView=convertView;
            LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
            itemView=inflater.inflate(layoutResourceId, parent, false);
            txtView = (TextView)itemView.findViewById(android.R.id.text1);
            txtView.setText(mItems.get(position));
            return itemView;
        }
        public void setItems(List <String> items) {
            mItems = items;
        }

    }
    private void updateAdapter(List <String> array){
        if(adapter==null){
            adapter = new SortedListAdapter(MainActivity.this,
            android.R.layout.simple_list_item_1,array);

            resultListView.setAdapter(adapter);}
        else{
            adapter.setItems(array);
            adapter.notifyDataSetChanged();
        }
    }
}
