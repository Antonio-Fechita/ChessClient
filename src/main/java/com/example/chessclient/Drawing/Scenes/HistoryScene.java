package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;

public class HistoryScene implements Drawable {

    Scene scene;

    Pane layout;
    GridPane contentsOfScrollPane = new GridPane();

    int layoutWidth = 1500;
    int layoutHeight = 1000;
    ScrollPane scrollPane;

    Font font,fontHeader;

    SceneManager sceneManager;
    Client client;


    public HistoryScene(Client client, SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.client = sceneManager.getClient();

        ImageView background = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\background.jpg"));
        Text chessOnlineText = new Text("Chess online");
        chessOnlineText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\Black White.ttf", 120));
        chessOnlineText.setX(100);
        chessOnlineText.setY(120);
        chessOnlineText.setFill(Color.WHITE);
        font = Font.font("Verdana", FontWeight.BOLD, 25);
        fontHeader = Font.font("Verdana", FontWeight.EXTRA_BOLD, 30);




        contentsOfScrollPane.setPrefHeight(1000);
        contentsOfScrollPane.setPrefWidth(1500);
        contentsOfScrollPane.setHgap(40);
        contentsOfScrollPane.setVgap(5);
        contentsOfScrollPane.setAlignment(Pos.CENTER);


        layout = new Pane();
        layout.getChildren().add(background);
        layout.getChildren().add(chessOnlineText);

        client.setLatestCommand("history");
        String response;
        do {
            response = client.getLatestResponse();
        } while (!response.startsWith("#id"));

        String[] replays = response.split("#");
        System.out.println("Replays length");
        for (int index = 1; index < replays.length; index++) {
            String[] breakdown = replays[index].split("/");

            String matchId = breakdown[0].split(":")[1];
            String whitePlayer = breakdown[1].split(":")[1];
            String blackPlayer = breakdown[2].split(":")[1];
            String date = breakdown[3].split(":",2)[1];
            String duration = breakdown[4].split(":")[1];
            String winner = breakdown[5].split(":")[1];

            String hour = "";
            int separatorCounter = 0;
            for (int j = 0; j < date.length(); j++) {
                if (date.charAt(j) == ':') {
                    separatorCounter++;
                }
                if (separatorCounter == 3) {
                    hour = date.substring(j + 1);
                    date = date.substring(0, j);
                    break;
                }
            }

//            System.out.println("White player: " + whitePlayer + " Black player: " + blackPlayer + " Date: " + date +
//                    " Hour: " + hour + " Duration: " + duration + " Winner: " + winner);
            drawRow(index,matchId,whitePlayer,blackPlayer,date,hour,duration,winner);
        }

        contentsOfScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane = new ScrollPane();
        scrollPane.setContent(contentsOfScrollPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(layoutWidth * 0.8);
        scrollPane.setPrefHeight(layoutHeight * 0.8);
        scrollPane.setLayoutX(layoutWidth * 0.1);
        scrollPane.setLayoutY(layoutHeight * 0.2);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: rgba(76, 175, 80, 0.3);-fx-background-radius: 25;-fx-background-size: 120, 120");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        drawHeaders(layout);
        layout.getChildren().add(scrollPane);
        scene = new Scene(layout,1500,1000);
        scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());
        System.out.println();
    }


    private void onButtonPress(String matchId) throws IOException {
        client.setLatestCommand("replay " + matchId);
        sceneManager.swapScene(AvailableScene.REPLAY_SCENE);
    }

    private void drawRow(int rowNumber, String matchId, String whitePlayer, String blackPlayer, String date, String hour, String duration, String winner){
        Button replayButton = new Button("Replay!");
        replayButton.setMaxHeight(50);
        replayButton.setMaxWidth(50);

        replayButton.setMinHeight(50);
        replayButton.setMinWidth(50);

        replayButton.setOnAction(actionEvent -> {
            try {
                onButtonPress(matchId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Text whitePlayerText = new Text(whitePlayer);
        Text blackPlayerText = new Text(blackPlayer);
        Text dateText = new Text(date);
        Text hourText = new Text(hour);
        Text durationText = new Text(duration);
        Text winnerText = new Text(winner);

        whitePlayerText.setFont(font);
        blackPlayerText.setFont(font);
        dateText.setFont(font);
        hourText.setFont(font);
        durationText.setFont(font);
        winnerText.setFont(font);

        whitePlayerText.setFill(Color.WHITE);
        blackPlayerText.setFill(Color.WHITE);
        dateText.setFill(Color.WHITE);
        hourText.setFill(Color.WHITE);
        durationText.setFill(Color.WHITE);
        winnerText.setFill(Color.WHITE);



        contentsOfScrollPane.add(whitePlayerText,0,rowNumber);
        contentsOfScrollPane.add(blackPlayerText,1,rowNumber);
        contentsOfScrollPane.add(dateText,2,rowNumber);
        contentsOfScrollPane.add(hourText,3,rowNumber);
        contentsOfScrollPane.add(durationText,4,rowNumber);
        contentsOfScrollPane.add(winnerText,5,rowNumber);
        contentsOfScrollPane.add(replayButton,6,rowNumber);

    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
