import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

class Joueur{
	private final int numero;
	private int x, y;
	private Color couleur;
	//private boolean vivant;

	//Liste des artefacts du joueur
	private HashMap<TypeArtefact, Integer> artefacts;
	//Liste des clés du joueur
	private HashMap<TypeArtefact, Integer> cles;

	public Joueur(int numero, int x, int y, Color couleur){
		this.numero = numero;
		this.x = x;
		this.y = y;
		this.couleur = couleur;
		//this.vivant = true;

		artefacts = new HashMap<>();
		cles = new HashMap<>();

		for (int i = 0; i <4; i++){
			artefacts.put(TypeArtefact.values()[i], 0);
			cles.put(TypeArtefact.values()[i], 0);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNumero() {
		return numero;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void deplace(Direction dir){
		switch (dir){
			case Haut: this.y--;
				break;
			case Bas: this.y++;
				break;
			case Gauche: this.x--;
				break;
			case Droite: this.x++;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + dir);
		}
	}

	public void deplacementLibre(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getNbArtefacts(TypeArtefact type){
		return artefacts.get(type);
	}

	public void addArtefact(TypeArtefact type){
		artefacts.replace(type, artefacts.get(type) + 1);
	}

	public int getNbCles(TypeArtefact type){
		return cles.get(type);
	}

	public void addCle(TypeArtefact type){
		cles.replace(type, cles.get(type) + 1);
	}

	public void removeCle(TypeArtefact type, int nbCles){
		cles.replace(type, cles.get(type) - nbCles);
	}


	@Override
	public String toString() {
		return  "Joueur " + numero+ " {" + "x=" + x + ", y=" + y + ", " + artefacts.toString() + ", " + cles.toString() +"}";
	}

	public String artefactsToString() {
		return "Artefacts du Joueur " + numero + " " + artefacts.toString();
	}

	public String clesToString() {
		return "Clés du Joueur " + numero + " " + cles.toString();
	}
}