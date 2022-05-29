package com.example.chessclient.Threads;

import com.example.chessclient.Client;

public class InviteListener implements Runnable{

    boolean isStopped = false;
    Client client;

    private final static double SECONDS_PER_FRAME = 1 / 30.0;

    public InviteListener(Client client) {
        this.client = client;
    }


    @Override
    public void run() {
        long prevFrameNano = System.nanoTime();

        while (!isStopped) {

            if ((System.nanoTime() - prevFrameNano) / 1000000000.0 > SECONDS_PER_FRAME) {
                client.setLatestCommand("boardStatus");
                prevFrameNano = System.nanoTime();

            }


            String response = client.getLatestResponse();

        }



    }
}
