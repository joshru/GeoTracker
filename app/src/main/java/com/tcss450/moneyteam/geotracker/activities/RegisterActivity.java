package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Authenticator;


public class RegisterActivity extends Activity {

    EditText mEmail;
    EditText mPassword;
    EditText mRepeatPassword;
    Spinner mSecuritySpinner;
    EditText mSecurityAnswer;
    Button mRegisterButton;
    CheckBox mTermsCheckBox;

    private boolean mTermsBool;

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
        mEmail = (EditText) findViewById(R.id.register_email);
        mPassword = (EditText) findViewById(R.id.register_password);
        mRepeatPassword = (EditText) findViewById(R.id.register_repeat_password);
        mSecuritySpinner = (Spinner) findViewById(R.id.register_security_spinner);
        mSecurityAnswer = (EditText) findViewById(R.id.register_security_answer);
        mRegisterButton = (Button) findViewById(R.id.register_register_button);
        mTermsCheckBox = (CheckBox) findViewById(R.id.register_checkbox);
        mEmail.setText(userEmail);

        TextView htmlTOS = (TextView) findViewById(R.id.tos_text_view);
        htmlTOS.setText(Html.fromHtml(getString(R.string.tos_agreement)));


        /*Assign Spinner Values*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
    }
}
