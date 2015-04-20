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


public class RegisterActivity extends Activity implements View.OnTouchListener {

    EditText mEmail;
    EditText mPassword;
    EditText mRepeatPassword;
    Spinner mSecuritySpinner;
    EditText mSecurityAnswer;
    Button mRegisterButton;
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

    private boolean mTermsBool;
    private ImageView mProgressBar;
    private PipTextEnterListener mEnterListener;
    private ImageView mProgressBarIcon;
    private Animation animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.e("d", "Made it into register");
        Intent i = getIntent();
        // Receiving the Data
        String userEmail = i.getStringExtra("email");

        mTermsBool = false;
        progressArr = new int[5];

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

        //SET EMAIL IF PASSED~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(Authenticator.emailFormatCheck(userEmail)) {
            mEmail.setText(userEmail);
            progressArr[0] = 1;
            mEnterListener.set();
        }


        /*Assign Spinner Values*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions_array,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);

        mSecuritySpinner.setAdapter(adapter);
    }

    /**
     * Acting OnClickListener for the Registration button.
     * @param view
     */
    public void registerUser(View view) {
        SharedPreferences myPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor myPrefEditor = myPreferences.edit();
        String toastString = "Error Registering";
        final String email = mEmail.getText().toString();
        final String passphrase = mPassword.getText().toString();
        final String repeatedPass = mRepeatPassword.getText().toString();
        final String passphraseHash = Authenticator.generateHash(passphrase);
        final String question = mSecuritySpinner.getSelectedItem().toString();
        final String answer = mSecurityAnswer.getText().toString();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        //Field testing
        final boolean validEmail = Authenticator.emailFormatCheck(email);
        final boolean validPass = Authenticator.passFormatCheck(passphrase);
        final boolean validRepeat = passphrase.equals(repeatedPass);
        final boolean validQuestionResponse = (answer.length() > 0);

        if(validEmail && validPass && validRepeat && validQuestionResponse && mTermsCheckBox.isChecked()) {
            myPrefEditor.putString(getString(R.string.saved_email_key), email);
            myPrefEditor.putString(getString(R.string.saved_pass_key), passphraseHash);
            myPrefEditor.putString(getString(R.string.saved_question_key), question);
            myPrefEditor.putString(getString(R.string.saved_question_answer_key), answer);
            myPrefEditor.apply();
            toastString = getString(R.string.register_succesful);
            finish();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return true;
    }

    public class PipTextEnterListener implements TextView.OnEditorActionListener {

        public PipTextEnterListener() {
            super();
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_NEXT ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                    actionId == EditorInfo.IME_ACTION_NONE) {
                    switch (v.getId()){
                        case R.id.register_email:
                            if(progressArr[0] == 0 && Authenticator.emailFormatCheck(mEmail.getText().toString())) {
                                progressArr[0] = 1;
                            } else if(!Authenticator.emailFormatCheck(mEmail.getText().toString())) {
                                progressArr[0] = 0;
                            }
                            break;
                        case R.id.register_password:
                            if(progressArr[1] == 0 && Authenticator.passFormatCheck(mPassword.getText().toString())) {
                                progressArr[1] = 1;
                            } else if(!Authenticator.passFormatCheck(mPassword.getText().toString())) {
                                progressArr[1] = 0;
                            }
                            break;
                        case R.id.register_repeat_password:
                            if(progressArr[1] == 1 &&
                                    mRepeatPassword.getText().toString().equals(mPassword.getText().toString())) {
                                progressArr[2] = 1;
                            } else if(progressArr[1] == 0
                            || !mRepeatPassword.getText().toString().equals(mPassword.getText().toString())) {
                                progressArr[2] = 0;
                            }
                            break;
                        case R.id.register_security_answer:
                            if(progressArr[3] == 0 && (mSecurityAnswer.getText().length() > 0)) {
                                progressArr[3] = 1;
                            } else if(!(mSecurityAnswer.getText().length() > 0)){
                                progressArr[3] = 0;
                            }
                            break;
                        default:
                            break;
                    }
                    set();
                    return false; // consume.
            }
            return false; // pass on to other listeners
        }

        public void set() {
            int totalProgress = 0;
            for (int i = 0; i < 5; i++) {
                totalProgress += progressArr[i];
            }
            Log.e("REGISTER", "set Called with progress count: " + totalProgress);
            int progress = R.drawable.pip_progress_0;
            int icon = R.drawable.pip_101;
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
            mProgressBarIcon.startAnimation(animAlpha);
            mProgressBar.startAnimation(animAlpha);
            mProgressBar.setBackgroundDrawable(getResources().getDrawable(progress));
            mProgressBarIcon.setBackgroundDrawable(getResources().getDrawable(icon));
        }
    }
}
