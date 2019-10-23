package org.ilri.eweigh.hg_lw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.hg_lw.models.Submission;
import org.ilri.eweigh.utils.Utils;

import java.util.List;

public class SubmissionsAdapter extends BaseAdapter {

    private Context context;
    private List<Submission> submissions;
    private LayoutInflater inflater;

    public SubmissionsAdapter(Context context, List<Submission> submissions){
        this.context = context;
        this.submissions = submissions;
    }

    @Override
    public int getCount() {
        return submissions.size();
    }

    @Override
    public Object getItem(int position) {
        return submissions.get(position);
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
            view = inflater.inflate(R.layout.list_row_submissions, null);

            Fonty.setFonts(parent);
        }

        Submission s = (Submission) getItem(position);

        String liveWeight = Utils.formatDecimal(s.getLw());
        String metadata = "Heart Girth: " + (int) s.getHg() + "\n" +
                // "Coordinates: " + s.getLat() + ", " + s.getLng() + "\n" +
                "Submitted on: " + s.getCreatedOn();

        ((TextView) view.findViewById(R.id.txt_lw)).setText(liveWeight);
        ((TextView) view.findViewById(R.id.txt_metadata)).setText(metadata);

        return view;
    }
}
