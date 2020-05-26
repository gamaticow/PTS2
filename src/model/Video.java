package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.Serializable;

public class Video extends Media implements Serializable {

    private javafx.scene.media.Media media;
    private MediaPlayer mediaPlayer;

    public Video(String path){
        super(path);
    }

    @Override
    public void load(MediaView mv) {
        media = new javafx.scene.media.Media(new File(tempFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mv.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(false);
        duration = mediaPlayer.getTotalDuration().toSeconds();
        mv.fitWidthProperty();
        mv.fitWidthProperty();

        /*mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
                if (sliderDebug) progressSlider.setValue((current.toSeconds() / mp.getTotalDuration().toSeconds()) * 100);
            }
        });*/
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @Override
    public void play() {
        mediaPlayer.play();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public double getVolume(){
        return mediaPlayer.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void avancer(double valeur) {
        double temps = mediaPlayer.getCurrentTime().toSeconds() + valeur;
        if (temps > duration)
            temps = duration;
        mediaPlayer.seek(Duration.seconds(temps));
    }

    @Override
    public void reculer(double valeur) {
        double temps = mediaPlayer.getCurrentTime().toSeconds() - valeur;
        if (temps < 0)
            temps = 0;
        mediaPlayer.seek(Duration.seconds(temps));
    }

    @Override
    public void recommencer() {
        mediaPlayer.seek(Duration.ZERO);
    }


}