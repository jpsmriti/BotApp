package com.fi.botapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fi.botapp.R;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {

    @Bind(R.id.username)
    protected EditText mUsername;
    @Bind(R.id.emailid_edittext)
    protected EditText mEmail;
    @Bind(R.id.password)
    protected EditText mPassword;
    @Bind(R.id.password_confirm)
    protected EditText mPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sign_up_button)
    protected void signUp(View v) {
        Logger.D("signUp");
        Utils.hideKeyboard(SignUpActivity.this, v);
        /*if (!validateForm()) {
            return;
        }*/

        Utils.showProgressDialog(SignUpActivity.this);
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        Logger.D(email + " - " + password);
        if (getAuthInstance() == null) {
            Logger.D("auth NOT null");
        } else {

            Logger.D("auth null");
        }
        createUser(email, password);
    }

    @OnClick(R.id.sign_in)
    protected void signIn(View v) {
        Logger.D("go back to sign in");
        Utils.hideKeyboard(SignUpActivity.this, v);
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    void onResultReturned(String result) {

    }
}
