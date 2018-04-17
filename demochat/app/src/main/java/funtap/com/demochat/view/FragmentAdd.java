package funtap.com.demochat.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import funtap.com.demochat.R;
import funtap.com.demochat.model.Friends;
import funtap.com.demochat.model.User;

/**
 * Created by Vinh on 3/14/2018.
 */

public class FragmentAdd extends Fragment {
    private static final String TAG = "SignupFragment";
    private EditText edt_roleid, edt_areaid, edt_avatar, edt_account;
    private TextView tv_signup;
    private List<Friends> friendsList= new ArrayList<>();
    private Button btn_add_friends_to_list;
    private ProgressDialog progressDialog;
    private DatabaseReference mUsersDBref;

    public FragmentAdd() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        if (view != null) initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        tv_signup = view.findViewById(R.id.add);
        edt_roleid = view.findViewById(R.id.edt_roleid);
        edt_areaid = view.findViewById(R.id.edt_areaid);
        edt_avatar = view.findViewById(R.id.edt_avatar);
        edt_account = view.findViewById(R.id.edt_account);
        btn_add_friends_to_list = view.findViewById(R.id.addListFr);
        tv_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createUserInDb();
            }
        });
        btn_add_friends_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });
    }

    private void showDialogAdd() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Add List Fr");
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_dialog_add_friends, null);
        dialog.setView(convertView);
        final EditText edt_account_dialog = convertView.findViewById(R.id.edt_account_dialog);
        final EditText edt_areaid_dialog = convertView.findViewById(R.id.edt_areaid_dialog);
        final EditText edt_roleid_dialog = convertView.findViewById(R.id.edt_roleid_dialog);
        final EditText edt_avatar_dialog = convertView.findViewById(R.id.edt_avatar_dialog);

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Friends friends = new Friends();
                friends.setAreaId(edt_areaid_dialog.getText().toString());
                friends.setRoleId(edt_roleid_dialog.getText().toString());
                friends.setDisplayName(edt_account_dialog.getText().toString());
                friends.setImage(edt_avatar_dialog.getText().toString());
                friendsList.add(friends);
            }
        });
        dialog.show();
    }

    private void createUserInDb() {
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        String roleid = edt_roleid.getText().toString();
        String areaid = edt_areaid.getText().toString();
        String account = edt_account.getText().toString();
        String avatar = edt_avatar.getText().toString();
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("areaid: " + areaid).child("Users");
        User user = new User(account, avatar, roleid, areaid,friendsList);
        mUsersDBref.child(roleid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        });
    }
}