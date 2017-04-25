package sri.vokal.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sridhar on 25/4/17.
 */

public class StringItemAdapter extends BaseAdapter {

    private List<StringGroup.StringItem> items = new ArrayList<StringGroup.StringItem>();

    public StringItemAdapter(List<StringGroup.StringItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public StringGroup.StringItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        int type = getItemViewType(i);
        if (view == null) {
            holder = new ViewHolder();
            switch (type) {
                case StringGroup.StringItem.TYPE_HEADER:
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header, null);
                    holder.row = (TextView) view.findViewById(R.id.headerText);
                    break;
                case StringGroup.StringItem.TYPE_CHILD:
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child, null);
                    holder.row = (TextView) view.findViewById(R.id.childText);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.row.setText(getItem(i).toString());

        return view;
    }

    static class ViewHolder {
        public TextView row;
    }
}
