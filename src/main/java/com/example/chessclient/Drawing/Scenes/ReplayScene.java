package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.Enums.ChessColor;
import com.example.chessclient.Drawing.Enums.ChessPiece;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Piece;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReplayScene implements Drawable {

    int tileLength;
    Color blackTileColor = Color.CORAL;
    Color whiteTileColor = Color.BEIGE;
    Pane layout;
    Client client;
    List<Piece> pieces;
    List<Piece> eatenPieces;
    SceneManager sceneManager;
    Scene scene;
    int currentMove;
    String[] moves;

    Button forwardButton,backwardButton, backButton;

    public ReplayScene(int tileLength, SceneManager sceneManager) {
        this.tileLength = tileLength;
        this.sceneManager = sceneManager;
        this.client = sceneManager.getClient();
        this.pieces = new ArrayList<>();
        this.eatenPieces = new ArrayList<>();
        this.layout = new Pane();
        this.currentMove = 0;

        String history;
        do{
            history = client.getLatestResponse();
        }while (!history.startsWith("matchRep:"));


        history = history.substring(9);
        System.out.println(history);


        drawBoard(TableOrientation.WHITE_PLAYING);
        placePieces(TableOrientation.WHITE_PLAYING);

        forwardButton = new Button("->");
        forwardButton.setOnAction(actionEvent -> onForwardButtonPress());
        forwardButton.setLayoutX(4 * tileLength);
        forwardButton.setLayoutY(8.5 * tileLength);

        backwardButton = new Button("<-");
        backwardButton.setOnAction(actionEvent -> onBackwardButtonPress());
        backwardButton.setLayoutX(forwardButton.getLayoutX() - 50);
        backwardButton.setLayoutY(forwardButton.getLayoutY());

        backButton = new Button("Back");
        backButton.setOnAction(actionEvent -> {
            try {
                onBackButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        backButton.setLayoutX(20);
        backButton.setLayoutY(forwardButton.getLayoutY());

        backwardButton.setDisable(true);
        if(history.equals("null"))
            forwardButton.setDisable(true);
        else{
            moves = history.split("/");
        }

        layout.getChildren().add(forwardButton);
        layout.getChildren().add(backwardButton);
        layout.getChildren().add(backButton);

        scene = new Scene(layout,8 * tileLength,9 * tileLength);
    }

    private void onForwardButtonPress(){

        Piece piece = getPieceAtTile(moves[currentMove].substring(0,2),true);
        Piece eatenPiece = getPieceAtTile(moves[currentMove].substring(3),true);
        piece.placePieceAtTile(moves[currentMove].substring(3),TableOrientation.WHITE_PLAYING,tileLength);
        if(eatenPiece!=null){
            eatenPiece.getImageView().setVisible(false);
        }

        if(currentMove == eatenPieces.size())
            eatenPieces.add(eatenPiece);

        currentMove++;
        backwardButton.setDisable(false);
        if(currentMove == moves.length)
            forwardButton.setDisable(true);

    }

    private void onBackwardButtonPress(){
        Piece piece = getPieceAtTile(moves[currentMove-1].substring(3),true);
        Piece eatenPiece = eatenPieces.get(currentMove-1);
        piece.placePieceAtTile(moves[currentMove-1].substring(0,2),TableOrientation.WHITE_PLAYING,tileLength);
        if(eatenPiece!=null){
            eatenPiece.getImageView().setVisible(true);
        }

        currentMove--;
        forwardButton.setDisable(false);
        if(currentMove==0)
            backwardButton.setDisable(true);
    }

    private void onBackButtonPress() throws IOException {
        sceneManager.swapScene(AvailableScene.MAIN_MENU_SCENE);
    }

    public void drawBoard(TableOrientation tableOrientation) {
        //drawing chessboard's tiles
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(columnIndex * tileLength);
                rectangle.setY(rowIndex * tileLength);
                rectangle.setHeight(tileLength);
                rectangle.setWidth(tileLength);
                if ((rowIndex + columnIndex) % 2 == 0)
                    rectangle.setFill(whiteTileColor);
                else
                    rectangle.setFill(blackTileColor);

                layout.getChildren().add(rectangle);
            }
        }

        int xOffSet = tileLength / 20;//5
        int yOffSet = tileLength / 5;
        //drawing chessboard's coordinates on tiles
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            Text text = new Text();
            if (tableOrientation.equals(TableOrientation.WHITE_PLAYING))
                text.setText(String.valueOf(8 - rowIndex));
            else
                text.setText(String.valueOf(rowIndex + 1));
            if (rowIndex % 2 == 0)
                text.setFill(blackTileColor);
            else
                text.setFill(whiteTileColor);

            text.setX(xOffSet);
            text.setY(tileLength * rowIndex + yOffSet);
            text.setFont(Font.font("Verdana", FontWeight.BOLD, tileLength / 5)); //20 before
            layout.getChildren().add(text);
        }

        xOffSet = 4 * tileLength / 5; //was 80
        yOffSet = 8 * tileLength - tileLength / 20; //simplify later //was 795 so 5 bits less of 100
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            Text text = new Text();
            if (tableOrientation.equals(TableOrientation.WHITE_PLAYING))
                text.setText(String.valueOf((char) ((int) 'a' + columnIndex)));
            else
                text.setText(String.valueOf((char) ((int) 'h' - columnIndex)));
            if (columnIndex % 2 == 1)
                text.setFill(blackTileColor);
            else
                text.setFill(whiteTileColor);

            text.setY(yOffSet);
            text.setX(tileLength * columnIndex + xOffSet);
            text.setFont(Font.font("Verdana", FontWeight.BOLD, tileLength / 5)); //20 before
            layout.getChildren().add(text);
        }
    }

    public void placePieces(TableOrientation tableOrientation) {
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            switch (columnIndex) {
                case 0: //rook
                case 7:
                    Piece whiteRook = new Piece(ChessPiece.ROOK, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, null);
                    Piece blackRook = new Piece(ChessPiece.ROOK, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, null);
                    pieces.add(whiteRook);
                    pieces.add(blackRook);
                    break;

                case 1: //knight
                case 6:
                    Piece whiteKnight = new Piece(ChessPiece.KNIGHT, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, null);
                    Piece blackKnight = new Piece(ChessPiece.KNIGHT, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, null);
                    pieces.add(whiteKnight);
                    pieces.add(blackKnight);
                    break;

                case 2: //bishop
                case 5:
                    Piece whiteBishop = new Piece(ChessPiece.BISHOP, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, null);
                    Piece blackBishop = new Piece(ChessPiece.BISHOP, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, null);
                    pieces.add(whiteBishop);
                    pieces.add(blackBishop);

                    break;

                case 3: //to check orientation of table
                    Piece whiteQueen = new Piece(ChessPiece.QUEEN, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, null);
                    Piece blackQueen = new Piece(ChessPiece.QUEEN, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, null);
                    pieces.add(whiteQueen);
                    pieces.add(blackQueen);
                    break;
                case 4:
                    Piece whiteKing = new Piece(ChessPiece.KING, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, null);
                    Piece blackKing = new Piece(ChessPiece.KING, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, null);
                    pieces.add(whiteKing);
                    pieces.add(blackKing);
                    break;
            }

            Piece whitePawn = new Piece(ChessPiece.PAWN, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "2", tableOrientation, client, null);
            Piece blackPawn = new Piece(ChessPiece.PAWN, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "7", tableOrientation, client, null);

            pieces.add(whitePawn);
            pieces.add(blackPawn);
        }
    }

    public Piece getPieceAtTile(String tile, boolean lookForVisible){
        for(Piece piece : pieces){
            if(piece.getTile().equals(tile) && ((piece.getImageView().isVisible() && lookForVisible) || (!piece.getImageView().isVisible() && !lookForVisible)))
                return piece;
        }
        return null;
    }


    @Override
    public Scene getScene() {
        return scene;
    }
}
