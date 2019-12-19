package com.emerchat.views;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emerchat.R;
import com.emerchat.managers.ApManager;
import com.emerchat.receivers.WifiReceiver;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    TextView infoText;
    WifiManager wifiManager;
    WifiReceiver wifiReceiver;


    Button bSearch,b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoText = (TextView) findViewById(R.id.infoText);
        bSearch = (Button)findViewById(R.id.btSearch);
        b = (Button)findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);

            }
        });


        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Devices.class);
                startActivity(intent);
            }
        });


        //Initialize WiFi Connection

        wifiManager =(WifiManager) getSystemService(Context.WIFI_SERVICE);

        Log.e("MAIN_ACTIVITY","WIFI MANAGER SET");





        ApManager.isApOn(MainActivity.this); // check Ap state :boolean\
        Log.e("MAIN_ACTIVITY","CHECKING FOR AP");
        ApManager.configApState(MainActivity.this); // change Ap state :boolean
        Log.e("MAIN_ACTIVITY","CONFIGURING AP");



        //Checking WiFi For Availability

      /*  if(wifiManager.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "Wifi is Disabled , Enabling WIFI Services", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);

        }*/


        //Scan Broadcast Receiver
        //wifiReceiver = new WifiReceiver(wifiManager,infoText);
        //registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //wifiManager.startScan();
        //infoText.setText("Scaning For Available Devices ...");




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        wifiManager.setWifiEnabled(false);

        try
        {
        Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
        method.setAccessible(true);

        WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

        Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled",
                WifiConfiguration.class, boolean.class);
        method2.invoke(wifiManager, config, false);
    }
    catch (NoSuchMethodException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
}
