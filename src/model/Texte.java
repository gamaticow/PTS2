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
     * @param modeCherche :
     * @return Retourne si une occurence a été trouvé
     * <ul>
     *     <li>true : Au moins une occurence a été trouvé</li>
     *     <li>false : Aucune occurence a été trouvé</li>
     * </ul>
     */
    public boolean chercherMot(String mot, int debut_recherche, boolean modeCherche){

        int debut =0;
        int caractere_trouver=0;
        boolean trouver = false;

        for (int i=debut_recherche;i<texte_decouvert.length();i++){
            if (caractere_trouver==mot.length()){
                if (!CARACTERE_DE_SUBSTITUTION.equals(texte.charAt(i))){
                    caractere_trouver=0;
                    continue;
                }
                trouver=true;
                break;
            }
            if (texte_decouvert.charAt(i)==mot.charAt(caractere_trouver)){
                if (caractere_trouver==0){
                    debut=i;
                }
                caractere_trouver++;
            }
            else{
                caractere_trouver=0;
            }
        }

        if (trouver==true){
            for (int i=debut;i<(debut+caractere_trouver);i++){
                replaceChar(texte,texte_decouvert.charAt(i),i);
            }
            System.out.println(texte_decouvert+" "+texte+" "+mot+" "+ (debut+caractere_trouver));

        }
        else{
            System.out.println(texte);
        }
        return true;
    }
  
    private String replaceChar(String str, char ch, int index) {
        return str.substring(0, index) + ch + str.substring(index+1);
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
