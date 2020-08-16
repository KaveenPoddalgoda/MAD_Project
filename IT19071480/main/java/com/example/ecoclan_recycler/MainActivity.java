package com.example.ecoclan_recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_receive:
                        Toast.makeText(MainActivity.this, "Receive", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_request:
                        Toast.makeText(MainActivity.this, "Request", Toast.LENGTH_SHORT).show();

                        break;
                }
                return true;
            }
        });
    }

}