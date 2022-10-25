package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference docIdRef;
    private static final String TAG = "Auth";
    private String studentID;
    private EditText resetEmailText;
    private Button resetButton;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        checkCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    public void checkCurrentUser() {
        Intent intentFaceRec = new Intent(this, faceRecognitionActivity.class);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            db = FirebaseFirestore.getInstance();
            docIdRef = db.collection("Users").document(user.getUid());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if(document.get("extra") != null){
                                setContentView(R.layout.activity_home);
                            }else{
                                startActivity(intentFaceRec);
                            }
                        } else {
                            Log.d(TAG, "Document does not exist!");
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });

        } else {
            // No user is signed in
            setContentView(R.layout.activity_sign_in);
        }
    }


    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Email sent.",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Failed with: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void sendPasswordReset(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Email sent.",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Failed with: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

        if(user.isEmailVerified()){

            checkCurrentUser();

        }else if(!user.isEmailVerified()){
            sendEmailVerification();
            Toast.makeText(MainActivity.this, "You need to verified your email.",
                    Toast.LENGTH_LONG).show();
            createDB(user.getUid());
        }else{
            Toast.makeText(MainActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void createDB(String uid) {
        db = FirebaseFirestore.getInstance();

        docIdRef = db.collection("Users").document(uid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                    } else {
                        Log.d(TAG, "Document does not exist!");

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("fname", "");
                        userData.put("mname", "");
                        userData.put("lname", "");
                        userData.put("studentID", studentID);
                        userData.put("courses", new HashMap<>());

                        docIdRef.set(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });


    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }



    public void noAccountButtonAction(View view) {
        setContentView(R.layout.activity_sign_in);
    }

    public void hasAccountButtonAction(View view) {
        setContentView(R.layout.activity_main);
    }

    public void singUpButtonAction(View view) {
        EditText email = (EditText) findViewById(R.id.emailText);
        String emailText = email.getText().toString();
        EditText idStudent = (EditText) findViewById(R.id.idStudentText);
        String idStudentText = idStudent.getText().toString();
        EditText password = (EditText) findViewById(R.id.passwordText);
        String passwordText = password.getText().toString();
        EditText confirmPassword = (EditText) findViewById(R.id.ConfirmPasswordText);
        String confirmPasswordText = confirmPassword.getText().toString();
        studentID = idStudentText;

        if(passwordText.equals(confirmPasswordText) && passwordText.length() >= 6 && idStudentText.length() == 7 && emailText.contains("@") && emailText.contains(".")) {
//          Create User With Email And Password
            createAccount(emailText, passwordText);
        }else{
            if(!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(this, "Make sure your Confirm password is correct!", Toast.LENGTH_SHORT).show();
            }else if(passwordText.length() < 6) {
                Toast.makeText(this, "Make sure your Password is greater than 6 character!", Toast.LENGTH_SHORT).show();
            }else if(idStudentText.length() != 7) {
                Toast.makeText(this, "Make sure your Student ID is correct!", Toast.LENGTH_SHORT).show();
            }else if(!emailText.contains("@") || !emailText.contains(".")) {
                Toast.makeText(this, "Make sure your Email is correct!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void singInButtonAction(View view) {
        EditText email = (EditText) findViewById(R.id.emailText);
        String emailText = email.getText().toString();
        EditText password = (EditText) findViewById(R.id.passwordText);
        String passwordText = password.getText().toString();

        if( passwordText.length() >= 6 && emailText.contains("@") && emailText.contains(".")) {
//          SignIn User With Email And Password
            signIn(emailText, passwordText);

        }else{
            if(passwordText.length() < 6) {
                Toast.makeText(this, "Make sure your Password is greater than 6 character!", Toast.LENGTH_SHORT).show();
            }else if(!emailText.contains("@") || !emailText.contains(".")) {
                Toast.makeText(this, "Make sure your Email is correct!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void singOutButtonAction(View view) {
        signOut();
        setContentView(R.layout.activity_sign_in);
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.pop);
        resetButton = bottomSheetDialog.findViewById(R.id.ResetButton);
        resetEmailText = bottomSheetDialog.findViewById(R.id.azaz);

        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordReset(resetEmailText.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

    }

    public void forgotPasswordAction(View view) {
        showBottomSheetDialog();
    }

}