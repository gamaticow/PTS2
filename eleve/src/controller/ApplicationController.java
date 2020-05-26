package controller;
/*
 *   Created by Corentin on 20/05/2020 at 00:52
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;
import model.Exercice;
import model.Partie;

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
            ois.close();
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
        if(exercice == null) return null;

        return exercice.getPartie(parties.getSelectionModel().getSelectedItem().getText());
    }

    public void changerPartie(){
        if(exercice == null) return;

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
        exercice.getMedia().initialize();
        exercice.getMedia().load(mv);
    }

    public void pausePlay(){
        if(exercice == null) return;

        if (exercice.getMedia().isPlaying()){
            exercice.getMedia().pause();
        } else{
            exercice.getMedia().play();
        }

    }

    public void recommencer(){
        if(exercice == null) return;

        exercice.getMedia().recommencer();
    }

    public void reculer(){
        if(exercice == null) return;

        exercice.getMedia().reculer(5);
    }

    public void avancer(){
        if(exercice == null) return;

        exercice.getMedia().avancer(5);
    }


    private double lastVolume = 10;
    public void mute(){
        if(exercice == null) return;
        System.out.println(lastVolume);

        if (exercice.getMedia().getVolume() == 0){
            exercice.getMedia().setVolume(lastVolume/100);
            volumeSlider.setValue(lastVolume);
        }else{
            exercice.getMedia().setVolume(0);
            volumeSlider.setValue(0);
        }
    }

    public void volumeSlider(){
        if(exercice == null) return;

        lastVolume = volumeSlider.getValue();
        exercice.getMedia().setVolume(lastVolume/100);
    }

    public void progressBarDebut() {
        if(exercice == null) return;

        sliderDebug = false;

    }

    public void progressBarFin(){
        /*if(exercice == null) return;

        mp.seek(Duration.seconds((progressSlider.getValue() * mp.getTotalDuration().toSeconds()) / 100));
        sliderDebug = true;*/
    }

    public void aide(){
        if(exercice == null) return;

        getSelectedPartie().getIndice().utiliserIndice();
        changerPartie();
    }

    public void solution(){
        if(exercice == null) return;

        exercice.getSolution().utiliseSolution();
        changerPartie();
        validerProposition.setVisible(false);
    }
}
