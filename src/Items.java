import java.util.List;

public class Items {
    int item_nb;
    int size;
    List<Integer> items;
    String file_name;

    public Items() {
        OpenTxt file = new OpenTxt();
        try {
            items = file.open();
            String[] strings = file.getFeature();
            this.size = Integer.parseInt(strings[0]);
            this.item_nb = Integer.parseInt(strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItem_nb() {
        return item_nb;
    }

    public List<Integer> getItems() {
        return items;
    }
}
