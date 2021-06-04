import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class Window extends JFrame {
    public ButtonEnum[] state;
    public static Map<ButtonEnum, Button> map;

    JLabel currentFile;
    int method;
    Display display;
    private JPanel wrapperEast;
    List<Integer> weights;
    Button validate;
    StringTextField iteration, iter_temp, temp, mu, tabu_size;
    BinsList binsList;

    public Window(String str) {
        state = new ButtonEnum[]{null, null, ButtonEnum.FIRST_FIT};
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        method = 0;

        display = new Display("");

        this.getContentPane().add(display, BorderLayout.CENTER, 0);


        Box boxNorth = Box.createHorizontalBox();
        boxNorth.add(new Button(ButtonEnum.OPEN));
        currentFile = new JLabel(" Current File : " + str);
        boxNorth.add(currentFile);

        JPanel wrapperNorth = new JPanel();
        wrapperNorth.setLayout(new BoxLayout(wrapperNorth, BoxLayout.X_AXIS));
        wrapperNorth.add(boxNorth);

        this.getContentPane().add(wrapperNorth, BorderLayout.NORTH, 1);


        resetEast();

        this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);


        JPanel boxWest = new JPanel(new GridLayout(3, 1, 5, 5));
        boxWest.add(new Button(ButtonEnum.FIRSTFIT));
        boxWest.add(new Button(ButtonEnum.RECUIT_SIMULE));
        boxWest.add(new Button(ButtonEnum.TABU_SEARCH));

        JPanel wrapperWest = new JPanel(new GridBagLayout());
        wrapperWest.add(boxWest);

        this.getContentPane().add(wrapperWest, BorderLayout.WEST, 3);


        JPanel boxSouth = new JPanel(new GridLayout(0, 2, 5, 5));
        boxSouth.add(new Button(ButtonEnum.FIRST_FIT));
        boxSouth.add(new Button(ButtonEnum.RANDOM));

        JPanel wrapperSouth = new JPanel(new GridBagLayout());
        wrapperSouth.add(boxSouth);

        this.getContentPane().add(wrapperSouth, BorderLayout.SOUTH, 4);


        this.setVisible(true);
    }

    private void resetEast() {
        wrapperEast = new JPanel(new GridBagLayout());
        wrapperEast.setDoubleBuffered(true);
    }

    public void changeContent(ButtonEnum bEnum) throws Exception {
        if (state[0] == null && bEnum != ButtonEnum.OPEN) {
            Main.open();
        }
        switch (bEnum) {
            case OPEN -> {
                Main.open();
                weights = Main.dataModel.weights;
                resetEast();
                reload();
            }
            case FIRSTFIT -> {
                method = 0;
                BinsList binsList = BinUtilities.firstFit(Main.dataModel);
                display.setText(binsList.toString());
                resetEast();
                reload();
            }
            case RECUIT_SIMULE -> {
                method = 1;

                resetEast();
                JPanel panEast = new JPanel(new GridLayout(6, 1, 5, 5));

                iteration = new StringTextField("Itérations : ", 50, Integer.MAX_VALUE, true);
                panEast.add(iteration.getJPanel());
                iter_temp = new StringTextField("Itération Température : ", 50, Integer.MAX_VALUE, true);
                panEast.add(iter_temp.getJPanel());
                temp = new StringTextField("Température initiale : ", 10000,Integer.MAX_VALUE, true);
                panEast.add(temp.getJPanel());
                mu = new StringTextField("Décroissance : ", 0.9, 1, false);
                panEast.add(mu.getJPanel());
                validate = new Button(ButtonEnum.VALIDATE);
                panEast.add(validate);

                wrapperEast.add(panEast);

                this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
                reload();
            }
            case TABU_SEARCH -> {
                method = 2;

                resetEast();
                JPanel panEast = new JPanel(new GridLayout(6, 1, 5, 5));

                iteration = new StringTextField("Itérations : ", 500, Integer.MAX_VALUE, true);
                panEast.add(iteration.getJPanel());
                tabu_size = new StringTextField("Liste Tabou size : ", 20, Integer.MAX_VALUE,true);
                panEast.add(tabu_size.getJPanel());
                panEast.add(validate);

                wrapperEast.add(panEast);

                this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
                reload();
            }
            case VALIDATE -> {
                switch (method) {
                    case 1 -> {
                        binsList.simulatedAnnealing(iteration.getInt(), iter_temp.getInt(), temp.getInt(), mu.getFloat());
                        display.setText(binsList.toString());
                    }
                    case 2 -> {
                            binsList.tabuSearch(iteration.getInt(), tabu_size.getInt());
                            display.setText(binsList.toString());
                    }
                }
            }
            case RANDOM -> {
                Main.dataModel.randomizeArray();
                binsList = BinUtilities.oneToOneBin(Main.dataModel);
            }
            case FIRST_FIT -> {
                Main.dataModel.randomizeArray();
                binsList = BinUtilities.firstFit(Main.dataModel);
            }
            case OPTIMAL -> {
                if (Main.dataModel.numItems > Main.TOO_BIG) {
                    JInternalFrame frame = new JInternalFrame();
                    String[] options = {"Continue", "Abort"};
                    int answer = JOptionPane.showOptionDialog(frame, "It can take a lot of time to process the optimal solution\nDo you want to continue ?", "File too big",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (answer == JOptionPane.YES_OPTION)
                        System.out.println("oui");
                    else
                        System.out.println("Aborted");
                }
            }
        }
    }

    public void reload() {
        repaint();
        revalidate();
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