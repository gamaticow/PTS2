package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Exercice;


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
    public Tab Section2;   //Section 2
    public Tab Section3;   //Section 3
    public Tab Section4;   //Section 4
    public Tab Section5;   //Section 5
    public Tab addSection;
    public Button DeleteS2;
    public Button DeleteS3;
    public Button DeleteS4;
    public Button DeleteS5;
    public TextArea TextSection1;
    public TextArea TextSection2;
    public TextArea TextSection3;
    public TextArea TextSection4;
    public TextArea TextSection5;
    public TextArea TextAddSection;
    public MenuItem HandicapVisuel;
    public MenuItem HandicapAuditif;

    private int NbSection; //Nombre de section
    private Exercice exercice;


    public void initialize() {
        exercice = new Exercice();
        TextSection1.setText(null);
        TextSection2.setText(null);
        TextSection3.setText(null);
        TextSection4.setText(null);
        TextSection5.setText(null);
        TextAddSection.setText(null);
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
        Sections.getTabs().removeAll(Section1, Section2, Section3, Section4, Section5, addSection);
        Sections.getTabs().addAll(Section1, addSection);
        NbSection=1;
    }

    public void deleteSection2() {
        if (NbSection > 2) {
            switch (NbSection) {
                case 3:
                    TextSection2.setText(TextSection3.getText());
                    Sections.getTabs().remove(Section3);
                    break;
                case 4:
                    TextSection2.setText(TextSection3.getText());
                    TextSection3.setText(TextSection4.getText());
                    Sections.getTabs().remove(Section4);
                    break;
                case 5:
                    TextSection2.setText(TextSection3.getText());
                    TextSection3.setText(TextSection4.getText());
                    TextSection4.setText(TextSection5.getText());
                    Sections.getTabs().remove(Section5);
                    break;
            }
        } else {
            Sections.getTabs().remove(Section2);
        }
        NbSection--;
        Sections.getTabs().add(addSection);
    }

    public void deleteSection3() {
        if (NbSection > 3) {
            switch (NbSection) {
                case 4:
                    TextSection3.setText(TextSection4.getText());
                    Sections.getTabs().remove(Section4);
                    break;
                case 5:
                    TextSection3.setText(TextSection4.getText());
                    TextSection4.setText(TextSection5.getText());
                    Sections.getTabs().remove(Section5);
                    break;
            }
        } else {
            Sections.getTabs().remove(Section3);
        }
        NbSection--;
        Sections.getTabs().add(addSection);
    }

    public void deleteSection4() {
        if (NbSection > 4) {
            TextSection4.setText(TextSection5.getText());
            Sections.getTabs().remove(Section5);
        } else {
            Sections.getTabs().remove(Section4);
        }
        NbSection--;
        Sections.getTabs().add(addSection);
    }

    public void deleteSection5() {
        Sections.getTabs().remove(Section5);
        NbSection--;
        Sections.getTabs().add(addSection);
    }

    public void addSection() {
        Sections.getTabs().removeAll(addSection);
        switch (NbSection) {
            case 1:
                Sections.getTabs().addAll(Section2, addSection);
                TextSection2.setText(TextAddSection.getText());
                NbSection++;
                break;
            case 2:
                Sections.getTabs().addAll(Section3, addSection);
                TextSection3.setText(TextAddSection.getText());
                NbSection++;
                break;
            case 3:
                Sections.getTabs().addAll(Section4, addSection);
                TextSection4.setText(TextAddSection.getText());
                NbSection++;
                break;
            case 4:
                Sections.getTabs().addAll(Section5, addSection);
                TextSection5.setText(TextAddSection.getText());
                NbSection++;
                break;
            default:
                System.out.println("#erreur# addSection/NbSection!=1,2,3,4");
                break;
        }
        TextAddSection.setText(null);
    }

    public void clearAddSection() { TextAddSection.setText(null); }

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

    }

    public void exporter() {

    }

    public void importerVideo() {

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
        //Demande si l'user souhaite enregistrer son travail en cours
        initialize();
    }

    public void leave() {
        //Quitter (avec demande d'enregistrement en cas de modif de l'exo)
    }

}
