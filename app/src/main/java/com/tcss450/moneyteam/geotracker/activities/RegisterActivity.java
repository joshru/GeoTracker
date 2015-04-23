package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.tcss450.moneyteam.geotracker.Authenticator;

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
        TextView htmlTOS = (TextView) findViewById(R.id.tos_text_view);
        htmlTOS.setText(Html.fromHtml(getString(R.string.tos_agreement)));
        mEnterListener = new PipTextEnterListener();
        mProgressBarIcon = (ImageView) findViewById(R.id.register_progress_icon);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        progressArr = new int[5];

        //ADD LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mEmail.setOnEditorActionListener(mEnterListener);
        mPassword.setOnEditorActionListener(mEnterListener);
        mRepeatPassword.setOnEditorActionListener(mEnterListener);
        mSecurityAnswer.setOnEditorActionListener(mEnterListener);
        mTermsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox myCheckbox = (CheckBox)v;
                if(myCheckbox.isChecked()) {
                    progressArr[4] = 1;
                    mEnterListener.set();
                }else if(!myCheckbox.isChecked()){
                    progressArr[4] = 0;
                    mEnterListener.set();
                }
            }
        });

        //SET EMAIL PROGRESS IF PASSED~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(Authenticator.emailFormatCheck(userEmail)) {
            mEmail.setText(userEmail);
            progressArr[0] = 1;
            mEnterListener.set();
        }


        //ASSIGN SPINNER VALUES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions_array,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        mSecuritySpinner.setAdapter(adapter);
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

        //DISPLAY TOAST BASED ON CREDENTIAL TESTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(validEmail && validPass && validRepeat && validQuestionResponse && mTermsCheckBox.isChecked()) {
            myPrefEditor.putString(getString(R.string.saved_email_key), email);
            myPrefEditor.putString(getString(R.string.saved_pass_key), passphraseHash);
            myPrefEditor.putString(getString(R.string.saved_question_key), question);
            myPrefEditor.putString(getString(R.string.saved_question_answer_key), answer);
            myPrefEditor.putBoolean(getString(R.string.logged_in_boolean), true);
            myPrefEditor.apply();
            toastString = getString(R.string.register_succesful);
            //LAUNCH MAIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            mainScreen.putExtra(getString(R.string.saved_email_key), email);
            startActivity(mainScreen);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if(!validEmail) {
            toastString = getString(R.string.register_email_invalid_toast);
        } else if(!validPass) {
            toastString = getString(R.string.register_pass_short_toast);
        } else if(!validQuestionResponse) {
            toastString = getString(R.string.register_question_response_invalid_toast);
        } else if(!validRepeat) {
            toastString = getString(R.string.register_pass_invalid_toast);
        } else if(!mTermsCheckBox.isChecked()) {
            toastString = getString(R.string.tos_toast);
        }
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
        mRegisterButton.startAnimation(animAlpha);
    }

    /**
     * Currently unimplemented.
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
                        if (progressArr[1] == 1 &&
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
            mProgressBar.setBackgroundDrawable(getResources().getDrawable(progress));
            mProgressBarIcon.setBackgroundDrawable(getResources().getDrawable(icon));
        }
    }
}
