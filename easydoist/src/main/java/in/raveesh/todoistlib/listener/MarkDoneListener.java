package in.raveesh.todoistlib.listener;

/**
 * Created by Raveesh on 05/07/16.
 */
public interface MarkDoneListener {
    void success();
    void failure(Throwable t);
}
