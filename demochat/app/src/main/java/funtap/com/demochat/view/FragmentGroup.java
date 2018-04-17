package funtap.com.demochat.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import funtap.com.demochat.R;
import funtap.com.demochat.adapter.ListFriendsAdapter;
import funtap.com.demochat.adapter.ListGroupsAdapter;
import funtap.com.demochat.model.Friends;
import funtap.com.demochat.model.Group;
import funtap.com.demochat.util.Constants;
import funtap.com.demochat.util.Preference;

import static android.content.ContentValues.TAG;

/**
 * Created by Vinh on 4/12/2018.
 */

public class FragmentGroup extends Fragment implements View.OnClickListener {
    private DatabaseReference mUsersDBref;
    private Button btn_chat_all, btn_add;
    private ListView lv_group;
    private String areaID, roleID;
    private ListGroupsAdapter listAdapter;
    ArrayList<Group> groupList;
    ArrayList<Friends> friendsList;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        areaID = Preference.getString(getContext(), Constants.AREAID);
        roleID = Preference.getString(getContext(), Constants.ROLEID);
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("areaid: " + areaID).child("Users");
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        if (view != null) initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        btn_chat_all = view.findViewById(R.id.btn_chat_all);
        btn_add = view.findViewById(R.id.btn_add);
        lv_group = view.findViewById(R.id.lv_group);
        getListGroup();
        btn_chat_all.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat_all:
                Intent intent_chat_all = new Intent(getActivity(), ChatActivity.class);
                intent_chat_all.putExtra(Constants.TYPE, Constants.CHAT_ALL);
                startActivity(intent_chat_all);
                break;
            case R.id.btn_add:
                showDialogAdd();
                break;
        }
    }

    private void getListGroup() {
        progressDialog.show();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList = new ArrayList<>();
                friendsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Friends friends = postSnapshot.getValue(Friends.class);
                    friendsList.add(friends);
                }
                Log.e(TAG, "friendsList: " + friendsList.size());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        };
        mUsersDBref.child(roleID).child("friends").addValueEventListener(eventListener);
        ValueEventListener eventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList = new ArrayList<>();
                groupList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Group group = postSnapshot.getValue(Group.class);
                    if (group != null) {
                        groupList.add(group);
                    }
                }
                listAdapter = new ListGroupsAdapter(getActivity(), groupList);
                lv_group.setAdapter(listAdapter);
                lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String senderID = roleID;
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(Constants.TYPE, Constants.GROUP_CHAT);
                        intent.putExtra("senderID", senderID);
                        intent.putExtra("groupID", groupList.get(position).getGroupId());
                        intent.putExtra("groupName", groupList.get(position).getGroupName());
                        startActivity(intent);
                    }
                });
                lv_group.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        deleteGroup(position);
                        return true;
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        };
        mUsersDBref.child(roleID).child("listGroup").addValueEventListener(eventListenerGroup);
    }

    private void showDialogAdd() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Add Group");
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_dialog_add_group, null);
        dialog.setView(convertView);
        final EditText edt_groupname = convertView.findViewById(R.id.edt_group_name);
        ListView lv = convertView.findViewById(R.id.list_name_user);
        final ListFriendsAdapter adapter = new ListFriendsAdapter(getActivity(), friendsList, true);
        lv.setAdapter(adapter);
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName;
                Group group = new Group();
                groupName = edt_groupname.getText().toString();
                Log.e(TAG, "friendsList2: " + friendsList.size());
                if (groupName.equals("")) {
                    Toast.makeText(getActivity(), "Group Name null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (groupList != null) {
                    for (int i = 0; i < groupList.size(); i++) {
                        if (groupName.equals(groupList.get(i).getGroupName())) {
                            Toast.makeText(getActivity(), "Group Name already existed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                group.setGroupName(groupName);
                group.setGroupId(groupName + roleID);
                for (int i = 0; i < friendsList.size(); i++) {
                    if (adapter.mCheckStates.get(i)) {
                        DatabaseReference users = mUsersDBref.child(friendsList.get(i).getRoleId()).child("listGroup");
                        users.push().setValue(group);
                    }
                }
                DatabaseReference usersMain = mUsersDBref.child(roleID).child("listGroup");
                usersMain.push().setValue(group);
                listAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private boolean validateGroup(String groupName) {
        boolean isValidate = false;
        if (groupName == null || groupName.equals("")) {
            Toast.makeText(getActivity(), "Group Name null", Toast.LENGTH_SHORT).show();
            isValidate = false;
        } else if (groupList == null) {
            isValidate = true;
        } else {
            for (int i = 0; i < groupList.size(); i++) {
                if (groupName.equals(groupList.get(i).getGroupName())) {
                    Toast.makeText(getActivity(), "Group Name already existed", Toast.LENGTH_SHORT).show();
                    isValidate = false;
                    break;
                } else {
                    isValidate = true;
                }
            }
        }
        Log.e(TAG, "group" + isValidate);
        return isValidate;
    }


    private void deleteGroup(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Delete Group");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupList.remove(position);
                listAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}
