import java.util.ArrayList;
import java.util.List;

public class Bin {
    private int binCapacity;
    private int available;
    private List<Integer> content;

    public Bin(int size) throws Exception {
        this.binCapacity = size;
        available = size;
        if (available <= 0)
            throw new Exception("Capcity needs to be greater than 0");
        content = new ArrayList<>();
    }



    public Bin(int capacity, int weight) throws Exception {
        this.binCapacity = capacity;
        available = capacity - weight;
        if (available < 0)
            throw new Exception("Weight > capacity");
        content = new ArrayList<>();
        content.add(weight);
    }

    public int getBinCapacity() {
        return binCapacity;
    }

    public List<Integer> getContent() {
        return content;
    }

    public boolean addWeight(int weight) {
        if (available >= weight) {
            content.add(weight);
            available -= weight;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    public int removeWeight(int index1) {
        return content.remove(index1);
    }
}
