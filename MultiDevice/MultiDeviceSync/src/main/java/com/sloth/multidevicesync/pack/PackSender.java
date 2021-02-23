package com.sloth.multidevicesync.pack;

import android.content.Context;

import com.sloth.multidevicesync.utils.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/29 17:58
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/29         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class PackSender {

    private DatagramSocket theSocket = null;
    private InetAddress server;

    public PackSender(Context context) {
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        int ipAddressInt = wifiMgr.getConnectionInfo().getIpAddress();
//        ipAddressInt = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
//                ? Integer.reverseBytes(ipAddressInt)
//                : ipAddressInt;
//        byte[] ipAddress = BigInteger.valueOf(ipAddressInt).toByteArray();

        try{
//            InetAddress myaddr = InetAddress.getByAddress(ipAddress);
//            String hostaddr = myaddr.getHostAddress();
//            log("获取address：" + hostaddr);
            server = InetAddress.getByName(PackManager.HOST);
            theSocket = new DatagramSocket();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void send(String data){

        if(server == null || theSocket == null){
            log("局域网广播发射器未初始化");
            return;
        }

        try {
            DatagramPacket theOutput = new DatagramPacket(data.getBytes(), data.length(), server, PackManager.PORT);
            theSocket.send(theOutput);
            log("发包成功：" + data + ",port:" + theOutput.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if(theSocket != null){
            theSocket.close();
        }
    }

    private void log(String info) {
        Log.d(this, info);
    }

}
