package com.emerchat.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emerchat.R;
import com.emerchat.managers.ApManager;
import com.emerchat.receivers.WifiReceiver;

import java.util.ArrayList;

/**
 * Created by root on 12/3/17.
 */

public class Devices extends Activity{

    WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    WifiInfo wifiInfo;
    TextView info;
    ListView listView;
   ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<>();
    Button broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_list);

        Toast.makeText(this, "Wifi HotSpot Mode Disabled , Now using Wifi Mode", Toast.LENGTH_SHORT).show();

        ApManager.configApState(Devices.this);

        info = (TextView)findViewById(R.id.tvCountInfo);
        listView =(ListView)findViewById(R.id.lvDevices);
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);



        wifiManager =(WifiManager) getSystemService(Context.WIFI_SERVICE);

          if(wifiManager.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "Wifi is Disabled , Enabling WIFI Services", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);

        }


        //Scan Broadcast Receiver


        wifiReceiver = new WifiReceiver(wifiManager,info,list);
        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();


        listView.setAdapter(adapter);

        broadcast = (Button) findViewById(R.id.btnBroadcast);
        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Devices.this,ChatActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

}
