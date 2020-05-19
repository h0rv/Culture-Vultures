package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    public static final int MAIN_REQUEST_CODE = 1;

    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        openFragment(FeedFragment.newInstance());
                        return true;
                    case R.id.action_profile:
                        openFragment(ProfileFragment.newInstance());
                        return true;
                    case R.id.action_search:
                        openFragment(SearchFragment.newInstance());
                        return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mPostsDatabaseReference = mDatabase.getReference("posts");
        mUsersDatabaseReference = mDatabase.getReference("users");
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}