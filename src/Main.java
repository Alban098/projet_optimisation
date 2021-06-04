import javax.swing.*;

public final class Main {
    public static final int TOO_BIG = 110; //constante arbitraire dictant si un fichier devient trop gros ou pas.
    public static Window window;
    public static DataModel dataModel;

    public static void main(String[] args) throws Exception {
//        dataModel = new DataModel();
//        BinUtilities binUtilities = new BinUtilities(dataModel);
//        System.out.println(binUtilities.getLowerBound());
//        System.out.println(binUtilities.getFitnessUpperBound());

        window = new Window("");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Projet Graphe");

        long startTime = System.nanoTime();
//        try {
//            BinsList binOptimal = BinPackingMip.getOptimal(dataModel);
//            System.out.println("Optimal: " + binOptimal.sort() + ", with " + binOptimal.getSize() + " bins");
//        }
//        catch (Exception ignored) {
//
//        }
//        BinsList binsListFirstFitDecreasing = binUtilities.firstFit(binUtilities.getDecreasingArray());
//        System.out.println("FirstFitDecreasing: " + binsListFirstFitDecreasing.getSize() + " " + binsListFirstFitDecreasing.sort());
//
//
//        BinsList binsListRandomOneToOne = binUtilities.oneToOneBin(binUtilities.getRandomArray());
//        System.out.println("RandomOneToOne: " + binsListRandomOneToOne.getSize() + " " + binsListRandomOneToOne.sort());
//
//
//        BinsList binsListFirstFitRandom = binUtilities.firstFit(binUtilities.getRandomArray());
//        System.out.println("FirstFitRandom: " + binsListFirstFitRandom.getSize() + " " + binsListFirstFitRandom.sort());
//
//        BinsList recuit = binUtilities.simulatedAnnealing(binsListFirstFitRandom, 500, 500, 100000, 0.99);
//        System.out.println("Recuit Simulé : " + recuit.getSize() + " " + recuit.sort());
//
//        binsListFirstFitRandom = binUtilities.firstFit(binUtilities.getRandomArray());
//        BinsList tabu = binUtilities.tabuSearch(binsListFirstFitRandom,1000, 5);
//        System.out.println("Tabu Search : " + tabu.getSize() + " " + tabu.sort());
//
//        long duration = (System.nanoTime() - startTime) / 1000000;
//        System.out.println("Executed in " + duration + " ms");
//        System.exit(0); // close filedialog
    }

    public static void open() {
        dataModel = new DataModel();

        System.out.println(BinUtilities.getLowerBound(dataModel));
        System.out.println(BinUtilities.getFitnessUpperBound(dataModel));
    }
}