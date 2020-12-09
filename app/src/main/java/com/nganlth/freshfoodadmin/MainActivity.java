package com.nganlth.freshfoodadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nganlth.freshfoodadmin.Fragment.CategoryFragment;
import com.nganlth.freshfoodadmin.Fragment.HomeFragment;
import com.nganlth.freshfoodadmin.Fragment.FoodFragment;
import com.nganlth.freshfoodadmin.Fragment.ProfileFragment;
import com.nganlth.freshfoodadmin.Fragment.StatisticalFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom = findViewById(R.id.bottomNav);
        bottom.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CategoryFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.category:
                            fragment = new CategoryFragment();
                            break;
                        case R.id.food:
                            fragment = new FoodFragment();
                            break;
                        case R.id.home:

                            fragment = new HomeFragment();
                            break;
                        case R.id.statistical:

                            fragment = new StatisticalFragment();
                            break;
                        case R.id.profile:

                            fragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                    return true;
                }
            };
}