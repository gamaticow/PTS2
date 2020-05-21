package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Indice implements Serializable {

    private String indice;
    private boolean indice_utilise;

    public Indice(){
        indice_utilise = false;
    }

    //================================================================================================FONCTIONS ETUDIANT
    /**
     * Retourne si l'indice est utilisable
     * @return True si l'indice est utilisable, false sinon
     */
    public boolean indiceUtilisable(){
        return indice != null;
    }

    /**
     * Utiliser l'indice
     * @return l'indice
     */
    public String utiliserIndice(){
        indice_utilise = true;
        return indice;
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    /**
     * Définir l'indice
     * @param indice Nouvel indice
     */
    public void setIndice(String indice){
        this.indice = indice;
    }

    /**
     * Récuperer l'indice
     * @return l'indice
     */
    public String getIndice(){
        return indice == null ? "" : indice;
    }

}
