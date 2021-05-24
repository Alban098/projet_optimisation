import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinPacking {
    DataModel dataModel;

    public BinPacking(DataModel dm){
        dataModel = dm;
    }

    public List<Integer> getDecreasingArray(){
        ArrayList<Integer> weightsSorted = new ArrayList<>(dataModel.weights);
        weightsSorted.sort(Collections.reverseOrder());
        return weightsSorted;
    }

    public List<Integer> getRandomArray(){
        ArrayList<Integer> randomArray = new ArrayList<>(dataModel.weights);
        Collections.shuffle(randomArray);
        return randomArray;
    }

    public List<Bin> getOneToOneBin(List<Integer> weights) throws Exception {
        List<Bin> oneToOneList = new ArrayList<>();
        for (Integer weight : weights) {
            oneToOneList.add(new Bin(dataModel.binCapacity, weight));
        }
        return oneToOneList;
    }

    public List<Bin> firstFit(List<Integer> weights) throws Exception {
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
        return binsList;
    }

    public boolean moveWeight(Bin bin1, int index1, Bin bin2){
        if (bin1.getWeight(index1) <= bin2.getBinCapacity()) {
            int toMove = bin1.removeWeight(index1);
            return bin2.addWeight(toMove);
        }
        return false;
    }

    public boolean swapWeight(Bin bin1, int index1, Bin bin2, int index2){
        if (bin1.getWeight(index1) <= bin2.getBinCapacity() && bin2.getWeight(index2) <= bin1.getBinCapacity()) {
            int toAdd2 = bin1.removeWeight(index1);
            bin2.addWeight(toAdd2);
            int toAdd1 = bin1.removeWeight(index2);
            bin2.addWeight(toAdd1);
            return true;
        }
        return false;
    }
}
