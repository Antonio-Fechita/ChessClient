package com.example.chessclient.Threads;

import com.example.chessclient.Client;
import com.example.chessclient.Drawing.Enums.ChessColor;
import com.example.chessclient.Drawing.Enums.ChessPiece;
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
    int numberOfMoves, lastKnownNumberOfMoves = -1;
    String lastMove;
    int numberOfMessages, lastNumberOfMessages = 0;
    int numberOfServerMessages = -1;
    List<String> messages;

    PlayingScene playingScene;

    boolean isStopped = false;
    boolean isBoardStatusRequired = true;

    private final static double SECONDS_PER_FRAME = 1 / 30.0;

    public BoardListener(Client client, Chat chat, PlayingScene playingScene) {
        this.client = client;
        this.chat = chat;
        this.playingScene = playingScene;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public void setBoardStatusRequired(boolean boardStatusRequired) {
        isBoardStatusRequired = boardStatusRequired;
    }

    @Override
    public void run() {
        String nameFirstPlayer;
        String nameSecondPlayer;

        long prevFrameNano = System.nanoTime();

        while (!isStopped) {

            if ((System.nanoTime() - prevFrameNano) / 1000000000.0 > SECONDS_PER_FRAME && isBoardStatusRequired) {
                client.setLatestCommand("boardStatus");
                prevFrameNano = System.nanoTime();

            }


            String response = client.getLatestResponse();

            if (!response.startsWith("%")) {
                if (!response.equals("--idle--") && !response.equals("MOVE WAS APPLIED!") &&
                        !response.equals("MESSAGE SENT!") && !response.equals("Make a move!")
                        && !response.equals("Wrong command") && !response.equals("!notingame!")
                        && !response.equals("MOVE WAS APPLIED! *!pawn upgraded")) {
                    int numberOfMessagesReceived = client.getNumberOfMessagesReceived();
                    if (numberOfMessagesReceived != numberOfServerMessages) {
                        Platform.runLater(() -> chat.addMessage(response, false, false, chat.getContentsOfScrollPane()));
                        numberOfServerMessages = numberOfMessagesReceived;
                    }
                }


                if (response.equals("!notingame!") && client.isInGame()) {
                    System.out.println("GAME FINISHED");
                    client.setInGame(false);
                    if (client.isPressedForfeit())
                        Platform.runLater(() -> chat.drawTransition(false));

                    else
                        Platform.runLater(() -> chat.drawTransition(true));
                    client.setPressedForfeit(false);
                }

                continue;
            }


            String[] breakdown = response.split("%");
            //System.out.println("boardListener response: " + response);
            try {
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
                        ((numberOfMoves % 2 == 1) && playingScene.getPov().equals(TableOrientation.BLACK_PLAYING)) &&
                                numberOfMoves > lastKnownNumberOfMoves) {

                    Piece piece = playingScene.getPieceAtTile(lastMove.substring(0, 2));
                    if (piece != null) { //should always be true
                        Piece pieceToBeRemoved = playingScene.getPieceAtTile(lastMove.substring(3));

                        if (pieceToBeRemoved == null) {
                            System.out.println("Piece to be removed is null");
                        } else {
                            System.out.println(pieceToBeRemoved.getColor() + " " + pieceToBeRemoved.getPiece());
                        }
                        Platform.runLater(() -> {
                            playingScene.removePiece(pieceToBeRemoved);
                            piece.placePieceAtTile(lastMove.substring(3), playingScene.getPov(), playingScene.getTileLength());
                            if (piece.getPiece().equals(ChessPiece.PAWN) &&
                                    ((piece.getTile().charAt(1) == '8' && piece.getColor().equals(ChessColor.WHITE)) ||
                                            (piece.getTile().charAt(1) == '1' && piece.getColor().equals(ChessColor.BLACK)))){
                                piece.promote();
                            }
                        });
                    }
                    lastKnownNumberOfMoves = numberOfMoves;
                }


                if (playingScene.getPov().equals(TableOrientation.WHITE_PLAYING)) {
                    Platform.runLater(() -> {
                        chat.setMyTimer(timeFirstPlayer);
                        chat.setOpponentTimer(timeSecondPlayer);
                    });
                } else {
                    Platform.runLater(() -> {
                        chat.setMyTimer(timeSecondPlayer);
                        chat.setOpponentTimer(timeFirstPlayer);
                    });
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                System.out.println(response);
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
