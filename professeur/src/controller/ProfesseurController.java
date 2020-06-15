package controller;

import core.ReconstitutionProfesseur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import model.Exercice;
import model.Media;
import model.MediaNotCompatibleException;
import model.Partie;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ProfesseurController implements Initializable {

    @FXML
    public Button importBtn;
    public CheckBox sensCasse;   //Sensibilité aux majuscules
    public CheckBox cb_solution;   //Bouton solution
    public CheckBox cb_motComplet;   //Remplacement partiel d'un mot
    public CheckBox cb_correspondance;   //Correspondance en temps réel
    public CheckBox sensAccent;   //Sensibilité aux accents
    public TabPane Sections;   //TabPane contenant toutes les sections
    public MediaView mv;
    public Slider progressSlider;
    public Slider volumeSlider;
    public TextArea texte;
    public TextArea aide;
    public TextArea consigne;
    public VBox player;
    public ImageView ha;
    public ImageView hv;

    private Exercice exercice;

    private boolean ignoreAdd = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newExercise();
        ignoreAdd = false;
        Sections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                return;
            if(newValue.getText().equals("+")){
                if(ignoreAdd){
                    ignoreAdd = false;
                    return;
                }
                if(Sections.getTabs().contains(oldValue))
                    Sections.getSelectionModel().select(oldValue);
                else
                    Sections.getSelectionModel().select(0);
                formNewTab();
            }else {
                changeTab();
            }
        });
        Sections.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        texte.textProperty().addListener((observable, oldValue, newValue) -> textFieldEdited(newValue));
        aide.textProperty().addListener((observable, oldValue, newValue) -> aideTextFieldEdited(newValue));
        consigne.textProperty().addListener((observable, oldValue, newValue) -> consigneTextFieldEdited(newValue));
        cb_solution.selectedProperty().addListener((observable, oldValue, newValue) -> exercice.getSolution().setSolution_autorise(newValue));
        cb_motComplet.selectedProperty().addListener((observable, oldValue, newValue) -> exercice.setMot_complet(newValue));
        cb_correspondance.selectedProperty().addListener((observable, oldValue, newValue) -> exercice.setCorrespondance(newValue));
        sensCasse.selectedProperty().addListener((observable, oldValue, newValue) -> exercice.setSensCasse(newValue));
        sensAccent.selectedProperty().addListener((observable, oldValue, newValue) -> exercice.setSensAccent(newValue));

        hv.setImage(new javafx.scene.image.Image(ReconstitutionProfesseur.class.getClassLoader().getResourceAsStream("hv.png")));
        ha.setImage(new Image(ReconstitutionProfesseur.class.getClassLoader().getResourceAsStream("ha.png")));
    }

    public void newExercise(){
        exercice = new Exercice();
        ignoreAdd = true;

        texte.clear();
        consigne.clear();
        aide.clear();
        cb_solution.setSelected(exercice.getSolution().isSolution_autorise());
        cb_motComplet.setSelected(exercice.isMot_complet());
        cb_correspondance.setSelected(exercice.isCorrespondance());
        sensCasse.setSelected(exercice.isSensCasse());
        sensAccent.setSelected(exercice.isSensAccent());

        player.setVisible(false);
        importBtn.setVisible(true);

        List<Tab> remove = new ArrayList<>();
        for(Tab t : Sections.getTabs()){
            if(t.getText().equals("+"))
                continue;
            remove.add(t);
        }
        Sections.getTabs().removeAll(remove);

        createTab("Partie 1");
    }

    public void changeTab(){
        Partie current = getSelectedPartie();
        if(current == null)
            return;

        texte.setText(current.getTexte().getOriginal());
        aide.setText(current.getIndice().getIndice());
    }

    public Partie getSelectedPartie(){
        return exercice.getPartie(Sections.getSelectionModel().getSelectedItem().getText());
    }

    public void dialogConfirmNewPrjct() {
        ButtonType buttonTypeOne = new ButtonType("Oui, sauvegarder");
        ButtonType buttonTypeTwo = new ButtonType("Ne pas sauvegarder");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = makeAlert("Nouveau", "Voulez vous enregistrer votre exercice avant de le quitter", buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        if (result.get() == buttonTypeOne){
            exporter();
        } else if (result.get() == buttonTypeTwo) {
            newExercise();
        }
    }

    public void dialogConfirmLeave() {
        ButtonType buttonTypeOne = new ButtonType("Oui, sauvegarder");
        ButtonType buttonTypeTwo = new ButtonType("Ne pas sauvegarder");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = makeAlert("Quitter", "Voulez vous enregistrer votre exercice avant de le quitter", buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        if (result.get() == buttonTypeOne){
            exporter();
        } else if (result.get() == buttonTypeTwo) {
            System.exit(0);
        }
    }

    public static Optional<ButtonType> makeAlert(String title, String headerText, ButtonType... btn){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        //alert.setContentText("Choose your option.");
        alert.getButtonTypes().setAll(btn);
        return alert.showAndWait();
    }

    public void confirmDeleteTab(Tab tab) {
        //alert.setContentText("Choose your option.");
        ButtonType buttonTypeOne = new ButtonType("Oui, supprimer");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = makeAlert("Supprimer la section", "Etes vous sûr de vouloir supprimer la section \""+ tab.getText() +"\" ?", buttonTypeOne, buttonTypeCancel);
        if (result.get() == buttonTypeOne)
            removeTab(tab);
    }

    public void formNewTab(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouvelle section");
        dialog.setHeaderText("Nom de la nouvelle section ?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (tabNameValidate(result.get())) {
                createTab(result.get());
            } else formNewTab();
        }
    }

    private void createTab(String name){
        Tab tab = new Tab(name);
        Tab add = Sections.getTabs().get(Sections.getTabs().size()-1);

        exercice.createPartie(name);
        Sections.getTabs().remove(add);
        Sections.getTabs().addAll(tab, add);
        Sections.getSelectionModel().select(tab);
        if(Sections.getTabs().size() > 3)
            tab.setClosable(true);
        else
            tab.setClosable(false);

        tab.setOnCloseRequest(event -> {
            event.consume();
            confirmDeleteTab(tab);
        });

        if(Sections.getTabs().size() == 3){
            for(Tab t : Sections.getTabs()){
                if(t.getText().equals("+"))
                    continue;
                t.setClosable(true);
            }
        }
    }

    private void removeTab(Tab tab){
        exercice.supprimerPartie(tab.getText());
        Sections.getTabs().remove(tab);

        if(Sections.getTabs().size() == 2)
            Sections.getTabs().get(0).setClosable(false);
    }

    public void renameSection() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Renommer : " + Sections.getSelectionModel().getSelectedItem().getText());
        dialog.setHeaderText("Nouveau nom de la section");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (tabNameValidate(result.get())) {
                Tab tab = Sections.getSelectionModel().getSelectedItem();
                exercice.getPartie(tab.getText()).setNom(result.get());
                tab.setText(result.get());
            }
            else renameSection();
        }
    }

    private boolean tabNameValidate(String name){
        return name.matches("^[a-zA-Z0-9][a-zA-Z0-9 ]*(?<! )$") && !exercice.sectionExiste(name);
    }

    public void importer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un exercice");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier exercice (.caft)", "*.caft"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
                Object o = ois.readObject();
                if(o instanceof Exercice){
                    ouvrirExercice((Exercice) o);
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
        }
    }

    private void ouvrirExercice(Exercice exercice){
        this.exercice = exercice;
        ignoreAdd = true;

        consigne.setText(exercice.getConsigne());
        cb_solution.setSelected(exercice.getSolution().isSolution_autorise());
        cb_motComplet.setSelected(exercice.isMot_complet());
        cb_correspondance.setSelected(exercice.isCorrespondance());
        sensCasse.setSelected(exercice.isSensCasse());
        sensAccent.setSelected(exercice.isSensAccent());

        if(exercice.getMedia() == null) {
            player.setVisible(false);
            importBtn.setVisible(true);
        }else{
            player.setVisible(true);
            importBtn.setVisible(false);
            exercice.getMedia().initialize(mv, progressSlider);
        }

        List<Tab> remove = new ArrayList<>();
        for(Tab t : Sections.getTabs()){
            if(t.getText().equals("+"))
                continue;
            remove.add(t);
        }
        Sections.getTabs().removeAll(remove);

        Tab add = Sections.getTabs().get(Sections.getTabs().size()-1);
        Sections.getTabs().remove(add);

        for (Partie partie : exercice.getParties())
            Sections.getTabs().add(new Tab(partie.getNom()));

        Sections.getTabs().add(add);

        Sections.getSelectionModel().select(0);

        if(Sections.getTabs().size() >= 3){
            for(Tab t : Sections.getTabs()){
                if(t.getText().equals("+"))
                    continue;
                t.setClosable(true);
            }
        }else if(Sections.getTabs().size() == 2)
            Sections.getTabs().get(0).setClosable(false);

        changeTab();
    }

    public void exporter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter");
        fileChooser.setInitialFileName("Nouvel exercice.caft");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier exercice", "*.caft"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if(!file.getName().endsWith(".caft"))
                file = new File(file.getAbsolutePath()+".caft");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(exercice);
                oos.close();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void importerVideo() throws MediaNotCompatibleException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un média");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Medias comptatibles (.mp4, .mp3)", "*.mp4", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            exercice.setMedia(Media.load(selectedFile.getAbsolutePath()));
            exercice.getMedia().initialize(mv, progressSlider);
            importBtn.setVisible(false);
            player.setVisible(true);
        }
    }

    public void supprimerVideo(){
        if(exercice.getMedia().isPlaying())
            exercice.getMedia().pause();
        exercice.setMedia(null);
        player.setVisible(false);
        importBtn.setVisible(true);
    }

    public void newProject() {
        dialogConfirmNewPrjct();
    }

    public void leave() {
        dialogConfirmLeave();
    }

    public void textFieldEdited(String newValue){
        Partie partie = getSelectedPartie();
        if(partie == null)
            return;

        partie.getTexte().setOriginal(newValue);
    }

    public void aideTextFieldEdited(String newValue){
        Partie partie = getSelectedPartie();
        if(partie == null)
            return;

        partie.getIndice().setIndice(newValue);
    }

    public void consigneTextFieldEdited(String newValue){
        exercice.setConsigne(newValue);
    }

    //===========================================================================PLAYER CONTROLLER

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

    public void help(){
        if(Desktop.isDesktopSupported()){
            try{

                ClassLoader classLoader = getClass().getClassLoader();

                URL resource = classLoader.getResource("manuel_professeur.pdf");
                if (resource == null) {
                    throw new IllegalArgumentException("file is not found!");
                } else {
                    Path dungeon_pdf = Files.createTempFile("Reconstitution", ".pdf");
                    try (InputStream is = getClass().getClassLoader().getResourceAsStream("manuel_professeur.pdf")) {
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