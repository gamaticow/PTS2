package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Texte implements Serializable {

    private static final char CARACTERE_DE_SUBSTITUTION = '#';
    private static final String REGEX = "[a-zA-Z0-9áàâäãåçéèêëíìîïñóòôöõúùûüýÿÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸ]";
    private static final String A_MIN = "[aáàâäãå]";
    private static final String A_MAJ = "[AÁÀÂÄÃÅ]";
    private static final String E_MIN = "[eéèêë]";
    private static final String E_MAJ = "[EÉÈÊË]";
    private static final String I_MIN = "[iíìîï]";
    private static final String I_MAJ = "[IÍÌÎÏ]";
    private static final String O_MIN = "[oóòôöõ]";
    private static final String O_MAJ = "[OÓÒÔÖÕ]";
    private static final String U_MIN = "[uúùûü]";
    private static final String U_MAJ = "[UÚÙÛÜ]";
    private static final String Y_MIN = "[yýÿ]";
    private static final String Y_MAJ = "[YÝŸ]";
    private static final String C_MIN = "[cç]";
    private static final String C_MAJ = "[CÇ]";
    private static final String N_MIN = "[nñ]";
    private static final String N_MAJ = "[NÑ]";

    private String texte;
    private String texte_decouvert;
    private String correspondance;

    //================================================================================================FONCTIONS ETUDIANT
    /**
     * Donne le texte caché
     * @return Le texte a affiché dans la version étudiant
     */
    public String getTexteCache(){
        if(texte_decouvert == null){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < texte.length(); i++){
                if(String.valueOf(texte.charAt(i)).matches(REGEX)){
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
    public boolean chercherMot(String mot, int debut_recherche, boolean rechercheMotComplet, boolean casse, boolean accent){
        correspondance = null;

        int debut =0;
        int caractere_trouver=0;
        boolean trouver = false;

        for (int i=debut_recherche;i<=texte_decouvert.length();i++){
            if (caractere_trouver==mot.length()){
                if (!motIncomplet(debut, caractere_trouver)){
                    caractere_trouver=0;
                    continue;
                }else if(rechercheMotComplet && (!(debut == 0 || !String.valueOf(texte.charAt(debut-1)).matches("[A-Za-z0-9]")) ||
                        !(debut+caractere_trouver == texte_decouvert.length() || !String.valueOf(texte.charAt(debut+caractere_trouver)).matches("[A-Za-z0-9]")))){
                    caractere_trouver=0;
                    continue;
                }
                trouver=true;
                break;
            }else {
                if(i == texte_decouvert.length())
                    break;
                if (caractereIdentique(mot.charAt(caractere_trouver), i, casse, accent)) {
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
            chercherMot(mot, debut+caractere_trouver, rechercheMotComplet, casse, accent);
            return true;
        }
        return false;
    }

    public boolean correspondance(String mot, int debut_recherche, boolean first, boolean casse, boolean accent){
        int debut =0;
        int caractere_trouver=0;
        boolean trouver = false;

        for (int i=debut_recherche;i<=texte_decouvert.length();i++){
            if(i == texte_decouvert.length())
                break;
            if (caractereIdentique(mot.charAt(caractere_trouver), i, casse, accent)) {
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
            correspondance(mot, debut+caractere_trouver, false, casse, accent);
            return true;
        }
        return false;
    }

    public boolean caractereIdentique(char c1, int position, boolean casse, boolean accent){
        char c2 = texte.charAt(position);
        if(casse) {
            c1 = Character.toUpperCase(c1);
            c2 = Character.toUpperCase(c2);
        }
        if(!accent)
            return c1 == c2;
        else{
            if(c1 == c2)
                return true;
            else if((String.valueOf(c1).matches(A_MIN) && String.valueOf(c2).matches(A_MIN)) || (String.valueOf(c1).matches(A_MAJ) && String.valueOf(c2).matches(A_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(E_MIN) && String.valueOf(c2).matches(E_MIN)) || (String.valueOf(c1).matches(E_MAJ) && String.valueOf(c2).matches(E_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(I_MIN) && String.valueOf(c2).matches(I_MIN)) || (String.valueOf(c1).matches(I_MAJ) && String.valueOf(c2).matches(I_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(O_MIN) && String.valueOf(c2).matches(O_MIN)) || (String.valueOf(c1).matches(O_MAJ) && String.valueOf(c2).matches(O_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(U_MIN) && String.valueOf(c2).matches(U_MIN)) || (String.valueOf(c1).matches(U_MAJ) && String.valueOf(c2).matches(U_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(Y_MIN) && String.valueOf(c2).matches(Y_MIN)) || (String.valueOf(c1).matches(Y_MAJ) && String.valueOf(c2).matches(Y_MAJ)))
                return true;
            else if((String.valueOf(c1).matches(C_MIN) && String.valueOf(c2).matches(C_MIN)) || (String.valueOf(c1).matches(C_MAJ) && String.valueOf(c2).matches(C_MAJ)))
                return true;
            else return (String.valueOf(c1).matches(N_MIN) && String.valueOf(c2).matches(N_MIN)) || (String.valueOf(c1).matches(N_MAJ) && String.valueOf(c2).matches(N_MAJ));
        }
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
