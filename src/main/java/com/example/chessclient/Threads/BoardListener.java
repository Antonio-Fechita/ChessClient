package com.example.chessclient.Threads;

import com.example.chessclient.Client;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.Scenes.Chat;
import com.example.chessclient.Drawing.Scenes.PlayingScene;
import com.example.chessclient.Piece;
import javafx.application.Platform;

import java.util.Arrays;
import java.util.List;

public class BoardListener implements Runnable {

    Client client;
    Chat chat;
    double timeFirstPlayer;
    double timeSecondPlayer;
    int numberOfMoves;
    String lastMove;
    int numberOfMessages, lastNumberOfMessages = 0;
    List<String> messages;

    PlayingScene playingScene;

    boolean isStopped;

    public BoardListener(Client client, Chat chat, PlayingScene playingScene) {
        this.client = client;
        this.chat = chat;
        this.playingScene = playingScene;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    @Override
    public void run() {
        String nameFirstPlayer;
        String nameSecondPlayer;


        while (!isStopped) {
            client.setLastCommand("boardStatus");

            String response = client.getLatestResponse(true);

            String[] breakdown = response.split("%");

            timeFirstPlayer = Double.parseDouble(breakdown[2].split("&")[0].split("-")[1]);
            nameFirstPlayer = breakdown[2].split("&")[0].split("-")[0];
            timeSecondPlayer = Double.parseDouble(breakdown[2].split("&")[1].split("-")[1]);
            nameSecondPlayer = breakdown[2].split("&")[1].split("-")[0];


            numberOfMoves = Integer.parseInt(breakdown[3].split("-")[1]);
            List<String> moves = Arrays.asList(breakdown[4].split("&"));
            lastMove = moves.get(moves.size() - 1);

            numberOfMessages = Integer.parseInt(breakdown[5].split("-")[1]);
            List<String> messages = Arrays.asList(breakdown[6].split("&"));

            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).startsWith(nameFirstPlayer)) {
                    messages.set(i, messages.get(i).replaceFirst(nameFirstPlayer, "1"));
                } else {
                    messages.set(i, messages.get(i).replaceFirst(nameSecondPlayer, "2"));
                }
            }

//                System.out.println("-------------------------------------");
//                System.out.println(response);
//                System.out.println(timeFirstPlayer);
//                System.out.println(timeSecondPlayer);
//                //System.out.println(moves);
//                System.out.println(lastMove);
//                System.out.println(nameFirstPlayer);
//                System.out.println(nameSecondPlayer);
//                System.out.println(numberOfMessages);
//                System.out.println(messages);

            if (numberOfMessages != lastNumberOfMessages) {
                int numberOfMessagesToAdd = numberOfMessages - lastNumberOfMessages;
                for (int i = numberOfMessagesToAdd; i > 0; i--) {
                    String message = messages.get(messages.size() - i);
                    if ((message.charAt(0) == '1' && playingScene.getPov().equals(TableOrientation.WHITE_PLAYING)) ||
                            (message.charAt(0) == '2' && playingScene.getPov().equals(TableOrientation.BLACK_PLAYING))) {
                        Platform.runLater(() -> chat.addMessage(message.substring(2), true, true, chat.getContentsOfScrollPane()));
                    } else {
                        Platform.runLater(() -> chat.addMessage(message.substring(2), false, true, chat.getContentsOfScrollPane()));
                    }
                }
                lastNumberOfMessages = numberOfMessages;
            }

            if ((numberOfMoves % 2 == 0 && playingScene.getPov().equals(TableOrientation.WHITE_PLAYING)) ||
                    ((numberOfMoves % 2 == 1) && playingScene.getPov().equals(TableOrientation.BLACK_PLAYING))) {
                Piece piece = playingScene.getPieceAtTile(lastMove.substring(0, 2));
                if (piece != null)
                    piece.placePieceAtTile(lastMove.substring(3), playingScene.getPov(), playingScene.getTileLength());
            }

        }

    }

    public double getTimeFirstPlayer() {
        return timeFirstPlayer;
    }

    public double getTimeSecondPlayer() {
        return timeSecondPlayer;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public String getLastMove() {
        return lastMove;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public List<String> getMessages() {
        return messages;
    }
}