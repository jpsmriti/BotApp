package com.fi.botapp.ui;

import android.support.v7.app.AppCompatActivity;

import com.fi.botapp.utils.Constants;
import com.fi.botapp.utils.Logger;
import com.google.firebase.auth.FirebaseAuth;
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
    protected DatabaseReference mDatabase;
    protected FirebaseAuth mAuth;

    protected void initApiService() {
        Logger.D("initApiService");
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag("en");
        config = new AIConfiguration(
                Constants.APP_ID,
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
    }

    protected void initFirebaseService() {
        Logger.D("initFirebaseService");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    protected void logoutUser(){
        Logger.D("logoutUser");
        if (mAuth != null) {
            mAuth.signOut();
        }
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
