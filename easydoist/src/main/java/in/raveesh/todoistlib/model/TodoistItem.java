package in.raveesh.todoistlib.model;

import java.util.List;

/**
 * Created by Raveesh on 20/04/16.
 */
public class TodoistItem {
    public int id;
    public int user_id;
    public int project_id;
    public String content;
    public String date_string;
    public String date_lang;
    public String due_date_utc;
    public int priority;
    public int indent;
    public int item_order;
    public int day_order;
    public int collapsed;
    public List<Integer> labels;
    public int assigned_by_uid;
    public int responsible_uid;
    public int checked;
    public int in_history;
    public int is_deleted;
    public int is_archived;
    public int sync_id;
    public String date_added;
}
