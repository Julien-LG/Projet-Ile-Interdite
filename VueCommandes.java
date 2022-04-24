import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Une classe pour représenter la zone contenant le bouton.
 *
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
	/**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private CModele modele;

	public static JLabel labelNumJoueur;
	public static JLabel labelActionsRestantes;

	GridLayout gridPrincipal = new GridLayout(4,1);
	GridLayout gridLabel = new GridLayout(2,1);
	GridLayout gridButton = new GridLayout(4,3);
	GridLayout gridComboBox = new GridLayout(3,2);

	public static JComboBox comboBoxJoueurs;
	public static JComboBox comboBoxTypes;

	public static JComboBox comboBoxX;
	public static JComboBox comboBoxY;

    /** Constructeur. */
	public VueCommandes(CModele modele) {
		this.modele = modele;
		this.setLayout(gridPrincipal);
		JPanel panel = new JPanel();
		JPanel panelBoutons = new JPanel();
		JPanel panelBoutons2 = new JPanel();
		JPanel panelComboBox = new JPanel();

		panel.setLayout(gridLabel);
		labelNumJoueur = new JLabel("Tour du joueur " + modele.getNumJoueurActuel());
		panel.add(labelNumJoueur);

		labelActionsRestantes = new JLabel("Actions restantes " + modele.getNbActionsRestantes());
		panel.add(labelActionsRestantes);


		panelBoutons.setLayout(gridButton);
		panelBoutons2.setLayout(gridButton);


		/**
		 * On crée un nouveau bouton, de classe [JButton], en précisant le
		 * texte qui doit l'étiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
		Controleur2 ctrl2 = new Controleur2(modele);

		this.add(panel);


		for (String s : new String[]{"↖", "↑", "↗", "←", "Fin de tour", "→", "↙", "↓", "↘"}) {
			JButton bouton = new JButton(s);
			if ("↖↗↙↘".contains(s)) {
				bouton.setEnabled(false);
			}
			bouton.addActionListener(ctrl2);
			panelBoutons.add(bouton);
		}
		this.add(panelBoutons);

		for (String s : new String[]{" ", "Assèche Haut", " ", "Assèche Gauche", "Assèche Centre", "Assèche Droite", " ", "Assèche Bas", " "}) {
			JButton bouton = new JButton(s);
			if (s.equals(" ")) {
				bouton.setVisible(false);
			}
			bouton.addActionListener(ctrl2);
			panelBoutons2.add(bouton);
		}
		JButton boutonArtefact = new JButton("Récuperer artefact");
		boutonArtefact.addActionListener(ctrl2);
		panelBoutons2.add(boutonArtefact);



		this.add(panelBoutons2);

		panelComboBox.setLayout(gridComboBox);

		JLabel labelComboJoueurs = new JLabel("Joueur sélectionné");
		panelComboBox.add(labelComboJoueurs);

		String s1[] = {"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"};
		comboBoxJoueurs = new JComboBox(s1);
		panelComboBox.add(comboBoxJoueurs);

		/////////////////////////////////////////////////////////////////
		JLabel labelComboTypes = new JLabel("Type sélectionné");
		panelComboBox.add(labelComboTypes);

		String s2[] = {"FEU", "EAU", "TERRE", "AIR"};
		comboBoxTypes  = new JComboBox(s2);
		panelComboBox.add(comboBoxTypes);

		JButton boutonVide = new JButton("");
		boutonVide.setEnabled(false);
		panelComboBox.add(boutonVide);

		JButton boutonGiveCle = new JButton("Donner Clé");
		boutonGiveCle.addActionListener(ctrl2);
		panelComboBox.add(boutonGiveCle);
		//////////////////////////////////////////////////////////////////
		
		//comboBoxX

		this.add(panelComboBox);
	}
}
/** Fin de la vue. */