import java.util.ArrayList;
import java.util.List;

public class Bin {
    private int size;
    private int available;
    private List<Integer> content;

    public Bin(int size){
        this.size = size;
        available = size;
        content = new ArrayList<>(size);
    }

    public int getSize() {
        return size;
    }

    public List<Integer> getContent() {
        return content;
    }

    public boolean addItem(int item) {
        if (available >= item) {
            content.add(item);
            available -= item;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
