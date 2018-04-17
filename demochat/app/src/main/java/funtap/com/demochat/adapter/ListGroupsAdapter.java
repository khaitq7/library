package funtap.com.demochat.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import funtap.com.demochat.R;
import funtap.com.demochat.model.Group;

/**
 * Created by Vinh on 4/16/2018.
 */

public class ListGroupsAdapter extends BaseAdapter{
    Activity activity;
    List<Group> groupsList;

    public ListGroupsAdapter(Activity activity,  List<Group> groupsList) {
        this.activity = activity;
        this.groupsList = groupsList;
    }

    @Override
    public int getCount() {
        return groupsList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return groupsList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.item_listfriends, null);
        TextView tv_name;
        tv_name = convertView.findViewById(R.id.tv_name);
        tv_name.setText(groupsList.get(position).getGroupName());
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

