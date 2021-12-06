package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private Label durationLabel;
    @FXML
    private Label playedTimeLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Button logOutButton;
    @FXML
    private Button minimizeButton;

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

        directory = new File("music");

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

        // the regex removes the .mp3 extension
        songLabel.setText(songs.get(songNumber).getName().replaceFirst("[.][^.]+$", ""));
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                String playedTime = formatSeconds(mediaPlayer.getCurrentTime().toSeconds());
                String duration = formatSeconds(media.getDuration().toSeconds());
                playedTimeLabel.setText(playedTime);
                durationLabel.setText(duration);
            }
        });
    }

    public String formatSeconds(double totalSecs){
        int minutes = (int) totalSecs / 60;
        int seconds = (int) totalSecs - minutes * 60;
        return String.format("%d:%02d", minutes, seconds);
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

    public void toggleMedia(){
        if(running){
            pauseMedia();
        } else{
            playMedia();
        }
    }

    public void previousMedia(){
        mediaPlayer.stop();

        if(running){
            cancelTimer();
        }

        if(songNumber > 0){
            songNumber--;
        } else{
            songNumber = songs.size() - 1;  // loop to last song if there's no previous song
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // need setOnReady here
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                String duration = formatSeconds(media.getDuration().toSeconds());
                durationLabel.setText(duration);
                songLabel.setText(songs.get(songNumber).getName().replaceFirst("[.][^.]+$", ""));
            }
        });

        playMedia();
    }

    public void nextMedia(){
        mediaPlayer.stop();

        if(running){
            cancelTimer();
        }

        if(songNumber < songs.size() - 1){
            songNumber++;
        } else{
            songNumber = 0;  // loop to first song if there's no next song
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                String duration = formatSeconds(media.getDuration().toSeconds());
                durationLabel.setText(duration);
                songLabel.setText(songs.get(songNumber).getName().replaceFirst("[.][^.]+$", ""));
            }
        });

        playMedia();
    }

    public void skipTo(double spot){
        double duration = media.getDuration().toSeconds();
        mediaPlayer.seek(Duration.seconds(duration * spot));
        //System.out.println("jumped to: " + duration * spot);
    }

    public void progressBarOnDragged(MouseEvent event){
        if(running){
            pauseMedia();
        }

        // update the progress bar
        Bounds bound = songProgressBar.getLayoutBounds();
        double mouseX = event.getSceneX();
        double percent = (((bound.getMinX() + mouseX)) / bound.getMaxX());
        songProgressBar.setProgress(percent);

        // update the played time label
        double duration = media.getDuration().toSeconds();
        double length = percent * duration;
        String playedTime = formatSeconds(length);
        if(0 < length && length < duration){
            Platform.runLater(() -> {
                playedTimeLabel.setText(playedTime);
            });
        }

    }

    public void progressBarOnReleased(MouseEvent event){
        Bounds bound = songProgressBar.getLayoutBounds();
        double mouseX = event.getSceneX();
        double percent = (((bound.getMinX() + mouseX)) / bound.getMaxX());
        if(percent < 0){  // restart the song if dragged to the origin
            skipTo(0);
        } else{
            skipTo(percent);
        }
        if(!running){
            playMedia();
        }
    }

    public void beginTimer(){
        timer = new Timer();

        task = new TimerTask(){
            @Override
            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();


                String playedTime = formatSeconds(mediaPlayer.getCurrentTime().toSeconds());
                Platform.runLater(() -> {
                    songProgressBar.setProgress(current / end);
                    playedTimeLabel.setText(playedTime);
                });

                if(current/end == 1){  // song ends

                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            nextMedia();
                        }
                    });

                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 100);  // update the progress bar every 0.1 second
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
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

    public void logout(ActionEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        if(running){
            pauseMedia();
        }
        stage.close();
    }

    public void minimize(ActionEvent event){
        // need this so hover works properly
        playButton.requestFocus();

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setIconified(true);
    }

}