package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Utils.Utilities;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class Chat {

    int chatPanelWidth;
    int chessTableLength;
    int chatTitleHeight;

    int timerHeight;
    int sendMessageRectangleHeight;
    Pane contentsOfScrollPane;
    String opponentName = "opponent";
    Pane layout;
    Text myTimer;
    Text opponentTimer;
    //
    int lastMessageY;
    TextArea messageArea;

    Client client;
    SceneManager sceneManager;
    PlayingScene playingScene;


    public Pane getContentsOfScrollPane() {
        return contentsOfScrollPane;
    }

    public Chat(int tileLength, Client client, Pane layout, SceneManager sceneManager, PlayingScene playingScene) {
        this.chatPanelWidth = 4 * tileLength;
        this.chatTitleHeight = (int) (tileLength / 2.5);
        this.chessTableLength = 8 * tileLength;
        this.sendMessageRectangleHeight = 2 * tileLength;
        this.timerHeight = (int) (tileLength * 0.7);
        this.client = client;
        this.layout = layout;
        this.sceneManager = sceneManager;
        this.playingScene = playingScene;
        contentsOfScrollPane = new Pane();
        lastMessageY = 0;
    }

    public void drawChat() {
        drawTimer(layout);
        drawChatTitle(layout);
        drawMessageSender(layout);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(chessTableLength);
        scrollPane.setLayoutY(chatTitleHeight + timerHeight);
        scrollPane.setMaxSize(chatPanelWidth, chessTableLength - (chatTitleHeight + sendMessageRectangleHeight + timerHeight));
        scrollPane.setViewOrder(-2);


        scrollPane.setContent(contentsOfScrollPane);
        scrollPane.setLayoutX(chessTableLength);
        scrollPane.setPrefSize(chatPanelWidth, chessTableLength);

        layout.getChildren().add(scrollPane);

    }

    public void setMyTimer(double time) {
        myTimer.setText("My time: " + time);
    }

    public void setOpponentTimer(double time) {
        opponentTimer.setText("Opponent time: " + time);
    }

    private void drawTimer(Pane layout) {
        Rectangle timerRectangle = new Rectangle();
        timerRectangle.setX(chessTableLength);
        timerRectangle.setY(0);
        timerRectangle.setWidth(chatPanelWidth);
        timerRectangle.setHeight(timerHeight);
        timerRectangle.setFill(Color.ANTIQUEWHITE);
        timerRectangle.setViewOrder(-2);
        myTimer = new Text("My timer: ");
        myTimer.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 25));
        myTimer.setX(chessTableLength + chatPanelWidth / 20);
        myTimer.setY(timerHeight / 2);
        myTimer.setViewOrder(-3);
        opponentTimer = new Text("Opponent timer: ");
        opponentTimer.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 25));
        opponentTimer.setX(myTimer.getX());
        opponentTimer.setY(myTimer.getY() + Utilities.getTextHeight(opponentTimer));
        opponentTimer.setViewOrder(myTimer.getViewOrder());


        layout.getChildren().add(timerRectangle);
        layout.getChildren().add(myTimer);
        layout.getChildren().add(opponentTimer);
    }

    private void drawChatTitle(Pane layout) {
        Rectangle chatTitleRectangle = new Rectangle();
        chatTitleRectangle.setWidth(chatPanelWidth);
        chatTitleRectangle.setHeight(chatTitleHeight);
        chatTitleRectangle.setX(chessTableLength);
        chatTitleRectangle.setY(timerHeight);
        chatTitleRectangle.setStroke(Color.RED);
        chatTitleRectangle.setFill(Color.ANTIQUEWHITE);
        chatTitleRectangle.setViewOrder(-2);


        Text chatTitle = new Text("Your chat with " + opponentName);
        chatTitle.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20));
        chatTitle.setX(chatTitleRectangle.getX() + (chatPanelWidth / 2 - Utilities.getTextWidth(chatTitle) / 2));
        chatTitle.setY(chatTitleHeight / 2 + Utilities.getTextHeight(chatTitle) / 2 + timerHeight);
        chatTitle.setViewOrder(-3);


        layout.getChildren().add(chatTitleRectangle);
        layout.getChildren().add(chatTitle);
    }

    public void addMessage(String message, boolean sentByMe, boolean sentByPlayer, Pane pane) {
        int verticalSpaceBetweenMessages = 30;
        int xOffSetMyMessage = 20;//50
        int xOffSetOpponentMessage = 30;
        int xOffSet = 30;
        Font font;
        if (sentByPlayer) {
            font = Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20);
        } else {
            font = Font.font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, chatPanelWidth / 20);
        }
//        System.out.println(pane.getWidth());
//        System.out.println(pane.getPrefWidth());


        Text messageBuilder = new Text();
        Text textToBeChecked = new Text();
        textToBeChecked.setFont(font);

        int widthLimit = (int) (chatPanelWidth * 0.7);
        String[] wordsInMessage = message.split("\\s+");
        for (String word : wordsInMessage) {
            word = word + " ";
            textToBeChecked.setText(messageBuilder.getText() + word);

            if (Utilities.getTextWidth(textToBeChecked) > widthLimit) {
                messageBuilder.setText(messageBuilder.getText() + "\n");

                textToBeChecked.setText(word);
                if (Utilities.getTextWidth(textToBeChecked) > widthLimit)
                    word = splitWord(word, widthLimit, font);
            }
            messageBuilder.setText(messageBuilder.getText() + word);
        }

        messageBuilder.setFont(font);
        messageBuilder.setViewOrder(-4);

        Rectangle messageBackground = new Rectangle();
        messageBackground.setArcHeight(40);
        messageBackground.setArcWidth(40);
        messageBackground.setWidth(Utilities.getTextWidth(messageBuilder) + 30);
        messageBackground.setHeight(Utilities.getTextHeight(messageBuilder) + 30);
        messageBackground.setViewOrder(-3);

        if (sentByPlayer) {

            if (!sentByMe) {
//                messageBackground.setX(xOffSetOpponentMessage + (widthLimit - Utilities.getTextWidth(messageBuilder)));
                messageBackground.setX(xOffSetMyMessage);
                messageBackground.setFill(Color.AQUAMARINE);
            } else {
                messageBackground.setX(chatPanelWidth - messageBackground.getWidth() - xOffSetMyMessage);
                //messageBackground.setX(xOffSetMyMessage);
                messageBackground.setFill(Color.YELLOW);
            }
        } else {
            messageBackground.setX(xOffSetMyMessage);
            messageBackground.setFill(Color.BLUE);
            messageBuilder.setFill(Color.WHITE);
        }
        messageBackground.setY(lastMessageY + verticalSpaceBetweenMessages);
        lastMessageY = (int) (messageBackground.getY() + messageBackground.getHeight());

        messageBuilder.setX(messageBackground.getX() + 15);
        messageBuilder.setY(messageBackground.getY() + 15 + Utilities.getHeightOfFirstLine(messageBuilder));

        pane.getChildren().add(messageBackground);
        pane.getChildren().add(messageBuilder);

    }

    private String splitWord(String word, int widthLimit, Font font) {

        Text textToBeChecked = new Text();
        textToBeChecked.setFont(font);
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            textToBeChecked.setText(wordBuilder.toString() + word.charAt(i));
            if (Utilities.getTextWidth(textToBeChecked) > widthLimit) {
                wordBuilder.append('\n');
            }
            wordBuilder.append(word.charAt(i));
        }

        return wordBuilder.toString();
    }


    private void drawMessageSender(Pane layout) {
        Rectangle sendMessageRectangle = new Rectangle();
        sendMessageRectangle.setWidth(chatPanelWidth);
        sendMessageRectangle.setHeight(sendMessageRectangleHeight);
        sendMessageRectangle.setX(chessTableLength);
        sendMessageRectangle.setY(chessTableLength - sendMessageRectangleHeight);
        sendMessageRectangle.setStroke(Color.RED);
        sendMessageRectangle.setFill(Color.ANTIQUEWHITE);
        sendMessageRectangle.setViewOrder(-2);

        messageArea = new TextArea();
        int messageAreaHeight = (int) (sendMessageRectangleHeight * 0.6);
        int messageAreaWidth = (int) (chatPanelWidth * 0.9);
        messageArea.setPrefHeight(messageAreaHeight);
        messageArea.setPrefWidth(messageAreaWidth);

        messageArea.setLayoutX(sendMessageRectangle.getX() + chatPanelWidth * 0.05);
        messageArea.setLayoutY(sendMessageRectangle.getY() + sendMessageRectangleHeight * 0.1);
        messageArea.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 25));

        messageArea.setOnMouseClicked(mouseEvent -> onMessageAreaClick());

        messageArea.setViewOrder(-3);

        Button sendMessageButton = new Button("Send");
        Button forfeitButton = new Button("Forfeit");

        forfeitButton.setLayoutX(messageArea.getLayoutX() + messageAreaWidth / 20);
        forfeitButton.setLayoutY(messageArea.getLayoutY() + messageAreaHeight + messageAreaHeight / 20);
        forfeitButton.setPrefWidth(messageAreaWidth / 2 - messageAreaWidth / 15);
        forfeitButton.setPrefHeight(messageAreaHeight / 2 - messageAreaHeight / 12);
        forfeitButton.setFont(Font.font("Verdana", FontWeight.NORMAL, forfeitButton.getPrefWidth() / 8));
        forfeitButton.setOnMouseClicked(mouseEvent -> {
            try {
                onForfeitButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        forfeitButton.setViewOrder(-3);

        sendMessageButton.setLayoutX(messageArea.getLayoutX() + messageAreaWidth / 2 + messageAreaWidth / 20);
        sendMessageButton.setLayoutY(forfeitButton.getLayoutY());
        sendMessageButton.setPrefWidth(forfeitButton.getPrefWidth());
        sendMessageButton.setPrefHeight(forfeitButton.getPrefHeight());
        sendMessageButton.setFont(forfeitButton.getFont());
        sendMessageButton.setOnMouseClicked(mouseEvent -> onSendButtonPress());
        sendMessageButton.setViewOrder(-3);


        layout.getChildren().add(sendMessageRectangle);
        layout.getChildren().add(forfeitButton);
        layout.getChildren().add(sendMessageButton);
        layout.getChildren().add(messageArea);
    }

    private void onSendButtonPress() {
        String message = messageArea.getText();
        messageArea.setText("");
        client.setLatestCommand("gameMessage " + message);
    }

    private void onForfeitButtonPress() throws IOException {
        client.setPressedForfeit(true);
//        playingScene.stopBoardStatusRequests();
        client.setLatestCommand("forfeit");
    }

    public void drawTransition(boolean isWinner) {
        System.out.println("TRANSITION");
        Text conclusionText;
        if (isWinner) {
            conclusionText = new Text("WINNER");
            conclusionText.setFill(Color.BLUE);
        } else {
            conclusionText = new Text("LOSER");
            conclusionText.setFill(Color.RED);
        }
        conclusionText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\gunplay 3d.ttf", chessTableLength / 5));
        conclusionText.toFront();
        conclusionText.setX(chessTableLength / 2 - Utilities.getTextWidth(conclusionText) / 2);
        conclusionText.setY(chessTableLength / 2);
        conclusionText.setViewOrder(-4);
        layout.getChildren().add(conclusionText);

        PauseTransition transition = new PauseTransition(Duration.seconds(5));
        System.out.println("before transition");
        transition.play();
        transition.setOnFinished(e -> {
            try {
                System.out.println("after transition");
                sceneManager.swapScene(AvailableScene.MAIN_MENU_SCENE);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
    }


    private void onMessageAreaClick() {
        messageArea.requestFocus();
    }


}
