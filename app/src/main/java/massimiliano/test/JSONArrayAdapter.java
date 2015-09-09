package massimiliano.test;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Massimiliano on 06/09/2015.
 */
public class JSONArrayAdapter extends ArrayAdapter<JSONObject> {

    public JSONArrayAdapter(Context context, int resource, JSONObject[] array) {
        super(context, resource, array);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        JSONObject item = super.getItem(position);
        try {
            textView.setText(item.getString("label"));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return textView;
    }
}
