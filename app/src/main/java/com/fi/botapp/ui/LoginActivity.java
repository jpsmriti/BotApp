package com.fi.botapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fi.botapp.R;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.User;
import com.fi.botapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFirebaseService();

        mEmailField = (AutoCompleteTextView) findViewById(R.id.emailid_edittext);
        mPasswordField = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignUp = (TextView) findViewById(R.id.sign_up);

        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(LoginActivity.this, mSignUp);
                signUp();
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Logger.D("signIn");
        if (!validateForm()) {
            return;
        }

        Utils.showProgressDialog(LoginActivity.this);
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
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

    private void signUp() {
        Logger.D("signUp");
        if (!validateForm()) {
            return;
        }

        Utils.showProgressDialog(LoginActivity.this);
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Logger.D("createUser:onComplete:" + task.isSuccessful());
                        Utils.hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(LoginActivity.this, ChatActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
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

    private void writeNewUser(String userId, String name, String email) {
        Logger.D("writing new user : " + name + " - " + email);
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    void onResultReturned(String result) {
        // Nothing to do
    }
}

