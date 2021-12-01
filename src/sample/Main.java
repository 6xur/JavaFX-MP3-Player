package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()){
                    case SPACE:
                        controller.playOrPause();
                        break;
                    case DIGIT0:
                        controller.jumpTo(0);
                        break;
                    case DIGIT1:
                        controller.jumpTo(1);
                        break;
                    case DIGIT2:
                        controller.jumpTo(2);
                        break;
                    case DIGIT3:
                        controller.jumpTo(3);
                        break;
                    case DIGIT4:
                        controller.jumpTo(4);
                        break;
                    case DIGIT5:
                        controller.jumpTo(5);
                        break;
                    case DIGIT6:
                        controller.jumpTo(6);
                        break;
                    case DIGIT7:
                        controller.jumpTo(7);
                        break;
                    case DIGIT8:
                        controller.jumpTo(8);
                        break;
                    case DIGIT9:
                        controller.jumpTo(9);
                        break;
                }
            }
        });

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}