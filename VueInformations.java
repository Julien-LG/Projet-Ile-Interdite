import java.awt.*;
import javax.swing.*;


class VueInformations extends JPanel {
	/** On maintient une référence vers le modèle. */
	private CModele modele;

    public static JLabel labelCleJ1;
    public static JLabel labelArtJ1;

    public static JLabel labelCleJ2;
    public static JLabel labelArtJ2;

    public static JLabel labelCleJ3;
    public static JLabel labelArtJ3;

    public static JLabel labelCleJ4;
    public static JLabel labelArtJ4;

    GridLayout grid = new GridLayout(4,1);
    GridLayout gridLabels = new GridLayout(2,1);

	/** Constructeur. */
	public VueInformations(CModele modele) {
        JLabel[] labelList = new JLabel[]{labelCleJ1,labelArtJ1,labelCleJ2,labelArtJ2,labelCleJ3,labelArtJ3,labelCleJ4,labelArtJ4};
        Joueur[] joueurs = modele.getJoueurs();

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        JPanel[] panelsList = new JPanel[]{panel1,panel2,panel3,panel4};

        this.modele = modele;
        this.setLayout(grid);


        for (int i = 0; i < 4; i++){
            panelsList[i].setLayout(gridLabels);
            labelList[i] = new JLabel(joueurs[i].clesToString());
            this.add(labelList[i]);

            labelList[i+1] = new JLabel(joueurs[i].artefactsToString());
            this.add(labelList[i+1]);

            panelsList[i].add(labelList[i]);
            panelsList[i].add(labelList[i+1]);
            this.add(panelsList[i]);
        }
	}
}