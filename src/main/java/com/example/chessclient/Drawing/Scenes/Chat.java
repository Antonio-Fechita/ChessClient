package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Utils.Utilities;
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

public class Chat {

    int chatPanelWidth;
    int chessTableLength;
    int chatTitleHeight;
    int sendMessageRectangleHeight;
    Pane contentsOfScrollPane;
    String opponentName = "opponent";
    //
    int lastMessageY;
    TextArea messageArea;

    Client client;


    public Pane getContentsOfScrollPane() {
        return contentsOfScrollPane;
    }

    public Chat(int tileLength, Client client) {
        this.chatPanelWidth = 4 * tileLength;
        this.chatTitleHeight = (int) (tileLength / 2.5);
        this.chessTableLength = 8 * tileLength;
        this.sendMessageRectangleHeight = 2 * tileLength;
        this.client = client;
        contentsOfScrollPane = new Pane();
        lastMessageY = 0;
    }

    public void drawChat(Pane layout) {
        drawChatTitle(layout);
        drawMessageSender(layout);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(chessTableLength);
        scrollPane.setLayoutY(chatTitleHeight);
        scrollPane.setMaxSize(chatPanelWidth, chessTableLength - (chatTitleHeight + sendMessageRectangleHeight));

//        for (int i = 0; i <= 1000; i++) {
//            Text text = new Text("Some text " + i);
//            box.getChildren().add(text);
//        }

        //to delete later
//        addMessage("duten pizda matii", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii, miai adus marfa?", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("da tiam aduso", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii, cat costa", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("6 ron", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("nu platesc asa de mult pentru o bucata de drog", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda matii daca nu vrei sa platesti nu platesti si atunci nu ai drog", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("dar am nevoie de drog, ma doare aici", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("...", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("...", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("sorata ce mai face", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("e la munca in italia", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("daon pula mea", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("bine til las cu 5 ron", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("ai si drogul.. marihuana?", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("vorbeste mai incet", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//        addMessage("de unde sa am drogul marihuana?", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("hai nu mai fa, nu mai face pe prostul", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//        addMessage("ai sau nu?", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("imi datorezi bani", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("duten pizda mati", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("dute tu in pizda matii, de unde sati dau eu bani?", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("dami banii mei sau te omor.. cu cutitul", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("*pune cutitul inapoi* tii dau maine", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("asta e ultima pasuire, chiriac, iti omor familia si pe sorata, ai inteles?", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("chill omule, nu e nicio problema", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("uite banii", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("...", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("asta e marfa.. cu asta.. esti aspirator de gagici", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("...", true,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
//
//        addMessage("...*poc*.. cea cazut?", false,Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20),contentsOfScrollPane);
        //

        scrollPane.setContent(contentsOfScrollPane);
        scrollPane.setLayoutX(chessTableLength);
        scrollPane.setPrefSize(chatPanelWidth, chessTableLength);

        layout.getChildren().add(scrollPane);

    }

    private void drawChatTitle(Pane layout) {
        Rectangle chatTitleRectangle = new Rectangle();
        chatTitleRectangle.setWidth(chatPanelWidth);
        chatTitleRectangle.setHeight(chatTitleHeight);
        chatTitleRectangle.setX(chessTableLength);
        chatTitleRectangle.setY(0);
        chatTitleRectangle.setFill(Color.TRANSPARENT);
        chatTitleRectangle.setStroke(Color.RED);


        Text chatTitle = new Text("Your chat with " + opponentName);
        chatTitle.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20));
        chatTitle.setX(chatTitleRectangle.getX() + (chatPanelWidth / 2 - Utilities.getTextWidth(chatTitle) / 2));
        chatTitle.setY(chatTitleHeight / 2 + Utilities.getTextHeight(chatTitle) / 2);


        layout.getChildren().add(chatTitleRectangle);
        layout.getChildren().add(chatTitle);
    }

    public void addMessage(String message, boolean sentByMe, boolean sentByPlayer, Pane pane) {
        int verticalSpaceBetweenMessages = 30;
        int xOffSetMyMessage = 50;
        int xOffSetOpponentMessage = 30;
        Font font;
        if (sentByPlayer) {
            font = Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 20);
        }
        else {
            font = Font.font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, chatPanelWidth / 20);
        }


        Text textMessage = new Text(message);
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

        Rectangle messageBackground = new Rectangle();
        messageBackground.setArcHeight(40);
        messageBackground.setArcWidth(40);
        messageBackground.setWidth(Utilities.getTextWidth(messageBuilder) + 30);
        messageBackground.setHeight(Utilities.getTextHeight(messageBuilder) + 30);

        if (!sentByMe) {
            messageBackground.setX(xOffSetOpponentMessage);
            messageBackground.setFill(Color.AQUAMARINE);
        } else {
            messageBackground.setX(xOffSetMyMessage + (widthLimit - Utilities.getTextWidth(messageBuilder)));
            messageBackground.setFill(Color.YELLOW);
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


    private void drawScrollPane() {

    }

    private void drawMessageSender(Pane layout) {
        Rectangle sendMessageRectangle = new Rectangle();
        sendMessageRectangle.setWidth(chatPanelWidth);
        sendMessageRectangle.setHeight(sendMessageRectangleHeight);
        sendMessageRectangle.setX(chessTableLength);
        sendMessageRectangle.setY(chessTableLength - sendMessageRectangleHeight);
        sendMessageRectangle.setFill(Color.TRANSPARENT);
        sendMessageRectangle.setStroke(Color.RED);

        messageArea = new TextArea();
        int messageAreaHeight = (int) (sendMessageRectangleHeight * 0.6);
        int messageAreaWidth = (int) (chatPanelWidth * 0.9);
        messageArea.setPrefHeight(messageAreaHeight);
        messageArea.setPrefWidth(messageAreaWidth);

        messageArea.setLayoutX(sendMessageRectangle.getX() + chatPanelWidth * 0.05);
        messageArea.setLayoutY(sendMessageRectangle.getY() + sendMessageRectangleHeight * 0.1);
        messageArea.setFont(Font.font("Verdana", FontWeight.BOLD, chatPanelWidth / 25));

        messageArea.setOnMouseClicked(mouseEvent -> onMessageAreaClick());

        Button sendMessageButton = new Button("Send");
        Button forfeitButton = new Button("Forfeit");

        forfeitButton.setLayoutX(messageArea.getLayoutX() + messageAreaWidth / 20);
        forfeitButton.setLayoutY(messageArea.getLayoutY() + messageAreaHeight + messageAreaHeight / 20);
        forfeitButton.setPrefWidth(messageAreaWidth / 2 - messageAreaWidth / 15);
        forfeitButton.setPrefHeight(messageAreaHeight / 2 - messageAreaHeight / 12);
        forfeitButton.setFont(Font.font("Verdana", FontWeight.NORMAL, forfeitButton.getPrefWidth() / 8));
        forfeitButton.setOnMouseClicked(mouseEvent -> onForfeitButtonPress());

        sendMessageButton.setLayoutX(messageArea.getLayoutX() + messageAreaWidth / 2 + messageAreaWidth / 20);
        sendMessageButton.setLayoutY(forfeitButton.getLayoutY());
        sendMessageButton.setPrefWidth(forfeitButton.getPrefWidth());
        sendMessageButton.setPrefHeight(forfeitButton.getPrefHeight());
        sendMessageButton.setFont(forfeitButton.getFont());
        sendMessageButton.setOnMouseClicked(mouseEvent -> onSendButtonPress());


        layout.getChildren().add(sendMessageRectangle);
        layout.getChildren().add(forfeitButton);
        layout.getChildren().add(sendMessageButton);
        layout.getChildren().add(messageArea);
    }

    private void onSendButtonPress() {
        String message = messageArea.getText();
        //addMessage(message, true, true,contentsOfScrollPane);
        messageArea.setText("");
        client.setLatestCommand("gameMessage " + message);
    }

    private void onForfeitButtonPress(){
    }

    private void onMessageAreaClick(){
        messageArea.requestFocus();
    }


}
