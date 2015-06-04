package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Authenticator;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;

import de.greenrobot.event.EventBus;

/**
 * The activity for registering a new user.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class RegisterActivity extends Activity implements View.OnTouchListener {

    /** The email text field */
    EditText mEmail;

    /** The password text field*/
    EditText mPassword;

    /** The repeat password text field*/
    EditText mRepeatPassword;

    /** The spinner of security questions*/
    Spinner mSecuritySpinner;

    /** The text field for user's security answer*/
    EditText mSecurityAnswer;

    /** The button to register new user*/
    Button mRegisterButton;

    /** The checkbox for accepting terms of service*/
    CheckBox mTermsCheckBox;
    /**
     * States of register views (1 - input, 0 - empty)
     * 0 - email
     * 1 - password
     * 2 - password repeat
     * 3 - question answer
     * 4 - checkbox
     */
    int[] progressArr;

    /** The registration progress bar*/
    private ImageView mProgressBar;

    /** The listener for detecting entered text*/
    private PipTextEnterListener mEnterListener;

    /** The progree bar icon*/
    private ImageView mProgressBarIcon;

    /** The animation alpha effect*/
    private Animation animAlpha;
    private SharedPreferences myPreferences;

    /**
     * Creates the registration activity and sets all appropriate listeners.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SUPER CALL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        super.onCreate(savedInstanceState);

        //THIS CALLS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        setLoginStatus("register");

        //READ IN FROM CALLING ACTIVITY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Intent i = getIntent();
        String userEmail = i.getStringExtra("email");

        //REFERENCES TO VIEWS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mEmail = (EditText) findViewById(R.id.register_email);
        mPassword = (EditText) findViewById(R.id.register_password);
        mRepeatPassword = (EditText) findViewById(R.id.register_repeat_password);
        mSecuritySpinner = (Spinner) findViewById(R.id.register_security_spinner);
        mSecurityAnswer = (EditText) findViewById(R.id.register_security_answer);
        mRegisterButton = (Button) findViewById(R.id.register_register_button);
        mTermsCheckBox = (CheckBox) findViewById(R.id.register_checkbox);
        mProgressBar = (ImageView) findViewById(R.id.register_progress);
        mEnterListener = new PipTextEnterListener();
        mProgressBarIcon = (ImageView) findViewById(R.id.register_progress_icon);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        progressArr = new int[5];

        //Getting the agreement~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        WebServiceHelper helper = new WebServiceHelper(this);

        helper.getAgreement();
        Log.d("Getting Agreement", "called getAgreement from RegisterAct");

        //ADD LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mEmail.setOnEditorActionListener(mEnterListener);
        mPassword.setOnEditorActionListener(mEnterListener);
        mRepeatPassword.setOnEditorActionListener(mEnterListener);
        mSecurityAnswer.setOnEditorActionListener(mEnterListener);
        mTermsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox myCheckbox = (CheckBox) v;
                if (myCheckbox.isChecked()) {
                    progressArr[4] = 1;
                    mEnterListener.set();
                } else {
                    progressArr[4] = 0;
                    mEnterListener.set();
                }
            }
        });

        setFocusListeners(); //experimenting with setting focus changed listeners.

        //SET EMAIL PROGRESS IF PASSED~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(userEmail != null && Authenticator.emailFormatCheck(userEmail)) {
            mEmail.setText(userEmail);
            progressArr[0] = 1;
            mEnterListener.set();
        }

        //ASSIGN SPINNER VALUES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions_array,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        mSecuritySpinner.setAdapter(adapter);
      //

        Log.d("REGISTERYUNOWORK", "RegisterActivity created.");
    }

    /**
     * Assigns focus change listeners to components so the progress bar will
     * update without needing to hit the enter key.
     */
    private void setFocusListeners() {
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)mEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) mPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        mRepeatPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)mRepeatPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        mSecurityAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)mSecurityAnswer.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Called after finalize(). Destroys the activity, usually done by OS to free resources.
     * Disconnects the Event Bus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);

    }

    /**
     * Registers the Event Bus
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EVENTBUS~~~~~~~~~~~~~~~~
        EventBus.getDefault().register(this);
       /* WebServiceHelper helper = new WebServiceHelper(this);

        helper.getAgreement();*/
    }

    /**
     * Handles events from the webservices. Totally works
     */
    public void onEventMainThread(WebServiceHelper.WebServiceEvent event) {
        Poptart.displayCustomDuration(this, event.message, 4);
        if (event.success) {
            Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginScreen);
        }
    }

    /**
     * Gets the agreement from the webservice and displays it
     * @param event the agreement wrapper event
     */
    public void onEventMainThread(WebServiceHelper.AgreementEvent event) {
        Log.d("AGREEMENTEVENTRECEIVED", "agreement arrived.");
        TextView htmlTOS = (TextView) findViewById(R.id.tos_text_view);
        htmlTOS.setText(Html.fromHtml(event.theAgreement));
    }

    /**
     * Acting OnClickListener for the Registration button.
     * @param view the register label view.
     */
    public void registerUser(View view) {
        //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        SharedPreferences myPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor myPrefEditor = myPreferences.edit();
        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String toastString = getString(R.string.register_error_toast);
        final String email = mEmail.getText().toString();
        final String passphrase = mPassword.getText().toString();
        final String repeatedPass = mRepeatPassword.getText().toString();
        final String passphraseHash = Authenticator.generateHash(passphrase);
        final String question = mSecuritySpinner.getSelectedItem().toString();
        final String answer = mSecurityAnswer.getText().toString();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        //CREDENTIAL TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        final boolean validEmail = Authenticator.emailFormatCheck(email);
        final boolean validPass = Authenticator.passFormatCheck(passphrase);
        final boolean validRepeat = passphrase.equals(repeatedPass);
        final boolean validQuestionResponse = (answer.length() > 0);

        WebServiceHelper webServiceHelper = new WebServiceHelper(this);

        if (validEmail && validPass && validRepeat && validQuestionResponse && mTermsCheckBox.isChecked()) {
            myPrefEditor.putString(getString(R.string.saved_email_key), email);

            myPrefEditor.apply();

            webServiceHelper.addUser(email, passphraseHash, question, answer);

        } else if (!validEmail) {

            Poptart.display(this, "Please input a valid email.", Toast.LENGTH_SHORT);
        } else if (!validPass) {
            Poptart.display(this, "Invalid password format.", Toast.LENGTH_SHORT);
        } else if  (!validRepeat) {
            Poptart.display(this, "Passwords do not match.", Toast.LENGTH_SHORT);
        } else if (!validQuestionResponse) {
            Poptart.display(this, "Please input a security answer.", Toast.LENGTH_SHORT);
        } else {
            Poptart.display(this, "Please accept the terms of service.", Toast.LENGTH_SHORT);
        }

        mRegisterButton.startAnimation(animAlpha);

    }

    /**
     * Currently unimplemented.
     * needs to be overriden to satisfy the interface
     * @param v the view
     * @param event the motion event
     * @return true
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //TODO update progress bar when views lose focus.


        return true;
    }

    /**
     * Listener for detecting entered text
     * @author Brandon Bell
     * @author Alexander Cherry
     * @author Joshua Rueschenberg
     */
    public class PipTextEnterListener implements TextView.OnEditorActionListener {

        /**
         * Empty constructor call to super.
         */
        public PipTextEnterListener() {
            super();
        }



        /**
         * Listener for actions in the editor
         * @param v the text view
         * @param actionId the action ID
         * @param event the key event
         * @return boolean
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //CHECK FOR KEY INPUT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_NEXT ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                    actionId == EditorInfo.IME_ACTION_NONE) {
                //CASES BASED ON VIEW~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                switch (v.getId()) {
                    //REGISTER EMAIL UPDATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    case R.id.register_email:
                        if (progressArr[0] == 0 && Authenticator.emailFormatCheck(mEmail.getText().toString())) {
                            progressArr[0] = 1;
                        } else if (!Authenticator.emailFormatCheck(mEmail.getText().toString())) {
                            progressArr[0] = 0;
                        }
                        break;
                    //REGISTER PASSWORD UPDATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    case R.id.register_password:
                        if (progressArr[1] == 0 && Authenticator.passFormatCheck(mPassword.getText().toString())) {
                            progressArr[1] = 1;
                        } else if (!Authenticator.passFormatCheck(mPassword.getText().toString())) {
                            progressArr[1] = 0;
                        }
                        break;
                    //REGISTER REPEAT PASSWORD UPDATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    case R.id.register_repeat_password:
                        if ((progressArr[1] == 1) &&
                                mRepeatPassword.getText().toString().equals(mPassword.getText().toString())) {
                            progressArr[2] = 1;
                        } else if (progressArr[1] == 0
                                || !mRepeatPassword.getText().toString().equals(mPassword.getText().toString())) {
                            progressArr[2] = 0;
                        }
                        break;
                    //REGISTER SECURITY ANSWER UPDATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    case R.id.register_security_answer:
                        if (progressArr[3] == 0 && (mSecurityAnswer.getText().length() > 0)) {
                            progressArr[3] = 1;
                        } else if (!(mSecurityAnswer.getText().length() > 0)) {
                            progressArr[3] = 0;
                        }
                        break;
                    default:
                        break;
                }
                set();
                return false; //DO NOT CONSUME (leave as return false)
            }
            return false;
        }

        /**
         * Method for setting all fancy visual stuff such as colors and icons in the activity.
         */
        public void set() {
            //GET RESOURCE REFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            int progress = R.drawable.pip_progress_0;
            int icon = R.drawable.pip_101;
            //SET COUNTER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            int totalProgress = 0;
            //GET HOW MANY ARE SET~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            for (int i = 0; i < 5; i++) {
                totalProgress += progressArr[i];
            }
            //BASED ON NUMBER OF PROPERLY SET REGISTER FIELDS~~~~~~~~~~~~~~~~~~~~~~~
            if (totalProgress == 1) {
                progress = R.drawable.pip_progress_1;
                icon = R.drawable.pip_fresh_rads_here;
            }else if(totalProgress == 2) {
                progress = R.drawable.pip_progress_2;
                icon = R.drawable.pip_shining;
            }else if(totalProgress == 3) {
                progress = R.drawable.pip_progress_3;
                icon = R.drawable.pip_almighty;
            }else if(totalProgress == 4) {
                progress = R.drawable.pip_progress_4;
                icon = R.drawable.pip_gains;
            }else if(totalProgress == 5) {
                progress = R.drawable.pip_progress_5;
                icon = R.drawable.pip_man;
            }
            //CALL ANIMATION/SET ICON~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mProgressBarIcon.startAnimation(animAlpha);
            mProgressBar.startAnimation(animAlpha);
            //All they did was change the name of the method to make this deprecated
            //no use in changing target api for it
            mProgressBar.setBackgroundDrawable(getResources().getDrawable(progress));
            mProgressBarIcon.setBackgroundDrawable(getResources().getDrawable(icon));
        }
    }



    public void setLoginStatus(String loginStatus) {
        myPreferences.edit()
                .putString(getString(R.string.logged_in_activity), loginStatus)
                .apply();
    }

    /**
     * Saves all user's choices for configuration changes.
     * @param outState bundle to store args
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.register_config_email),
                mEmail.getText().toString());
        outState.putString(getString(R.string.register_config_password),
                mPassword.getText().toString());


        outState.putString(getString(R.string.register_config_repeat),
                mRepeatPassword.getText().toString());
        outState.putInt(getString(R.string.register_config_spinner),
                mSecuritySpinner.getSelectedItemPosition());
        outState.putString(getString(R.string.register_config_answer),
                mSecurityAnswer.getText().toString());
        outState.putBoolean(getString(R.string.register_checkbox_bool
        ), mTermsCheckBox.isChecked());


        super.onSaveInstanceState(outState);
    }

    /**
     * Restores user input on configuration change.
     * @param savedInstanceState bundle to retrieve args
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEmail.setText(savedInstanceState.getString(getString(R.string.register_config_email), ""));
        mPassword.setText(savedInstanceState.getString(getString(R.string.register_config_password), ""));
        mRepeatPassword.setText(savedInstanceState.getString(getString(R.string.register_config_repeat), ""));
        mSecuritySpinner.setSelection(savedInstanceState.getInt(getString(R.string.register_config_spinner), 0));
        mSecurityAnswer.setText(savedInstanceState.getString(getString(R.string.register_config_answer), ""));
        mTermsCheckBox.setChecked(savedInstanceState.getBoolean(getString(R.string.register_checkbox_bool), false));
    }
}
