import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Window extends JFrame {
    public static ButtonEnum gen_sol;
    public static ButtonEnum method;
    public static Map<ButtonEnum, Button> map;

    JLabel currentFile;
    JPanel panEastSA, panEastTS, wrapperEast;
    Display display;
    StringTextField iteration, iter_temp, temp, mu, tabu_size;
    BinsList binsList;

    public Window(String str) {
        map = new HashMap<>();
        gen_sol = null;
        method = null;
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Center
        display = new Display();
        JScrollPane displayScrollable = new JScrollPane(display);
        this.getContentPane().add(displayScrollable, BorderLayout.CENTER, 0);

        //North
        Box boxNorth = Box.createHorizontalBox();
        boxNorth.add(new Button("Ouvrir...", ButtonEnum.OPEN));
        currentFile = new JLabel(" Fichier ouvert : ");
        boxNorth.add(currentFile);

        JPanel wrapperNorth = new JPanel();
        wrapperNorth.setLayout(new BoxLayout(wrapperNorth, BoxLayout.X_AXIS));
        wrapperNorth.add(boxNorth);

        this.getContentPane().add(wrapperNorth, BorderLayout.NORTH, 1);

        //East
        wrapperEast = new JPanel(new GridBagLayout());
        wrapperEast.setDoubleBuffered(true);

        panEastSA = new JPanel(new GridLayout(6, 1, 5, 5));
        iteration = new StringTextField("Itérations : ", 50, Integer.MAX_VALUE, true);
        iter_temp = new StringTextField("Itération Température : ", 50, Integer.MAX_VALUE, true);
        temp = new StringTextField("Température initiale : ", 10000,Integer.MAX_VALUE, true);
        mu = new StringTextField("Décroissance : ", 0.9, 1, false);
        panEastSA.add(iteration.getJPanel());
        panEastSA.add(iter_temp.getJPanel());
        panEastSA.add(temp.getJPanel());
        panEastSA.add(mu.getJPanel());
        panEastSA.add(new Button("Valider", ButtonEnum.VALIDATE));

        panEastTS = new JPanel(new GridLayout(6, 1, 5, 5));
        iteration = new StringTextField("Itérations : ", 500, Integer.MAX_VALUE, true);
        tabu_size = new StringTextField("Taille liste tabou : ", 20, Integer.MAX_VALUE,true);
        panEastTS.add(iteration.getJPanel());
        panEastTS.add(tabu_size.getJPanel());
        panEastTS.add(new Button("Valider", ButtonEnum.VALIDATE));

        this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);

        // West
        JPanel boxWest = new JPanel(new GridLayout(4, 1, 5, 5));
        boxWest.add(new Button("Firstfit décroissant", ButtonEnum.FIRSTFIT));
        boxWest.add(new Button("Recuit simulé", ButtonEnum.RECUIT_SIMULE));
        boxWest.add(new Button("Recherche tabou", ButtonEnum.TABU_SEARCH));
        boxWest.add(new Button("ORtools (Optimal)", ButtonEnum.OPTIMAL));

        JPanel wrapperWest = new JPanel(new GridBagLayout());
        wrapperWest.add(boxWest);

        this.getContentPane().add(wrapperWest, BorderLayout.WEST, 3);

        //South
        JPanel boxSouth = new JPanel(new GridLayout(0, 3, 5, 5));
        boxSouth.add(new JLabel("Générateur de solution :"));
        boxSouth.add(new Button("Random firstfit", ButtonEnum.RANDOM_FIRSTFIT));
        boxSouth.add(new Button("Random one2one", ButtonEnum.RANDOM_ONE2ONE));

        JPanel wrapperSouth = new JPanel(new GridBagLayout());
        wrapperSouth.add(boxSouth);

        this.getContentPane().add(wrapperSouth, BorderLayout.SOUTH, 4);
        this.setVisible(true);
    }

    private void resetEast() {
        wrapperEast.removeAll();
        wrapperEast.setDoubleBuffered(true);
    }

    public void changeContent(ButtonEnum bEnum) throws Exception {
        display.reloadText("");
        if (Main.dataModel == null || bEnum == ButtonEnum.OPEN) {
            Main.open();
            if (Main.dataModel.weights.isEmpty()) {
                Main.dataModel = null;
                return;
            }
            currentFile.setText(" Fichier ouvert : " + Main.dataModel.file_name);
            gen_sol = null;
            method = null;
            resetEast();
            this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
        }
        switch (bEnum) {
            //method
            case FIRSTFIT -> {
                method = bEnum;
                changeContent(ButtonEnum.VALIDATE);
            }
            case RECUIT_SIMULE, TABU_SEARCH -> {
                method = bEnum;

                resetEast();

                if (bEnum == ButtonEnum.RECUIT_SIMULE)
                    wrapperEast.add(panEastSA);
                else
                    wrapperEast.add(panEastTS);

                this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
            }
            case OPTIMAL -> {
                if (Main.dataModel.numItems > Main.TOO_BIG) {
                    JInternalFrame frame = new JInternalFrame();
                    String[] options = {"Continuer", "Annuler"};
                    int answer = JOptionPane.showOptionDialog(frame,
                            "Le fichier spécifié est trop lourd.\n" +
                                    "La recherche de la solution optimale peut prendre beaucoup de temps", "Fichier trop lourd",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (answer == JOptionPane.NO_OPTION) {
                        method = null;
                        return;
                    }
                }
                method = bEnum;
                gen_sol = null;
                BinsList binOptimal = BinPackingMip.getOptimal(Main.dataModel);
                display.reloadText(binOptimal.sort().toString());
            }

            //gen sol
            case RANDOM_FIRSTFIT -> {
                gen_sol = bEnum;
                Main.dataModel.randomizeArray();
                binsList = BinUtilities.firstFit(Main.dataModel);
                if (method != null)
                    changeContent(ButtonEnum.VALIDATE);
            }
            case RANDOM_ONE2ONE -> {
                gen_sol = bEnum;
                Main.dataModel.randomizeArray();
                binsList = BinUtilities.oneToOneBin(Main.dataModel);
                if (method != null)
                    changeContent(ButtonEnum.VALIDATE);
            }

            //validate
            case VALIDATE -> {
                if (gen_sol == null && method != ButtonEnum.FIRSTFIT) {
                    JInternalFrame frame = new JInternalFrame();
                    JOptionPane.showMessageDialog(frame,
                            "Il faut choisir un générateur de solution initiale !",
                            "Pas de solution initiale !",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                switch (method) {
                    case FIRSTFIT -> {
                        Main.dataModel.sortArrayDecreasingOrder();
                        BinsList binsList = BinUtilities.firstFit(Main.dataModel);
                        display.reloadText(binsList.toString());
                        resetEast();
                        this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
                    }
                    case RECUIT_SIMULE -> {
                        binsList.simulatedAnnealing(iteration.getInt(), iter_temp.getInt(), temp.getInt(), mu.getFloat());
                        display.reloadText(binsList.toString());
                    }
                    case TABU_SEARCH -> {
                            binsList.tabuSearch(iteration.getInt(), tabu_size.getInt());
                            display.reloadText(binsList.toString());
                    }
                }
            }
        }
        reload();
//        System.out.println(method + " " + gen_sol + "      " + bEnum);
    }

    public void reload() {
        for (Button button: map.values())
            button.changeColor();
        revalidate();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private static class StringTextField extends JPanel{
        JTextField jTextField;
        JPanel jPanel;

        public StringTextField(String text, double value, int max, boolean isInt) {
            jPanel = new JPanel();
            jPanel.add(new JLabel(text));
            jTextField = new JTextField(4);
            if (isInt)
                jTextField.setText(String.valueOf((int) value));
            else
                jTextField.setText(String.valueOf(value));
            PlainDocument doc = (PlainDocument) jTextField.getDocument();
            doc.setDocumentFilter(new NumericFilter(max, isInt));
            jPanel.add(jTextField);
        }

        public JPanel getJPanel() {
            return jPanel;
        }

        public int getInt() {
            return Integer.parseInt(jTextField.getText());
        }

        public float getFloat() {
            return Float.parseFloat(jTextField.getText());
        }
    }
}