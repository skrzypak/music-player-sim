package com.polsl.player.server;

import com.polsl.player.server.sockets.Player;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Thread {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, InterruptedException {
        Player player = new Player();

        List<Runnable> runners = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        runners.add(player);

        for (Runnable runner : runners) {
            threads.add(new Thread(runner));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

}
