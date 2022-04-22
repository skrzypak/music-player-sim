package com.polsl.player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main extends Thread {

    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException {
        Main thread = new Main();
        thread.start();

        Client.main(args);
    }

    public void run() {
        try {
            Server.main(null);
        } catch (IOException | ClassNotFoundException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
