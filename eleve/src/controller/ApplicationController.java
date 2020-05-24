package controller;
/*
 *   Created by Corentin on 20/05/2020 at 00:52
 */

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import model.Exercice;
import model.Partie;
import model.Solution;
import model.Texte;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class ApplicationController implements Initializable {

    //TAG FXML
    @FXML private TabPane parties;
    @FXML private MediaView mv;
    @FXML private TextArea texte;
    @FXML private TextArea aideTexte;
    @FXML private Media me;
    private MediaPlayer mp;
    @FXML private Button playPause;
    @FXML private Button mute;
    @FXML private Button avancer;
    @FXML private Button reculer;
    @FXML private Button recommencer;
    @FXML private Button aide;
    @FXML private Button solution;
    @FXML private Slider volumeSlider;
    @FXML private Slider progressSlider;
    @FXML private TextArea consigne;
    @FXML private Button validerProposition;

    private boolean sliderDebug = true; //permet de debug le multiclique pour changer le temps de la vidéo

    //Exercice en cours par l'etudiant
    //Si null aucun exercice n'est chargé
    private Exercice exercice;

    /**
     * Permet de charger l'exercice depuis le chemin passer en paramètre
     * @param path Chemin du fichier de l'exercie
     */
    private void chargerExercice(String path) {
        try {
            File exerciceFile = new File(path);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(exerciceFile));
            Object o = ois.readObject();
            if(o instanceof Exercice){
                exercice = (Exercice) o;
            }else{
                Alert fileError = new Alert(Alert.AlertType.WARNING);
                fileError.setHeaderText("Fichier non compatible");
                fileError.setContentText("Le fichier ne peut pas être ouvert");
                fileError.show();
            }
        } catch (IOException | ClassNotFoundException e) {
            Alert fileError = new Alert(Alert.AlertType.WARNING);
            fileError.setHeaderText("Une erreur c'est produite");
            fileError.setContentText("Une erreur interne c'est produite");
            fileError.show();
        }finally {
            for (Partie partie : exercice.getParties()){
                parties.getTabs().add(new Tab(partie.getNom()));
            }
            consigne.setText(exercice.getConsigne());
            changerPartie();
            if(!exercice.getSolution().isSolution_autorise())
                solution.setVisible(false);
        }
    }

    public Partie getSelectedPartie(){
        return exercice.getPartie(parties.getSelectionModel().getSelectedItem().getText());
    }

    public void changerPartie(){
        Partie partie = getSelectedPartie();
        texte.setText(!exercice.isSolutionUtilise() ? partie.texteAAficherEtudiant() : partie.getTexte().getOriginal());

        if(partie.getIndice().indiceUtilise()){
            aideTexte.setText(partie.getIndice().getIndice());
        }else {
            aideTexte.setText("");
        }

        //cacher le bouton aide
        if(partie.getIndice().indiceUtilisable() && !partie.getIndice().indiceUtilise() && !exercice.isSolutionUtilise())
            aide.setVisible(true);
        else
            aide.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        chargerExercice("test.caft");
        /*String path = new File("I:/video/Takagi san/Karakai Jouzu No Takagi-San 1.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(false);
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
        });*/

    }

    public void pausePlay(){
        if (PLAYING == mp.getStatus()){
            mp.pause();
        } else{
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

    public void aide(){
        getSelectedPartie().getIndice().utiliserIndice();
        changerPartie();
    }

    public void solution(){
        exercice.getSolution().utiliseSolution();
        changerPartie();
        validerProposition.setVisible(false);
    }
}
