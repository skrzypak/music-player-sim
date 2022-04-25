package com.polsl.player.server.sockets;

import com.polsl.player.server.functions.Functions;
import com.polsl.player.tcp.ControllerEnum;

import java.io.IOException;

public class Controller extends Socket implements Runnable {
    public Controller() throws IOException {
        super(6667, "CONTROLLER");
    }

    @Override
    public void run() {
        while(true) {

            while (!this.isEstablished()) {
                Functions.threadSleep(1000);
            }

            while (this.isEstablished()) {
                try {
                    ControllerEnum action = (ControllerEnum) Functions.fromString(getBufferedReader().readLine());
                    System.out.println("SERVER::" + this.getName() + " -> " + action);

                    // TODO...
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Functions.threadSleep(1000);
            }

            System.out.println("SERVER::" + this.getName() + " -> CLIENT CONNECTION LOSE");

        }
    }
}
