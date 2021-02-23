package com.sloth.multidevicesync.pack;

import com.sloth.multidevicesync.utils.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;

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
public class PackReceiver {

    private DatagramSocket server = null;

    private DatagramPacket packet;

    private boolean listening;

    public PackReceiver() {
        try {
//            server = new DatagramSocket(PackManager.RECEIVE_PORT);
            MulticastSocket multicastSocket = new MulticastSocket(PackManager.RECEIVE_PORT);
            multicastSocket.setLoopbackMode(false);
            multicastSocket.joinGroup(InetAddress.getByName(PackManager.HOST));
            multicastSocket.setNetworkInterface(NetworkInterface.getByName("wlan0"));
            server = multicastSocket;
            server.setReuseAddress(true);
            server.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);

    }

    public void receive(ReceiveCallback callback){
        if(server == null || packet == null) {
            log("未初始化完成");
            return;
        }
        listening = true;
        try {
            while(listening){
                if(server == null || packet == null) {
                    log("开始监听，但未初始化完成");
                    break;
                }
                server.receive(packet);

                String s = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                if(callback != null){
                    callback.receive(s);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        if(server != null) {
            server.close();
        }
    }

    public void stop(){
        listening = false;
    }

    private void log(String info) {
        Log.d(this, info);
    }

    public interface ReceiveCallback {
        void receive(String data);
    }

}
