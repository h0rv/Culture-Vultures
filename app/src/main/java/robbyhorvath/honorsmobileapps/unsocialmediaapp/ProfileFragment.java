package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    public static final int PROFILE_REQUEST_CODE = 3;

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView bioTextView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageButton editButton = (ImageButton) rootView.findViewById(R.id.edit_button);
        profileImageView = rootView.findViewById(R.id.profile_picture);
        usernameTextView = rootView.findViewById(R.id.username_text);
        bioTextView = rootView.findViewById(R.id.bio_text);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        populateProfile();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivityForResult(intent, PROFILE_REQUEST_CODE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            populateProfile();
        }
    }

    private void populateProfile() {
        Glide.with(this)
                .load(mDatabaseRef.child("Users")
                        .child(mAuth.getCurrentUser().getUid())
                        .child("profilePicUrl"))
                .into(profileImageView);

        mDatabaseRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.getValue();
                username = '@'+""+username;
                usernameTextView.setText(username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        mDatabaseRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("bio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bio = (String) dataSnapshot.getValue();
                bioTextView.setText(bio);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


}

