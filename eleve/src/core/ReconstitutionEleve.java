package core;
/*
 *   Created by Corentin on 20/05/2020 at 00:42
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ReconstitutionEleve extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Etudiant.fxml"));
        primaryStage.setTitle("Reconstitution (Logiciel de d'exercice linguistique)");
        primaryStage.getIcons().add(new Image("https://www.zupimages.net/up/20/22/2k9q.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
