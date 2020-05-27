package core;
/*
 *   Created by Corentin on 20/05/2020 at 00:43
 */


import controller.ProfesseurController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Exercice;
import model.Media;
import model.MediaNotCompatibleException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ReconstitutionProfesseur {

    public static void main(String[] args) throws MediaNotCompatibleException, IOException {
        String path = "C:\\Users\\coren\\OneDrive\\Bureau\\test.mp3";
        Exercice exercice = new Exercice();
        exercice.setConsigne("Traduire la vidéo");
        exercice.createPartie("Test 1");
        exercice.getPartie("Test 1").getTexte().setOriginal("Salut les BGs");
        exercice.createPartie("Partie 2");
        exercice.getPartie("Partie 2").getTexte().setOriginal("Texte de test vurgz feiuha feaif heuaifhaufuieza hfuehza iufhaeiu fheiau fjkhkjh");
        exercice.getPartie("Partie 2").getIndice().setIndice("C'est vachement compliqué");
        exercice.getSolution().setSolution_autorise(true);
        exercice.setMedia(Media.load(path));

        FileOutputStream fos = new FileOutputStream("test.caft");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(exercice);
        oos.close();
        fos.close();
    }
}
