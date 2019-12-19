package com.emerchat.views;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emerchat.R;
import com.emerchat.managers.ApManager;
import com.emerchat.receivers.WifiReceiver;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by root on 12/3/17.
 */

public class SendMessage extends Activity {

    TextView message;
    Button btnSend;
    List<ScanResult> devices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_messsage);


        message = (TextView) findViewById(R.id.etSendMessage);
        btnSend = (Button) findViewById(R.id.btSendMessage);

        devices = WifiReceiver.getList();


      btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<devices.size();i++)
                {
                        ApManager.connectToHotspot(devices.get(i).SSID,SendMessage.this);
                    Log.e("SendMessage","Connected to " + devices.get(i).SSID.toString());


                }


            }
        });

    }







}
