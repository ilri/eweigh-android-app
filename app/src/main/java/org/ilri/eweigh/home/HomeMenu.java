package org.ilri.eweigh.home;

public class HomeMenu {

    private String label;
    private int icon;
    private Class activity;

    public HomeMenu(String label, int icon, Class activity){
        this.label = label;
        this.icon = icon;
        this.activity = activity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }
}
