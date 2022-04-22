package com.polsl.player;

import com.polsl.player.tcp.MusicBufferPackage;
import com.polsl.player.tcp.MusicHeaderPackage;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException {

        SourceDataLine soundLine = null;
        int nBytesRead = 0;

        ServerSocket ss = new ServerSocket(6666);
        Socket s = ss.accept();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        MusicHeaderPackage musicHeaderPackage = null;

        System.out.println("SERVER://Client connect");

        try {
            musicHeaderPackage = new MusicHeaderPackage();

            soundLine = (SourceDataLine) AudioSystem.getLine(musicHeaderPackage.info);
            soundLine.open(musicHeaderPackage.audioFormat);
            soundLine.start();

            while(true) {
                MusicBufferPackage musicBufferPackage = (MusicBufferPackage)fromString(bf.readLine());
                nBytesRead = musicBufferPackage.getNumOfBytesRead();

                if (nBytesRead >= 0) {
                    soundLine.write(musicBufferPackage.getArrBuff(), 0, nBytesRead);
                    System.out.println("SERVER://" + nBytesRead);
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("SERVER://Could not play music!");
        }

        soundLine.drain();
        soundLine.close();

        s.close();
        System.out.println("SERVER://Music end. Connection with client close.");
    }

    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
}
