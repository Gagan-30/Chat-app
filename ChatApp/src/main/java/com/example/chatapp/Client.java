package com.example.chatapp;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection
{
    private String ip;
    private int port;

    public Client(String s, int i, Consumer<Serializable> onRecieveCallback)
    {
        super(onRecieveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer()
    {
        return false;
    }

    @Override
    protected String getIP()
    {
        return ip;
    }

    @Override
    protected int getPort()
    {
        return port;
    }
}
