package com.emerchat.managers;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.List;

/**
 * Created by root on 12/3/17.
 */

public class ApManager {


    public static String getIpAddress(Context context)
    {
        DhcpInfo dhcpInfo;
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo =  wifimanager.getConnectionInfo();
        dhcpInfo = wifimanager.getDhcpInfo();
        int i = dhcpInfo.ipAddress;



        String ip =(i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
        Log.e("IPADD::",ip);

        return ip;

    }

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {

        WifiManager wifimanager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if(isApOn(context)) {
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            Log.e("APMANAGER","WIFI AP SET");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // this method help to connect hotspot programmatically
    public static boolean connectToHotspot(String netSSID, Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wifiConf = new WifiConfiguration();
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        if (wifiManager.isWifiEnabled()) {
            for (ScanResult result : scanResultList) {
                if (result.SSID.equals(netSSID)) {

                    //removeWifiNetwork(result.SSID, wifiManager);
                    String mode = getSecurityMode(result);// get mode of hospot

                    if (mode.equalsIgnoreCase("OPEN")) {
                        wifiConf.SSID = "\"" + netSSID + "\"";
                        wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        int res = wifiManager.addNetwork(wifiConf);
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(res, true);
                        wifiManager.reconnect();
                        wifiManager.setWifiEnabled(true);
                        return true;
                    } //else if (mode.equalsIgnoreCase("WEP")) {
                        //wifiConf.SSID = "\"" + netSSID + "\"";
                        //wifiConf.wepKeys[0] = "\"" + netPass + "\"";
                        //wifiConf.wepTxKeyIndex = 0;
                        //wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        //wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        //int res = wifiManager.addNetwork(wifiConf);
                        //wifiManager.disconnect();
                        //wifiManager.enableNetwork(res, true);
                        //wifiManager.reconnect();
                        //wifiManager.setWifiEnabled(true);
                        //return true;
                    //}
                else {
                        wifiConf.SSID = "\"" + netSSID + "\"";
                        //wifiConf.preSharedKey = "\"" + netPass + "\"";
                        wifiConf.hiddenSSID = true;
                        wifiConf.status = WifiConfiguration.Status.ENABLED;
                        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        int res = wifiManager.addNetwork(wifiConf);
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(res, true);
                        wifiManager.reconnect();
                        wifiManager.saveConfiguration();
                        wifiManager.setWifiEnabled(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // get Security Mode
    public static String getSecurityMode(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] modes = {"WPA", "EAP", "WEP"};
        for (int i = modes.length - 1; i >= 0; i--) {
            if (cap.contains(modes[i])) {
                return modes[i];
            }
        }
        return "OPEN";
    }
}
