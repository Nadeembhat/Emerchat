package com.emerchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tahir on 12/3/17.
 */

public class WifiReceiver extends BroadcastReceiver {

   static List<ScanResult> devicesList;
    List list;


    StringBuilder sb;
    ListView listView;
    TextView info;


    static  WifiManager wifiManager;



    static public List<ScanResult> getList()
    {
        return devicesList;
    }

    public WifiReceiver(WifiManager wifiManagar,TextView info,List list)
    {
        this.wifiManager = wifiManagar;
        this.info = info;
        this.list = list;

    }


    @Override
    public void onReceive(Context context, Intent intent) {



        sb = new StringBuilder();
        devicesList = wifiManager.getScanResults();
        sb.append(devicesList.size());

        info.setText(sb);




        for(int i=0;i< devicesList.size();i++)
        {

            list.add(devicesList.get(i).SSID);

            //sb.append(new Integer(i+1).toString()+". " );
            //sb.append(devicesList.get(i).toString());

            //sb.append("\n\n");


        }

        //infoText.setText(sb);



    }



}
