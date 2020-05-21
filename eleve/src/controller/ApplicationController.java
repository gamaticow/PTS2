package controller;
/*
 *   Created by Corentin on 20/05/2020 at 00:52
 */

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.media.MediaView;
import model.Exercice;
import model.Partie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ApplicationController {

    //TAG FXML
    //TODO Modifier les types si besoin
    @FXML private MediaView media;
    @FXML private TextArea texte;

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
                return;
            }
        } catch (IOException | ClassNotFoundException e) {
            Alert fileError = new Alert(Alert.AlertType.WARNING);
            fileError.setHeaderText("Une erreur c'est produite");
            fileError.setContentText("Une erreur interne c'est produite");
            fileError.show();
        }finally {
            //TODO mettre la première partie de l'exercice
            demarerPartie(null);
        }
    }

    private void demarerPartie(Partie partie){
        //TODO afficher le texte de la partie
        //TODO cacher le bouton indice s'il n'est pas disponible
        //TODO cacher le bouton solution si elle n'est pas autorise
        texte.setText(partie.texteAAficherEtudiant());
    }

    //TODO faire tout le reste

}
