package funtap.com.demochat.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import funtap.com.demochat.R;
import funtap.com.demochat.model.User;
import funtap.com.demochat.util.Constants;
import funtap.com.demochat.util.Preference;

/**
 * Created by Vinh on 3/14/2018.
 */

public class FragmentLogin extends Fragment {
    private static final String TAG = "LoginFragment";
    private EditText edt_roleid, edt_areaid;
    private TextView tv_login;
    private ProgressDialog progressDialog;
    private DatabaseReference mUsersDBref;
    public FragmentLogin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        if (view != null) initView(view);
        return view;

    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        tv_login = view.findViewById(R.id.login);
        edt_roleid = view.findViewById(R.id.edt_roleid);
        edt_areaid = view.findViewById(R.id.edt_areaid);
        tv_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final String roleid = edt_roleid.getText().toString();
        final String areaid = edt_areaid.getText().toString();
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("areaid: " + areaid).child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "data= " + dataSnapshot.child(roleid).getValue());
                if (!roleid.isEmpty() && dataSnapshot.child(roleid).getValue() != null){
                    Preference.save(getActivity(), Constants.ROLEID,roleid);
                    Preference.save(getActivity(), Constants.AREAID,areaid);
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mUsersDBref.addListenerForSingleValueEvent(eventListener);

    }
}