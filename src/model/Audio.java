package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.scene.media.MediaView;

import java.io.Serializable;

public class Audio extends Media implements Serializable {
    public Audio(String path) {
        super(path);
    }

    @Override
    public void load(MediaView mv) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public double getVolume(){
        return 0;
    }

    @Override
    public void setVolume(double volume) {
    }

    @Override
    public void avancer(double valeur) {
    }

    @Override
    public void reculer(double valeur) {
    }

    @Override
    public void recommencer() {

    }


}
