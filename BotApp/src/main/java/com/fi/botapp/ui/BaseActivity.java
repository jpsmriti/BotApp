package com.fi.botapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fi.botapp.utils.Constants;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.User;
import com.fi.botapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIConfiguration;
import ai.api.AIService;

public abstract class BaseActivity extends AppCompatActivity {

    // API.AI releted
    abstract void onResultReturned(String result);

    protected AIService aiService;
    protected AIConfiguration config;

    // Firebase related
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    protected void initApiService() {
        Logger.D("initApiService");
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag("en");
        config = new AIConfiguration(
                Constants.APP_ID,
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
    }

    protected FirebaseAuth getAuthInstance(){
        if (mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    protected DatabaseReference getDatabaseInstance(){
        if (mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return mDatabase;
    }

    protected void createUser(String email, String password){
        Logger.D("createUser with : " + email + " - " + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Logger.D("createUser:onComplete:" + task.isSuccessful());
                        Utils.hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(BaseActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void logoutUser(){
        Logger.D("logoutUser");
        if (mAuth != null) {
            mAuth.signOut();
        }
    }

    protected void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(BaseActivity.this, ChatActivity.class));
        finish();
    }

    protected String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    protected void writeNewUser(String userId, String name, String email) {
        Logger.D("writing new user : " + name + " - " + email);
        User user = new User(name, email);
        getDatabaseInstance().child("users").child(userId).setValue(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (aiService != null) {
            Logger.D("aiService.pause");
            aiService.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aiService != null) {
            Logger.D("aiService.resume");
            aiService.resume();
        }
    }

}
