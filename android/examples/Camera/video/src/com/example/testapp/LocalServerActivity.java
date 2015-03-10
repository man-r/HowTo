package com.example.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

public class LocalServerActivity extends Activity
{
    public static String SOCKET_ADDRESS = "your.local.socket.address";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        String message = "hello";
        try {
            LocalServerSocket server = new LocalServerSocket(SOCKET_ADDRESS);
            LocalSocket sender = new LocalSocket();

            sender.connect(server.getLocalSocketAddress());
            sender.getOutputStream().write(message.getBytes());
            sender.getOutputStream().close();

            LocalSocket receiver = server.accept();
            if (receiver != null) {
                InputStream input = receiver.getInputStream();
                
                // simply for java.util.ArrayList
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = input.read(data, 0, data.length)) != -1) {
                  buffer.write(data, 0, nRead);
                }

                buffer.flush();

                byte[] bytes = buffer.toByteArray();
                
                
                Toast.makeText(this, new String(bytes, 0, bytes.length), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "" + size, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }
}