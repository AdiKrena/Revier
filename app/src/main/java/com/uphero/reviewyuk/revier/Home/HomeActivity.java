package com.uphero.reviewyuk.revier.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.uphero.reviewyuk.revier.Login.LoginActivity;
import com.uphero.reviewyuk.revier.Login.SignupActivity;
import com.uphero.reviewyuk.revier.R;
import com.uphero.reviewyuk.revier.Utils.BottomNavigationViewHelper;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;
    private static final int ACTIVITY_NUM = 2;

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseProfileImage;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
//        mDatabaseProfileImage = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("image");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupNavigationView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        
        FirebaseRecyclerAdapter<Home, HomeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Home, HomeViewHolder>(
                Home.class,
                R.layout.layout_cardview,
                HomeViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(HomeViewHolder viewHolder, Home model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setNamaTempat(model.getNamaTempat());
                viewHolder.setDeskripsi(model.getDeskripsi());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setProfileImage(getApplicationContext(), model.getProfileImage());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
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

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public HomeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUsername(String username){
            TextView postUsername = (TextView) mView.findViewById(R.id.home_username);
            postUsername.setText(username);
        }

        public void setNamaTempat(String namatempat){
            TextView postTitle = (TextView) mView.findViewById(R.id.nama_tempat);
            postTitle.setText(namatempat);
        }

        public void setDeskripsi(String deskripsi){
            TextView postDeskripsi = (TextView) mView.findViewById(R.id.desc_tempat);
            postDeskripsi.setText(deskripsi);
        }

        public void setImage(Context ctx, String image){
            ImageView postImage = mView.findViewById(R.id.image_post);
            Picasso.with(ctx).load(image).into(postImage);
        }

        public void setProfileImage(Context ctx, String profileimage){
            ImageView profileImage = mView.findViewById(R.id.profile_icon);
            Picasso.with(ctx).load(profileimage).into(profileImage);
        }
    }
}
