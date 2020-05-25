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

public class ReconstitutionProfesseur extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Professeur.fxml"));
        primaryStage.setTitle("Reconstitution version professeur");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
