package model;
/*
 *   Created by Corentin on 20/05/2020 at 00:56
 */

import java.io.Serializable;

public class Solution implements Serializable {

    private boolean solution_autorise;
    private boolean solution_utilise;

    public Solution(){
        solution_autorise = false;
        solution_utilise = false;
    }

    public boolean isSolution_autorise(){
        return solution_autorise;
    }

    //================================================================================================FONCTIONS ETUDIANT

    public void utiliseSolution(){
        solution_utilise = true;
    }

    public boolean isSolution_utilise(){
        return solution_utilise;
    }

    //==============================================================================================FONCTIONS PROFESSEUR

    public void setSolution_autorise(boolean solution_autorise){
        this.solution_autorise = solution_autorise;
    }
}
