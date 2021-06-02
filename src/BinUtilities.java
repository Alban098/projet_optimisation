import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinUtilities {
    DataModel dataModel;

    public BinUtilities(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public int getLowerBound(){
        int sum = 0;
        for (int i = 0; i < dataModel.numItems; i++) {
            sum += dataModel.weights.get(i);
        }
        return (int) (Math.ceil((double) sum / dataModel.binCapacity));
    }

    public ArrayList<Integer> getDecreasingArray(){
        ArrayList<Integer> weightsSorted = new ArrayList<>(dataModel.weights);
        weightsSorted.sort(Collections.reverseOrder());
        return weightsSorted;
    }

    public ArrayList<Integer> getRandomArray(){
        ArrayList<Integer> randomArray = new ArrayList<>(dataModel.weights);
        Collections.shuffle(randomArray);
        return randomArray;
    }

    public BinsList firstFit(ArrayList<Integer> weights) throws Exception {
        List<Bin> binsList = new ArrayList<>();
        int j;
        for (Integer weight : weights) {
            j = -1;
            do {
                j++;
            } while (j < binsList.size() && !binsList.get(j).addWeight(weight));
            if (j >= binsList.size()) {
                binsList.add(new Bin(dataModel.binCapacity));
                binsList.get(j).addWeight(weight);
            }
        }
        return new BinsList(binsList, dataModel.binCapacity);
    }

    public BinsList oneToOneBin(ArrayList<Integer> weights) throws Exception {
        List<Bin> binsList = new ArrayList<>();
        for (Integer weight : weights) {
            binsList.add(new Bin(dataModel.binCapacity, weight));
        }
        return new BinsList(binsList, dataModel.binCapacity);
    }

    public BinsList tabuSearch(BinsList binsList, int n, int tabuListSize) {
        BinsList tempBinsList = new BinsList(binsList);
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
                return binsList;
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
                binsList.setBinsList(tempBinsList);
                fmax = temp_v;
            }
//            System.out.println(tabuList + " - " + delta);
        }
        return binsList;
    }

    public BinsList simulatedAnnealing(BinsList binsList, int n1, int n2, double t0, double mu){
        BinsList tempBinsList = new BinsList(binsList);
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
                        binsList.setBinsList(tempBinsList);
                        fmax = f_next;
                    }
                } else if (rand.nextFloat() <= Math.exp(-delta/t0)) {
                    binsList.setBinsList(tempBinsList);
                }
            }
            t0 *= mu;
        }
        return binsList;
    }
}
