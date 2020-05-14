package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditProfile extends AppCompatActivity {
    public static final int EDIT_PROFILE_REQUEST_CODE = 4;
    private ImageView imageView;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int STORAGE_REQUEST = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        String bio = intent.getStringExtra("BIO_TEXT");
        EditText editText = findViewById(R.id.bio_edit_text);
        imageView = (ImageView) findViewById(R.id.edit_profile_picture);
        if (bio != null)
            editText.setText(bio);
    }

    public void pickImage(View view) {
        checkLocationRequest();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(STORAGE_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), EDIT_PROFILE_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "Please grant permission", STORAGE_REQUEST, perms);
        }
    }
}
