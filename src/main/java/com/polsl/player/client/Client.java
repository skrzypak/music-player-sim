package com.polsl.player.client;

import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.ControllerEnum;
import com.polsl.player.tcp.MusicBufferPackage;
import com.polsl.player.tcp.MusicHeaderPackage;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket sPlayer = new Socket("localhost", 6666);
        PrintWriter prPlayer = new PrintWriter(sPlayer.getOutputStream());

        Socket sController = new Socket("localhost", 6667);
        PrintWriter prController = new PrintWriter(sController.getOutputStream());

        try {
            MusicHeaderPackage musicHeaderPackage = new MusicHeaderPackage();

            int bitRead = 0;
            byte[] buff = new byte[MusicBufferPackage.BUFFER_SIZE];

            // Buffering music to server

            prController.println(Functions.toString(ControllerEnum.START));
            prController.flush();

            while (bitRead != -1) {
                bitRead = musicHeaderPackage.getAudioInputStream().read(buff, 0, buff.length);
                if (bitRead >= 0) {
                    MusicBufferPackage msg = new MusicBufferPackage(buff, bitRead);
                    prPlayer.println(Functions.toString(msg));
                    prPlayer.flush();
                }

                // TODO For test only
                if(Instant.now().getEpochSecond() % 3 == 0) {
                    prController.println(Functions.toString(ControllerEnum.PAUSE));
                    prController.flush();
                } else {
                    prController.println(Functions.toString(ControllerEnum.RESUME));
                    prController.flush();
                }

                //TODO Delay
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("CLIENT://Could not send music package!");
        }

    }
}