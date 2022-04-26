package com.polsl.player.client;

import com.polsl.player.client.example.AudioExample;
import com.polsl.player.serializable.SerializableAudioFormat;
import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.ControllerEnum;
import com.polsl.player.tcp.SoundBufferPackage;

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
            AudioExample audioExample = new AudioExample();
            SerializableAudioFormat saf = new SerializableAudioFormat(audioExample.getAudioFormat());

            // Sending audio format to server
            OutputStream outputStream = sPlayer.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(saf);

            // Buffering music to server
            int bitRead = 0;
            byte[] buff = new byte[SoundBufferPackage.BUFFER_SIZE];

            prController.println(Functions.toString(ControllerEnum.START));
            prController.flush();

            while (bitRead != -1) {
                bitRead = audioExample.getAudioInputStream().read(buff, 0, buff.length);
                if (bitRead >= 0) {
                    SoundBufferPackage msg = new SoundBufferPackage(buff, bitRead);
                    prPlayer.println(Functions.toString(msg));
                    prPlayer.flush();
                }

                // TODO For test controller only
                if(Instant.now().getEpochSecond() % 3 == 0) {
                    prController.println(Functions.toString(ControllerEnum.PAUSE));
                    prController.flush();
                } else {
                    prController.println(Functions.toString(ControllerEnum.RESUME));
                    prController.flush();
                }

                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("CLIENT://Could not send music package!");
        }

    }
}