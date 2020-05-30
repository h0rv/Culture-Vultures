package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        mProgressBar.setVisibility(View.VISIBLE);
        mNoInternetTextView = rootView.findViewById(R.id.feed_emptyView);
        mRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mPostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            new FirebaseTask().execute();
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

    private List<Post> getPosts() {
        posts = new ArrayList<>();
        mPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    posts.add(0, child.getValue(Post.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return posts;
    }

    private class FirebaseTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... aVoid) {
            try {
                getPosts();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (posts != null && posts.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.setPosts(posts);
                mAdapter.notifyDataSetChanged();
            }
            else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mNoInternetTextView.setText("No Posts Found");
                mNoInternetTextView.setVisibility(View.VISIBLE);
            }
        }
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