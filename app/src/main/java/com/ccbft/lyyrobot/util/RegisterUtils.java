package com.ccbft.lyyrobot.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;


import com.ccbft.lyyrobot.MyApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class RegisterUtils {
    private  static ConnectivityManager connectivityManager= (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    private static TelephonyManager telephonyManager= (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
    private static NetworkInfo info=connectivityManager.getActiveNetworkInfo();

    /**
     * 判断网络是否已经连接
     * @return 如果已经连接则返回true，否则返回false
     */
    public static boolean isNetworkAvailable(){
        if(info==null||!connectivityManager.getBackgroundDataSetting()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 获取网络连接的类型
     * @return
     */
    public static String getNetworkType(){
        int netType=info.getType();
        int netSubType=info.getSubtype();
        if(netType==connectivityManager.TYPE_WIFI){
            return "wifi";
        }else if(netType==connectivityManager.TYPE_MOBILE){
            return "mobile";
        }else {
            return "未知的网络连接类型";
        }
    }

    private static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取用户的IP地址
     * @return
     */
    public static String getIpAddress(){
        if(isNetworkAvailable()){//如果网络连接可用
            String networkType = getNetworkType();
            if(networkType.equals("wifi")){
                try {

                    WifiManager wifiManager = (WifiManager) MyApplication.getContext()
                            .getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int i = wifiInfo.getIpAddress();
                    return int2ip(i);
                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }else if(networkType.equals("mobile")){
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                return "未知的IP地址";

            }else{
                return "未知的IP地址";
            }

        }else{
            return "未知的IP地址";
        }
    }

    /**
     * 扫描各个网络接口获取MAC地址
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

}
