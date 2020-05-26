package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Texte implements Serializable {

    private static final String CARACTERE_DE_SUBSTITUTION = "#";

    private String texte;
    private String texte_decouvert;

    //================================================================================================FONCTIONS ETUDIANT
    /**
     * Donne le texte caché
     * @return Le texte a affiché dans la version étudiant
     */
    public String getTexteCache(){
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
        return texte_decouvert;
    }

    /**
     * Algorithme de recherche de mot du dossier de spécification avec :
     * texte : original
     * texte_découvert : cacher
     * @param mot : mot_a_cherche
     * @param debut_recherche : Mettre a 0 pour commencer la recherche au debut du texte
     * @return Retourne si une occurence a été trouvé
     * <ul>
     *     <li>true : Au moins une occurence a été trouvé</li>
     *     <li>false : Aucune occurence a été trouvé</li>
     * </ul>
     */
    public boolean chercherMot(String mot, int debut_recherche){
        //TODO faire ca
        //TODO et le modifier un peut pour le retour
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
