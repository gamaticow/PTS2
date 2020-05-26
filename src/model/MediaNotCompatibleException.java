package model;
/*
 *   Created by Corentin on 24/05/2020 at 20:34
 */

public class MediaNotCompatibleException extends Exception {

    public MediaNotCompatibleException(String extension){
        super("L'extension ( ."+extension+" ) n'est pas compatible");
    }

}
