import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirstFitDecreasing {
    List<Bin> binsList;
    List<Integer> weightsSorted;
    DataModel dataModel;

    public FirstFitDecreasing(DataModel dm){
        dataModel = dm;
        binsList = new ArrayList<>(BinUtilities.getLowerBound(dataModel));
        weightsSorted = new ArrayList<>(dm.weights);
        weightsSorted.sort(Collections.reverseOrder());

        int i,j;
        for (i = 0; i < weightsSorted.size(); i++){
            j = -1;
            do {
                j++;
            } while (j < binsList.size() && !binsList.get(j).addItem(weightsSorted.get(i)));
            if (j >= binsList.size()) {
                binsList.add(new Bin(dataModel.binCapacity));
                binsList.get(j).addItem(weightsSorted.get(i));
            }
        }
        System.out.println(binsList + ", with " + binsList.size() + " bins");
    }
}
