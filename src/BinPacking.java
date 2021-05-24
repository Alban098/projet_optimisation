import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinPacking {
    DataModel dataModel;

    public BinPacking(DataModel dm){
        dataModel = dm;
    }

    public BinsList getDecreasingArray(){
        ArrayList<Integer> weightsSorted = new ArrayList<>(dataModel.weights);
        weightsSorted.sort(Collections.reverseOrder());
        return new BinsList(weightsSorted, dataModel.binCapacity);
    }

    public BinsList getRandomArray(){
        ArrayList<Integer> randomArray = new ArrayList<>(dataModel.weights);
        Collections.shuffle(randomArray);
        return new BinsList(randomArray, dataModel.binCapacity);
    }

    public List<Bin> getOneToOneBin(List<Integer> weights) throws Exception {
        List<Bin> oneToOneList = new ArrayList<>();
        for (Integer weight : weights) {
            oneToOneList.add(new Bin(dataModel.binCapacity, weight));
        }
        return oneToOneList;
    }
}
