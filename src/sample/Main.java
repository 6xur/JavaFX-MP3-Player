package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
//        String bip = "High Pressure Dave-Ny_HsSmiL9A.mp3";
//        Media hit = new Media(new File(bip).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(hit);
//
//
//        Button button = new Button("click me");
//        button.setOnMouseClicked(mouseEvent -> {
//            mediaPlayer.play();
//        });
//
//        Button pauseButton = new Button("pause");
//        pauseButton.setOnMouseClicked(mouseEvent -> {
//            mediaPlayer.pause();
//        });
//
//
//        Group root = new Group();
//        root.getChildren().add(button);
//        root.getChildren().add(pauseButton);
//        Scene scene = new Scene(root, 300, 300);
//        //Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
//        stage.setTitle("Hello World");
//        stage.setScene(scene);
//        stage.show();

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Music App");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
