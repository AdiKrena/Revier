package com.uphero.reviewyuk.revier.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.uphero.reviewyuk.revier.R;
import com.uphero.reviewyuk.revier.Utils.BottomNavigationViewHelper;

public class ProfileActivity extends AppCompatActivity {
    private Context mContext = ProfileActivity.this;

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 3;

    private TextView username;
    private ImageView profileImage;
    private Button profileSetting;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabaseName;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseImage;
    private StorageReference mStorageImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseName = mDatabaseUsers.child("name");
        mDatabaseImage = mDatabaseUsers.child("image");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile Images");

        username = (TextView) findViewById(R.id.profile_name);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileSetting = (Button) findViewById(R.id.profile_change);

        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(ProfileActivity.this, ProfileSettingActivity.class);
                startActivity(settingIntent);
            }
        });

        setupNavigationView();
        getUsername();
        getImage();
    }

    private void getImage() {
        mDatabaseImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(!value.equals("default")){
                    Picasso.with(mContext).load(value).into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUsername() {
        mDatabaseName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                username.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupNavigationView() {
        BottomNavigationViewEx bottomNavigationEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        bottomNavigationEx.setTextVisibility(false);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationEx);
        Menu menu = bottomNavigationEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
