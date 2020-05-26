package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private Post[] posts;

    private DatabaseReference mPostsRef;
    private FirebaseAuth mAuth;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mPostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        getPosts();



        return rootView;
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    private void getPosts() {
        mPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Post> postsList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    postsList.add(child.getValue(Post.class));
                }
                posts = new Post[postsList.size()];
                postsList.toArray(posts);
                mAdapter = new PostAdapter(posts);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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