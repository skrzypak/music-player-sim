package com.polsl.player.server.sockets;

import com.polsl.player.tcp.MusicHeaderPackage;
import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.MusicBufferPackage;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player extends Socket implements Runnable  {

    private SourceDataLine soundLine = null;

    public Player() throws IOException {
        super(6666, "PLAYER");
    }

    @Override
    public void run() {
        while (true) {

            while (!isEstablished()) {
                Functions.threadSleep(1000);
            }

            while(isEstablished()) {
                try {

                    System.out.println("SERVER::"  + this.getName() + " -> WAITING FOR THE HEADER");

                    // TODO Header send over TCP
                    MusicHeaderPackage musicHeaderPackage = new MusicHeaderPackage();

                    soundLine = (SourceDataLine) AudioSystem.getLine(musicHeaderPackage.getInfo());
                    soundLine.open(musicHeaderPackage.getAudioFormat());
                    soundLine.start();

                    System.out.println("SERVER::"  + this.getName() + " -> MUSIC PLAY START");

                    while(true) {
                        MusicBufferPackage musicBufferPackage = (MusicBufferPackage)
                                Functions.fromString(getBufferedReader().readLine());
                        int nBytesRead = musicBufferPackage.getNumOfBytesRead();

                        if (nBytesRead >= 0) {
                            soundLine.write(musicBufferPackage.getArrBuff(), 0, nBytesRead);
                        } else {
                            break;
                        }
                    }

                } catch (IOException | ClassNotFoundException | LineUnavailableException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } finally {
                    soundLine.drain();
                    soundLine.close();
                    System.out.println("SERVER::"  + this.getName() + " -> MUSIC PLAY FINISHED");
                }

                Functions.threadSleep(1000);
            }

            System.out.println("SERVER::" + this.getName() + " -> CLIENT CONNECTION LOSE");

        }
    }
}
