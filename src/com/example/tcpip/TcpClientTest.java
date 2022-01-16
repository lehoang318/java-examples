package com.example.tcpip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Locale;

import com.example.util.Log;

public class TcpClientTest {
    private static final String TAG = "TcpClientTest";
    private static final String REMOTE_ADDR = "127.0.0.1";
    private static final int REMOTE_PORT = 3188;

    public static void main(String[] args) {
        try (Socket socket = new Socket(REMOTE_ADDR, REMOTE_PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Log.i(TAG, String.format(Locale.US, "Received: '%s'", (reader.readLine())));

            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write("Nice to meet you!\n");
            writer.flush();

            Log.i(TAG, "Press any key to exit ...");
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            consoleReader.readLine();
        } catch (IOException e) {
            Log.e(
                TAG,
                String.format(
                    Locale.US,
                    "Could not create a Tcp Client which connects to %s/%d", REMOTE_ADDR, REMOTE_PORT
                ),
                e
            );
        }
    }
}
