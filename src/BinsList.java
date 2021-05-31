import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.List;

public class BinsList {
    private final int binCapacity;
    public List<Bin> binsList;

    public BinsList(int binCapacity) {
        this.binCapacity = binCapacity;
    }

    public BinsList(BinsList binsList) {
        this.binCapacity = binsList.binCapacity;
        this.binsList = new ArrayList<>(binsList.binsList);
    }

    public BinsList(MPVariable[][] x, MPVariable[] y, List<Integer> weights, int binCapacity) throws Exception {
        this.binCapacity = binCapacity;
        this.binsList = new ArrayList<>();
        for (int j = 0; j < x.length; ++j) {
            if (y[j].solutionValue() == 1) {
                Bin bin = new Bin(binCapacity);
                binsList.add(bin);
                for (int i = 0; i < y.length; ++i)
                    if (x[i][j].solutionValue() == 1)
                        bin.addWeight(weights.get(i));
            }
        }
    }

    public BinsList(List<Bin> binsList, int binCapacity) {
        this.binsList = binsList;
        this.binCapacity = binCapacity;
    }

    public long calculate() {
        long sum = 0;
        for (Bin bin : binsList) {
            long f = bin.sum();
            sum += f * f;
        }
        return sum;
    }

    public long deltaMoveWeight(Bin bin1, int index1, Bin bin2) {
        if (bin1.getWeight(index1) <= bin2.getAvailable()) {
            int toMove = bin1.getWeight(index1);
            long f1 = bin1.sum();
            long f2 = bin2.sum();
            return (f1 - toMove) * (f1 - toMove) + (f2 + toMove) * (f2 + toMove) - f1 * f1 - f2 * f2;
        }
        return Long.MIN_VALUE;
    }

    public long deltaSwapWeight(Bin bin1, int index1, Bin bin2, int index2) {
        if (bin1.getWeight(index1) <= bin2.getAvailable() + bin2.getWeight(index2)
                && bin2.getWeight(index2) <= bin1.getAvailable() + bin1.getWeight(index1)) {
            int toAdd2 = bin1.getWeight(index1);
            int toAdd1 = bin2.getWeight(index2);
            long f1 = bin1.sum();
            long f2 = bin2.sum();
            return (f1 - toAdd2 + toAdd1) * (f1 - toAdd2 + toAdd1) + (f2 - toAdd1 + toAdd2) * (f2 - toAdd1 + toAdd2) - f1 * f1 - f2 * f2;
        }
        return Long.MIN_VALUE;
    }

    public boolean moveWeight(Bin bin1, int index1, Bin bin2){
        if (bin1.getWeight(index1) <= bin2.getAvailable()) {
            int toMove = bin1.removeWeight(index1);
            boolean bool = bin2.addWeight(toMove);
            if (bin1.getContent().isEmpty())
                binsList.remove(bin1);
            return bool;
        }
        return false;
    }

    public boolean swapWeight(Bin bin1, int index1, Bin bin2, int index2){
        if (bin1.getWeight(index1) <= bin2.getAvailable() + bin2.getWeight(index2)
                && bin2.getWeight(index2) <= bin1.getAvailable() + bin1.getWeight(index1)) {
            int toAdd2 = bin1.removeWeight(index1);
            int toAdd1 = bin2.removeWeight(index2);
            bin2.addWeight(toAdd2);
            bin1.addWeight(toAdd1);
            return true;
        }
        return false;
    }

    public int getSize() {
        return binsList.size();
    }

    public Bin getBin(int bin) {
        return binsList.get(bin);
    }

    public void setBinsList(BinsList binsList) {
        this.binsList = new ArrayList<>(binsList.binsList);
    }

    public BinsList sort() {
        for (Bin bin : binsList) {
            bin.sort();
        }

        binsList.sort(Bin::compareTo);
        return this;
    }

    @Override
    public String toString() {
        return binsList.toString() + " " + calculate();
    }
}
