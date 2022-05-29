package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.ChessColor;
import com.example.chessclient.Drawing.Enums.ChessPiece;
import com.example.chessclient.Drawing.Enums.TableOrientation;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Piece;
import com.example.chessclient.Threads.BoardListener;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PlayingScene implements Drawable {

    Color blackTileColor = Color.CORAL;
    Color whiteTileColor = Color.BEIGE;
    int tileLength;
    List<Piece> pieces = new ArrayList<>();
    TableOrientation pov;
    Scene scene;
    Client client;
    Pane layout;
    int chatWidth;
    String opponentName = "opponent";
    SceneManager sceneManager;

    public TableOrientation getPov() {
        return pov;
    }

    public int getTileLength() {
        return tileLength;
    }

    public PlayingScene(Client client, int tileLength, TableOrientation pov,SceneManager sceneManager) {
        this.client = client;
        this.tileLength = tileLength;
        this.pov = pov;
        this.chatWidth = tileLength * 4;
        this.layout = new Pane();
        this.sceneManager = sceneManager;
        
        drawBoard(pov);
        Chat chat = new Chat(tileLength,client,layout,sceneManager);
        chat.drawChat();
        placePieces(pov);
        scene = new Scene(layout, 8 * tileLength + chatWidth, 8 * tileLength);
        new Thread(new BoardListener(client,chat,this)).start();
    }


    public Piece getPieceAtTile(String tile){
        for(Piece piece : pieces){
            if(piece.getTile().equals(tile))
                return piece;
        }
        return null;
    }

    public void removePiece(Piece piece){
        if(piece!=null){
            layout.getChildren().remove(piece.getImageView());
            pieces.remove(piece);
        }
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
                    Piece whiteRook = new Piece(ChessPiece.ROOK, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, this);
                    Piece blackRook = new Piece(ChessPiece.ROOK, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, this);
                    pieces.add(whiteRook);
                    pieces.add(blackRook);
                    break;

                case 1: //knight
                case 6:
                    Piece whiteKnight = new Piece(ChessPiece.KNIGHT, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, this);
                    Piece blackKnight = new Piece(ChessPiece.KNIGHT, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, this);
                    pieces.add(whiteKnight);
                    pieces.add(blackKnight);
                    break;

                case 2: //bishop
                case 5:
                    Piece whiteBishop = new Piece(ChessPiece.BISHOP, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, this);
                    Piece blackBishop = new Piece(ChessPiece.BISHOP, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, this);
                    pieces.add(whiteBishop);
                    pieces.add(blackBishop);

                    break;

                case 3: //to check orientation of table
                    Piece whiteQueen = new Piece(ChessPiece.QUEEN, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, this);
                    Piece blackQueen = new Piece(ChessPiece.QUEEN, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, this);
                    pieces.add(whiteQueen);
                    pieces.add(blackQueen);
                    break;
                case 4:
                    Piece whiteKing = new Piece(ChessPiece.KING, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "1", tableOrientation, client, this);
                    Piece blackKing = new Piece(ChessPiece.KING, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "8", tableOrientation, client, this);
                    pieces.add(whiteKing);
                    pieces.add(blackKing);
                    break;
            }

            Piece whitePawn = new Piece(ChessPiece.PAWN, ChessColor.WHITE, tileLength, layout, (char) ((int) 'A' + columnIndex) + "2", tableOrientation, client, this);
            Piece blackPawn = new Piece(ChessPiece.PAWN, ChessColor.BLACK, tileLength, layout, (char) ((int) 'A' + columnIndex) + "7", tableOrientation, client, this);

            pieces.add(whitePawn);
            pieces.add(blackPawn);
        }
    }


    @Override
    public Scene getScene() {
        return scene;
    }

}
