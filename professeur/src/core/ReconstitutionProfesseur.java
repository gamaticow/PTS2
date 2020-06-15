package core;
/*
 *   Created by Corentin on 20/05/2020 at 00:43
 */

import controller.ProfesseurController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Exercice;
import model.Media;
import model.MediaNotCompatibleException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class ReconstitutionProfesseur extends Application {
  
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Professeur.fxml"));
        Parent root = loader.load();
        primaryStage.setOnCloseRequest(event -> {
            ButtonType yes = new ButtonType("Oui");
            ButtonType no = new ButtonType("Non");
            Optional<ButtonType> result = ProfesseurController.makeAlert("Confirmation", "Voulez-vous quittez ?", yes, no);
            if(result.get() != yes)
                event.consume();
        });
        primaryStage.getIcons().add(new Image(ReconstitutionProfesseur.class.getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.setTitle("Reconstitution (Logiciel d'exercice linguistique)");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /*Parent root = FXMLLoader.load(getClass().getResource("../view/Professeur.fxml"));
        primaryStage.setTitle("Reconstitution version étudiant");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();*/
    }

    /**
     * Création d'un exercice de test
     * @throws MediaNotCompatibleException
     * @throws IOException
     */
    public static void creerExercice() throws MediaNotCompatibleException, IOException {
      String path = "C:\\Users\\coren\\OneDrive\\Bureau\\test.mp4";
      Exercice exercice = new Exercice();
      exercice.setConsigne("Traduire la vidéo");
      exercice.createPartie("Test 1");
      exercice.getPartie("Test 1").getTexte().setOriginal("Salut lé BGs");
      exercice.createPartie("Partie 2");
      exercice.getPartie("Partie 2").getTexte().setOriginal("Texte de test vurgz feiuha feaif heuaifhaufuieza hfuehza iufhaeiu fheiau fjkhkjh");
      exercice.getPartie("Partie 2").getIndice().setIndice("C'est vachement compliqué");
      exercice.getSolution().setSolution_autorise(true);
      exercice.setMedia(Media.load(path));
      exercice.setCorrespondance(true);
      exercice.setSensCasse(true);
      exercice.setSensAccent(true);

      FileOutputStream fos = new FileOutputStream("test.caft");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(exercice);
      oos.close();
      fos.close();
    }
}
