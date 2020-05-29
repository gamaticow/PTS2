package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Partie implements Serializable {

    private String nom;
    //Si -1 temps illimité
    private long temps_max;
    private long debut_media;
    //Si -1 jusqu'a la fin du media
    private long fin_media;
    private int nb_fautes;

    private Texte texte;
    private Indice indice;

    public Partie(String nom){
        this.nom = nom;
        temps_max = -1;
        debut_media = 0;
        fin_media = -1;
        nb_fautes = 0;
        texte = new Texte();
        indice = new Indice();
    }

    public String getNom(){
        return nom;
    }

    //================================================================================================FONCTIONS ETUDIANT
    /**
     * Cherche un mot dans le texte
     * @param mot Mot a chercher
     * @param bouton Si l'etudiant a cliquer sur le bouton valider pour lancer la recherche ou si la recherche est faite car elle a été activé en temps réel
     * @return Si le texte a été modifier pour le rafraichir si oui
     */
    public boolean chercherMot(String mot, boolean bouton){
        if(!bouton)
            return texte.chercherMot(mot, 0, true);
        else
            if(texte.chercherMot(mot, 0, true))
                return true;
            else {
                nb_fautes++;
                return false;
            }
    }

    /**
     * Recuperer le texte a afficher pour la version etudiant
     * @return Le texte pour la version etudiant
     */
    public String texteAAficherEtudiant(){
        return texte.getTexteCache();
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    public Indice getIndice() {
        return indice;
    }

    public Texte getTexte(){
        return texte;
    }
}
