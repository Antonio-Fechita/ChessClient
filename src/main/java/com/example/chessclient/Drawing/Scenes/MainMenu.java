package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Threads.QueueListener;
import com.example.chessclient.Utils.Utilities;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainMenu implements Drawable {

    Pane layout = new Pane();
    Scene scene;
    SceneManager sceneManager;
    Button queueButton = new Button("Queue");
    Button inviteButton = new Button("Invite");
    Button replaysButton = new Button("Replays");
    Button statsButton = new Button("Stats");
    Button quitButton = new Button("Quit");
    Client client;

    boolean isInQueue = false;


    public MainMenu(SceneManager sceneManager){


        ImageView background = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\background.jpg"));
        Text chessOnlineText = new Text("Chess online");
        chessOnlineText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\Black White.ttf",120));
        chessOnlineText.setX(100);
        chessOnlineText.setY(120);
        chessOnlineText.setFill(Color.WHITE);

        Utilities.setWidthHeightXY(queueButton,250,100,100,200);
        queueButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(inviteButton,250,100, (int) queueButton.getLayoutX(), (int) (queueButton.getLayoutY() + 150));
        inviteButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(replaysButton,250,100, (int) inviteButton.getLayoutX(), (int) (inviteButton.getLayoutY() + 150));
        replaysButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(statsButton,250,100, (int) replaysButton.getLayoutX(), (int) (replaysButton.getLayoutY() + 150));
        statsButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(quitButton,250,100, (int) queueButton.getLayoutX(),800);
        quitButton.setPadding(Insets.EMPTY);
        //250,100,loginButton.getLayoutX(),800


        layout.getChildren().add(background);
        layout.getChildren().add(queueButton);
        layout.getChildren().add(inviteButton);
        layout.getChildren().add(quitButton);
        layout.getChildren().add(replaysButton);
        layout.getChildren().add(statsButton);
        layout.getChildren().add(chessOnlineText);
        scene = new Scene(layout,1500,1000);
        scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());


        inviteButton.setOnAction(actionEvent -> {
            try {
                onInviteButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        queueButton.setOnAction(actionEvent -> {
            try {
                onQueueButtonPress();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        replaysButton.setOnAction(actionEvent -> {
            try {
                onReplaysButton();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        quitButton.setOnAction(actionEvent -> Platform.exit());

        this.sceneManager = sceneManager;
        this.client = sceneManager.getClient();
    }

    private void onQueueButtonPress() throws IOException, InterruptedException {
        if(!isInQueue){
            isInQueue = true;
            QueueListener queueListener = new QueueListener(client,sceneManager);

            new Thread(queueListener).start();

        }
    }

    private void onInviteButtonPress() throws IOException {
        //sceneManager.swapScene(AvailableScene.REGISTER_SCENE);
    }

    private void onReplaysButton() throws IOException {
        sceneManager.swapScene(AvailableScene.HISTORY_SCENE);
    }

    @Override
    public Scene getScene(){
        return scene;
    }
}
