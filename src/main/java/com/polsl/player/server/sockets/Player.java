package com.polsl.player.server.sockets;

import com.polsl.player.serializable.SerializableAudioFormat;
import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.SoundBufferPackage;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

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

                    System.out.println("SERVER::"  + this.getName() + " -> WAITING FOR THE AUDIO FORMAT");

                    InputStream inputStream = this.getSocket().getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    SerializableAudioFormat serializableAudioFormat = (SerializableAudioFormat) objectInputStream.readObject();

                    AudioFormat audioFormat = serializableAudioFormat.getAf();
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                    System.out.println("SERVER::"  + this.getName() + " -> GETTING AUDIO FORMAT");

                    soundLine = (SourceDataLine) AudioSystem.getLine(info);
                    soundLine.open(audioFormat);

                    soundLine.start();

                    System.out.println("SERVER::"  + this.getName() + " -> START MUSIC PLAY");

                    while(true) {
                        SoundBufferPackage soundBufferPackage = (SoundBufferPackage)
                                Functions.fromString(getBufferedReader().readLine());
                        int nBytesRead = soundBufferPackage.getNumOfBytesRead();

                        if (nBytesRead >= 0) {
                            soundLine.write(soundBufferPackage.getArrBuff(), 0, nBytesRead);
                            System.out.println("SERVER::"  + this.getName() + " -> BUFFER OF SOUND HAS WRITTEN");
                        } else {
                            break;
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
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
