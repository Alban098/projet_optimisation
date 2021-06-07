import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public final class Main {
    public static final int TOO_BIG = 110; //constante arbitraire dictant si un fichier devient trop gros ou pas.
    public static Window window;
    public static DataModel dataModel;

    public static void main(String[] args){
        window = new Window("");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Projet BinPacking");
    }

    public static void open() {
        dataModel = new DataModel();
    }
}