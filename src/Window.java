import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class Window extends JFrame {
    JLabel currentFile;
    int method;
    JTextField dField;
    private JPanel wrapperEast;

    public Window(String str) {
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        method = 0;

        Display display = new Display();

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
        boxSouth.add(new Button(ButtonEnum.ALEATOIRE));
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

    public void changeContent(ButtonEnum bEnum) {
        switch (bEnum) {
            case OPEN -> {
                Main.open();
                resetEast();
            }
            case FIRSTFIT -> {
                if (Main.dataModel == null)
                    Main.open();
                resetEast();
            }
            case RECUIT_SIMULE -> {
                if (Main.dataModel == null)
                    Main.open();
                JPanel panEast = new JPanel(new GridLayout(6, 1, 5, 5));

                StringTextField iteration = new StringTextField("Itérations : ", 50, Integer.MAX_VALUE, true);
                panEast.add(iteration.getjPanel());
                StringTextField iter_temp = new StringTextField("Itération Température : ", 50, Integer.MAX_VALUE, true);
                panEast.add(iter_temp.getjPanel());
                StringTextField temp = new StringTextField("Température initiale : ", 10000,Integer.MAX_VALUE, true);
                panEast.add(temp.getjPanel());
                StringTextField mu = new StringTextField("Décroissance : ", 0.9, 1, false);
                panEast.add(mu.getjPanel());

                wrapperEast.add(panEast);

                this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
                reload();
            }
            case TABU_SEARCH -> {
                if (Main.dataModel == null)
                    Main.open();

                JPanel panEast = new JPanel(new GridLayout(6, 1, 5, 5));

                StringTextField iteration = new StringTextField("Itérations : ", 500, Integer.MAX_VALUE, true);
                panEast.add(iteration.getjPanel());
                StringTextField tabu_size = new StringTextField("Liste Tabou size : ", 20, Integer.MAX_VALUE,true);
                panEast.add(tabu_size.getjPanel());

                wrapperEast.add(panEast);

                this.getContentPane().add(wrapperEast, BorderLayout.EAST, 2);
                reload();
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

        public JPanel getjPanel() {
            return jPanel;
        }

        public JTextField getjTextField() {
            return jTextField;
        }
    }
}