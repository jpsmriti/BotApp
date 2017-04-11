package com.fi.botapp.ui;

import android.support.v7.app.AppCompatActivity;

import com.fi.botapp.utils.Constants;
import com.fi.botapp.utils.Logger;

import ai.api.AIConfiguration;
import ai.api.AIService;

public abstract class BaseActivity extends AppCompatActivity {

    abstract void onResultReturned(String result);

    protected AIService aiService;
    protected AIConfiguration config;

    protected void initService() {
        Logger.D("initService");
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag("en");
        config = new AIConfiguration(
                Constants.APP_ID,
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (aiService != null) {
            aiService.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aiService != null) {
            aiService.resume();
        }
    }

}
