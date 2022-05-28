module com.example.chessclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chessclient to javafx.fxml;
    exports com.example.chessclient;
    exports com.example.chessclient.Drawing;
    opens com.example.chessclient.Drawing to javafx.fxml;
    exports com.example.chessclient.Drawing.Enums;
    opens com.example.chessclient.Drawing.Enums to javafx.fxml;
    exports com.example.chessclient.Drawing.Scenes;
    opens com.example.chessclient.Drawing.Scenes to javafx.fxml;
    exports com.example.chessclient.Utils;
    opens com.example.chessclient.Utils to javafx.fxml;
}