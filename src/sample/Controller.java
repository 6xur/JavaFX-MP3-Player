package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable{

    @FXML
    private Button playButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label songLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Button logOutButton;

    private Media media;
    private  MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber = 0;

    private Timer timer;
    private TimerTask task;
    boolean running = false;

    // for dragging the window
    double dX;
    double dY;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs = new ArrayList<>();

        directory = new File("C:\\Users\\Robert\\Music");

        files = directory.listFiles();

        // add only the mp3 files to songs
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
        beginTimer();
        mediaPlayer.play();
        playButton.setText("⏸");
    }

    public void pauseMedia(){
        cancelTimer();
        mediaPlayer.pause();
        playButton.setText("▶");
    }

    public void playOrPause(){
        if(running){
            pauseMedia();
        } else{
            playMedia();
        }
    }

    public void jumpTo(double spot){
        double totalDuration = media.getDuration().toSeconds();
        songProgressBar.setProgress(spot);
        mediaPlayer.seek(Duration.seconds(totalDuration * spot));
        System.out.println("jumped to: " + totalDuration * spot);
    }

    public void previousMedia(){
        if(songNumber > 0){
            songNumber--;
        } else{
            songNumber = songs.size() - 1;
        }
        mediaPlayer.stop();

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        if(running){
            cancelTimer();
        }

        songLabel.setText(songs.get(songNumber).getName());

        playMedia();
    }

    public void nextMedia(){
        if(songNumber < songs.size() - 1){
            songNumber++;
        } else{
            songNumber = 0;
        }
        mediaPlayer.stop();

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        if(running){
            cancelTimer();  // running becomes false
        }

        songLabel.setText(songs.get(songNumber).getName());

        playMedia();
    }


    public void setOnMousePressed(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        dX = stage.getX() - event.getScreenX();
        dY = stage.getY() - event.getScreenY();
    }


    public void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() + dX);
        stage.setY(event.getScreenY() + dY);
    }

    public void progressBarOnDragged(MouseEvent event){
        if(running){
            pauseMedia();
        }
        Bounds b1 = songProgressBar.getLayoutBounds();
        double mouseX = event.getSceneX();
        double percent = (((b1.getMinX() + mouseX)) / b1.getMaxX());
        songProgressBar.setProgress(percent);
    }

    public void progressBarOnReleased(MouseEvent event){
        Bounds b1 = songProgressBar.getLayoutBounds();
        double mouseX = event.getSceneX();
        double percent = (((b1.getMinX() + mouseX)) / b1.getMaxX());
        jumpTo(percent);
        if(!running){
            playMedia();
        }
        System.out.println(percent);
    }

    public void beginTimer(){
        timer = new Timer();

        task = new TimerTask(){
            @Override
            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current / end);

                if(current/end == 1){  // song ends
                    //mediaPlayer.stop();

                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            nextMedia();
                        }
                    });

                    //TODO: understand how the progress bar works
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 100);  // update the progress bar every 0.1 second
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }

    public void logout(ActionEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        if(running){
            pauseMedia();
        }
        stage.close();
    }

    public void minimize(ActionEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setIconified(true);
    }

}