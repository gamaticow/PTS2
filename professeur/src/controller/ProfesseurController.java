package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import model.Exercice;
import model.Media;
import model.MediaNotCompatibleException;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class ProfesseurController implements Initializable {

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
    public Tab SectionAdd;
    public MenuItem HandicapVisuel;
    public MenuItem HandicapAuditif;
    public MediaView mv;

    private int NbSection = 2; //Nombre de section
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private Tab lastTab;
    private Boolean aBoolean = false;

    private Exercice exercice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aBoolean = false;
        exercice = new Exercice();

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

        Tab section1 = new Tab("Partie 1");
        Tab section2 = new Tab("Partie 2");

        Sections.getTabs().addAll(SectionAdd, section1, section2);

        exercice.createPartie(section1.getText());
        exercice.createPartie(section2.getText());

        lastTab = section1;
        NbSection=1;
        Sections.getSelectionModel().select(section1);
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
            exporter();
        } else if (result.get() == buttonTypeTwo) {
            initialize(null, null);
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
            exporter();
        } else if (result.get() == buttonTypeTwo) {
            System.exit(0);
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
            if (result.get().length() >= 1 && !exercice.sectionExiste(result.get())) {
                Sections.getTabs().addAll(new Tab(result.get()));
                Sections.getSelectionModel().selectNext();
                exercice.createPartie(result.get());
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
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            say("cc");
        }
    }

    public void exporter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            /*try {
                ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }*/
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
            exercice.getMedia().initialize();
            exercice.getMedia().load(mv);
            importBtn.setVisible(false);
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