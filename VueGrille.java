import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Une classe pour représenter la zone d'affichage des cellules.
 *
 * JPanel est une classe d'éléments graphiques, pouvant comme JFrame contenir
 * d'autres éléments graphiques.
 *
 * Cette vue va être un observateur du modèle et sera mise à jour à chaque
 * nouvelle génération des cellules.
 */
class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private CModele modele;
	private Joueur[] joueurs;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 40;

    /** Constructeur. */
	public VueGrille(CModele modele) {
		this.modele = modele;
		this.joueurs = modele.getJoueurs();
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		modele.addObserver(this);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,TAILLE*CModele.HAUTEUR);
		this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() {
		repaint();
	}

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
		super.repaint();
		/** Pour chaque cellule... */
		for(int i=0; i<=CModele.LARGEUR; i++) {
			for(int j=0; j<=CModele.HAUTEUR; j++) {
				/**
				 * ... Appeler une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				paint(g, modele.getZone(i, j),  (i)*TAILLE, (j)*TAILLE);
			}
		}
		// ici couleur des joueurs
		for (Joueur j : joueurs) {
			g.setColor(Color.BLACK);
			g.fillRect(((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), TAILLE/2, TAILLE/2);
		}
    }
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
    //private void paint(Graphics g, Zone z, Joueur j, int x, int y) {
	private void paint(Graphics g, Zone z, int x, int y) {
        /** Sélection d'une couleur. */
		switch (z.getEtat()){
			case Normale -> g.setColor(Color.WHITE);
			case Innondee -> g.setColor(Color.CYAN);
			case Submergee -> g.setColor(Color.BLUE);
		}

		/** Coloration d'un rectangle. */
		g.fillRect(x, y, TAILLE, TAILLE);
	}
}