package com.example.chessclient.Drawing;

import com.example.chessclient.Client;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.Scenes.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager extends Application {

    Client client = new Client();
    Stage stage;
    TableOrientation pov = TableOrientation.WHITE_PLAYING;

    public void setPov(TableOrientation pov) {
        this.pov = pov;
    }

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;
        swapScene(AvailableScene.INITIAL_SCENE);
        stage.setResizable(true);
        stage.show();
        client.start();


    }

    @Override
    public void stop(){

        String response;
        client.setLatestCommand("forfeit");
        do{
            response = client.getLatestResponse();
        }while (!response.equals("You must be in game to forfeit!") && !response.equals("YOU HAVE FORFEITED THE GAME"));
        client.setLatestCommand("exit");
        System.exit(1);
    }

    public Client getClient() {
        return client;
    }

    public void swapScene(AvailableScene scene) throws IOException {
        switch (scene) {
            case PLAYING_SCENE -> stage.setScene(new PlayingScene(client, 120, pov,this).getScene());
            case INITIAL_SCENE -> stage.setScene(new InitialScene(this).getScene());
            case LOGIN_SCENE -> stage.setScene(new LoginScene(this).getScene());
            case REGISTER_SCENE -> stage.setScene(new RegisterScene(this).getScene());
            case MAIN_MENU_SCENE -> stage.setScene(new MainMenu(this).getScene());
            case HISTORY_SCENE -> stage.setScene(new HistoryScene(client,this).getScene());
            case REPLAY_SCENE -> stage.setScene(new ReplayScene(120,this).getScene());
        }
    }

    public static void main(String[] args) {
        launch();


    }
}
