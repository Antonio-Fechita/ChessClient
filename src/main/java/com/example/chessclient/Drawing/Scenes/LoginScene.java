package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Utils.Utilities;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class LoginScene implements Drawable {
    Pane layout = new Pane();
    Button loginButton = new Button("Login");
    Button quitButton = new Button("Quit");
    Scene scene;
    SceneManager sceneManager;
    TextField usernameTextField = new TextField();
    TextField passwordTextField = new PasswordField();
    Client client;
    Text wrongDataText = new Text("Wrong data");
    Rectangle wrongDataTextBackground = new Rectangle();
    Group wrongDataGroup = new Group();

    public LoginScene(SceneManager sceneManager) throws IOException {
        ImageView background = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\background.jpg"));
        Text chessOnlineText = new Text("Chess online");
        chessOnlineText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\Black White.ttf", 120));
        chessOnlineText.setX(100);
        chessOnlineText.setY(120);
        chessOnlineText.setFill(Color.WHITE);


        Text usernameLabel = new Text("Username:");
        Text passwordLabel = new Text("Password:");


        usernameTextField.setPrefHeight(70);
        usernameTextField.setPrefWidth(350);
        usernameTextField.setLayoutX(50);
        usernameTextField.setLayoutY(250);

        passwordTextField.setPrefHeight(usernameTextField.getPrefHeight());
        passwordTextField.setPrefWidth(usernameTextField.getPrefWidth());
        passwordTextField.setLayoutX(usernameTextField.getLayoutX());
        passwordTextField.setLayoutY(usernameTextField.getLayoutY() + 170);

        usernameLabel.setX(usernameTextField.getLayoutX());
        usernameLabel.setY(usernameTextField.getLayoutY() - 20);

        passwordLabel.setX(passwordTextField.getLayoutX());
        passwordLabel.setY(passwordTextField.getLayoutY() - 20);

        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        passwordLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Utilities.setWidthHeightXY(loginButton,250,100,100,550);
        loginButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(quitButton,250,100,100,800);
        quitButton.setPadding(Insets.EMPTY);

        wrongDataText.setFill(Color.WHITE);
        wrongDataText.setX(100);
        wrongDataText.setY(950);
        wrongDataText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        wrongDataTextBackground.setX(wrongDataText.getX()-20);
        wrongDataTextBackground.setY(wrongDataText.getY() - 30);
        wrongDataTextBackground.setWidth(170);
        wrongDataTextBackground.setHeight(45);
        wrongDataTextBackground.setFill(Color.RED);

        wrongDataGroup.getChildren().add(wrongDataTextBackground);
        wrongDataGroup.getChildren().add(wrongDataText);
        wrongDataGroup.setVisible(false);

        layout.getChildren().add(background);
        layout.getChildren().add(loginButton);
        layout.getChildren().add(quitButton);
        layout.getChildren().add(chessOnlineText);
        layout.getChildren().add(usernameTextField);
        layout.getChildren().add(passwordTextField);
        layout.getChildren().add(passwordLabel);
        layout.getChildren().add(usernameLabel);
        layout.getChildren().add(wrongDataGroup);


        scene = new Scene(layout,1500,1000);
        scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/text-field.css").toExternalForm());


        loginButton.setOnAction(actionEvent -> {
            try {
                onLoginButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        quitButton.setOnAction(actionEvent -> Platform.exit());

        this.sceneManager = sceneManager;
        this.client = sceneManager.getClient();
//        this.wrongDataText.setVisible(false);
    }

    private void onLoginButtonPress() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        client.setLatestCommand("login " + username + " " + password);

        String response;
        do{
            response = client.getLatestResponse();
        }while (!response.startsWith("Wrong") && !response.startsWith("#@Tkn%"));


        if (response.equals("Wrong command")) {

            FadeTransition fader = createFader(wrongDataGroup);
            SequentialTransition transition = new SequentialTransition(wrongDataGroup, fader);
            transition.play();
        } else {
            sceneManager.swapScene(AvailableScene.MAIN_MENU_SCENE);
        }
    }

    private FadeTransition createFader(Node node) {
        wrongDataGroup.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);

        return fade;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
