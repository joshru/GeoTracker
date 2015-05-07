package com.tcss450.moneyteam.geotracker.activities;

import android.content.Intent;
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
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.fragments.ForgotPasswordDialog;
import com.tcss450.moneyteam.geotracker.Utilities.Authenticator;

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
                Poptart.display(LoginActivity.this, "Admin Login", Toast.LENGTH_LONG);
//                Toast.makeText(LoginActivity.this, "Admin Login", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        //JOSH TESTING CUSTOM TOASTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ImageView toastTest = (ImageView) findViewById(R.id.imageView3);

        toastTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Poptart.display(LoginActivity.this, "Testing Poptarts \n double line test", Toast.LENGTH_LONG);
            }
        });

    }

    /**
     * Acting OnClickListener for the login button.
     * @param view the view that represents the called button.
     */
    public void loginUser(View view) {
        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String toastString = getString(R.string.login_no_device_user);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final String emailCred = mEmailText.getText().toString();
        final String passCredHash = Authenticator.generateHash(mPassText.getText().toString());
        final String userEmail;
        final String userPassHash;
        final boolean emailForm = Authenticator.emailFormatCheck(emailCred);
        final boolean passForm = Authenticator.passFormatCheck(mPassText.getText().toString());
        View badView = mLoginButton;

        WebServiceHelper webServiceHelper = new WebServiceHelper(this);
       // webServiceHelper.loginUser();




        webServiceHelper.loginUser(emailCred, passCredHash);
        //TODO reimplement multi-fail functionality
       // }
        //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /*SharedPreferences myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        userEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
        userPassHash = myPreferences.getString(getString(R.string.saved_pass_key), "");

        //CHECKS FOR VALID FORMATTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (emailForm && passForm && userEmail != null) {
            //CHECK FOR PROPER CREDENTIALS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if ((emailCred.equals(userEmail)) && (passCredHash.equals(userPassHash))) {
                //LOGIN THE USER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                toastString = getString(R.string.login_welcome_toast);
                launchActivity("");
                mLoginTries = 0;

               //keep track of which activity to start up on --B
                getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE).edit()
                        .putBoolean(getString(R.string.logged_in_boolean), true).apply();
                finish();
            } else {
                //BAD USER CREDENTIALS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                toastString = getString(R.string.bad_creds_toast);
                mLoginTries++;
                badView = (mLoginTries % 3 == 0)?(mForgotLabel):(mRegisterLabel);
                if(badView == mForgotLabel) toastString = getString(R.string.login_forgot_pass);
            }
        }
        //EMAIL FORMATTING IS INVALID~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (!emailForm) {
            badView = mEmailText;
            toastString = getString(R.string.bad_email_toast);
        }
        //PASSWORD FORMATTING IS BAD~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (!passForm) {
            badView = mPassText;
            toastString = getString(R.string.bad_pass_toast);
        }*/
        //START ANIMATION AND PROVIDE USER HINT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mLoginButtonLayout.startAnimation(animAlpha);
       // badView.startAnimation(animAlpha);
       // Poptart.display(LoginActivity.this, toastString, Toast.LENGTH_LONG);
    }

    /**
     * Currently unimplemented.
     * @param view
     */
    public void iAmANoob (View view) {
        //TODO Add something cool here.
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        //super.onStart();
        EventBus.getDefault().register(this);
        super.onStart();
    }

    public void onEvent(WebServiceHelper.WebServiceEvent event) {
        if (event.success) {
            Poptart.displayCustomDuration(this, "Welcome, " + mEmailText.getText().toString(), 4);

            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            mainScreen.putExtra(getString(R.string.saved_email_key), mEmailText.getText().toString());
            startActivity(mainScreen);
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




