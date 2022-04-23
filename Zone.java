import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

class Zone {
    /** On conserve un pointeur vers la classe principale du modèle. */
    protected CModele modele;

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

	protected void assecheZone() {
		this.etat = EtatZone.Normale;
	}

	protected TypeArtefact getType() {
		return null;
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

	public int getNbCles() {
		return 0;
	}

	public boolean isHeliport(){
		return false;
	}
}

class ZoneArtefact extends Zone {
	private TypeArtefact type;
	private int nbCles;

	ZoneArtefact(CModele modele, int x, int y, TypeArtefact type, int nbCles){
		super(modele, x, y);
		this.type = type;
		this.nbCles = nbCles;
	}

	public TypeArtefact getType(){
		return type;
	}
	public int getNbCles(){
		return nbCles;
	}

}

class Heliport extends Zone {

	Heliport(CModele modele, int x, int y){
		super(modele, x, y);
	}

	public boolean isHeliport(){
		return true;
	}
}