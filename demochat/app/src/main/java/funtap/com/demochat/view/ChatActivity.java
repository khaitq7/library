package funtap.com.demochat.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import funtap.com.demochat.BuildConfig;
import funtap.com.demochat.R;
import funtap.com.demochat.adapter.ListMessageAdapter;
import funtap.com.demochat.model.ChatModel;
import funtap.com.demochat.model.FileModel;
import funtap.com.demochat.model.User;
import funtap.com.demochat.model.UserModel;
import funtap.com.demochat.util.Constants;
import funtap.com.demochat.util.Preference;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by Vinh on 4/12/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;

    static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mFirebaseDatabaseReference, mUsersDBref;
    private FirebaseStorage storage;

    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView btSendMessage, btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    private UserModel userModel = new UserModel();
    private List<ChatModel> chatModels;
    private ListMessageAdapter firebaseAdapter;;
    private Toolbar toolbar;

    private String areaID;
    private String typeChat;
    private String senderID;
    private String receiverID;
    private String groupID;
    private String nameChild;

    //File
    private File filePathImageCamera;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        storage = FirebaseStorage.getInstance();
        areaID = Preference.getString(this, Constants.AREAID);
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("areaid: " + areaID);

        initView();
        getUserInfo();
        getIntentAndMessages();
    }

    private void getIntentAndMessages() {
        Intent intent = getIntent();
        typeChat = intent.getStringExtra(Constants.TYPE);
        if(typeChat.equals(Constants.SINGLE_CHAT)){
            senderID = intent.getStringExtra("senderID");
            receiverID = intent.getStringExtra("receiverID");
            String name = intent.getStringExtra("name");
            getSupportActionBar().setTitle(name+"");
            checkNameChild(senderID, receiverID);
        }
        if(typeChat.equals(Constants.CHAT_ALL)){
            getSupportActionBar().setTitle("Chat All");
            mFirebaseDatabaseReference = mUsersDBref.child("ChatAll");
            getMessagensFirebase();
        }
        if(typeChat.equals(Constants.GROUP_CHAT)){
            senderID = intent.getStringExtra("senderID");
            groupID = intent.getStringExtra("groupID");
            String groupName = intent.getStringExtra("groupName");
            getSupportActionBar().setTitle(groupName);
            mFirebaseDatabaseReference = mUsersDBref.child("Groups").child(groupID);
            getMessagensFirebase();
        }
    }

    private void checkNameChild(final String senderID, final String receiverID) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameChild = senderID+receiverID;
                Log.e(TAG, "nameChild: " + senderID+" "+ receiverID);
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                if(ds.getKey().equals(receiverID+senderID)){
                        nameChild = receiverID+senderID;
                    }
                }
                mFirebaseDatabaseReference = mUsersDBref.child("singleChat").child(nameChild);
                getMessagensFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        FirebaseDatabase.getInstance().getReference().child("areaid: " + areaID).child("singleChat").addValueEventListener(eventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.URL_STORAGE_REFERENCE).child("images/"+ UUID.randomUUID().toString());
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    sendFileFirebase(storageRef, selectedImageUri);
                }
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (filePathImageCamera != null && filePathImageCamera.exists()) {
                    StorageReference imageCameraRef = storageRef.child(filePathImageCamera.getName() + "_camera");
                    sendFileFirebase(imageCameraRef, filePathImageCamera);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sendPhoto:
                verifyStoragePermissions();
//
                break;
            case R.id.sendPhotoGallery:
                photoGalleryIntent();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendFileFirebase(StorageReference storageReference, final Uri file) {
        if (storageReference != null) {
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name + "_gallery");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure sendFileFirebase " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel("img", downloadUrl.toString(), name, "");
                    ChatModel chatModel = new ChatModel(userModel, "", Calendar.getInstance().getTime().getTime() + "", fileModel);
                    mFirebaseDatabaseReference.push().setValue(chatModel);
                }
            });
        }

    }

    private void sendFileFirebase(StorageReference storageReference, final File file) {
        if (storageReference != null) {
            Uri photoURI = FileProvider.getUriForFile(ChatActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            UploadTask uploadTask = storageReference.putFile(photoURI);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure sendFileFirebase " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel("img", downloadUrl.toString(), file.getName(), file.length() + "");
                    ChatModel chatModel = new ChatModel(userModel, "", Calendar.getInstance().getTime().getTime() + "", fileModel);
                    mFirebaseDatabaseReference.push().setValue(chatModel);
                }
            });
        }

    }

    private void photoCameraIntent() {
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto + "camera.jpg");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(ChatActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                filePathImageCamera);
        it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(it, IMAGE_CAMERA_REQUEST);
    }

    private void photoGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Get Photo From"), IMAGE_GALLERY_REQUEST);
    }

    private void sendMessageFirebase() {
        if (edMessage.getText().toString().equals("")){
            Toast.makeText(ChatActivity.this, "Chưa điền message", Toast.LENGTH_SHORT).show();
        } else {
            ChatModel model = new ChatModel(userModel, edMessage.getText().toString(), Calendar.getInstance().getTime().getTime() + "", null);
            mFirebaseDatabaseReference.push().setValue(model);
            edMessage.setText(null);
            if (firebaseAdapter != null) {
                firebaseAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getMessagensFirebase() {
        chatModels = new ArrayList<>();
        mFirebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    chatModels.add(chatModel);
                    firebaseAdapter = new ListMessageAdapter(userModel.getName(), chatModels);
                    firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            int friendlyMessageCount = firebaseAdapter.getItemCount();
                            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (lastVisiblePosition == -1 ||
                                    (positionStart >= (friendlyMessageCount - 1) &&
                                            lastVisiblePosition == (positionStart - 1))) {
                                rvListMessage.scrollToPosition(positionStart);
                            }
                        }
                    });
                    rvListMessage.setLayoutManager(mLinearLayoutManager);
                    rvListMessage.setAdapter(firebaseAdapter);
                    Log.e(TAG,"chatmode: "+chatModels.size());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }
                for (int i = 0; i < userList.size();i++){
                    if(userList.get(i).getRoleId().equals(Preference.getString(ChatActivity.this, Constants.ROLEID))){
                        userModel.setId(userList.get(i).getRoleId());
                        userModel.setName(userList.get(i).getDisplayName());
                        userModel.setPhoto_profile(userList.get(i).getImage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mUsersDBref.child("Users").addValueEventListener(eventListener);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = findViewById(R.id.editTextMessage);
        btSendMessage = findViewById(R.id.buttonMessage);
        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageFirebase();
            }
        });
        btEmoji = findViewById(R.id.buttonEmoji);
        emojIcon = new EmojIconActions(this, contentRoot, edMessage, btEmoji);
        emojIcon.ShowEmojIcon();
        rvListMessage = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
    }


    public void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(ChatActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ChatActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoCameraIntent();
                }
                break;
        }
    }
}
