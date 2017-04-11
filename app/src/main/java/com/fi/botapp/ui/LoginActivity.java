package com.fi.botapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fi.botapp.R;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.emailid_edittext)
    protected AutoCompleteTextView mEmailField;
    @Bind(R.id.password)
    protected EditText mPasswordField;
    @Bind(R.id.sign_up)
    protected TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.email_sign_in_button)
    protected void signIn(View v) {
        Logger.D("signIn");
        if (!validateForm()) {
            return;
        }

        Utils.showProgressDialog(LoginActivity.this);
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        getAuthInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Logger.D("signIn:onComplete:" + task.isSuccessful());
                        Utils.hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.sign_up)
    protected void signUp(View v) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    @Override
    void onResultReturned(String result) {
        // Nothing to do
    }
}

