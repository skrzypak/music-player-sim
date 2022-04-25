package com.polsl.player;

import com.polsl.player.client.Client;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main extends Thread {

    public static void main(String[] args) throws IOException {
        Main thread = new Main();
        thread.start();

        Client.main(args);
    }

    public void run() {
        try {
            com.polsl.player.server.Main.main(null);
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
