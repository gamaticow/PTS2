package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:41
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Exercice implements Serializable {

    private String nom;

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

    public Partie getDefaultPartie(){
        return parties.get(0);
    }

    public Solution getSolution(){
        return solution;
    }

    //================================================================================================FONCTIONS ETUDIANT

    public boolean isSolutionUtilise(){
        return solution.isSolution_utilise();
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    public void createPartie(String nom) {
        parties.add(new Partie(nom));
    }
}
