package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.scene.media.MediaView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Media implements Serializable {

    private byte[] content;
    private final String extention;
    protected double duration;

    protected String tempFile;

    public Media(String path){
        extention = path.split("\\.")[path.split("\\.").length-1];
        try {
            content = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Media load(String path) throws MediaNotCompatibleException {
        if(path.endsWith(".mp4")){
            return new Video(path);
        }
        throw new MediaNotCompatibleException(path.split("\\.")[path.split("\\.").length-1]);
    }

    public void initialize(){
        try {
            File file = File.createTempFile("Reconstitution", "." + extention);
            OutputStream os = new FileOutputStream(file.getAbsolutePath());
            os.write(content);
            os.close();
            tempFile = file.getAbsolutePath();
            System.out.println(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDuration(){
        return duration;
    }

    public abstract void load(MediaView mv);
    public abstract boolean isPlaying();
    public abstract void play();
    public abstract void pause();
    public abstract double getVolume();
    public abstract void setVolume(double value);
    public abstract void avancer(double valeur);
    public abstract void reculer(double valeur);
    public abstract void recommencer();

}
