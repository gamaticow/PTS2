package core;
/*
 *   Created by Corentin on 20/05/2020 at 00:43
 */


import controller.ProfesseurController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ReconstitutionProfesseur extends Application {

    public static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Professeur.fxml"));
        Parent root = loader.load();
        primaryStage.getIcons().add(new Image("https://www.zupimages.net/up/20/22/2k9q.png"));
        primaryStage.setTitle("Reconstitution");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /*Parent root = FXMLLoader.load(getClass().getResource("../view/Professeur.fxml"));
        primaryStage.setTitle("Reconstitution version étudiant");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();*/
    }

    public static void leave() {
        primaryStage.close();
    }
}
