package com.example.tcpip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.util.Log;

public class TcpServerTest {
    private static final String TAG = "TcpServerTest";
    public static void main(String[] args) throws IOException {
        TcpServer tcpServer = TcpServer.create(3188);

        Log.i(TAG, "Press any key to exit ...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        Log.i(TAG, "Terminating ...");
        tcpServer.close();
    }
}
