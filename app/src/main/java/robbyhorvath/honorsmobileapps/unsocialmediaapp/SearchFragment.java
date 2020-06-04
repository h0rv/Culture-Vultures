package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public static final int SEARCH_REQUEST_CODE = 8;
    private TextView mErrorTextView;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;

    private List<User> users;

    private DatabaseReference mUsersRef;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        mErrorTextView = rootView.findViewById(R.id.searchErrorText);
        mRecyclerView = rootView.findViewById(R.id.searchRecyclerView);
        mSearchView = rootView.findViewById(R.id.searchView);
        attachSearchViewListener();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        } else {
            mErrorTextView.setText("No Internet Connection");
            mErrorTextView.setVisibility(View.VISIBLE);
        }
        users = new ArrayList<>();
        mAdapter = new UserAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void attachSearchViewListener(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                FirebaseTask(s.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void FirebaseTask(String queryText) {
        Query usersQuery = mUsersRef.orderByChild("username")
                .startAt(queryText);
        usersQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    users.add(dataSnapshot.getValue(User.class));
                    mAdapter.setUsers(users);
                    mAdapter.notifyDataSetChanged();
                    System.out.println("change");
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
}
