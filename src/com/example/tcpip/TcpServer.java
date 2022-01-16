package com.example.tcpip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.util.Log;

public class TcpServer implements Runnable {
    private static final String TAG = "TcpServer";

    ServerSocket mServerSocket;
    Thread mThread;
    AtomicBoolean mExitFlag;

    protected TcpServer(ServerSocket serverSocket) {
        mServerSocket = serverSocket;
        mThread = new Thread(this);
        mExitFlag = new AtomicBoolean(false);
        mThread.start();
    }

    public static TcpServer create(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            return new TcpServer(serverSocket);
        } catch (IOException e) {
            Log.e(TAG, "Could not create a Server Socket which listening at port " + port);
        }

        return null;
    }

    public void close() {
        Log.i(TAG, "Terminating ...");
        mExitFlag.set(true);
        if ((null != mThread) && mThread.isAlive()) {
            try {
                mThread.join();
            } catch (InterruptedException e) {}
        }

        if (null != mServerSocket) {
            try {
                mServerSocket.close();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void run() {
        if (null == mServerSocket) {
            Log.e(TAG, "Server Socket has not been initialized!");
            return;
        }

        while (!mExitFlag.get()) {
            try {
                Socket remoteSocket = mServerSocket.accept();
                Writer writer = new OutputStreamWriter(remoteSocket.getOutputStream(), "UTF-8");
                writer.write("Hello :D\n");
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));
                Log.i(TAG, String.format("Received: '%s'", reader.readLine()));
                reader.close();
                writer.close();remoteSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Something was wrong!", e);
                break;
            }
        }
    }

}