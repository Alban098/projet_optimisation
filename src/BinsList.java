import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinsList {
    private final int binCapacity;
    private final List<Integer> weights;
    public List<Bin> binsList;
    private List<Bin> tempBinsList;

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

    public long calculate(List<Bin> binsList) {
        long sum = 0;
        for (Bin bin : binsList) {
            long f = bin.sum();
            sum += f * f;
        }
        return sum;
    }

    public BinsList simulatedAnnealing(int n1, int n2, double t0, double mu){
        tempBinsList = new ArrayList<>(binsList);
        long fmax = calculate(tempBinsList);
        Random rand = new Random();
        for (int k = 0; k < n1; k++){
            for (int i = 0; i < n2; i++){
                long delta;
                int bin1, index1, bin2;
                do {
                    bin1 = rand.nextInt(binsList.size());
                    index1 = rand.nextInt(binsList.get(bin1).getSize());
                    bin2 = rand.nextInt(binsList.size());
                    delta = deltaMoveWeight(bin1, index1, bin2);
                } while (delta == Long.MIN_VALUE);

                if (delta >= 0) {
                    moveWeight(bin1, index1, bin2);
                    long x_next = calculate(tempBinsList);
                    if (x_next > fmax) {
                        binsList = new ArrayList<>(tempBinsList);
                        fmax = x_next;
                    }
                } else if (rand.nextFloat() <= Math.exp(-delta/t0)) {
                    binsList = new ArrayList<>(tempBinsList);
                }
            }
            t0 *= mu;
        }
        return this;
    }



    public long deltaMoveWeight(int bin1, int index1, int bin2) {
        if (tempBinsList.get(bin1).getWeight(index1) <= tempBinsList.get(bin2).getAvailable()) {
            int toMove = tempBinsList.get(bin1).getWeight(index1);
            long f1 = tempBinsList.get(bin1).sum();
            long f2 = tempBinsList.get(bin2).sum();
            return (f1 - toMove) * (f1 - toMove) + (f2 + toMove) * (f2 + toMove) - f1 * f1 - f2 * f2;
        }
        return Long.MIN_VALUE;
    }

    public long deltaSwapWeight(int bin1, int index1, int bin2, int index2) {
        if (tempBinsList.get(bin1).getWeight(index1) <= tempBinsList.get(bin2).getAvailable() + tempBinsList.get(bin2).getWeight(index2)
                && tempBinsList.get(bin2).getWeight(index2) <= tempBinsList.get(bin1).getAvailable() + tempBinsList.get(bin1).getWeight(index1)) {
            int toAdd2 = tempBinsList.get(bin1).getWeight(index1);
            int toAdd1 = tempBinsList.get(bin2).getWeight(index2);
            long f1 = tempBinsList.get(bin1).sum();
            long f2 = tempBinsList.get(bin2).sum();
            return (f1 - toAdd2 + toAdd1) * (f1 - toAdd2 + toAdd1) + (f2 - toAdd1 + toAdd2) * (f2 - toAdd1 + toAdd2) - f1 * f1 - f2 * f2;
        }
        return Long.MIN_VALUE;
    }

    public boolean moveWeight(int bin1, int index1, int bin2){
        if (tempBinsList.get(bin1).getWeight(index1) <= tempBinsList.get(bin2).getAvailable()) {
            int toMove = tempBinsList.get(bin1).removeWeight(index1);
            return tempBinsList.get(bin2).addWeight(toMove);
        }
        return false;
    }

    public boolean swapWeight(int bin1, int index1, int bin2, int index2){
        if (tempBinsList.get(bin1).getWeight(index1) <= tempBinsList.get(bin2).getAvailable() + tempBinsList.get(bin2).getWeight(index2)
                && tempBinsList.get(bin2).getWeight(index2) <= tempBinsList.get(bin1).getAvailable() + tempBinsList.get(bin1).getWeight(index1)) {
            int toAdd2 = tempBinsList.get(bin1).removeWeight(index1);
            int toAdd1 = tempBinsList.get(bin2).removeWeight(index2);
            tempBinsList.get(bin2).addWeight(toAdd2);
            tempBinsList.get(bin1).addWeight(toAdd1);
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
