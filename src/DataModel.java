import java.util.List;

public class DataModel {
    int numItems;
    int numBins;
    int binCapacity;
    List<Integer> weights;
    String file_name;

    public DataModel() {
        OpenTxt file = new OpenTxt();
        try {
            weights = file.open();
            String[] strings = file.getFeature();
            this.binCapacity = Integer.parseInt(strings[0]);
            this.numItems = Integer.parseInt(strings[1]);
            this.numBins = this.numItems;
            this.file_name = file.getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
