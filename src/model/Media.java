package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Media implements Serializable {

    private byte[] content;
    private String extention;
    protected double duration;

    protected String tempFile;

    private javafx.scene.media.Media media;
    private MediaPlayer mediaPlayer;

    private Media(String path){
        extention = path.split("\\.")[path.split("\\.").length-1];
        try {
            content = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Media load(String path) throws MediaNotCompatibleException {
        if(path.endsWith(".mp4") || path.endsWith(".mp3")){
            return new Media(path);
        }
        throw new MediaNotCompatibleException(path.split("\\.")[path.split("\\.").length-1]);
    }


    public double getDuration(){
        return duration;
    }

    /**
     * Initialise la vidéo dans les fichier temporaire de l'utilisateur. Puis lie la video a la MediaView passé en paramètre
     * @param mv MediaView où la vidéo doit être lu
     */
    public void initialize(MediaView mv, Slider progressSlider) {
        try {
            File file = File.createTempFile("Reconstitution", "." + extention);
            file.deleteOnExit();
            OutputStream os = new FileOutputStream(file.getAbsolutePath());
            os.write(content);
            os.close();
            tempFile = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        media = new javafx.scene.media.Media(new File(tempFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mv.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(false);
        duration = mediaPlayer.getTotalDuration().toSeconds();
        mv.fitWidthProperty();
        mv.fitWidthProperty();

        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, current) ->{
            if(!progressSlider.isPressed())
                progressSlider.setValue((current.toSeconds() / mediaPlayer.getTotalDuration().toSeconds()) * 100);
        });
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public double getVolume(){
        return mediaPlayer.getVolume();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void avancer(double valeur) {
        double temps = mediaPlayer.getCurrentTime().toSeconds() + valeur;
        if (temps > duration)
            temps = duration;
        mediaPlayer.seek(Duration.seconds(temps));
    }

    public void reculer(double valeur) {
        double temps = mediaPlayer.getCurrentTime().toSeconds() - valeur;
        if (temps < 0)
            temps = 0;
        mediaPlayer.seek(Duration.seconds(temps));
    }

    public void recommencer() {
        mediaPlayer.seek(Duration.ZERO);
    }

    public void goTo(double percentage) {
        mediaPlayer.seek(Duration.seconds((percentage * mediaPlayer.getTotalDuration().toSeconds()) / 100));
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(content);
        oos.writeUTF(extention);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        content = (byte[]) ois.readObject();
        extention = ois.readUTF();
    }
}
