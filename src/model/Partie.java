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
     * @return Si le texte a été modifier pour le rafraichir si oui
     */
    public boolean chercherMot(String mot, boolean rechercheMotComplet, boolean casse, boolean accent){
        if(mot == null || mot.equals(""))
            return false;

        while (mot.startsWith(" "))
            mot = mot.substring(1);
        while (mot.endsWith(" "))
            mot = mot.substring(0, mot.length()-1);
        if(mot.isEmpty())
            return false;
        return texte.chercherMot(mot, 0, rechercheMotComplet, casse, accent);
    }

    public boolean correspondance(String mot, boolean casse, boolean accent){
        if(mot == null || mot.equals("")) {
            texte.supprimerCorrespondance();
            return true;
        }

        while (mot.startsWith(" "))
            mot = mot.substring(1);
        while (mot.endsWith(" "))
            mot = mot.substring(0, mot.length()-1);
        if(mot.equals(""))
            return false;
        return texte.correspondance(mot, 0, true, casse, accent);
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

    public void setNom(String nom){
        this.nom = nom;
    }
}
