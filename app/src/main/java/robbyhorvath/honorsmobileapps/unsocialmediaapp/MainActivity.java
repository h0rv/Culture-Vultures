package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final int MAIN_REQUEST_CODE = 1;

    BottomNavigationView bottomNavigationView;

    private FeedFragment feedFragment;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private FragmentManager fragmentManager;
    private Fragment currentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);

        feedFragment = new FeedFragment();
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        fragmentManager = getSupportFragmentManager();
        currentFrag = feedFragment;

        fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, feedFragment)
                .add(R.id.fragment_holder, profileFragment)
                .add(R.id.fragment_holder, searchFragment).hide(profileFragment).hide(searchFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        fragmentManager.beginTransaction().hide(currentFrag).show(feedFragment).commit();
                        currentFrag = feedFragment;
                        return true;
                    case R.id.action_profile:
                        fragmentManager.beginTransaction().hide(currentFrag).show(profileFragment).commit();
                        currentFrag = profileFragment;
                        return true;
                    case R.id.action_search:
                        fragmentManager.beginTransaction().hide(currentFrag).show(searchFragment).commit();
                        currentFrag = searchFragment;
                        return true;
                }
                return false;
            }
        });
    }
}