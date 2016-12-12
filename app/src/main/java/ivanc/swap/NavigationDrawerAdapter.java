package ivanc.swap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ivanc on 12/3/2016.
 */

public class NavigationDrawerAdapter extends ArrayAdapter {
    private Context context;
    private List<NavigationDrawerItem> itemList;
    private int layoutResourceID;

    public NavigationDrawerAdapter(Context context, int layoutResourceID, List<NavigationDrawerItem> itemList) {
        super(context, layoutResourceID);
        this.context = context;
        this.itemList = itemList;
        this.layoutResourceID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerItemHolder drawerItemHolder;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(layoutResourceID, parent, false);
            drawerItemHolder = new DrawerItemHolder();
            drawerItemHolder.name = (TextView) view.findViewById(R.id.drawer_tab_name);

            view.setTag(drawerItemHolder);
        } else {
            drawerItemHolder = (DrawerItemHolder) view.getTag();
        }

        NavigationDrawerItem drawerItem = this.itemList.get(position);
        drawerItemHolder.name.setText(drawerItem.getName());

        return view;
    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public NavigationDrawerItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class DrawerItemHolder {
        TextView name;
    }
}
