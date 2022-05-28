package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
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

public class InitialScene implements Drawable {

    Pane layout = new Pane();
    Button loginButton = new Button("Login");
    Button registerButton = new Button("Register");
    Button quitButton = new Button("Quit");
    Scene scene;
    SceneManager sceneManager;


    public InitialScene(SceneManager sceneManager){


        ImageView background = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\background.jpg"));
        Text chessOnlineText = new Text("Chess online");
        chessOnlineText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\Black White.ttf",120));
        chessOnlineText.setX(100);
        chessOnlineText.setY(120);
        chessOnlineText.setFill(Color.WHITE);

        Utilities.setWidthHeightXY(loginButton,250,100,100,350);
        loginButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(registerButton,250,100, (int) loginButton.getLayoutX(), (int) (loginButton.getLayoutY() + 150));
        registerButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(quitButton,250,100, (int) loginButton.getLayoutX(),800);
        quitButton.setPadding(Insets.EMPTY);
        //250,100,loginButton.getLayoutX(),800


        layout.getChildren().add(background);
        layout.getChildren().add(loginButton);
        layout.getChildren().add(registerButton);
        layout.getChildren().add(quitButton);
        layout.getChildren().add(chessOnlineText);
        scene = new Scene(layout,1500,1000);
        scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());


        registerButton.setOnAction(actionEvent -> {
            try {
                onRegisterButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnAction(actionEvent -> {
            try {
                onLoginButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        quitButton.setOnAction(actionEvent -> Platform.exit());

        this.sceneManager = sceneManager;
    }

    private void onLoginButtonPress() throws IOException {
        sceneManager.swapScene(AvailableScene.LOGIN_SCENE);
    }

    private void onRegisterButtonPress() throws IOException {
        sceneManager.swapScene(AvailableScene.REGISTER_SCENE);
    }

    @Override
    public Scene getScene(){
        return scene;
    }

}
