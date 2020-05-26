package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:41
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Exercice implements Serializable {

    private String nom;
    private String consigne;

    private Media media;
    private List<Partie> parties;
    private Solution solution;

    public Exercice(){
        parties = new LinkedList<>();
        solution = new Solution();
    }

    public List<Partie> getParties(){
        return parties;
    }

    public Partie getPartie(String nom){
        for(Partie partie : parties){
            if(partie.getNom().equals(nom))
                return partie;
        }
        return null;
    }

    public Solution getSolution(){
        return solution;
    }

    public String getConsigne(){
        return consigne;
    }

    public Media getMedia(){
        return media;
    }

    //================================================================================================FONCTIONS ETUDIANT

    public boolean isSolutionUtilise(){
        return solution.isSolution_utilise();
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    public void createPartie(String nom) {
        parties.add(new Partie(nom));
    }

    public void setConsigne(String consigne){
        this.consigne = consigne;
    }

    public void setMedia(Media media){
        this.media = media;
    }
}
