package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Video implements Initializable {

    @FXML
    private MediaView mv;

    private MediaPlayer mp;
    private Media me;

    @FXML
    private Button playPause;

    @FXML
    private Button avancer;

    @FXML
    private Button reculer;

    @FXML
    private Button recommencer;



    public void initialize(URL location, ResourceBundle resources){
        String path = new File("chemin d'accé").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitWidthProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
    }

    public void pause(){

    }

    public void recommencer(){

    }
}
