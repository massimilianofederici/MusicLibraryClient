package massimiliano.test;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Massimiliano on 08/09/2015.
 */
public class Commands {

    private Commands(){

    }

    public static JSONObject chooseLibraryCommand() {
        try {
            JSONObject command = new JSONObject();
            command.put("choose_library", "music1");
            return command;
        } catch (JSONException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static JSONObject selectItemCommand(final JSONObject selected, final List<String> views) {
        JSONObject message = new JSONObject();
        JSONObject command = new JSONObject();
        try {
            message.put("view_library", command);
            command.put("query", selected.getJSONObject("item"));
            command.put("view", findNextView(selected, views));
            return message;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static String findNextView(JSONObject selected, List<String> views) {
        try {
            String type = selected.getString("type");
            int index = views.indexOf(type);
            return views.get(index + 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
