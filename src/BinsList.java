import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
                int index1, index2;
                Bin bin1, bin2;
                do {
                    bin1 = tempBinsList.get(rand.nextInt(tempBinsList.size()));
                    index1 = rand.nextInt(bin1.getSize());
                    bin2 = tempBinsList.get(rand.nextInt(tempBinsList.size()));
                    index2 = rand.nextInt(bin1.getSize());
                    delta = deltaMoveWeight(bin1, index1, bin2);
                } while ((delta == Long.MIN_VALUE || bin1 == bin2) && hasNeighboors(tempBinsList));
                if (!hasNeighboors(tempBinsList)) {
                    System.out.println("no more neighboors");
                    return this;
                }
//                System.out.println(tempBinsList + " move" + bin1 + ":" + index1 + " to " + bin2 + bin1 + ";" + bin2 + "   " + (bin1.getWeight(index1) <= bin2.getAvailable()));
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

    public BinsList tabuSearch(int n, int tabooListSize) {
        tempBinsList = new ArrayList<>(binsList);
        long fmax = calculate(tempBinsList);
        long delta;
        long temp;
        int b1 = -1, i1 = -1, b2 = -1, i2 = -1;
        List<Integer[]> tabuList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            delta = Long.MIN_VALUE;
            for (int bin1 = 0; bin1 < tempBinsList.size(); bin1++){
                for (int ind1 : tempBinsList.get(bin1).getContent()){
                    for (int bin2 = bin1 + 1; bin2 < tempBinsList.size(); bin2++){
                        for (int ind2 : tempBinsList.get(bin2).getContent()){
                            temp = deltaSwapWeight(tempBinsList.get(bin1), ind1, tempBinsList.get(bin2), ind2);
                            if (temp >= delta && !tabuList.contains(new Integer[]{bin1, bin2, ind1, ind2})) {
                                delta = temp;
                                b1 = bin1;
                                b2 = bin2;
                                i1 = ind1;
                                i2 = ind2;
                            }
                            temp = deltaMoveWeight(tempBinsList.get(bin1), ind1, tempBinsList.get(bin2));
                            if (temp >= delta && !tabuList.contains(new Integer[]{bin1, bin2, ind1, -1})) {
                                delta = temp;
                                b1 = bin1;
                                b2 = bin2;
                                i1 = ind1;
                                i2 = -1;
                            }
                            temp = deltaMoveWeight(tempBinsList.get(bin2), ind2, tempBinsList.get(bin1));
                            if (temp >= delta && !tabuList.contains(new Integer[]{bin2, bin1, ind2, -1})) {
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
            if (i2 == -1)
                moveWeight(tempBinsList.get(b1), i1, tempBinsList.get(b2));
            else
                swapWeight(tempBinsList.get(b1), i1, tempBinsList.get(b2), i2);
            if (delta >= 0)
                tabuList.add(new Integer[]{b1, i1, b2, i2});
            long temp_v = calculate(tempBinsList);
            if (temp_v > calculate(binsList))
                binsList = new ArrayList<>(tempBinsList);
                fmax = temp_v;
        }
        return this;
    }

    public boolean hasNeighboors(List<Bin> binsList) {
        int max_available = 0;
        int min_weight = Integer.MAX_VALUE;
        int temp;
        for (Bin bin : binsList){
            if (max_available < bin.getAvailable())
                max_available = bin.getAvailable();
            temp = Collections.min(bin.getContent());
            if (min_weight > temp)
                min_weight = temp;
        }
        return min_weight <= max_available;
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
                tempBinsList.remove(bin1);
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

    @Override
    public String toString() {
        return binsList.toString() + " " + calculate(binsList);
    }
}
