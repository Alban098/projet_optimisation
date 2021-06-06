import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Random;

public final class Main {
    public static final int TOO_BIG = 110; //constante arbitraire dictant si un fichier devient trop gros ou pas.
    public static Window window;
    public static DataModel dataModel;

    public static void main(String[] args) throws Exception {
        long t1;
        dataModel = new DataModel();
        Random rand = new Random();
        int start = dataModel.numItems;
        for (int i = 0; dataModel.numItems < 150; i++){
            t1 = System.nanoTime();
            BinsList binOptimal = BinPackingMip.getOptimal(Main.dataModel);
            System.out.println(System.nanoTime() - t1 + "ns " + dataModel.numItems + " " + dataModel.weights);
            for (int w = 0; w < start; w++) {
                dataModel.weights.add(dataModel.weights.get(w));
            }
            dataModel.numItems+= start;
            dataModel.numBins+= start;
        }
        window = new Window("");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Projet BinPacking");
    }

    public static void open() {
        dataModel = new DataModel();
    }
}