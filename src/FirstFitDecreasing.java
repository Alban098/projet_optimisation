import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FirstFitDecreasing {
    List<Bin> binsList;
    List<Integer> itemsSorted;
    Items items;

    public FirstFitDecreasing(Items it){
        items = it;
        binsList = new ArrayList<>(BinUtilities.getLowerBound(items));
        itemsSorted = new ArrayList<>(it.getItems());
        itemsSorted.sort(Collections.reverseOrder());

        int i,j;
        for (i = 0; i < itemsSorted.size(); i++){
            j = -1;
            do {
                j++;
            } while (j < binsList.size() && !binsList.get(j).addItem(itemsSorted.get(i)));
            if (j >= binsList.size()) {
                binsList.add(new Bin(items.size));
                binsList.get(j).addItem(itemsSorted.get(i));
            }
        }
        System.out.println(binsList + ", with" + binsList.size() + " bins");
    }
}
