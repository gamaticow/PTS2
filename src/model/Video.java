package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class Video implements Initializable{
    private boolean sliderDebug = true; //permet de debug le multiclique pour changer le temps de la vidéo

    public static int compteurDebug = -1; //permet de debug le fait que la fonction progressBarDebut soit appelé au debut et a la fin du changement de temps.
    @FXML
    private MediaView mv;
    private MediaPlayer mp;
    private Media me;

    @FXML
    private Button playPause;

    @FXML
    private Button mute;

    @FXML
    private Button avancer;

    @FXML
    private Button reculer;

    @FXML
    private Button recommencer;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressSlider;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        String path = new File("I:/video/Takagi san/Karakai Jouzu No Takagi-San 1.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitWidthProperty();
        volumeSlider.setValue(mp.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volumeSlider.getValue() / 100);
            }
        });
        mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
                if (sliderDebug) progressSlider.setValue((current.toSeconds() / mp.getTotalDuration().toSeconds()) * 100);
            }
        });

    }

    public void pausePlay(){
        if (PLAYING == mp.getStatus()){
            mp.pause();
        }
        else{
            mp.play();
        }

    }

    public void recommencer(){
        mp.stop();
        mp.play();
    }

    public void reculer(){
       Double temps;
       temps = mp.getCurrentTime().toSeconds() - 5;
       mp.seek(Duration.seconds(temps));
    }

    public void avancer(){
        Double temps;
        temps = mp.getCurrentTime().toSeconds() + 5;
        mp.seek(Duration.seconds(temps));
    }

    public void mute(){
        double volumeBase = 10;
        if (mp.getVolume() != 0){
            mp.setVolume(0);
        }else{
            mp.setVolume(volumeBase);
        }
    }

    public void progressBarDebut() {
        sliderDebug = false;

    }

    public void progressBarFin(){
        mp.seek(Duration.seconds((progressSlider.getValue() * mp.getTotalDuration().toSeconds()) / 100));
        sliderDebug = true;
    }
}