package com.example.diksha.thirdeye;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_FACEBOOK_LOGIN = 1;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseUser != null){
            databaseReference.child("users")
                    .child(firebaseUser.getUid()).setValue(firebaseUser.getDisplayName());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            displayScreen();

            (findViewById(R.id.login_button))
                    .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                                    ))
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .build(),
                            REQUEST_FACEBOOK_LOGIN
                    );
                }
            });
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FACEBOOK_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                firebaseUser = firebaseAuth.getCurrentUser();
                notifyUser("Signed In!");
                databaseReference.child("users")
                        .child(firebaseUser.getUid()).setValue(firebaseUser.getDisplayName());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    notifyUser("Sign In Cancelled!");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    notifyUser("No Internet Connection!");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    notifyUser("Unknown Error!");
                    return;
                }
            }

            notifyUser("Unknown Sign In Response");
        }
    }



    private void notifyUser(String message){
        Snackbar.make(findViewById(R.id.activity_login), message, Snackbar.LENGTH_SHORT).show();
    }

    private void displayScreen(){
        ImageView imageView, backgroundImage, name;
        setContentView(R.layout.activity_login);

        backgroundImage = (ImageView)findViewById(R.id.background_login);
        imageView = (ImageView)findViewById(R.id.thirdEyeLogo);
        name = (ImageView)findViewById(R.id.thirdEyeName);

        Glide.with(this).load(R.drawable.bg05).into(backgroundImage);
        Glide.with(this).load(R.drawable.telogo).into(imageView);
        Glide.with(this).load(R.drawable.thirdeye).into(name);
    }


    public void FullScreenCall() {
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreenCall();
    }
}

/*
<com.facebook.login.widget.LoginButton
        android:id="@+id/login_button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginLeft="40dp"
        android:layout_marginRight="40dp"
        android:layout_alignParentBottom="true"
        android:layout_marginBottom="57dp"
        android:paddingBottom="15dp"
        android:paddingTop="15dp"
        android:textSize="15sp"
        android:paddingLeft="15dp"
        android:layout_alignParentEnd="true" />
 */
