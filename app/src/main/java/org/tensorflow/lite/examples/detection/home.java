package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.classitem:
                        Toast.makeText(home.this, "one", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.homeitem:
                        Toast.makeText(home.this, "two", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.personitem:
                        Toast.makeText(home.this, "three", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }



}