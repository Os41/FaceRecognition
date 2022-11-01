package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

    public void closeSign(View view) {
        MainActivity.signOut();
    }

    public void ccam(View view) {
        Intent intent = new Intent(this, facePresentActivity.class);
        startActivity(intent);
    }

    public void classStatus(View view) {
        Intent intent = new Intent(this, classesStatusActivity.class);
        startActivity(intent);
    }
}