package com.example.chatapp;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection
{
    private int port;

    public Server(int i, Consumer<Serializable> onRecieveCallback)
    {
        super(onRecieveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer()
    {
        return true;
    }

    @Override
    protected String getIP()
    {
        return null;
    }

    @Override
    protected int getPort()
    {
        return port;
    }
}
