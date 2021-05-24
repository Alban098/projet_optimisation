import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinsList {
    private final int binCapacity;
    private final List<Integer> weights;
    public List<Bin> binsList;

    public BinsList(ArrayList<Integer> weights, int binCapacity) {
        this.weights = weights;
        this.binCapacity = binCapacity;
    }

    public BinsList firstFit() throws Exception {
        binsList = new ArrayList<>();
        int j;
        for (Integer weight : weights) {
            j = -1;
            do {
                j++;
            } while (j < binsList.size() && !binsList.get(j).addWeight(weight));
            if (j >= binsList.size()) {
                binsList.add(new Bin(binCapacity));
                binsList.get(j).addWeight(weight);
            }
        }
        return this;
    }

    public BinsList oneToOneBin() throws Exception {
        binsList = new ArrayList<>();
        for (Integer weight : weights) {
            binsList.add(new Bin(binCapacity, weight));
        }
        return this;
    }

    public BinsList simulatedAnnealing(int n1, int n2, double t0){
        Random rand = new Random();
        for (int k = 0; k < n1; k++){
            for (int i = 0; i < n2; i++){
                rand.nextInt(sizeOfNeighboorhod);
            }
        }
        return this;
    }

    public boolean moveWeight(int bin1, int index1, int bin2){
        if (binsList.get(bin1).getWeight(index1) <= binsList.get(bin2).getAvailable()) {
            int toMove = binsList.get(bin1).removeWeight(index1);
            return binsList.get(bin2).addWeight(toMove);
        }
        return false;
    }

    public boolean swapWeight(int bin1, int index1, int bin2, int index2){
        if (binsList.get(bin1).getWeight(index1) <= binsList.get(bin2).getAvailable() + binsList.get(bin2).getWeight(index2)
                && binsList.get(bin2).getWeight(index2) <= binsList.get(bin1).getAvailable() + binsList.get(bin1).getWeight(index1)) {
            int toAdd2 = binsList.get(bin1).removeWeight(index1);
            int toAdd1 = binsList.get(bin2).removeWeight(index2);
            binsList.get(bin2).addWeight(toAdd2);
            binsList.get(bin1).addWeight(toAdd1);
            return true;
        }
        return false;
    }

    public int getSize() {
        return binsList.size();
    }

    @Override
    public String toString() {
        return binsList.toString();
    }
}
