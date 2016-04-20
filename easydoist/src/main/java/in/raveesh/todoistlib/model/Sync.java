package in.raveesh.todoistlib.model;

import java.util.List;

/**
 * Created by Raveesh on 20/04/16.
 */
public class Sync {
    private long seq_no;
    private List<Item> Items;

    public long getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(long seq_no) {
        this.seq_no = seq_no;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }
}
