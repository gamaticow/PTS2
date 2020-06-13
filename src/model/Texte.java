package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Texte implements Serializable {

    private static final char CARACTERE_DE_SUBSTITUTION = '#';

    private String texte;
    private String texte_decouvert;
    private String correspondance;

    //================================================================================================FONCTIONS ETUDIANT
    /**
     * Donne le texte caché
     * @return Le texte a affiché dans la version étudiant
     */
    public String getTexteCache(){
        /*String texte = "First, I wake up. Then, I get dressed. I walk to school. I do not ride a bike. I do not ride the bus.\nI like to go to school. It rains. I do not like rain. I eat lunch. I eat a sandwich and an apple.\n" +
                "\n" +
                "I play outside. I like to play. I read a book. I like to read books. I walk home. I do not like walking home.\nMy mother cooks soup for dinner. The soup is hot. Then, I go to bed. I do not like to go to bed.";*/
        if(texte_decouvert == null){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < texte.length(); i++){
                if(String.valueOf(texte.charAt(i)).matches("[A-Za-z0-9]")){
                    sb.append(CARACTERE_DE_SUBSTITUTION);
                }else {
                    sb.append(texte.charAt(i));
                }
            }
            texte_decouvert = sb.toString();
        }
        return correspondance != null ? correspondance : texte_decouvert;
    }

    /**
     * Algorithme de recherche de mot du dossier de spécification avec :
     * texte : original
     * texte_découvert : cacher
     * @param mot : mot_a_cherche
     * @param debut_recherche : Mettre a 0 pour commencer la recherche au debut du texte
     * @param rechercheMotComplet :
     * @return Retourne si une occurence a été trouvé
     * <ul>
     *     <li>true : Au moins une occurence a été trouvé</li>
     *     <li>false : Aucune occurence a été trouvé</li>
     * </ul>
     */
    public boolean chercherMot(String mot, int debut_recherche, boolean rechercheMotComplet){
        correspondance = null;

        int debut =0;
        int caractere_trouver=0;
        boolean trouver = false;

        for (int i=debut_recherche;i<=texte_decouvert.length();i++){
            if (caractere_trouver==mot.length()){
                if (!motIncomplet(debut, caractere_trouver)){
                    caractere_trouver=0;
                    continue;
                }else if(rechercheMotComplet && !(debut == 0 || !String.valueOf(texte.charAt(debut-1)).matches("[A-Za-z0-9]")) ||
                        !(debut+caractere_trouver == texte_decouvert.length() || !String.valueOf(texte.charAt(debut+caractere_trouver)).matches("[A-Za-z0-9]"))){
                    caractere_trouver=0;
                    continue;
                }
                trouver=true;
                break;
            }else {
                if(i == texte_decouvert.length())
                    break;
                if (texte.charAt(i) == mot.charAt(caractere_trouver)) {
                    if (caractere_trouver == 0) {
                        debut = i;
                    }
                    caractere_trouver++;
                } else {
                    caractere_trouver = 0;
                }
            }
        }

        if (trouver){
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<texte_decouvert.length();i++){
                if(i >= debut && i < debut+caractere_trouver)
                    sb.append(texte.charAt(i));
                else
                    sb.append(texte_decouvert.charAt(i));
            }
            texte_decouvert = sb.toString();
            chercherMot(mot, debut+caractere_trouver, rechercheMotComplet);
            return true;
        }
        return false;
    }

    public boolean correspondance(String mot, int debut_recherche, boolean first){
        int debut =0;
        int caractere_trouver=0;
        boolean trouver = false;

        for (int i=debut_recherche;i<=texte_decouvert.length();i++){
            if(i == texte_decouvert.length())
                break;
            if (texte.charAt(i) == mot.charAt(caractere_trouver)) {
                if (caractere_trouver == 0) {
                    debut = i;
                }
                caractere_trouver++;
            } else {
                caractere_trouver = 0;
            }

            if (caractere_trouver==mot.length()){
                if (!motIncomplet(debut, caractere_trouver)){
                    caractere_trouver=0;
                }else {
                    trouver = true;
                    break;
                }
            }
        }

        if(correspondance == null || first)
            correspondance = texte_decouvert;

        if (trouver){
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<correspondance.length();i++){
                if(i >= debut && i < debut+caractere_trouver)
                    sb.append(texte.charAt(i));
                else
                    sb.append(correspondance.charAt(i));
            }
            correspondance = sb.toString();
            correspondance(mot, debut+caractere_trouver, false);
            return true;
        }
        return false;
    }

    public void supprimerCorrespondance(){
        correspondance = null;
    }
  
    private boolean motIncomplet(int debut, int longueur){
        for(int i = debut; i <= (debut+longueur); i++){
            if (texte_decouvert.charAt(i) == CARACTERE_DE_SUBSTITUTION) {
                return true;
            }
        }

        return false;
    }


    //==============================================================================================FONCTIONS PROFESSEUR
    /**
     * Définit le texte original
     * @param texte Texte original
     */
    public void setOriginal(String texte){
        this.texte = texte;
    }

    /**
     * Récupérer le texte original
     * @return Le text original
     */
    public String getOriginal(){
        return texte;
    }

}
