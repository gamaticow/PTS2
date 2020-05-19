
/*
 *   Created by Corentin on 20/05/2020 at 00:42
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReconstitutionEleve extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/version_etudiant.fxml"));
        primaryStage.setTitle("Reconstitution");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
