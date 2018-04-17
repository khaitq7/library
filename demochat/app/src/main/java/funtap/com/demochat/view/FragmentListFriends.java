package funtap.com.demochat.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import funtap.com.demochat.R;
import funtap.com.demochat.adapter.ListFriendsAdapter;
import funtap.com.demochat.model.Friends;
import funtap.com.demochat.model.User;
import funtap.com.demochat.util.Constants;
import funtap.com.demochat.util.Preference;

import static android.content.ContentValues.TAG;

/**
 * Created by Vinh on 4/12/2018.
 */

public class FragmentListFriends extends Fragment{
    private DatabaseReference mUsersDBref;
    private List<User> userList;
    private List<Friends> friendsList;
    private String roleID, areaID;
    private ListView listFriends;
    private ListFriendsAdapter adapter;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);
        roleID = Preference.getString(getContext(), Constants.ROLEID);
        areaID = Preference.getString(getContext(), Constants.AREAID);
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("areaid: " + areaID).child("Users");
        progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        if (view != null) initView(view);
        return view;
    }

    private void initView(View view) {
        listFriends = view.findViewById(R.id.lv_friends);
        getListUser();
    }

    private void getListUser() {
        progressDialog.show();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList = new ArrayList<>();
                friendsList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Friends friends = postSnapshot.getValue(Friends.class);
                    friendsList.add(friends);
                }
                adapter = new ListFriendsAdapter(getActivity(), friendsList, false);
                listFriends.setAdapter(adapter);
                listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String senderID = roleID;
                        String receiverID = friendsList.get(position).getRoleId();
                        String name = friendsList.get(position).getDisplayName();
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(Constants.TYPE,Constants.SINGLE_CHAT);
                        intent.putExtra("senderID",senderID);
                        intent.putExtra("receiverID",receiverID);
                        intent.putExtra("name",name);
                        startActivity(intent);
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { progressDialog.dismiss();}
        };
        mUsersDBref.child(roleID).child("friends").addValueEventListener(eventListener);
    }
}
