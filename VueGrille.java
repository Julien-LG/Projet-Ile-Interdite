import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.imageio.ImageIO;
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
    private final static int TAILLE = 70;

	/*ImageIcon  imageHelico = new ImageIcon("Textures/helico.png");
	ImageIcon  imageHelicoInondee = new ImageIcon("Textures/helicoInondee.png");
	//ImageIcon  imageZone = new ImageIcon("Textures/zone.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
	//ImageIcon  imageZone = getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
	//BufferedImage imageZone
	/*ImageIcon  imageZoneInondee = new ImageIcon("Textures/zoneInondee.png");
	ImageIcon  imageSub = new ImageIcon("Textures/sub.png");

	ImageIcon  imageArtAir = new ImageIcon("Textures/AIR.png");
	ImageIcon  imageArtEau = new ImageIcon("Textures/EAU.png");
	ImageIcon  imageArtFeu = new ImageIcon("Textures/FEU.png");
	ImageIcon  imageArtTerre = new ImageIcon("Textures/TERRE.png");

	ImageIcon  imageJ1 = new ImageIcon("Textures/j1.png");
	ImageIcon  imageJ2 = new ImageIcon("Textures/j2.png");
	ImageIcon  imageJ3 = new ImageIcon("Textures/j3.png");
	ImageIcon  imageJ4 = new ImageIcon("Textures/j4.png");

	ImageIcon  imageJ1s = new ImageIcon("Textures/j1s.png");
	ImageIcon  imageJ2s = new ImageIcon("Textures/j2s.png");
	ImageIcon  imageJ3s = new ImageIcon("Textures/j3s.png");
	ImageIcon  imageJ4s = new ImageIcon("Textures/j4s.png");*/

	BufferedImage  imageHelico;
	BufferedImage  imageHelicoInondee;
	BufferedImage  imageZone;
	BufferedImage  imageZoneInondee;
	BufferedImage  imageSub;

	BufferedImage  imageArtAir;
	BufferedImage  imageArtEau;
	BufferedImage  imageArtFeu;
	BufferedImage  imageArtTerre;

	BufferedImage  imageJ1;
	BufferedImage  imageJ2;
	BufferedImage  imageJ3;
	BufferedImage  imageJ4;

	BufferedImage  imageJ1s;
	BufferedImage  imageJ2s;
	BufferedImage  imageJ3s;
	BufferedImage  imageJ4s;

    /** Constructeur. */
	public VueGrille(CModele modele) {
		this.modele = modele;
		this.joueurs = modele.getJoueurs();
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		modele.addObserver(this);

		//imageZone.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);

		//this.add(new JLabel(image));

		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,TAILLE*CModele.HAUTEUR);
		this.setPreferredSize(dim);

		try {
			imageHelico = ImageIO.read(new File("Textures/helico.png"));
			imageHelicoInondee = ImageIO.read(new File("Textures/helicoInondee.png"));
			imageZone = ImageIO.read(new File("Textures/zone.png"));
			imageZoneInondee = ImageIO.read(new File("Textures/zoneInondee.png"));
			imageSub = ImageIO.read(new File("Textures/sub.png"));

			imageArtAir = ImageIO.read(new File("Textures/AIR.png"));
			imageArtEau = ImageIO.read(new File("Textures/EAU.png"));
			imageArtFeu = ImageIO.read(new File("Textures/FEU.png"));
			imageArtTerre = ImageIO.read(new File("Textures/TERRE.png"));

			imageJ1 = ImageIO.read(new File("Textures/j1.png"));
			imageJ2 = ImageIO.read(new File("Textures/j2.png"));
			imageJ3 = ImageIO.read(new File("Textures/j3.png"));
			imageJ4 = ImageIO.read(new File("Textures/j4.png"));

			imageJ1s = ImageIO.read(new File("Textures/j1s.png"));
			imageJ2s = ImageIO.read(new File("Textures/j2s.png"));
			imageJ3s = ImageIO.read(new File("Textures/j3s.png"));
			imageJ4s = ImageIO.read(new File("Textures/j4s.png"));

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		setOpaque(false);
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
			/*g.setColor(j.getCouleur());
			g.fillRect(((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), TAILLE/2, TAILLE/2);*/
			switch (j.getNumero()){
				case 1: g.drawImage(imageJ1.getScaledInstance(50, 50, Image.SCALE_DEFAULT), ((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), null);
					break;
				case 2: g.drawImage(imageJ2.getScaledInstance(50, 50, Image.SCALE_DEFAULT), ((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), null);
					break;
				case 3: g.drawImage(imageJ3.getScaledInstance(50, 50, Image.SCALE_DEFAULT), ((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), null);
					break;
				case 4: g.drawImage(imageJ4.getScaledInstance(50, 50, Image.SCALE_DEFAULT), ((j.getX())*TAILLE)+(TAILLE / 4), ((j.getY())*TAILLE)+(TAILLE / 4), null);
					break;

			}

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
		if (z.isHeliport()) {
			switch (z.getEtat()) {
				case Normale -> g.drawImage(imageHelico.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
				case Innondee -> g.drawImage(imageHelicoInondee.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
				case Submergee -> g.drawImage(imageSub.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
			}
		}
		else {
			switch (z.getEtat()){
				case Normale -> g.drawImage(imageZone.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
				case Innondee -> g.drawImage(imageZoneInondee.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
				case Submergee -> g.drawImage(imageSub.getScaledInstance(TAILLE, TAILLE, Image.SCALE_DEFAULT), x, y, null);
			}
		}

		if (z.isZoneArtefact())
			switch (z.getType()){
				case AIR -> g.drawImage(imageArtAir.getScaledInstance(40, 40, Image.SCALE_DEFAULT), x, y, null);
				case EAU -> g.drawImage(imageArtEau.getScaledInstance(40, 40, Image.SCALE_DEFAULT), x, y, null);
				case FEU -> g.drawImage(imageArtFeu.getScaledInstance(40, 40, Image.SCALE_DEFAULT), x, y, null);
				case TERRE -> g.drawImage(imageArtTerre.getScaledInstance(40, 40, Image.SCALE_DEFAULT), x, y, null);
			}

		/** Coloration d'un rectangle. */
		//g.fillRect(x, y, TAILLE, TAILLE);
	}
}