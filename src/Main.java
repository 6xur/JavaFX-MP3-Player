import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
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
                        controller.toggleMedia();
                        break;
                    case DIGIT0:
                        controller.skipTo(0);
                        break;
                    case DIGIT1:
                        controller.skipTo(0.1);
                        break;
                    case DIGIT2:
                        controller.skipTo(0.2);
                        break;
                    case DIGIT3:
                        controller.skipTo(0.3);
                        break;
                    case DIGIT4:
                        controller.skipTo(0.4);
                        break;
                    case DIGIT5:
                        controller.skipTo(0.5);
                        break;
                    case DIGIT6:
                        controller.skipTo(0.6);
                        break;
                    case DIGIT7:
                        controller.skipTo(0.7);
                        break;
                    case DIGIT8:
                        controller.skipTo(0.8);
                        break;
                    case DIGIT9:
                        controller.skipTo(0.9);
                        break;
                }
            }
        });

        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}