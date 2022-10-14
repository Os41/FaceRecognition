package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

    }

    public void forgotPasswordAction(View view) {
    }

    public void singInButtonAction(View view) {
    }

//    public void RestButtonAction(View view) {
//
//        EditText emailTextReset = findViewById(R.id.azaz);
//
//        Toast.makeText(this, "Azzoozo", Toast.LENGTH_SHORT).show();
//    }
}