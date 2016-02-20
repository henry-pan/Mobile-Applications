package pan.henry.messaging;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Display extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] msgs;

    public Display(Activity context, String[] msgs) {
        super(context, R.layout.single_msg, msgs);
        this.context = context;
        this.msgs = msgs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_msg, null, true);
        TextView message_text = (TextView) rowView.findViewById(R.id.textView);
        message_text.setText(msgs[position]);
        return rowView;
    }
}