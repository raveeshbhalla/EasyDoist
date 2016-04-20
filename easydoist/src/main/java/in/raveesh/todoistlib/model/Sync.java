package in.raveesh.todoistlib.model;

import java.util.List;

/**
 * Created by Raveesh on 20/04/16.
 */
public class Sync {
    private long seq_no;
    private List<TodoistItem> Items;

    public long getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(long seq_no) {
        this.seq_no = seq_no;
    }

    public List<TodoistItem> getItems() {
        return Items;
    }

    public void setItems(List<TodoistItem> items) {
        this.Items = items;
    }
}
