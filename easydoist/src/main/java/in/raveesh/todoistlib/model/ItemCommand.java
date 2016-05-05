package in.raveesh.todoistlib.model;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raveesh on 05/05/16.
 */
public class ItemCommand {
    @StringDef({Commands.complete})
    private @interface Commands {
        String complete = "item_complete";
    }

    private String type;
    private String uuid;
    private Args args;

    @NonNull
    public static ItemCommand getCompleteCommand(@NonNull String uuid, int arg){
        return new ItemCommand(Commands.complete, uuid, arg);
    }

    private ItemCommand(@Commands String type, String uuid, int arg){
        this.type = type;
        this.uuid = uuid;
        args = new Args(arg);
    }

    public class Args{
        List<Integer> ids;
        public Args(int id){
            ids = new ArrayList<>();
            ids.add(id);
        }
    }
}
