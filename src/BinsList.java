import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


    public BinsList tabuSearch(int n, int tabuListSize) {
        BinsList tempBinsList = new BinsList(this);
        long fmax = tempBinsList.calculate();
        long delta;
        long temp;
        int b1 = -1, i1 = -1, b2 = -1, i2 = -1;
        boolean gotDeleted;
        List<String> tabuList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            delta = Long.MIN_VALUE;
            gotDeleted = false;
            for (int bin1 = 0; bin1 < tempBinsList.getSize(); bin1++){
                for (int ind1 = 0; ind1 < tempBinsList.getBin(bin1).getSize(); ind1++){
                    for (int bin2 = bin1 + 1; bin2 < tempBinsList.getSize(); bin2++){
                        for (int ind2 = 0; ind2 < tempBinsList.getBin(bin2).getSize(); ind2++){
                            temp = tempBinsList.deltaSwapWeight(tempBinsList.getBin(bin1), ind1, tempBinsList.getBin(bin2), ind2);
                            if (temp >= delta && !tabuList.contains(tempBinsList.getBin(bin2) + ":" + ind2 + "-" + tempBinsList.getBin(bin1) + ":" + ind1)) {
                                delta = temp;
                                b1 = bin1;
                                b2 = bin2;
                                i1 = ind1;
                                i2 = ind2;
                            }
                            temp = tempBinsList.deltaMoveWeight(tempBinsList.getBin(bin1), ind1, tempBinsList.getBin(bin2));
                            if (temp >= delta && !tabuList.contains(tempBinsList.getBin(bin1) + ":" + (tempBinsList.getBin(bin1).getSize() - 1) + ">" + tempBinsList.getBin(bin2))) {
                                delta = temp;
                                b1 = bin1;
                                b2 = bin2;
                                i1 = ind1;
                                i2 = -1;
                            }
                            temp = tempBinsList.deltaMoveWeight(tempBinsList.getBin(bin2), ind2, tempBinsList.getBin(bin1));
                            if (temp >= delta && !tabuList.contains(tempBinsList.getBin(bin2) + ":" + (tempBinsList.getBin(bin2).getSize() - 1) + ">" + tempBinsList.getBin(bin1))) {
                                delta = temp;
                                b1 = bin2;
                                b2 = bin1;
                                i1 = ind2;
                                i2 = -1;
                            }
                        }
                    }
                }
            }
            if (delta == Long.MIN_VALUE)
                return this;
//            System.out.println(tempBinsList + " " + tempBinsList.get(b1) + " " + tempBinsList.get(b2) + " " + i1 + " " + i2);
            if (i2 == -1) {
                if (tempBinsList.getBin(b1).getSize() == 1)
                    gotDeleted = true;
                tempBinsList.moveWeight(tempBinsList.getBin(b1), i1, tempBinsList.getBin(b2));
            }
            else
                tempBinsList.swapWeight(tempBinsList.getBin(b1), i1, tempBinsList.getBin(b2), i2);
            if (delta <= 0)
                if (tabuList.size() >= tabuListSize)
                    tabuList.remove(0);
            if (gotDeleted)
                tabuList.add("deleted");
            else if (i2 == -1)
                tabuList.add(tempBinsList.getBin(b2) + ":" + (tempBinsList.getBin(b2).getSize() - 1) + ">" + tempBinsList.getBin(b1));
            else
                tabuList.add(tempBinsList.getBin(b2) + ":" + i2 + "-" + tempBinsList.getBin(b1) + ":" + i1);
            long temp_v = tempBinsList.calculate();
            if (temp_v > fmax) {
                setBinsList(tempBinsList);
                fmax = temp_v;
            }
//            System.out.println(tabuList + " - " + delta);
        }
        return this;
    }

    public BinsList simulatedAnnealing(int n1, int n2, double t0, double mu){
        BinsList tempBinsList = new BinsList(this);
        long fmax = tempBinsList.calculate();
        Random rand = new Random();
        boolean isMethodSwap;
        long delta;
        int index1, index2;
        for (int k = 0; k < n1; k++){
            for (int i = 0; i < n2; i++){
                Bin bin1, bin2;
                do {
                    bin1 = tempBinsList.getBin(rand.nextInt(tempBinsList.getSize()));
                    index1 = rand.nextInt(bin1.getSize());
                    bin2 = tempBinsList.getBin(rand.nextInt(tempBinsList.getSize()));
                    index2 = rand.nextInt(bin2.getSize());
                    isMethodSwap = rand.nextBoolean();
                    delta = isMethodSwap ? tempBinsList.deltaSwapWeight(bin1, index1, bin2, index2) : tempBinsList.deltaMoveWeight(bin1, index1, bin2);
                } while ((delta == Long.MIN_VALUE || bin1 == bin2));  // on boucle tant qu'on n'a pas un couple qui permette un swap ou un move ET qu'il a des voisins si on veut faire un move
//                System.out.println(tempBinsList + " move" + bin1 + ":" + index1 + " to " + bin2 + bin1 + ";" + bin2 + "   " + (bin1.getWeight(index1) <= bin2.getAvailable()));
                if (delta >= 0) {
                    if (isMethodSwap) {
                        tempBinsList.swapWeight(bin1, index1, bin2, index2);
                    } else {
                        tempBinsList.moveWeight(bin1, index1, bin2);
                    }
                    long f_next = tempBinsList.calculate();
                    if (f_next > fmax) {
                        setBinsList(tempBinsList);
                        fmax = f_next;
                    }
                } else if (rand.nextFloat() <= Math.exp(-delta/t0)) {
                    setBinsList(tempBinsList);
                }
            }
            t0 *= mu;
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(binsList.size() + " bins avec une taille de " + binCapacity
                + "\net une fitness de " + calculate()
                + "\n\nBorne inférieure du nombre de bin : " + BinUtilities.getLowerBound(Main.dataModel) + " bins"
                + "\nBorne suppérieure de fitness : " + BinUtilities.getFitnessUpperBound(Main.dataModel)
                + "\n\nDétails des bins:\n");
        sort();
        for (Bin bin: binsList) {
            str.append(bin.toString()).append("\n");
        }
        return str.toString();
    }
}
