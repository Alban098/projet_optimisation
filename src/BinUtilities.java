import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinUtilities {
    public static int getLowerBound(DataModel dataModel){
        int sum = 0;
        for (int i = 0; i < dataModel.numItems; i++) {
            sum += dataModel.weights.get(i);
        }
        return (int) (Math.ceil((double) sum / dataModel.binCapacity));
    }

    public static BinsList firstFit(DataModel dataModel) throws Exception {
        List<Bin> binsList = new ArrayList<>();
        int j;
        for (Integer weight : dataModel.weights) {
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

    public static BinsList oneToOneBin(DataModel dataModel) throws Exception {
        List<Bin> binsList = new ArrayList<>();
        for (Integer weight : dataModel.weights) {
            binsList.add(new Bin(dataModel.binCapacity, weight));
        }
        return new BinsList(binsList, dataModel.binCapacity);
    }

    public static long getFitnessUpperBound(DataModel dataModel) {
        return ((long) dataModel.binCapacity * dataModel.binCapacity * getLowerBound(dataModel));
    }
}
