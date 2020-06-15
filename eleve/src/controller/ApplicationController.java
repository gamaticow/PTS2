package controller;
/*
 *   Created by Corentin on 20/05/2020 at 00:52
 */

import core.ReconstitutionEleve;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Exercice;
import model.Partie;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    //TAG FXML
    @FXML private TabPane parties;
    @FXML private MediaView mv;
    @FXML private TextArea texte;
    @FXML private TextArea aideTexte;
    @FXML private Button aide;
    @FXML private Button solution;
    @FXML private Slider volumeSlider;
    @FXML private Slider progressSlider;
    @FXML private TextArea consigne;
    @FXML private Button validerProposition;
    @FXML private TextField entrerTexte;
    @FXML private VBox player;
    @FXML private ImageView hv;
    @FXML private ImageView ha;

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
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            Alert fileError = new Alert(Alert.AlertType.WARNING);
            fileError.setHeaderText("Fichier non compatible");
            fileError.setContentText("Le fichier ne peut pas être ouvert");
            fileError.show();
            return;
        }
        parties.getTabs().clear();
        for (Partie partie : exercice.getParties())
            parties.getTabs().add(new Tab(partie.getNom()));
        consigne.setText(exercice.getConsigne());
        changerPartie();
        if(!exercice.getSolution().isSolution_autorise())
            solution.setVisible(false);

        if(exercice.getMedia() == null)
            player.setVisible(false);
        else {
            player.setVisible(true);
            exercice.getMedia().initialize(mv, progressSlider);
        }

        validerProposition.setVisible(true);
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
            aideTexte.clear();
        }

        //cacher le bouton aide
        if(partie.getIndice().indiceUtilisable() && !partie.getIndice().indiceUtilise() && !exercice.isSolutionUtilise())
            aide.setVisible(true);
        else
            aide.setVisible(false);

        entrerTexte.clear();
    }

    public void rafraichirTexte(){
        if(exercice == null) return;

        Partie partie = getSelectedPartie();
        texte.setText(!exercice.isSolutionUtilise() ? partie.texteAAficherEtudiant() : partie.getTexte().getOriginal());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //chargerExercice("test.caft");
        entrerTexte.textProperty().addListener((observable, oldValue, newValue) -> {
            if(exercice == null)
                return;
            if(!exercice.isCorrespondance())
                return;
            if (getSelectedPartie().correspondance(newValue, exercice.isSensCasse(), exercice.isSensAccent())) rafraichirTexte();
            rafraichirTexte();
        });

        hv.setImage(new Image(ReconstitutionEleve.class.getClassLoader().getResourceAsStream("hv.png")));
        ha.setImage(new Image(ReconstitutionEleve.class.getClassLoader().getResourceAsStream("ha.png")));
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


    public void progressBarFin(){
        if(exercice == null) return;
        exercice.getMedia().goTo(progressSlider.getValue());
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

    public void valider(){
        if(exercice == null) return;
        if(getSelectedPartie().chercherMot(entrerTexte.getText(), false, exercice.isSensCasse(), exercice.isSensAccent())) rafraichirTexte();
        rafraichirTexte();
        entrerTexte.clear();
    }

    public void importer(){
        Stage primaryStage = (Stage) texte.getScene().getWindow();
        primaryStage.setOnCloseRequest(event -> {
            if(!quitter())
                event.consume();
        });
        FileChooser importFileChooser = new FileChooser();
        importFileChooser.setTitle("Ouvrir un exercice");
        importFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier exercice reconstitution", "*.caft"));
        File file = importFileChooser.showOpenDialog(null);
        if(file != null)
            chargerExercice(file.getAbsolutePath());
    }

    public boolean quitter(){
        if(exercice == null){
            System.exit(0);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Etes-vous sûr de vouloir quitter ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                System.exit(0);
                return true;
            }
        }
        return false;
    }

    public void entrerTexte(KeyEvent event) {
        if(exercice == null)
            return;
        if(event.getCode() == KeyCode.ENTER) {
            valider();
            event.consume();
        }
    }

    public void help(){
        if(Desktop.isDesktopSupported()){
            try{

                ClassLoader classLoader = getClass().getClassLoader();

                URL resource = classLoader.getResource("manuel_etudiant.pdf");
                if (resource == null) {
                    throw new IllegalArgumentException("file is not found!");
                } else {
                    Path dungeon_pdf = Files.createTempFile("Reconstitution", ".pdf");
                    try (InputStream is = getClass().getClassLoader().getResourceAsStream("manuel_etudiant.pdf")) {
                        Files.copy(is, dungeon_pdf, StandardCopyOption.REPLACE_EXISTING);
                    }
                    File dungeon = dungeon_pdf.toFile();
                    Desktop.getDesktop().open(dungeon_pdf.toFile());
                    dungeon.deleteOnExit();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
