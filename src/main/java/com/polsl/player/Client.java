package com.polsl.player;

import com.polsl.player.tcp.MusicBufferPackage;
import com.polsl.player.tcp.MusicHeaderPackage;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 6666);
        PrintWriter pr = new PrintWriter(s.getOutputStream());

        try {
            MusicHeaderPackage musicHeaderPackage = new MusicHeaderPackage();

            int bitRead = 0;
            byte[] buff = new byte[MusicBufferPackage.BUFFER_SIZE];

            while (bitRead != -1) {
                bitRead = musicHeaderPackage.audioInputStream.read(buff, 0, buff.length);
                if (bitRead >= 0) {
                    MusicBufferPackage msg = new MusicBufferPackage(buff, bitRead);
                    pr.println(toString(msg));
                    pr.flush();
                }
                System.out.println("CLIENT://" + bitRead);

                //TODO Delay
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("CLIENT://Could not send music package!");
        }

    }

    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}

//        SourceDataLine soundLine = null;
//        int BUFFER_SIZE = 64*1024;
//
//        try {
//            File soundFile = new File("C:\\Users\\Macintosh\\IdeaProjects\\ServerSidePlayer\\assets\\wav\\file_example_WAV_10MG.wav");
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
//            AudioFormat audioFormat = audioInputStream.getFormat();
//            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
//
//            soundLine = (SourceDataLine) AudioSystem.getLine(info);
//            soundLine.open(audioFormat);
//            soundLine.start();
//
//            int nBytesRead = 0;
//            byte[] sampledData = new byte[BUFFER_SIZE];
//
//            while (nBytesRead != -1) {
//                nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
//                if (nBytesRead >= 0) {
//                    // Message msg = new Message('P', sampledData, nBytesRead);
//                    //pr.println(toString(msg));
//                    //pr.flush();
//                    soundLine.write(sampledData, 0, nBytesRead);
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Could not start music!");
//        }
//
//
//        soundLine.drain();
//        soundLine.close();
