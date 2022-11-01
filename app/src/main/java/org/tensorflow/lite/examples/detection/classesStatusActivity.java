package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;

public class classesStatusActivity extends AppCompatActivity {
    private FirebaseUser user;
    private static final String TAG = "Auth";
    private FirebaseFirestore db;
    private DocumentReference docIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_status);
//        getSupportActionBar().hide();

        user = FirebaseAuth.getInstance().getCurrentUser();
        getAttend();
    }


    public void getAttend() {
        TextView c1 = (TextView) findViewById(R.id.c_1);
        TextView c2 = (TextView) findViewById(R.id.c_2);
        TextView c3 = (TextView) findViewById(R.id.c_3);

        db = FirebaseFirestore.getInstance();

        docIdRef = db.collection("Users").document(user.getUid());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.get("course1").equals(true)) {
                        c1.setTextColor(R.color.teal_700);
                        c1.setText("Class 1 / Done");
                    }
                    if(document.get("course2").equals(true)) {
                        c2.setTextColor(R.color.teal_700);
                        c2.setText("Class 2 / Done");
                    }

                    if(document.get("course3").equals(true)) {
                        c3.setTextColor(R.color.teal_700);
                        c3.setText("Class 3 / Done");
                    }

                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
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

    public void goBack(View view) {
        this.finish();
    }
}