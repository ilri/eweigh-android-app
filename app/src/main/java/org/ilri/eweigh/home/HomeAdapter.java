package org.ilri.eweigh.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;

import java.util.List;

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<HomeMenu> menus;

    private LayoutInflater inflater;

    public HomeAdapter(Context context, List<HomeMenu> menus){
        this.context = context;
        this.menus = menus;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (v == null) {
            v = inflater.inflate(R.layout.grid_item_home_menu, null);
            Fonty.setFonts(parent);
        }

        HomeMenu menu = menus.get(position);

        ImageView icon = v.findViewById(R.id.img_home_icon);
        icon.setImageResource(menu.getIcon());

        TextView label = v.findViewById(R.id.txt_home_label);
        label.setText(menu.getLabel());

        return v;
    }
}
