package com.example.chessclient.Threads;

import com.example.chessclient.Client;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.SceneManager;
import javafx.application.Platform;

import java.io.IOException;

public class QueueListener implements Runnable {

    Client client;
    SceneManager sceneManager;

    public QueueListener(Client client, SceneManager sceneManager) {
        this.client = client;
        this.sceneManager = sceneManager;
    }

    @Override
    public void run() {
        client.setFirstCommand("queue");
        String response;
        do{
            response = client.getLatestResponse(false);
        }while (!response.startsWith("WAITING") && !response.startsWith("GAME"));
        System.out.println("[" + response + "]");
        if(response.equals("WAITING FOR OPPONENT..")) { //white player
            sceneManager.setPov(TableOrientation.WHITE_PLAYING);
            while(!response.equals("GAME IS ABOUT TO BEGIN!")) {

                client.setFirstCommand("idle");
//                do{
                    response = client.getLatestResponse(false);
//                }while (!response.startsWith("GAME"));

//                response = client.getLatestResponse(false);
                System.out.println("RSP: [" + response + "]");
            }
        }
        else{
            sceneManager.setPov(TableOrientation.BLACK_PLAYING);
        }

//        System.out.println("SWAPPING SCENE");
        Platform.runLater(() -> {
            try {
                sceneManager.swapScene(AvailableScene.PLAYING_SCENE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
