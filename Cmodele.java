import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Le modèle : le coeur de l'application.
 *
 * Le modèle étend la classe [Observable] : il va posséder un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les prévenir avec [notifyObservers] lors des modifications.
 * Voir la méthode [avance()] pour cela.
 */
class CModele extends Observable {
    //On fixe la taille de la grille.
    public static final int HAUTEUR=6, LARGEUR=6;

    //On crée un tableau de zones
    private final Zone[][] zones;
	private final Joueur[] joueurs;

	private Heliport heliport;

	private int joueurActuel = 0;
	private int nbActionsRestantes = 3;

	private final int nbClesNecessaires = 1;

	private Color[] couleursJoueurs = new Color[]{Color.MAGENTA, Color.ORANGE, Color.GREEN, Color.PINK};

    /** Construction : on initialise un tableau de zones. */
    public CModele() {
	/**
	 * Pour éviter les problèmes aux bords, on ajoute une ligne et une
	 * colonne de chaque côté, dont les cellules n'évolueront pas.
	 */
		zones = new Zone[LARGEUR+2][HAUTEUR+2];
		//zones = new Zone[LARGEUR][HAUTEUR];
		for(int i=0; i<LARGEUR+2; i++) {
			for(int j=0; j<HAUTEUR+2; j++) {
				zones[i][j] = new Zone(this,i, j);
			}
		}

		joueurs = new Joueur[4];
		Zone z;
		for (int i = 0; i < 4; i++){
			z = zoneAlea();
			//joueurs[i] = new Joueur(i+1, i,0, couleursJoueurs[i]);
			joueurs[i] = new Joueur(i+1, z.getX(),z.getY(), couleursJoueurs[i]);
		}

		init();
    }

	public int getNumJoueurActuel() {
		return joueurActuel+1;
	}

	public int getNbActionsRestantes() {
		return nbActionsRestantes;
	}

    /**
     * Initialisation aléatoire des cellules, exceptées celle des bords qui
     * ont été ajoutés.
     */
    public void init() {
		Zone z = zoneAlea();
		heliport = new Heliport(this, z.getX(), z.getY());

		zones[heliport.getX()][heliport.getY()] = heliport;

		for (int i = 0; i < 4; i++) {
			do {
				z = zoneAlea();
			} while (z.isHeliport() || z.isZoneArtefact());
			zones[z.getX()][z.getY()] = new ZoneArtefact(this,z.getX(),z.getY(), TypeArtefact.values()[i], nbClesNecessaires);
		}
    }
	private Zone zoneAlea(){
		return zones[((int) (Math.random() * LARGEUR))][((int) (Math.random() * LARGEUR))];
	}

	/** Innonde 3 zones aléatoires **/
	public void innondeZones(){
		Zone z;
		for (int i = 0; i < 3; i++){
			z = zoneAlea();
			while (z.getEtat() == EtatZone.Submergee){
				z = zoneAlea();
			}
			z.innonde();
		}
	}

	public Joueur[] getJoueurs(){
		return joueurs;
	}

	/** Verifie si la zone proche du joueur dans la direction donnée n'est pas submergée **/
	public boolean checkZoneJoueur(Joueur j, Direction dir) {
		int x = j.getX();
		int y = j.getY();

		switch (dir) {
			case Haut:
				if (y - 1 >= 0 && zones[x][y - 1].getEtat() != EtatZone.Submergee) {
					return true;
				}
				break;
			case Bas:
				if (y + 1 < CModele.HAUTEUR && zones[x][y + 1].getEtat() != EtatZone.Submergee) {
					return true;
				}
				break;
			case Gauche:
				if (x - 1 >= 0 && zones[x - 1][y].getEtat() != EtatZone.Submergee) {
					return true;
				}
				break;
			case Droite:
				if (x + 1 < CModele.LARGEUR && zones[x + 1][y].getEtat() != EtatZone.Submergee) {
					return true;
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + dir);
		}
		return false;
	}

	/** Déplace le joueur s'il est bien dans la grille et qu'il ne va pas sur une zone submergée **/
	public void deplaceJoueur(Direction dir) {
		// À voir s'il ne faut pas directement utiliser le tableau plutôt que j lorsqu'on déplace
		if (nbActionsRestantes > 0) {
			if (checkZoneJoueur(joueurs[joueurActuel], dir)){
				joueurs[joueurActuel].deplace(dir);
				System.out.println(joueurs[joueurActuel].toString());
				nbActionsRestantes--;
				//System.out.println("Actions restantes :" + nbActionsRestantes);
				VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
			}
		}
	}

	public void assecheJoueurDirection(Direction dir) {
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			int x = j.getX();
			int y = j.getY();

			if (dir == null){
				if (zones[x][y].getEtat() == EtatZone.Innondee) {
					zones[x][y].assecheZone();
					nbActionsRestantes--;
				}
			}
			else {
				switch (dir) {
					case Haut:
						if (y - 1 > 0 && zones[x][y - 1].getEtat() == EtatZone.Innondee) {
							zones[x][y-1].assecheZone();
							nbActionsRestantes--;
						}
						break;
					case Bas:
						if (y + 1 < CModele.HAUTEUR && zones[x][y + 1].getEtat() == EtatZone.Innondee) {
							zones[x][y+1].assecheZone();
							nbActionsRestantes--;
						}
						break;
					case Gauche:
						if (x - 1 > 0 && zones[x - 1][y].getEtat() == EtatZone.Innondee) {
							zones[x-1][y].assecheZone();
							nbActionsRestantes--;
						}
						break;
					case Droite:
						if (x + 1 < CModele.LARGEUR && zones[x + 1][y].getEtat() == EtatZone.Innondee) {
							zones[x+1][y].assecheZone();
							nbActionsRestantes--;
						}
						break;
					default:
						throw new IllegalStateException("Unexpected value: " + dir);
				}
			}
			VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
		}
	}

	public void lanceSacSable(int x, int y) {
		if (nbActionsRestantes > 0) {
			if (zones[x][y].getEtat() == EtatZone.Innondee) {
				zones[x][y].assecheZone();
				nbActionsRestantes--;
			}
		}
	}

	public void deplacementHelico(int x2, int y2) {
		if (nbActionsRestantes > 0) {
			if (!zones[x2][y2].estSubmergee()) {
				int x1 = joueurs[joueurActuel].getX();
				int y1 = joueurs[joueurActuel].getY();
				for (Joueur j: joueurs) {
					if (j.getX() == x1 && j.getY() == y1)
						j.deplacementLibre(x2, y2);
				}
				nbActionsRestantes--;
			}
		}
		//TODO
		//VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
	}

	/** Met en place le prochain joueur et son nombre d'actions **/
	public void joueurSuivant() {
		joueurActuel = (joueurActuel+1) % 4;
		nbActionsRestantes = 3;
	}

	public void cleAlea() {
		if (Math.random() < 0.5)
			joueurs[joueurActuel].addCle(TypeArtefact.values()[new Random().nextInt(TypeArtefact.values().length)]);
		//System.out.println(joueurs[joueurActuel].clesToString());
	}

	public void joueurRecupArtefact() {
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			int x = j.getX();
			int y = j.getY();

			TypeArtefact typeZone = zones[x][y].getType();
			int nbCles = zones[x][y].getNbCles();

			if (typeZone != null) {
				switch (typeZone){
					case AIR -> {
						if (j.getNbCles(TypeArtefact.AIR) >= nbCles) {
							j.addArtefact(TypeArtefact.AIR);
							j.removeCle(TypeArtefact.AIR, nbCles);
							nbActionsRestantes--;
						}
					}
					case EAU -> {
						if (j.getNbCles(TypeArtefact.EAU) >= nbCles) {
							j.addArtefact(TypeArtefact.EAU);
							j.removeCle(TypeArtefact.EAU, nbCles);
							nbActionsRestantes--;
						}
					}
					case FEU -> {
						if (j.getNbCles(TypeArtefact.FEU) >= nbCles) {
							j.addArtefact(TypeArtefact.FEU);
							j.removeCle(TypeArtefact.FEU, nbCles);
							nbActionsRestantes--;
						}
					}
					case TERRE -> {
						if (j.getNbCles(TypeArtefact.TERRE) >= nbCles) {
							j.addArtefact(TypeArtefact.TERRE);
							j.removeCle(TypeArtefact.TERRE, nbCles);
							nbActionsRestantes--;
						}
					}
				}
				VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
				//System.out.println(joueurs[joueurActuel].artefactsToString());
			}
		}
	}

	public void donnerCle(int numJoueurCible, TypeArtefact typeCle) {
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			//Joueur j2 = joueurs[numJoueurCible-1];
			Joueur j2 = joueurs[numJoueurCible];
			int x = j.getX();
			int y = j.getY();

			TypeArtefact typeZone = zones[x][y].getType();
			int nbCles = zones[x][y].getNbCles();

			if (numJoueurCible != joueurActuel && j.getNbCles(typeCle) > 0) {
				if (j2.getX() == j.getX() && j2.getY() == j.getY()) {
					j.removeCle(typeCle, 1);
					j2.addCle(typeCle);
					nbActionsRestantes--;
				}
				VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
				System.out.println(j.toString());
				System.out.println(j2.toString());
			}
		}
	}
	/** Indique si au moins une zone voisine est franchissable **/
	public boolean zonesVoisinesOK(){
		for (int i = 0; i <4; i++){
			if (checkZoneJoueur(joueurs[joueurActuel], Direction.values()[i]))
				return true;
		}
		return false;
	}

	public boolean joueurEnVie() {
		int x = joueurs[joueurActuel].getX();
		int y = joueurs[joueurActuel].getY();

		if (zones[x][y].estSubmergee() && !zonesVoisinesOK()){
			return false;
		}
		return true;
	}

	public boolean unArtefactDeChaque() {
		Integer[] tab = new Integer[]{0,0,0,0};
		for (Joueur j: joueurs) {
			for (int i = 0; i < 4; i++) {
				tab[i] += j.getNbCles(TypeArtefact.values()[i]);
			}
		}
		for (Integer i: tab) {
			if (i == 0)
				return false;
		}
		return true;
	}

	public boolean partieGagnee(){
		for (Joueur j: joueurs) {
			if (j.getX() != heliport.getX() || j.getY() != heliport.getY()){
				return false;
			}
		}
		return unArtefactDeChaque();
	}

	public boolean partiePerdue(){
		if (!joueurEnVie())
			return true;
		if (heliport.estSubmergee())
			return true;

		return false;
	}

	/*
	public void removeJoueur(Joueur joueur){
		int x = joueur.getX();
		int y = joueur.getY();
		Zone z;
		Zone j = getZone(x,y);
		if(j.zoneInondee()){
			JOptionPane.showMessageDialog(null, "Je suis tombé dans l'eau c'est la faute à Rousseau....!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}*/

    /**
     * Calcul de la génération suivante.
     */
    public void avance() {
	/**
	 * On procède en deux étapes.
	 *  - D'abord, pour chaque cellule on évalue ce que sera son état à la
	 *    prochaine génération.
	 *  - Ensuite, on applique les évolutions qui ont été calculées.
	 **/
		if (partiePerdue()){
			JOptionPane.showMessageDialog(null, "Vous avez perdu...", "Error", JOptionPane.ERROR_MESSAGE);
		}
		if (partieGagnee()) {
			JOptionPane.showMessageDialog(null, "Vous avez gagné !", "Error", JOptionPane.ERROR_MESSAGE);
		}

		cleAlea();
		innondeZones();

		joueurSuivant();

	/**
	 * Pour finir, le modèle ayant changé, on signale aux observateurs
	 * qu'ils doivent se mettre à jour.
	 */
		notifyObservers();
    }

    /**
     * Méthode auxiliaire : compte le nombre de voisines vivantes d'une
     * cellule désignée par ses coordonnées.
     */
    /*protected int compteVoisines(int x, int y) {
		int res=0;
	/**
	 * Stratégie simple à écrire : on compte les cellules vivantes dans le
	 * carré 3x3 centré autour des coordonnées (x, y), puis on retire 1
	 * si la cellule centrale est elle-même vivante.
	 * On n'a pas besoin de traiter à part les bords du tableau de cellules
	 * grâce aux lignes et colonnes supplémentaires qui ont été ajoutées
	 * de chaque côté (dont les cellules sont mortes et n'évolueront pas).
	 */
		/*for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
				if (zones[i][j].etat) {
					res++;
				}
			}
		}
		return (res - ((zones[x][y].etat)?1:0));
	/**
	 * L'expression [(c)?e1:e2] prend la valeur de [e1] si [c] vaut [true]
	 * et celle de [e2] si [c] vaut [false].
	 * Cette dernière ligne est donc équivalente à
	 *     int v;
	 *     if (cellules[x][y].etat) { v = res - 1; }
	 *     else { v = res - 0; }
	 *     return v;
	 */
    //}

    /**
     * Une méthode pour renvoyer la cellule aux coordonnées choisies (sera
     * utilisée par la vue).
     */
    public Zone getZone(int x, int y) {
		return zones[x][y];
    }
    /**
     * Notez qu'à l'intérieur de la classe [CModele], la classe interne est
     * connue sous le nom abrégé [Cellule].
     * Son nom complet est [CModele.Cellule], et cette version complète est
     * la seule à pouvoir être utilisée depuis l'extérieur de [CModele].
     * Dans [CModele], les deux fonctionnent.
     */
}

/** Fin de la classe CModele. */



/** Fin de la classe Joueur, et du modèle en général. */
