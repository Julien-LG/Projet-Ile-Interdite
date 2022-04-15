import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Basé sur l'Application avec interface graphique de Mr. Thibaut Balabonski, Université Paris-Sud.
 */

/**
 * Interface des objets observateurs.
 */ 
interface Observer {
    /**
     * Un observateur doit posséder une méthode [update] déclenchant la mise à
     * jour.
     */
    public void update();
}

/**
 * Classe des objets pouvant être observés.
 */
abstract class Observable {
    /**
     * On a une liste [observers] d'observateurs, initialement vide, à laquelle
     * viennent s'inscrire les observateurs via la méthode [addObserver].
     */
    private ArrayList<Observer> observers;
    public Observable() {
	this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
	observers.add(o);
    }

    /**
     * Lorsque l'état de l'objet observé change, il est convenu d'appeler la
     * méthode [notifyObservers] pour prévenir l'ensemble des observateurs
     * enregistrés.
     * On le fait ici concrètement en appelant la méthode [update] de chaque
     * observateur.
     */
    public void notifyObservers() {
		for(Observer o : observers) {
			o.update();
		}
    }
}
/** Fin du schéma observateur/observé. */


/**
 * Nous allons commencer à construire notre application, en voici la classe
 * principale.
 */
public class IleInterdite {
    /**
     * L'amorçage est fait en créant le modèle et la vue, par un simple appel
     * à chaque constructeur.
     * Ici, le modèle est créé indépendamment (il s'agit d'une partie autonome
     * de l'application), et la vue prend le modèle comme paramètre (son
     * objectif est de faire le lien entre modèle et utilisateur).
     */
    public static void main(String[] args) {
	/**
	 * Pour les besoins du jour on considère la ligne EvenQueue... comme une
	 * incantation qu'on pourra expliquer plus tard.
	 */
	EventQueue.invokeLater(() -> {
		/** Voici le contenu qui nous intéresse. */
                CModele modele = new CModele();
				Joueur joueur1 = new Joueur(1, 0,0);
                CVue vue = new CVue(modele, joueur1);
	    });
    }
}
/** Fin de la classe principale. */


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
		init();
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

    /**
     * Calcul de la génération suivante.
     */
    public void avance() {
	/**
	 * On procède en deux étapes.
	 *  - D'abord, pour chaque cellule on évalue ce que sera son état à la
	 *    prochaine génération.
	 *  - Ensuite, on applique les évolutions qui ont été calculées.
	 */ 
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

		innondeZones();
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

enum EtatZone {Normale, Innondee, Submergee};
/**
 * Définition d'une classe pour les cellules.
 * Cette classe fait encore partie du modèle.
 */
class Zone {
    /** On conserve un pointeur vers la classe principale du modèle. */
    private CModele modele;

    // L'état d'une zone est donné par le type enum EtatZone.
    protected EtatZone etat;

	//On stocke les coordonnées pour pouvoir les passer au modèle.
    private final int x, y;
	/** Constructeur de la zone **/
    public Zone(CModele modele, int x, int y) {
        this.modele = modele;
        this.etat = EtatZone.Normale;
        this.x = x;
		this.y = y;
    }

	/** getter de l'etat de la zone **/
	public EtatZone getEtat() {
		return etat;
	}

	protected void innonde() {
		switch (etat){
			case Normale -> this.etat = EtatZone.Innondee;
			case Innondee, Submergee -> this.etat = EtatZone.Submergee;
		}
	}

	/**
     * Le passage à la génération suivante se fait en deux étapes :
     *  - D'abord on calcule pour chaque cellule ce que sera sont état à la
     *    génération suivante (méthode [evalue]). On stocke le résultat
     *    dans un attribut supplémentaire [prochainEtat].
     *  - Ensuite on met à jour l'ensemble des cellules (méthode [evolue]).
     * Objectif : éviter qu'une évolution immédiate d'une cellule pollue
     * la décision prise pour une cellule voisine.
     */

	/*private boolean prochainEtat;
    protected void evalue() {
        switch (this.modele.compteVoisines(x, y)) {
			case 2: prochainEtat=etat; break;
			case 3: prochainEtat=true; break;
			default: prochainEtat=false;
        }
    }
    protected void evolue() {
		etat = prochainEtat;
    }
    
    /** Un test à l'usage des autres classes (sera utilisé par la vue). */
    /*public boolean estVivante() {
		return etat;
    }*/



	@Override
	public String toString() {
		return "Zone{" +
				"modele=" + modele +
				", etat=" + etat +
				", x=" + x +
				", y=" + y +
				'}';
	}
}
/** Fin de la classe Zone */
enum Direction {Haut, Bas, Gauche, Droite};

class Joueur{
	private int numero;
	private int x, y;

	public Joueur(int numero, int x, int y){
		this.numero = numero;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void deplace(Direction dir){
		switch (dir){
			case Haut: if (y-1 > 0){this.y--;}
				break;
			case Bas: if (y+1 < CModele.HAUTEUR){this.y++;}
				break;
			case Gauche: if (x-1 > 0){this.x--;}
				break;
			case Droite: if (x+1 < CModele.LARGEUR){this.x++;}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + dir);
		}
	}
}


/** Fin de la classe Joueur, et du modèle en général. */


/**
 * La vue : l'interface avec l'utilisateur.
 *
 * On définit une classe chapeau [CVue] qui crée la fenêtre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage où on voit l'ensemble des cellules.
 *  - Une zone de commande avec un bouton pour passer à la génération suivante.
 */
class CVue {
    /**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueGrille grille;
    private VueCommandes commandes;

    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele, Joueur joueur) {
	/** Définition de la fenêtre principale. */
		frame = new JFrame();
		frame.setTitle("Jeu de la vie de Conway");
	/**
	 * On précise un mode pour disposer les différents éléments à
	 * l'intérieur de la fenêtre. Quelques possibilités sont :
	 *  - BorderLayout (défaut pour la classe JFrame) : chaque élément est
	 *    disposé au centre ou le long d'un bord.
	 *  - FlowLayout (défaut pour un JPanel) : les éléments sont disposés
	 *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche à droite et de haut en bas. Un élément peut
	 *    passer à la ligne lorsque l'on redimensionne la fenêtre.
	 *  - GridLayout : les éléments sont disposés l'un à la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes définis par le programmeur, dont toutes les cases ont la
	 *    même dimension. Cette dimension est calculée en fonction du
	 *    nombre de cases à placer et de la dimension du contenant.
	 */
		frame.setLayout(new FlowLayout());

	/** Définition des deux vues et ajout à la fenêtre. */
		grille = new VueGrille(modele, joueur);
		frame.add(grille);
		commandes = new VueCommandes(modele, joueur);
		frame.add(commandes);
	/**
	 * Remarque : on peut passer à la méthode [add] des paramètres
	 * supplémentaires indiquant où placer l'élément. Par exemple, si on
	 * avait conservé la disposition par défaut [BorderLayout], on aurait
	 * pu écrire le code suivant pour placer la grille à gauche et les
	 * commandes à droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
	 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
	 */
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
    }
}


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
	private Joueur joueur;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 40;

    /** Constructeur. */
    public VueGrille(CModele modele, Joueur joueur) {
		this.modele = modele;
		this.joueur = joueur;
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
		for(int i=1; i<=CModele.LARGEUR; i++) {
			for(int j=1; j<=CModele.HAUTEUR; j++) {
				/**
				 * ... Appeler une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				paint(g, modele.getZone(i, j), joueur, (i-1)*TAILLE, (j-1)*TAILLE);
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
    private void paint(Graphics g, Zone z, Joueur j, int x, int y) {
        /** Sélection d'une couleur. */
		switch (z.getEtat()){
			case Normale -> g.setColor(Color.WHITE);
			case Innondee -> g.setColor(Color.CYAN);
			case Submergee -> g.setColor(Color.BLUE);
		}

		if (j.getX() == x && j.getY() == y){
			g.setColor(Color.magenta);
		}


		/*if (c.estVivante()) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.WHITE);
		}
		/** Coloration d'un rectangle. */
		g.fillRect(x, y, TAILLE, TAILLE);
	}
}


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
	private Joueur joueur;

    /** Constructeur. */
    public VueCommandes(CModele modele, Joueur joueur) {
	this.modele = modele;
	this.joueur = joueur;
	/**
	 * On crée un nouveau bouton, de classe [JButton], en précisant le
	 * texte qui doit l'étiqueter.
	 * Puis on ajoute ce bouton au panneau [this].
	 */
	JButton boutonFinDeTour = new JButton("Fin de tour");
	this.add(boutonFinDeTour);

	JButton boutonHaut = new JButton("/|\\");
	this.add(boutonHaut);

	Controleur ctrl = new Controleur(modele);
	ControleurHaut ctrl2 = new ControleurHaut(joueur);
	/** Enregistrement du contrôleur comme auditeur du bouton. */
	boutonFinDeTour.addActionListener(ctrl);
	//boutonHaut.setAction(joueur.deplace(Direction.Bas));
	boutonHaut.addActionListener(ctrl2);
	
	/**
	 * Variante : une lambda-expression qui évite de créer une classe
         * spécifique pour un contrôleur simplissime.
         *
         JButton boutonAvance = new JButton(">");
         this.add(boutonAvance);
         boutonAvance.addActionListener(e -> { modele.avance(); });
         *
         */

    }
}
/** Fin de la vue. */

/**
 * Classe pour notre contrôleur rudimentaire.
 * 
 * Le contrôleur implémente l'interface [ActionListener] qui demande
 * uniquement de fournir une méthode [actionPerformed] indiquant la
 * réponse du contrôleur à la réception d'un événement.
 */
class Controleur implements ActionListener {
	/**
	 * On garde un pointeur vers le modèle, car le contrôleur doit
	 * provoquer un appel de méthode du modèle.
	 * Remarque : comme cette classe est interne, cette inscription
	 * explicite du modèle est inutile. On pourrait se contenter de
	 * faire directement référence au modèle enregistré pour la classe
	 * englobante [VueCommandes].
	 */
	CModele modele;

	public Controleur(CModele modele) {
		this.modele = modele;
	}

	/**
	 * Action effectuée à réception d'un événement : appeler la
	 * méthode [avance] du modèle.
	 */
	public void actionPerformed(ActionEvent e) {
		modele.avance();
	}
}

class ControleurHaut implements ActionListener {
	Joueur joueur;

	public ControleurHaut(Joueur joueur) {
		this.joueur = joueur;
	}

	public void actionPerformed(ActionEvent e) {
		joueur.deplace(Direction.Bas);
	}
}
/** Fin du contrôleur. */