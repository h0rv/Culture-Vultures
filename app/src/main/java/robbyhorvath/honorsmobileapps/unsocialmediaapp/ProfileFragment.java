package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    public static final int PROFILE_REQUEST_CODE = 3;

    private CircleImageView profileImageView;
    private TextView usernameTextView;
    private TextView bioTextView;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mNoInternetTextView;
    private List<Post> posts;
    private String currentUserId;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mPostsRef;

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
        setHasOptionsMenu(true);
        profileImageView = rootView.findViewById(R.id.profile_picture);
        usernameTextView = rootView.findViewById(R.id.username_text);
        bioTextView = rootView.findViewById(R.id.bio_text);
        mProgressBar = rootView.findViewById(R.id.profile_loading_spinner);
        mProgressBar.setVisibility(View.VISIBLE);
        mNoInternetTextView = rootView.findViewById(R.id.profile_emptyView);
        mRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseRef = mDatabase.getReference();
            mPostsRef = mDatabaseRef.child("Posts");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseTask();
            populateProfile();
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
        posts = new ArrayList<>();
        mAdapter = new PostAdapter(posts);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            populateProfile();
        }
    }

    private void populateProfile() {
        mDatabaseRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                try {
                    // TODO fix profile picture firebase caching to display new picture when updated
                    // TODO fix new users profile picture
                    GlideApp.with(ProfileFragment.this)
                            .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/").child(currentUserId + ".jpg"))
                            .into(profileImageView);
                    String username = '@' + "" + currentUser.getUsername();
                    usernameTextView.setText(username);
                    bioTextView.setText(currentUser.getBio());
                } catch (Exception e) {
                    GlideApp.with(ProfileFragment.this)
                            .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/xidK56sEwjUiq0HLBbMRSiMRkJ92.jpg"))
                            .into(profileImageView);
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void FirebaseTask() {
        posts = new ArrayList<>();
        Query userPostsQuery = mPostsRef.orderByChild("posterId");
        userPostsQuery.equalTo(currentUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mProgressBar.setVisibility(View.INVISIBLE);
                posts.add(0, dataSnapshot.getValue(Post.class));
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.setPosts(posts);
                mAdapter.notifyDataSetChanged();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivityForResult(intent, PROFILE_REQUEST_CODE);
        return true;
    }

}

