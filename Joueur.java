import java.security.KeyPair;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

class Joueur{
	private int numero;
	private int x, y;

	//Liste des artefacts du joueur
	private HashMap<TypeArtefact, Integer> artefacts;
	//Liste des clés du joueur
	private HashMap<TypeArtefact, Integer> cles;

	public Joueur(int numero, int x, int y){
		this.numero = numero;
		this.x = x;
		this.y = y;

		artefacts = new HashMap<>();
		cles = new HashMap<>();

		artefacts.put(TypeArtefact.FEU, 0);
		artefacts.put(TypeArtefact.AIR, 0);
		artefacts.put(TypeArtefact.EAU, 0);
		artefacts.put(TypeArtefact.TERRE, 0);
		cles.put(TypeArtefact.FEU, 0);
		cles.put(TypeArtefact.AIR, 0);
		cles.put(TypeArtefact.EAU, 0);
		cles.put(TypeArtefact.TERRE, 0);

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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

	/** Partie sur les Artéfacts  dans la classe Joueur*/
	/*public static TypeArtefact getTypeArtefact(TypeArtefact typeArtefact) {
		TypeArtefact artefact = artefactListe.get(typeArtefact);
		if(artefact ==  null){
			artefactListe.put(typeArtefact, artefact);
		}return artefact;
	}

	public TypeArtefact getType(){
		return typeArtefact;
	}

	/** Partie sur les Clés  dans la classe Joueur*/
	/*public TypeArtefact getTypeCle() {
		return typeCle;
	}*/

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
}