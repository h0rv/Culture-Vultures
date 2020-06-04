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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public static final int FEED_REQUEST_CODE = 2;
    private ProgressBar mProgressBar;
    private TextView mNoInternetTextView;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private String currentUserId;

    private List<Post> posts;

    private DatabaseReference mPostsRef;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        setHasOptionsMenu(true);
        mProgressBar = rootView.findViewById(R.id.feed_loading_spinner);
        mNoInternetTextView = rootView.findViewById(R.id.feed_emptyView);
        mRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mPostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseTask();
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
        mAdapter = new PostAdapter(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    private void FirebaseTask() {
        posts = new ArrayList<>();
        mPostsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mProgressBar.setVisibility(View.INVISIBLE);
                posts.add(0, dataSnapshot.getValue(Post.class));
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.setPosts(posts);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getActivity(), NewPostActivity.class);
        startActivityForResult(intent, FEED_REQUEST_CODE);
        return true;
    }
}