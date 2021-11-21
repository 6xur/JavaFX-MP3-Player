package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private Button playButton;
    @FXML
    private Label songLabel;
    @FXML
    private AnchorPane anchorPane;

    private Media media;
    private  MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber = 0;

    boolean running = false;

    double dX;
    double dY;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs = new ArrayList<>();

        //directory = new File("music");
        directory = new File("music");

        files = directory.listFiles();

        if(files != null){
            for(File file : files){
                String fileName = file.getName();
                int i = fileName.lastIndexOf(".");
                String extension = fileName.substring(i + 1);
                if(extension.equals("mp3")){
                    songs.add(file);
                    System.out.println(file + " added to songs");
                }
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());
    }

    public void playMedia(){
        if(running){
            mediaPlayer.pause();
            playButton.setText("▶");
            running = false;
        } else{
            mediaPlayer.play();
            playButton.setText("⏸");
            running = true;
        }
    }

    public void lighten(){
        playButton.setTextFill(Color.BEIGE);
    }

    public void darken(){
        playButton.setTextFill(Color.LIGHTGREY);
    }

    @FXML
    protected void setOnMousePressed(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        dX = stage.getX() - event.getScreenX();
        dY = stage.getY() - event.getScreenY();
    }

    @FXML
    protected void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() + dX);
        stage.setY(event.getScreenY() + dY);
    }


}