package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewPostActivity extends AppCompatActivity {
    public static final int NEW_POST_REQUEST_CODE = 5;

    private ImageView postImageView;
    private TextView descriptionTextView;

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int STORAGE_REQUEST = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postImageView = findViewById(R.id.new_post_image_view);
        descriptionTextView = findViewById(R.id.new_post_description);
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

    public void confirmPost(View view) {
        if(postImageView.getDrawable() != null && !descriptionTextView.getText().toString().matches("")){
            Drawable drawable = postImageView.getDrawable();
            String description = (String) descriptionTextView.getText();

        }
        else {
            Toast.makeText(this, "Select an image and make a description for your post.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelPost(View view) {
    }
}
