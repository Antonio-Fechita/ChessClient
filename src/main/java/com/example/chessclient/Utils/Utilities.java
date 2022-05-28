package com.example.chessclient.Utils;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Utilities {


    static public void setWidthHeightXY(Button button, int width, int height, int x, int y){
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    static public  int getTextWidth(Text text){
        Scene scene = new Scene(new Group(text));
        return (int) text.getLayoutBounds().getWidth();

    }

    static public int getTextHeight(Text text){
        Scene scene = new Scene(new Group(text));
        return (int) text.getLayoutBounds().getHeight();
    }
    static public int getHeightOfFirstLine(Text text){
        String message = text.getText();
        for(int i=0;i<message.length();i++){
            if(message.charAt(i) == '\n'){
                Text text1 = new Text(message.substring(0,i));
                return getTextHeight(text1);
            }
        }
        return getTextHeight(text);
    }

}
