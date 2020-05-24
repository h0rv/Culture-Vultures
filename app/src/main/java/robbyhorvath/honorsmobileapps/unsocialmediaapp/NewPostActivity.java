package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewPostActivity extends AppCompatActivity {
    public static final int NEW_POST_REQUEST_CODE = 5;

    private ImageView postImageView;
    private EditText descriptionEditText;

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int STORAGE_REQUEST = 222;

    private Uri filePath;
    private String postKey;
    private boolean successfulUpload = false;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postImageView = findViewById(R.id.new_post_image_view);
        descriptionEditText = findViewById(R.id.new_post_description);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void confirmPost(View view) {
        if (filePath != null && !descriptionEditText.getText().toString().matches("")) {
            createNewPost();
        } else {
            Toast.makeText(this, "Select an image and make a description for your post.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewPost() {

        postKey = mDatabase.getReference("posts").push().getKey();
        Post post = new Post(descriptionEditText.getText().toString(), mAuth.getCurrentUser().getUid());
        uploadPost();
        if(successfulUpload){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Posting");
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("Posts")
                    .child(postKey)
                    .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>()  {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(NewPostActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        successfulUpload = false;
                    }
                }
            });
        }
    }

    private void uploadPost() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on

            mStorageRef.child("posts/" + postKey + ".jpg")
                    .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            successfulUpload = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            successfulUpload = false;
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            successfulUpload = false;
        }
    }


    public void cancelPost(View view) {
        finish();
    }

    public void pickImage(View view) {
        checkWriteReadStorageRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(STORAGE_REQUEST)
    private void checkWriteReadStorageRequest() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), NEW_POST_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "Please grant permission", STORAGE_REQUEST, perms);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
