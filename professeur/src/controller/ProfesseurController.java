package controller;

import core.ReconstitutionProfesseur;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Exercice;

import javax.imageio.ImageIO;
import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class ProfesseurController {

    @FXML
    public Button importBtn;
    public TextArea Consigne;
    public TextArea Aide;
    public CheckBox CB1;   //Sensibilité aux majuscules
    public CheckBox CB2;   //Bouton solution
    public CheckBox CB3;   //Remplacement partiel d'un mot
    public CheckBox CB4;   //Bouton d'aide
    public CheckBox CB5;   //Correspondance en temps réel
    public CheckBox CB6;   //Sensibilité aux accents
    public TabPane Sections;   //TabPane contenant toutes les sections
    public Tab Section1;   //Section 1
    public Tab Section11;   //Section 1
    public Tab SectionAdd;
    public MenuItem HandicapVisuel;
    public MenuItem HandicapAuditif;

    private int NbSection = 2; //Nombre de section
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private Tab lastTab;
    private Boolean aBoolean = false;

    public void initialize() {
        aBoolean = false;
        Consigne.setText(null);
        Aide.setText(null);
        Aide.setEditable(true);
        Aide.setPromptText(null);
        CB1.setSelected(false);
        CB2.setSelected(false);
        CB3.setSelected(false);
        CB4.setSelected(true);
        CB5.setSelected(false);
        CB6.setSelected(false);
        Sections.getTabs().removeAll(Sections.getTabs());
        Sections.getTabs().removeAll(Sections.getTabs());
        Sections.getTabs().addAll(SectionAdd, Section1, Section11);
        lastTab = Section1;
        NbSection=1;
        Sections.getSelectionModel().select(Section1);
        aBoolean = true;
    }

    public void dialogConfirmNewPrjct() {
        alert.setTitle("Nouveau");
        alert.setHeaderText("Voulez vous enregistrer votre exercice avant de le quitter");
        //alert.setContentText("Choose your option.");
        ButtonType buttonTypeOne = new ButtonType("Oui, sauvegarder");
        ButtonType buttonTypeTwo = new ButtonType("Ne pas sauvegarder");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            importer();
        } else if (result.get() == buttonTypeTwo) {
            initialize();
        }
    }

    public void dialogConfirmLeave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nouveau");
        alert.setHeaderText("Voulez vous enregistrer votre exercice avant de le quitter");
        //alert.setContentText("Choose your option.");
        ButtonType buttonTypeOne = new ButtonType("Oui, sauvegarder");
        ButtonType buttonTypeTwo = new ButtonType("Ne pas sauvegarder");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            importer();
        } else if (result.get() == buttonTypeTwo) {
            ReconstitutionProfesseur.leave();
        }
    }

    public void dialogConfirmDeleteSection() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer la section");
        alert.setHeaderText("Etes vous sûr de vouloir supprimer la section ?");
        //alert.setContentText("Choose your option.");
        ButtonType buttonTypeOne = new ButtonType("Oui, supprimer");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            aBoolean = false;
            deleteSection();
        }
    }

    public void renameSection() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Renommer : " + Sections.getSelectionModel().getSelectedItem().getText());
        dialog.setHeaderText("Nouveau nom de la section");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().length() >= 1) {
                aBoolean = false;
                Sections.getSelectionModel().getSelectedItem().setText(result.get());
            } else renameSection();
        }
    }

    public void updateTab() {
        if (Sections.getSelectionModel().getSelectedItem() != lastTab) {
            if (Sections.getSelectionModel().getSelectedItem().getText().equals("+")) if (aBoolean == true || NbSection < 1) addSection();
        }
        lastTab = Sections.getSelectionModel().getSelectedItem();
    }

    public void addSection() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouvelle section");
        dialog.setHeaderText("Nom de la nouvelle section ?");
        //dialog.setContentText("Nom de la nouvelle section :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().length() >= 1) {
                Sections.getTabs().addAll(new Tab(result.get()));
                Sections.getSelectionModel().selectNext();
                aBoolean = true;
                NbSection++;
            } else addSection();
        } else {
            if (NbSection < 1) addSection();
            else aBoolean = true;
        }
    }

    public void deleteSection() {
        Sections.getTabs().remove(Sections.getSelectionModel().getSelectedItem());
        Sections.getSelectionModel().selectNext();
        NbSection--;
        aBoolean = true;
        if (Sections.getSelectionModel().getSelectedItem() == SectionAdd) {
            addSection();
        }
    }

    public void cb4() {
        if (CB4.isSelected()) {
            Aide.setEditable(true);
            Aide.setText(null);
        }
        else {
            Aide.setEditable(false);
            Aide.setText("Cette option est désactivée");
        }
    }

    public void importer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un exercice");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier comptatible (.rct)", "*.rct"),
                new FileChooser.ExtensionFilter("Tous les fichier", "*.*"));
        Stage mainStage = ReconstitutionProfesseur.primaryStage;
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            say("cc");
        }
    }

    public void exporter() {
        Stage stage = ReconstitutionProfesseur.primaryStage;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            /*try {
                ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }*/
        }
    }

    public void importerVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un média");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Medias comptatibles (.mp4, .mp3, .wav, .aac)", "*.mp4", "*.mp3", "*.wav", "*.aac"),
                new FileChooser.ExtensionFilter("Tous les fichier", "*.*"));
        Stage mainStage = ReconstitutionProfesseur.primaryStage;
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            say("cc");
        }
    }

    public void actuHandicapVisuel() {
        if (HandicapVisuel.getText() == "Handicap Visuel (actif)") HandicapVisuel.setText("Handicap Visuel (innactif)");
        else HandicapVisuel.setText("Handicap Visuel (actif)");
    }

    public void actuHandicapAuditif() {
        if (HandicapAuditif.getText() == "Handicap Auditif (actif)") HandicapAuditif.setText("Handicap Auditif (innactif)");
        else HandicapAuditif.setText("Handicap Auditif (actif)");
    }

    public void newProject() {
        dialogConfirmNewPrjct();
    }

    public void leave() {
        dialogConfirmLeave();
    }

    public void say(String text) {
        System.out.println(text);
    }

}