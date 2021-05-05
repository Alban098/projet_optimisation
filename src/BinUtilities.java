public final class BinUtilities {
    public static int getLowerBound(DataModel dataModel){
        int sum = 0;
        for (int i = 0; i < dataModel.numItems; i++) {
            sum += dataModel.weights.get(i);
        }
        return (int) (Math.ceil((double) sum / dataModel.binCapacity));
    }
}
