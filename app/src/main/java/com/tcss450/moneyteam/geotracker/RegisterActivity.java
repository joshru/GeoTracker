package com.tcss450.moneyteam.geotracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import de.greenrobot.event.EventBus;

import static com.tcss450.moneyteam.geotracker.R.id.tos_fragment;


public class RegisterActivity extends ActionBarActivity {

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
        SharedPreferences myPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor myPrefEditor = myPreferences.edit();
        String toastString;
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
            myPrefEditor.putString("userEmail", email);
            myPrefEditor.putString("userPassphraseHash", passphraseHash);
            myPrefEditor.putString("userQuestion", question);
            myPrefEditor.putString("userQuestionResponse", answer);
            myPrefEditor.apply();
            toastString = getString(R.string.register_succesful);
            preceed();
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

    }

    private void preceed() {
        //Launch MainActivity, starting a new intent.
        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
        //key,values to send to main.
        nextScreen.putExtra("email", mEmail.getText().toString());
        Log.e("d", mEmail.getText().toString() + " succesfully logged in.");
        startActivity(nextScreen);
    }

}
