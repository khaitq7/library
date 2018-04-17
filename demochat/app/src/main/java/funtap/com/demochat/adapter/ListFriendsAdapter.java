package funtap.com.demochat.adapter;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import funtap.com.demochat.R;
import funtap.com.demochat.model.Friends;

/**
 * Created by Vinh on 4/12/2018.
 */

public class ListFriendsAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    private Activity activity;
    private List<Friends> friendsList;
    private boolean isGroup;
    public SparseBooleanArray mCheckStates;
    public ListFriendsAdapter(Activity activity, List<Friends> friendsList, boolean isGroup) {
        this.activity = activity;
        this.friendsList = friendsList;
        this.isGroup = isGroup;
        mCheckStates = new SparseBooleanArray(friendsList.size());
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Friends getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return friendsList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListFriendsHolder holder = null;
        if (convertView != null){
            holder = (ListFriendsHolder)convertView.getTag();
        } else{
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_listfriends, null);
            holder = new ListFriendsHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.img_avatar = convertView.findViewById(R.id.avatar);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        holder.tv_name.setText(friendsList.get(position).getDisplayName());
        if (!isGroup){
            holder.img_avatar.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(activity).load(friendsList.get(position).getImage()).apply(options).into(holder.img_avatar);
        }else {
            holder.img_avatar.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(mCheckStates.get(position, false));
            holder.checkBox.setOnCheckedChangeListener(this);
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }

    static class ListFriendsHolder {
        ImageView img_avatar;
        TextView tv_name;
        CheckBox checkBox;

    }
}
