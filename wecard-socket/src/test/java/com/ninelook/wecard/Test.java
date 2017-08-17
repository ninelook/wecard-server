package com.ninelook.wecard;

import com.ninelook.wecard.library.net.WebSocketClient;

/**
 * Created by Simon on 16/9/7.
 */
public class Test {
    public static void main(String args[]) {
        WebSocketClient webSocketClient = new WebSocketClient(1001, "ws://192.168.31.195:8087/websocket");
        try {
            webSocketClient.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
