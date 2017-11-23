package com.uphero.reviewyuk.revier.AddInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.uphero.reviewyuk.revier.Home.HomeActivity;
import com.uphero.reviewyuk.revier.Maps.MapActivity;
import com.uphero.reviewyuk.revier.R;
import com.uphero.reviewyuk.revier.Utils.BottomNavigationViewHelper;

public class AddInfoActivity extends AppCompatActivity {
    private Context mContext = AddInfoActivity.this;

    private static final int GALLERY_REQUEST = 1;
    private static final int ACTIVITY_NUM = 1;

    private ImageView mMapBtn;
    private ImageView mImageAdd;
    private EditText mNamaTempat;
    private EditText mDeskripsi;
    private Button mUploadBtn;

    private Uri mImageUri = null;
    private ProgressDialog mProgess;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mMapBtn = (ImageView) findViewById(R.id.addinfo_maps);
        mImageAdd = (ImageView) findViewById(R.id.addinfo_image);
        mNamaTempat = (EditText) findViewById(R.id.addinfo_tempat);
        mDeskripsi = (EditText) findViewById(R.id.addinfo_desc);
        mUploadBtn = (Button) findViewById(R.id.addinfo_upload);

        mProgess = new ProgressDialog(this);

        mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(AddInfoActivity.this, MapActivity.class);
                startActivity(mapIntent);
            }
        });

        mImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUplaod();
            }
        });

        setupNavigationView();
        getImage();
    }

    private void getImage() {
        if(getIntent().getExtras() != null){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16, 9)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mImageAdd.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startUplaod() {
        mProgess.setMessage("Posting...");
        mProgess.show();

        final String namaTempat = mNamaTempat.getText().toString().trim();
        final String deskripsi = mDeskripsi.getText().toString().trim();

        if (!TextUtils.isEmpty(namaTempat) && !TextUtils.isEmpty(deskripsi) && mImageUri != null){
            StorageReference filepath = mStorage.child("Post images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("namatempat").setValue(namaTempat);
                            newPost.child("deskripsi").setValue(deskripsi);
                            newPost.child("image").setValue(downloadUri.toString());
                            newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                            newPost.child("profileimage").setValue(dataSnapshot.child("image").getValue());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mProgess.dismiss();

                                        startActivity(new Intent(AddInfoActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }else{
            Toast.makeText(mContext, "Field is empty", Toast.LENGTH_SHORT).show();
        }
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
