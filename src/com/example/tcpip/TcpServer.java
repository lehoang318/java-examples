package com.example.tcpip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.util.Log;

public class TcpServer implements Runnable {
    private static final String TAG = "TcpServer";
    private static final int DEFAULT_TIMEOUT_MS = 1000;

    ServerSocket mServerSocket;
    Thread mThread;
    AtomicBoolean mExitFlag;
    int mTimeoutMs;


    protected TcpServer(ServerSocket serverSocket, int timeoutMs) {
        mServerSocket = serverSocket;
        mThread = new Thread(this);
        mExitFlag = new AtomicBoolean(false);
        mTimeoutMs = timeoutMs;
        mThread.start();
    }

    public static TcpServer create(int port) {
        return create(port, DEFAULT_TIMEOUT_MS);
    }

    public static TcpServer create(int port, int timeoutMs) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeoutMs);
            return new TcpServer(serverSocket, timeoutMs);
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
            Socket remoteSocket;
            Log.i(TAG, "Waiting for client ...");
            try {
                remoteSocket = mServerSocket.accept();
            } catch (SocketTimeoutException e) {
                // Ignore
                continue;
            } catch (IOException e) {
                Log.e(TAG, "Something was wrong!", e);
                break;
            }

            if (null == remoteSocket) {
                continue;
            }

            try {
                remoteSocket.setSoTimeout(mTimeoutMs);
                Writer writer = new OutputStreamWriter(remoteSocket.getOutputStream(), "UTF-8");
                writer.write("Hello :D\n");
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));
                while (!mExitFlag.get()) {
                    try {
                        Log.i(TAG, String.format("Received: '%s'", reader.readLine()));
                        break;
                    } catch (SocketTimeoutException e) {
                        Log.i(TAG, "Rx timeout!");
                        continue;
                    }
                }

                reader.close();
                writer.close();
                remoteSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Something was wrong!", e);
                break;
            }
        }
    }

}