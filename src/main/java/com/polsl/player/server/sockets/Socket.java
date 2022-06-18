package com.polsl.player.server.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public abstract class Socket implements ISocket {
    private final ServerSocket ss;
    private java.net.Socket s = null;
    private InputStreamReader in = null;
    private BufferedReader bf = null;
    private final String name;

    public Socket(int port, String name) throws IOException {
        this.ss = new ServerSocket(port);
        this.name = name;
    }

    public Boolean establish() {
        try {
            this.s = this.ss.accept();
            this.in = new InputStreamReader(this.s.getInputStream());
            this.bf = new BufferedReader(this.in);

            System.out.println("SERVER::" + this.name + " -> CLIENT CONNECTION ESTABLISHED");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SERVER::" + this.name + " -> ESTABLISH CRITICAL ERROR");
            return false;
        }

        return true;
    }


    public Boolean isEstablished() {
        if(this.s != null && this.in != null && this.bf != null) return true;
        System.out.println("SERVER::" + this.name + " -> WAITING FOR CLIENT CONNECTION");
        return this.establish();
    }

    public ServerSocket getServerSocket()
    {
        return this.ss;
    }

    public java.net.Socket getSocket()
    {
        if(this.s != null) {
            return this.s;
        }

        throw new RuntimeException("SERVER::" + this.name + " -> NOT DEFINE SOCKET");
    }

    public InputStreamReader getInputStreamReader()
    {
        if(this.in != null) {
            return this.in;
        }

        throw new RuntimeException("SERVER::" + this.name + " -> NOT DEFINE STREAM");
    }

    public BufferedReader getBufferedReader()
    {
        if(this.bf != null) {
            return this.bf;
        }

        throw new RuntimeException("SERVER::" + this.name + " -> NOT DEFINE BUFFER");
    }

    public String getName() {
        return this.name;
    }

}
