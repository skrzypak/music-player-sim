package com.polsl.player.server.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polsl.player.serializable.SerializableAudioFormat;
import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.SoundBufferPackage;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Base64;

public class Player extends Socket implements Runnable  {

    private SourceDataLine soundLine = null;
    ObjectMapper mapper = new ObjectMapper();

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

                    String headerJson = convertFromBase64(getBufferedReader().readLine());
                    SerializableAudioFormat serializableAudioFormat = mapper.readValue(headerJson, SerializableAudioFormat.class);
                    AudioFormat audioFormat = serializableAudioFormat.convertToAudioFormat();

                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                    System.out.println("SERVER::"  + this.getName() + " -> GETTING AUDIO FORMAT");

                    soundLine = (SourceDataLine) AudioSystem.getLine(info);
                    soundLine.open(audioFormat);

                    soundLine.start();

                    System.out.println("SERVER::"  + this.getName() + " -> START MUSIC PLAY");

                    while(true) {
                        String line = getBufferedReader().readLine();
                        if (line != null) {
                            String dataJson = convertFromBase64(line);
                            SoundBufferPackage soundBufferPackage = mapper.readValue(dataJson, SoundBufferPackage.class);

                            if (soundBufferPackage.isSongChange()) {
                                break;
                            }

                            int nBytesRead = soundBufferPackage.getNumOfBytesRead();

                            if (nBytesRead >= 0) {
                                soundLine.write(soundBufferPackage.getArrBuff(), 0, nBytesRead);
                                System.out.println("SERVER::"  + this.getName() + " -> BUFFER OF SOUND HAS WRITTEN");
                            } else {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } finally {
                    if(this.soundLine != null) {
                        soundLine.drain();
                        soundLine.close();
                    }
                    System.out.println("SERVER::"  + this.getName() + " -> MUSIC PLAY FINISHED");
                }

                Functions.threadSleep(1000);
            }

            System.out.println("SERVER::" + this.getName() + " -> CLIENT CONNECTION LOSE");

        }
    }

    private String convertFromBase64(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
