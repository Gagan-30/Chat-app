package com.example.chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection
{
    //SERIALIZE ARRAY OF BYTES, SENT OVER A NETWORK

    private ConnectionThread connThread = new ConnectionThread();
    private Consumer<Serializable> onRecieveCallback;

    public NetworkConnection(Consumer<Serializable> onRecieveCallback)
    {
        this.onRecieveCallback = onRecieveCallback;
        connThread.setDaemon(true); //DAEMON ALWAYS RUNS IN BACKGROUND
    }

    public void startConnection() throws Exception
    {
        connThread.start(); // CREATES NEW THREAD TO EXECUTE run()
    }

    public void send(Serializable data) throws Exception
    {
        connThread.out.writeObject(data);//SENDING DATA
    }

    public void closeConnection() throws Exception
    {
        connThread.socket.close();
    }

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    //REQUIRED AS SOCKET DOES MULTIPLE TASKS SIMULTANEOUSLY
    private class ConnectionThread extends Thread
    {
        private  Socket socket;
        private ObjectOutputStream out;

        @Override
        public void run()
        {//IF SERVER IS TRUE, THEN GET PORT, ELSE DO NOT SINCE CLIENT DOES NOT NEED SERVER SIDE
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 //WAIT FOR CONNECTION ELSE ASSIGN NEW SOCKET
                 Socket socket = isServer() ? server.accept() : new Socket(getIP() , getPort());
                 //SEND OR RECEIVE OBJECTS
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
                 {
                    this.socket = socket;
                    this.out = out;
                    socket.setTcpNoDelay(true); //DISABLE BUFFER, SENDS MESSAGE QUICKER
                     while(true)
                     {
                         Serializable data = (Serializable) in.readObject();
                         onRecieveCallback.accept(data);
                     }
                 }
            catch (Exception e)
            {

            }
        }
    }
}
