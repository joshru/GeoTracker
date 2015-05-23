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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Authenticator;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.fragments.ForgotPasswordDialog;

import de.greenrobot.event.EventBus;

/**
 * This Activity handles all user log in procedures and proper redirects.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class LoginActivity extends FragmentActivity {

    /** Password text label. */
    private EditText mPassText;
    /** Email text label. */
    private EditText mEmailText;
    /** Login button. */
    private Button mLoginButton;
    /** Register label link. */
    private TextView mRegisterLabel;
    /** Forgot password label link. */
    private TextView mForgotLabel;
    /** Number of login attempts. */
    private int mLoginTries;
    /** Relative layout of the login button.*/
    private RelativeLayout mLoginButtonLayout;



    /**
     * onCreate is called when the LoginActivity is instantiated.
     * @param savedInstanceState the bundle containing intent info.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SUPER CALL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        super.onCreate(savedInstanceState);

        //THIS CALLS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);

        //REFERENCES TO VIEWS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mRegisterLabel = (TextView) findViewById(R.id.register_label);
        mForgotLabel = (TextView) findViewById(R.id.login_forgot_password_label);
        mPassText = (EditText) findViewById(R.id.passphrase_text);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mLoginButton = (Button)  findViewById(R.id.login_button);
        mLoginButtonLayout = (RelativeLayout) findViewById(R.id.login_button_layout);

        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mLoginTries = 1;

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

    }

    /**
     * Acting OnClickListener for the login button.
     * @param view the view that represents the called button.
     */
    public void loginUser(View view) {
        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final String emailCred = mEmailText.getText().toString();
        final boolean emailForm = Authenticator.emailFormatCheck(emailCred);
        final boolean passForm = Authenticator.passFormatCheck(mPassText.getText().toString());

        SharedPreferences myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString(getString(R.string.saved_email_key), emailCred).apply();

        WebServiceHelper webServiceHelper = new WebServiceHelper(this);
        if (emailForm && passForm) {
            webServiceHelper.loginUser(emailCred, mPassText.getText().toString());
        } else if(!emailForm) {
            Poptart.displayCustomDuration(this, "Invalid e-mail format. ", 2);
        } else { /* Bad Password format. */
            Poptart.displayCustomDuration(this, "Invalid passphrase format. ", 2);
        }

        //START ANIMATION AND PROVIDE USER HINT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mLoginButtonLayout.startAnimation(animAlpha);
    }

    /**
     * Used for disconnecting the Event Bus when the activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    /**
     * Registers the Event Bus when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        super.onStart();
    }

    /**
     * Receives events from Event Bus for logging in and welcomes the user
     * @param event
     */
    public void onEvent(WebServiceHelper.WebServiceEvent event) {
        if (event.success) {
            //success, user logged in
            if (event.callingMethod == WebServiceHelper.LOGIN_CALL) {
                Poptart.displayCustomDuration(this, "Welcome, " + mEmailText.getText().toString(), 4);

                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                mainScreen.putExtra(getString(R.string.saved_email_key), mEmailText.getText().toString());
                startActivity(mainScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                this.finish();
                //Failed, invalid credentials
            } else if (event.callingMethod == WebServiceHelper.PASSWORD_CALL) {
                Poptart.display(this, event.message, Toast.LENGTH_SHORT);
            }

        } else {
            Poptart.displayCustomDuration(getApplicationContext(), event.message, 6);
        }


    }

    /**
     * Launches the login activity and displays it to the user.
     * @param activity
     */
    public void launchActivity(final String activity) {
        switch (activity) {
            case "register":
                //LAUNCH REGISTRATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Intent registerScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                registerScreen.putExtra(getString(R.string.saved_email_key), mEmailText.getText().toString());
                startActivity(registerScreen);
                break;
            default:
                //LAUNCH MAIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                mainScreen.putExtra(getString(R.string.saved_email_key), mEmailText.getText().toString());
                startActivity(mainScreen);
                break;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}




