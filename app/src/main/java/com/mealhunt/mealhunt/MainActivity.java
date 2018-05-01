package com.mealhunt.mealhunt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*landing-navbar branch
        setContentView(R.layout.activity_landing);
        //startActivity(LpActivity); */

        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logOut();
        if (AccessToken.getCurrentAccessToken() == null) { //user needs to log in.
            loginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                        /*
                        info.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().getUserId()
                                        + "\n" +
                                        "Auth Token: "
                                        + loginResult.getAccessToken().getToken()
                        ); */
                            AccessToken token = loginResult.getAccessToken();
                            handleFacebookAccessToken(token);
                            FirebaseMessaging.getInstance().subscribeToTopic("eating_group");
                            //Intent activityChangeIntent = new Intent(MainActivity.this, huntActivity.class);
                            Intent activityChangeIntent = new Intent(MainActivity.this, LpActivity.class);
                            MainActivity.this.startActivity(activityChangeIntent);
                        }

                        @Override
                        public void onCancel() {
                            Log.i("edittext", "Login attempt cancelled.");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.i("edittext", "Login attempt failed.");
                        }
                    });
        }
        else { // user is logged in to FB
            handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        /* --- let's make them re-log every time.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            changeInfoMsg("You're already signed in! Welcome Back.");
        }
        else {
            changeInfoMsg("You're not signed in. Please sign in.");
        }
        */

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Signed in to both FB and firebase.",
                                    Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.activity_landing);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
