package com.example.chessclient.Drawing.Scenes;

import com.example.chessclient.Client;
import com.example.chessclient.Drawable;
import com.example.chessclient.Drawing.Enums.AvailableScene;
import com.example.chessclient.Drawing.SceneManager;
import com.example.chessclient.Exceptions.BlankFieldException;
import com.example.chessclient.Exceptions.PasswordMatchingException;
import com.example.chessclient.Exceptions.RegistrationFailException;
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

public class RegisterScene implements Drawable {

    Pane layout = new Pane();
    Button registerButton = new Button("Register");
    Button quitButton = new Button("Quit");
    Scene scene;
    SceneManager sceneManager;
    TextField usernameTextField = new TextField();
    TextField passwordTextField = new PasswordField();
    TextField passwordConfirmationTextField = new PasswordField();
    Client client;
    Text wrongDataText = new Text("Registration failed");
    Rectangle wrongDataTextBackground = new Rectangle();
    Group wrongPasswordGroup = new Group();

    public RegisterScene(SceneManager sceneManager) {
        ImageView background = new ImageView(new Image("file:src\\main\\java\\com\\example\\chessclient\\Images\\background.jpg"));
        Text chessOnlineText = new Text("Chess online");
        chessOnlineText.setFont(Font.loadFont("file:src\\main\\resources\\fonts\\Black White.ttf", 120));
        chessOnlineText.setX(100);
        chessOnlineText.setY(120);
        chessOnlineText.setFill(Color.WHITE);


        Text usernameLabel = new Text("Username:");
        Text passwordLabel = new Text("Password:");
        Text passwordConfirmationLabel = new Text("Password confirmation:");


        usernameTextField.setPrefHeight(70);
        usernameTextField.setPrefWidth(350);
        usernameTextField.setLayoutX(50);
        usernameTextField.setLayoutY(210);

        passwordTextField.setPrefHeight(usernameTextField.getPrefHeight());
        passwordTextField.setPrefWidth(usernameTextField.getPrefWidth());
        passwordTextField.setLayoutX(usernameTextField.getLayoutX());
        passwordTextField.setLayoutY(usernameTextField.getLayoutY() + 170);

        passwordConfirmationTextField.setPrefHeight(usernameTextField.getPrefHeight());
        passwordConfirmationTextField.setPrefWidth(usernameTextField.getPrefWidth());
        passwordConfirmationTextField.setLayoutX(usernameTextField.getLayoutX());
        passwordConfirmationTextField.setLayoutY(passwordTextField.getLayoutY() + 170);

        usernameLabel.setX(usernameTextField.getLayoutX());
        usernameLabel.setY(usernameTextField.getLayoutY() - 20);

        passwordLabel.setX(passwordTextField.getLayoutX());
        passwordLabel.setY(passwordTextField.getLayoutY() - 20);

        passwordConfirmationLabel.setX(passwordConfirmationTextField.getLayoutX());
        passwordConfirmationLabel.setY(passwordConfirmationTextField.getLayoutY() - 20);

        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        passwordLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        passwordConfirmationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Utilities.setWidthHeightXY(registerButton, 250, 100, 100, 660);
        registerButton.setPadding(Insets.EMPTY);

        Utilities.setWidthHeightXY(quitButton, 250, 100, 100, 800);
        quitButton.setPadding(Insets.EMPTY);

        wrongDataText.setFill(Color.WHITE);
        wrongDataText.setX(100);
        wrongDataText.setY(950);
        wrongDataText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        wrongDataTextBackground.setX(wrongDataText.getX() - 20);
        wrongDataTextBackground.setY(wrongDataText.getY() - 30);
        wrongDataTextBackground.setWidth(Utilities.getTextWidth(wrongDataText) + 40);
        wrongDataTextBackground.setHeight(Utilities.getTextHeight(wrongDataText) + 30);
        wrongDataTextBackground.setFill(Color.RED);

        wrongPasswordGroup.getChildren().add(wrongDataTextBackground);
        wrongPasswordGroup.getChildren().add(wrongDataText);
        wrongPasswordGroup.setVisible(false);

        layout.getChildren().add(background);
        layout.getChildren().add(registerButton);
        layout.getChildren().add(quitButton);
        layout.getChildren().add(chessOnlineText);
        layout.getChildren().add(usernameTextField);
        layout.getChildren().add(passwordTextField);
        layout.getChildren().add(passwordConfirmationTextField);
        layout.getChildren().add(passwordLabel);
        layout.getChildren().add(usernameLabel);
        layout.getChildren().add(passwordConfirmationLabel);
        layout.getChildren().add(wrongPasswordGroup);


        scene = new Scene(layout, 1500, 1000);
        scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/text-field.css").toExternalForm());


        registerButton.setOnAction(actionEvent -> {
            try {
                onRegisterButtonPress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        quitButton.setOnAction(actionEvent -> Platform.exit());

        this.sceneManager = sceneManager;
        this.client = sceneManager.getClient();
//        this.wrongDataText.setVisible(false);
    }

    private void onRegisterButtonPress() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String confirmedPassword = passwordConfirmationTextField.getText();
        try{
        if (!password.equals(confirmedPassword)) {
            throw new PasswordMatchingException();
        } else
            if(username.isBlank() || password.isBlank()) {
                throw new BlankFieldException();
            }
            else {
                client.setLatestCommand("register " + username + " " + password);
                String response;
                do {
                    response = client.getLatestResponse();
                } while (!response.startsWith("USER") && !response.startsWith("REGISTRATION"));

                if (response.startsWith("USER")) { //registration successful

                    client.setLatestCommand("login " + username + " " + password);
                    do {
                        response = client.getLatestResponse();
                    } while (!response.startsWith("#@Tkn%"));

                    sceneManager.swapScene(AvailableScene.MAIN_MENU_SCENE);
                } else { //registration failed
                    throw new RegistrationFailException();
                }
            }

        }catch (BlankFieldException | PasswordMatchingException | RegistrationFailException e){
            FadeTransition fader = createFader(wrongPasswordGroup);
            SequentialTransition transition = new SequentialTransition(wrongPasswordGroup, fader);
            transition.play();
        }

    }

    private FadeTransition createFader(Node node) {
        wrongPasswordGroup.setVisible(true);
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
