package com.polsl.player.server.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public interface ISocket {
    ServerSocket getServerSocket();
    Socket getSocket();
    InputStreamReader getInputStreamReader();
    BufferedReader getBufferedReader();
}
