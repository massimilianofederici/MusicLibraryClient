package massimiliano.test;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final List<String> VIEWS = Arrays.asList("COMPOSER", "GENRE", "ALBUM", "ARTIST", "TITLE");

    private TCPClient client;

    private Stack<JSONObject> selection = new Stack<>();


    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initialiseLibrary();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.listView = (ListView) view.findViewById(R.id.list_id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selection.push((JSONObject) listView.getAdapter().getItem(position));
                new SendMessage().execute(Commands.selectItemCommand(getSelectedItem(), VIEWS));
            }
        });
        return view;
    }

    private void initialiseLibrary() {
        client = new TCPClient(new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(JSONArray message) {
                updateView(message);
            }
        });
        new SendMessage().execute(Commands.chooseLibraryCommand());
    }

    private JSONObject getSelectedItem() {
        return selection.peek();
    }

    boolean hasSelection() {
        return !selection.isEmpty();
    }

    void goBack() {
        selection.pop();
        if (!selection.isEmpty()) {
            new SendMessage().execute(Commands.selectItemCommand(getSelectedItem(), VIEWS));
        } else {
            new SendMessage().execute(Commands.chooseLibraryCommand());
        }
    }


    void updateView(final JSONArray view) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject[] items = new JSONObject[view.length()];
                    for (int i = 0; i < view.length(); i++) {
                        items[i] = view.getJSONObject(i);
                    }
                    listView = (ListView) getActivity().findViewById(R.id.list_id);
                    listView.setAdapter(new JSONArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items));
                } catch (JSONException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
    }

    class SendMessage extends AsyncTask<JSONObject, String, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            for (JSONObject command : params) {
                client.sendMessage(command);
            }
            return null;
        }
    }
}
