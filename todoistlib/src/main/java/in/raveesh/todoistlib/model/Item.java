package in.raveesh.todoistlib.model;

import java.util.List;

/**
 * Created by Raveesh on 20/04/16.
 */
public class Item {
    int id;
    int user_id;
    int project_id;
    String content;
    String date_string;
    String date_lang;
    String due_date_utc;
    int priority;
    int indent;
    int item_order;
    int day_order;
    int collapsed;
    List<Integer> labels;
    int assigned_by_uid;
    int responsible_uid;
    int checked;
    int in_history;
    int is_deleted;
    int is_archived;
    int sync_id;
    String date_added;
}
