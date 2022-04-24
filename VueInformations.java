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

    public static JLabel[] labelList = new JLabel[]{labelCleJ1,labelArtJ1,labelCleJ2,labelArtJ2,labelCleJ3,labelArtJ3,labelCleJ4,labelArtJ4};

    GridLayout grid = new GridLayout(4,1);
    GridLayout gridLabels = new GridLayout(2,1);

	/** Constructeur. */
	public VueInformations(CModele modele) {
        Joueur[] joueurs = modele.getJoueurs();

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        JPanel[] panelsList = new JPanel[]{panel1,panel2,panel3,panel4};

        this.modele = modele;
        this.setLayout(grid);


        /*for (int i = 0; i < 4; i++){
            panelsList[i].setLayout(gridLabels);
            labelList[i] = new JLabel(joueurs[i].clesToString());
            this.add(labelList[i]);

            labelList[i+1] = new JLabel(joueurs[i].artefactsToString());
            this.add(labelList[i+1]);

            panelsList[i].add(labelList[i]);
            panelsList[i].add(labelList[i+1]);
            this.add(panelsList[i]);
        }*/
        panel1.setLayout(gridLabels);
        labelCleJ1 = new JLabel(joueurs[0].clesToString());
        labelArtJ1 = new JLabel(joueurs[0].artefactsToString());
        //this.add(labelCleJ1); //bizarre
        //this.add(labelArtJ1); //bizarre
        panel1.add(labelCleJ1);
        panel1.add(labelArtJ1);
        this.add(panel1);

        panel2.setLayout(gridLabels);
        labelCleJ2 = new JLabel(joueurs[1].clesToString());
        labelArtJ2 = new JLabel(joueurs[1].artefactsToString());
        panel2.add(labelCleJ2);
        panel2.add(labelArtJ2);
        this.add(panel2);

        panel3.setLayout(gridLabels);
        labelCleJ3 = new JLabel(joueurs[2].clesToString());
        labelArtJ3 = new JLabel(joueurs[2].artefactsToString());
        panel3.add(labelCleJ3);
        panel3.add(labelArtJ3);
        this.add(panel3);

        panel4.setLayout(gridLabels);
        labelCleJ4 = new JLabel(joueurs[3].clesToString());
        labelArtJ4 = new JLabel(joueurs[3].artefactsToString());
        panel4.add(labelCleJ4);
        panel4.add(labelArtJ4);
        this.add(panel4);
	}

    public static void majLabels(CModele modele) {
        Joueur[] joueurs = modele.getJoueurs();
        /*nt j = 1;
        for (int i = 0; i < 4; i++){
            VueInformations.labelList[i].setText(joueurs[i].clesToString());
            VueInformations.labelList[j].setText(joueurs[i].artefactsToString());

            j+=2;
        }*/
        labelCleJ1.setText(joueurs[0].clesToString());
        labelArtJ1.setText(joueurs[0].artefactsToString());

        labelCleJ2.setText(joueurs[1].clesToString());
        labelArtJ2.setText(joueurs[1].artefactsToString());

        labelCleJ3.setText(joueurs[2].clesToString());
        labelArtJ3.setText(joueurs[2].artefactsToString());

        labelCleJ4.setText(joueurs[3].clesToString());
        labelArtJ4.setText(joueurs[3].artefactsToString());

    }
}