package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:41
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Exercice implements Serializable {

    private String consigne;

    private Media media;
    private List<Partie> parties;
    private Solution solution;

    private boolean mot_complet;
    private boolean correspondance;
    private boolean sensCasse;
    private boolean sensAccent;

    public Exercice(){
        parties = new LinkedList<>();
        solution = new Solution();

        mot_complet = false;
        correspondance = false;
        sensCasse = false;
        sensAccent = false;
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

    public boolean isMot_complet(){
        return mot_complet;
    }

    public boolean isCorrespondance(){
        return correspondance;
    }

    public boolean isSensCasse() {
        return sensCasse;
    }

    public boolean isSensAccent() {
        return sensAccent;
    }

    //================================================================================================FONCTIONS ETUDIANT

    public boolean isSolutionUtilise(){
        return solution.isSolution_utilise();
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    public boolean sectionExiste(String nom){
        for(Partie partie : parties){
            if(partie.getNom().equalsIgnoreCase(nom))
                return true;
        }
        return false;
    }

    public void createPartie(String nom) {
        if(sectionExiste(nom))
            return;
        parties.add(new Partie(nom));
    }

    public void supprimerPartie(String nom){
        Partie remove = null;
        for (Partie partie : parties){
            if(partie.getNom().equalsIgnoreCase(nom))
                remove = partie;
        }
        if(remove != null)
            parties.remove(remove);
    }

    public void setConsigne(String consigne){
        this.consigne = consigne;
    }

    public void setMedia(Media media){
        this.media = media;
    }

    public void setMot_complet(boolean value){
        mot_complet = value;
    }

    public void setCorrespondance(boolean value){
        correspondance = value;
    }

    public void setSensCasse(boolean sensCasse) {
        this.sensCasse = sensCasse;
    }

    public void setSensAccent(boolean sensAccent) {
        this.sensAccent = sensAccent;
    }
}