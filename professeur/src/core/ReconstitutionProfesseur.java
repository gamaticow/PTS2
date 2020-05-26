package core;
/*
 *   Created by Corentin on 20/05/2020 at 00:43
 */

import model.Exercice;

import java.io.*;

public class ReconstitutionProfesseur {

    public static void main(String[] args) throws IOException {
        Exercice exercice = new Exercice();
        exercice.setConsigne("Traduire la vidéo");
        exercice.createPartie("Test 1");
        exercice.getPartie("Test 1").getTexte().setOriginal("Salut les BGs");
        exercice.createPartie("Partie 2");
        exercice.getPartie("Partie 2").getTexte().setOriginal("Texte de test vurgz feiuha feaif heuaifhaufuieza hfuehza iufhaeiu fheiau fjkhkjh");
        exercice.getPartie("Partie 2").getIndice().setIndice("C'est vachement compliqué");
        exercice.getSolution().setSolution_autorise(true);

        FileOutputStream fos = new FileOutputStream("test.caft");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(exercice);
    }

}
