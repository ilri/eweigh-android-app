package org.ilri.eweigh.cattle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;

import java.util.List;

public class CattleAdapter  extends BaseAdapter {

    private Context context;
    private List<Cattle> cattle;
    private LayoutInflater inflater;

    public CattleAdapter(Context context, List<Cattle> cattle){
        this.context = context;
        this.cattle = cattle;
    }

    @Override
    public int getCount() {
        return cattle.size();
    }

    @Override
    public Object getItem(int position) {
        return cattle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.list_row_cattle, null);

            Fonty.setFonts(parent);
        }

        Cattle c = (Cattle) getItem(position);

        ((TextView) view.findViewById(R.id.txt_tag)).setText(c.getTag());
        ((TextView) view.findViewById(R.id.txt_breed)).setText(String.format("Breed: %s", c.getBreed()));
        ((TextView) view.findViewById(R.id.txt_date_created)).setText(String.format("Added On: %s", c.getCreatedOn()));

        return view;
    }
}
