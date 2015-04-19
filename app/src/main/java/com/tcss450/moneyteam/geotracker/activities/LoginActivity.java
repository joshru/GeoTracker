package com.tcss450.moneyteam.geotracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.fragments.ForgotPasswordDialog;
import com.tcss450.moneyteam.geotracker.Authenticator;

/**
 *
 */
public class LoginActivity extends FragmentActivity {

    /** Password text label. */
    private EditText mPassText;
    /** Email text label. */
    private EditText mEmailText;
    /** Login button*/
    private Button mLoginButton;

    private TextView mRegisterLabel;

    private TextView mForgotLabel;

    private int mLoginTries;
    private RelativeLayout mLoginButtonLayout;

    /**
     * onCreate is called when the LoginActivity is instantiated.
     * @param savedInstanceState the bundle containing intent info.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mRegisterLabel = (TextView) findViewById(R.id.register_label);
        mForgotLabel = (TextView) findViewById(R.id.login_forgot_password_label);

        mPassText = (EditText) findViewById(R.id.passphrase_text);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mLoginButton = (Button)  findViewById(R.id.login_button);
        mLoginButtonLayout = (RelativeLayout) findViewById(R.id.login_button_layout);
        mLoginTries = 0;

        //REGISTER LABEL ONCLICK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mRegisterLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity("register");
            }
        });

        //FORGOT PASSWORD LABEL ONCLICK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mForgotLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
                dialog.show(getFragmentManager(), "forgotPW");
            }
        });

        //LOGIN BUTTON ONLONGCLICK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mLoginButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                launchActivity("");
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
        String toastString = getString(R.string.login_no_device_user);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final String emailCred = mEmailText.getText().toString();
        final String passCredHash = Authenticator.generateHash(mPassText.getText().toString());
        final String userEmail;
        final String userPassHash;
        final boolean emailForm = Authenticator.emailFormatCheck(emailCred);
        final boolean passForm = Authenticator.passFormatCheck(mPassText.getText().toString());
        View badView = mLoginButton;

        //Get Shared Preferences
        SharedPreferences myPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userEmail = myPreferences.getString("userEmail", "");
        userPassHash = myPreferences.getString("userPassphraseHash", "");

        if (emailForm && passForm && userEmail != null) {
            if ((emailCred.equals(userEmail)) && (passCredHash.equals(userPassHash))) {
                launchActivity("");
                mLoginTries = 0;
                finish();
            } else {
                toastString = getString(R.string.bad_creds_toast);
                mLoginTries++;
                badView = (mLoginTries % 3 == 0)?(mForgotLabel):(mRegisterLabel);
                if(badView == mForgotLabel) toastString = getString(R.string.login_forgot_pass);
            }
        } else if (!emailForm) {
            badView = mEmailText;
            toastString = getString(R.string.bad_email_toast);
        } else if (!passForm) {
            badView = mPassText;
            toastString = getString(R.string.bad_pass_toast);
        }
        mLoginButtonLayout.startAnimation(animAlpha);
        badView.startAnimation(animAlpha);
        Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
    }

    public void iAmANoob (View view) {

    }

    /**
     *
     * @param activity
     */
    public void launchActivity(final String activity) {
        switch (activity) {
            case "register":
                Intent registerScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                registerScreen.putExtra("email", mEmailText.getText().toString());
                startActivity(registerScreen);
                break;
            default:
                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                mainScreen.putExtra("email", mEmailText.getText().toString());
                startActivity(mainScreen);
                break;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}




