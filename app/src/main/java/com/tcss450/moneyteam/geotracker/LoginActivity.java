package com.tcss450.moneyteam.geotracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import android.app.FragmentTransaction;
//import android.app.FragmentTransaction;

public class LoginActivity extends FragmentActivity {

    private EditText mPassText;
    private EditText mEmailText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassText = (EditText) findViewById(R.id.passphrase_text);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mLoginButton = (Button)  findViewById(R.id.login_button);

        final TextView mRegisterLabel = (TextView) findViewById(R.id.register_label);

        mRegisterLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Register Activity, starting a new intent.
                Intent nextScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                //key,values to send to main.
                //Take this
                nextScreen.putExtra("email", mEmailText.getText().toString());
                Log.e("d", "Changed to RegisterActivity");
                startActivity(nextScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        final TextView mForgotLabel = (TextView) findViewById(R.id.login_forgot_password_label);

        //Forgot password dialog comes up here
        mForgotLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
                dialog.show(getFragmentManager(), "forgotPW");
            }
        });

        mLoginButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                nextScreen.putExtra("email", "ADMIN");
                startActivity(nextScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(LoginActivity.this, "Admin Login", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    /**
     * Acting OnClickListener for the login button.
     * @param view the view
     */
    public void loginUser(View view) {
        String toastString = "";
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final String emailCred = mEmailText.getText().toString();
        final String passCredHash = Authenticator.generateHash(mPassText.getText().toString());
        final String userEmail;
        final String userPassHash;
        final boolean emailForm = Authenticator.emailFormatCheck(emailCred);
        final boolean passForm = Authenticator.passFormatCheck(mPassText.getText().toString());

        //Get Shared Preferences
        SharedPreferences myPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userEmail = myPreferences.getString("userEmail", "");
        userPassHash = myPreferences.getString("userPassphraseHash", "");

        view.startAnimation(animAlpha);
        if (emailForm && passForm && userEmail != null) {
            if ((emailCred.equals(userEmail)) && (passCredHash.equals(userPassHash))) {
                //Launch Main Activity, starting a new intent.
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                //key,values to send to main.
                //Take this
                nextScreen.putExtra("email", mEmailText.getText().toString());
                Log.e("d", "Changed to RegisterActivity");
                startActivity(nextScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                toastString = getString(R.string.login_success);
            } else {
                toastString = getString(R.string.bad_creds_toast);
            }
        } else if (!emailForm) {
            toastString = getString(R.string.bad_email_toast);
        } else if (!passForm) {
            toastString = getString(R.string.bad_pass_toast);
        }
        Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
    }

    public void forgotPassphrase(View view) {

    }


}




