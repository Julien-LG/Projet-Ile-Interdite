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
    public static final int HAUTEUR=20, LARGEUR=20;
    //On crée un tableau de zones
    private Zone[][] zones;
	private Joueur[] joueurs;

	private int joueurActuel = 0;
	private int nbActionsRestantes = 3;

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
		for (int i = 0; i < 4; i++){
			joueurs[i] = new Joueur(i+1, i,0);
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
		/*for(int i=1; i<=LARGEUR; i++) {
			for(int j=1; j<=HAUTEUR; j++) {
				if (Math.random() < .2) {
					zones[i][j].etat = true;
				}
			}
		}*/
    }
	private Zone zoneAlea(){
		return zones[((int) (Math.random() * LARGEUR))+1][((int) (Math.random() * LARGEUR))+1];
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

	/** Déplace le joueur s'il est bien dans la grille et qu'il ne va pas sur une zone submergée **/
	public void deplaceJoueur(Direction dir) {
		// À voir s'il ne faut pas directement utiliser le tableau plutôt que j lorsqu'on déplace
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			int x = j.getX();
			int y = j.getY();

			switch (dir) {
				case Haut:
					if (y - 1 > 0 && zones[x][y - 1].getEtat() != EtatZone.Submergee) {
						joueurs[joueurActuel].deplace(dir);
						nbActionsRestantes--;
					}
					break;
				case Bas:
					if (y + 1 < CModele.HAUTEUR && zones[x][y + 1].getEtat() != EtatZone.Submergee) {
						joueurs[joueurActuel].deplace(dir);
						nbActionsRestantes--;
					}
					break;
				case Gauche:
					if (x - 1 > 0 && zones[x - 1][y].getEtat() != EtatZone.Submergee) {
						joueurs[joueurActuel].deplace(dir);
						nbActionsRestantes--;
					}
					break;
				case Droite:
					if (x + 1 < CModele.LARGEUR && zones[x + 1][y].getEtat() != EtatZone.Submergee) {
						joueurs[joueurActuel].deplace(dir);
						nbActionsRestantes--;
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + dir);
			}

			//System.out.println(zones[x][y].getEtat());
			System.out.println("Actions restantes :" + nbActionsRestantes);
			//System.out.println(joueurs[joueurActuel].getX() + " " + joueurs[joueurActuel].getY());
			VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
		}
	}

	public void assecheJoueur() {
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			int x = j.getX();
			int y = j.getY();

			if (zones[x][y].getEtat() == EtatZone.Innondee) {
				zones[x][y].assecheZone();
				nbActionsRestantes--;
			}
			VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
		}
	}

	public void assecheJoueurDirection(Direction dir) {
		if (nbActionsRestantes > 0) {
			Joueur j = joueurs[joueurActuel];
			int x = j.getX();
			int y = j.getY();

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
			VueCommandes.labelActionsRestantes.setText("Actions restantes " + nbActionsRestantes);
		}
	}

	/** Met en place le prochain joueur et son nombre d'actions **/
	public void joueurSuivant() {
		switch (joueurActuel){
			case 0 -> joueurActuel = 1;
			case 1 -> joueurActuel = 2;
			case 2 -> joueurActuel = 3;
			case 3 -> joueurActuel = 0;
		}
		nbActionsRestantes = 3;
	}

	public void cleAlea() {
		int alea = (int)(Math.random() * 8);

		switch (alea){
			case 0 -> joueurs[joueurActuel].addCle(TypeArtefact.FEU);
			case 1 -> joueurs[joueurActuel].addCle(TypeArtefact.AIR);
			case 2 -> joueurs[joueurActuel].addCle(TypeArtefact.EAU);
			case 3 -> joueurs[joueurActuel].addCle(TypeArtefact.TERRE);
			case 4,5,6,7 -> {}
			default -> throw new IllegalStateException("Unexpected value: " + alea);
		}
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
			}
		}
	}

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
		/*for(int i=1; i<LARGEUR+1; i++) {
			for(int j=1; j<HAUTEUR+1; j++) {
				zones[i][j].evalue();
			}
		}
		for(int i=1; i<LARGEUR+1; i++) {
			for(int j=1; j<HAUTEUR+1; j++) {
				zones[i][j].evolue();
			}
		}*/
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