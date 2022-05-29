package com.example.chessclient;

import com.example.chessclient.Drawing.Enums.ChessColor;
import com.example.chessclient.Drawing.Enums.ChessPiece;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.Scenes.PlayingScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Piece {
    private ChessPiece piece;
    private ChessColor color;
    private ImageView imageView;
    private String tile;

    private PlayingScene playingScene;

    public String getTile() {
        return tile;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public ChessColor getColor() {
        return color;
    }

    public Piece(ChessPiece piece, ChessColor color, int tileLength, Pane layout, String initialTile, TableOrientation
            tableOrientation, Client client, PlayingScene playingScene) {
        this.piece = piece;
        this.color = color;
        this.playingScene = playingScene;
        imageView = getPieceImageView(color, piece, tileLength);

        if(playingScene!=null)
            applyMouseEventsToPieceImage(imageView, tileLength, tableOrientation, client);
        layout.getChildren().add(imageView);
        tile = initialTile;
        placePieceAtTile(tile, tableOrientation, tileLength);
        //System.out.println("Placing " + color.toString().toLowerCase() + " " + piece.toString().toLowerCase() + " at " + tile);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void applyMouseEventsToPieceImage(ImageView imageView, int tileLength, TableOrientation tableOrientation, Client client) {
        imageView.setOnMouseDragged(mouseEvent -> {


//            imageView.setViewOrder(-1);

            imageView.setX(mouseEvent.getX() - tileLength / 2);
            imageView.setY(mouseEvent.getY() - tileLength / 2);
        });

        imageView.setOnMouseReleased(mouseEvent ->
        {
            if (mouseEvent.getX() >= 0 && mouseEvent.getX() <= tileLength * 8 &&
                    mouseEvent.getY() >= 0 && mouseEvent.getY() <= tileLength * 8) {

                String destinationTile = getTileFromCoordinates((int) mouseEvent.getX(), (int) mouseEvent.getY(), tableOrientation, tileLength);
                System.out.println("Moving " + color.toString().toLowerCase() + " " + piece.toString().toLowerCase() +
                        " from " + tile + " to " + destinationTile);

                client.setLatestCommand("move " + tile + "-" + destinationTile);

                String response;
                do {
                    response = client.getLatestResponse();
                } while (!response.startsWith("PLEASE") && !response.startsWith("INVALID MOVE") && !response.startsWith("MOVE"));

                if (response.equals("MOVE WAS APPLIED!")) { //if move is allowed by server
                    Piece pieceToBeRemoved = playingScene.getPieceAtTile(destinationTile);
                    playingScene.removePiece(pieceToBeRemoved);
                    placePieceAtTile(getTileFromCoordinates((int) mouseEvent.getX(), (int) mouseEvent.getY(), tableOrientation, tileLength), tableOrientation, tileLength);
                } else {
                    placePieceAtTile(tile, tableOrientation, tileLength);
                }
            }
            else
                placePieceAtTile(tile, tableOrientation, tileLength);
//            imageView.setViewOrder(0);
        });
    }


    private String getTileFromCoordinates(int xPos, int yPos, TableOrientation tableOrientation, int tileLength) {
        char column;
        int row;
        if (tableOrientation.equals(TableOrientation.WHITE_PLAYING)) {
            column = (char) ((int) 'A' + xPos / tileLength);
            row = 8 - yPos / tileLength;
        } else {
            column = (char) ((int) 'H' - xPos / tileLength);
            row = yPos / tileLength + 1;
        }

        return column + String.valueOf(row);
    }

    public void placePieceAtTile(String tile, TableOrientation tableOrientation, int tileLength) {
        int xPos, yPos;
        char column = tile.charAt(0);
        char row = tile.charAt(1);
        if (tableOrientation.equals(TableOrientation.WHITE_PLAYING)) {
            xPos = tileLength * (column - 'A');
            yPos = tileLength * (8 - Integer.parseInt(String.valueOf(row)));
        } else {
            xPos = tileLength * (7 - (column - 'A'));
            yPos = tileLength * (Integer.parseInt(String.valueOf(row)) - 1);
        }

        imageView.setX(xPos);
        imageView.setY(yPos);
        this.tile = tile;
    }

    public static ImageView getPieceImageView(ChessColor color, ChessPiece piece, int tileLength) {
        ImageView imageView = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\Pieces\\" + color.toString().charAt(0) +
                color.toString().substring(1).toLowerCase() + "\\" + piece.toString().toLowerCase() + ".png"));
        imageView.setFitHeight(tileLength);
        imageView.setFitWidth(tileLength);
        return imageView;
    }
}
