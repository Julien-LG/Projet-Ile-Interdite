import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

class Controleur2 implements ActionListener {
	CModele modele;

	public Controleur2(CModele modele) {
		this.modele = modele;
	}

	/**
	 * Action effectuée à réception d'un événement : appeler la
	 * méthode [avance] du modèle.
	 */
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()){
			case "Fin de tour": modele.avance();
				break;
			case "↑": modele.deplaceJoueur(Direction.Haut);
				break;
			case "←": modele.deplaceJoueur(Direction.Gauche);
				break;
			case "→": modele.deplaceJoueur(Direction.Droite);
				break;
			case "↓": modele.deplaceJoueur(Direction.Bas);
				break;
			case "Assèche Centre": modele.assecheJoueurDirection(null);
				break;
			case "Assèche Haut": modele.assecheJoueurDirection(Direction.Haut);
				break;
			case "Assèche Bas": modele.assecheJoueurDirection(Direction.Bas);
				break;
			case "Assèche Gauche": modele.assecheJoueurDirection(Direction.Gauche);
				break;
			case "Assèche Droite": modele.assecheJoueurDirection(Direction.Droite);
				break;
			case "Récuperer artefact": modele.joueurRecupArtefact();
				break;
		}

		VueCommandes.labelNumJoueur.setText("Tour du joueur " + modele.getNumJoueurActuel());
		VueCommandes.labelActionsRestantes.setText("Actions restantes " + modele.getNbActionsRestantes());

		/*Joueur[] joueurs = modele.getJoueurs();
		for (int i = 0; i < 4; i++){
			VueInformations.labelList[i].setText(joueurs[i].clesToString());
			//VueInformations.labelList[i+1].setText(joueurs[i].artefactsToString());

			//System.out.println(joueurs[i].artefactsToString());
			//System.out.println(joueurs[i+1].artefactsToString());
			/*System.out.println(i + " ");
			System.out.println(i+1 + " ");*/

			/*for (int j = 1; j < 8; j+=2){
				VueInformations.labelList[j].setText(joueurs[i].artefactsToString());
			}
		}*/

		VueInformations.majLabels(modele);


		/*int j = 0;
		for (int i = 0; i < 4; i++){
			VueInformations.labelList[j].setText(joueurs[i].clesToString());
			VueInformations.labelList[j+1].setText(joueurs[i].artefactsToString());

			//System.out.println(joueurs[i].artefactsToString());
			//System.out.println(joueurs[i+1].artefactsToString());
			//System.out.println(i + " ");
			/*System.out.println(j + " ");
			System.out.println(j+1 + " ");*/
			/*j+=2;
		}*/
	}
}

/** Fin du contrôleur. */