package com.calak.jemmy.spiro.Data.Online.Socket;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    String msg;

    public EchoWebSocketListener(String value){
        this.msg = value;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
//        super.onOpen(webSocket, response);
        webSocket.send("!!!!");
//        webSocket.send("What's up ?");
//        webSocket.send(ByteString.decodeHex("deadbeef"));
//        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        output("onOpen:"+response.message());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
//        super.onMessage(webSocket, text);
        output("Receiving : " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        super.onMessage(webSocket, bytes);
        output("Receiving bytes : " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
//        super.onClosing(webSocket, code, reason);
//        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        output("Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        super.onFailure(webSocket, t, response);
        output("onFailureError : " + t.getMessage());
    }

    private void output(final String txt) {
        Log.d("WEBSOCKET", txt);
    }
}
