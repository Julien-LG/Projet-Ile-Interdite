import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/*
class VueInformations extends JPanel implements Observer {
	/** On maintient une référence vers le modèle. */
	/*private CModele modele;

	/** Constructeur. */
	/*public VueInformations(CModele modele) {
		this.modele = modele;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		/*modele.addObserver(this);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		/*Dimension dim = new Dimension(100,100);
		this.setPreferredSize(dim);

		JLabel labelNumJoueur = new JLabel("Tour du joueur " + modele.getNumJoueurActuel());
		this.add(labelNumJoueur);

		JLabel labelActionsRestantes = new JLabel("Actions restantes " + modele.getNbActionsRestantes());
		this.add(labelActionsRestantes);
	}

	public void update() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.repaint();
		/** Pour chaque cellule... */
		//labelNumJoueur
	/*}
}*/