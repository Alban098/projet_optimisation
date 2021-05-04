public final class BinUtilities {
    public static int getLowerBound(Items items){
        int sum = 0;
        for (int i = 0; i < items.getItem_nb(); i++) {
            sum += items.getItems().get(i);
        }
        return (int) (Math.ceil((double) sum / items.size));
    }
}
