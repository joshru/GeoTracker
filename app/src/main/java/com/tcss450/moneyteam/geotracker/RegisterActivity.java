package com.tcss450.moneyteam.geotracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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

        mTermsBool = false;
        mEmail = (EditText) findViewById(R.id.register_email);
        mPassword = (EditText) findViewById(R.id.register_password);
        mRepeatPassword = (EditText) findViewById(R.id.register_repeat_password);
        mSecuritySpinner = (Spinner) findViewById(R.id.register_security_spinner);
        mSecurityAnswer = (EditText) findViewById(R.id.register_security_answer);
        mRegisterButton = (Button) findViewById(R.id.register_register_button);
        mTermsCheckBox = (CheckBox) findViewById(R.id.register_checkbox);

        mTermsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTermsBool ^= true;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TermsFragment fragment = new TermsFragment();
                ViewGroup fragId = (ViewGroup) fragment.getView().getParent();
                fragmentTransaction.add(fragId, fragment);
                fragmentTransaction.commit();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTermsBool) {
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();
                    String question = mSecuritySpinner.getSelectedItem().toString();
                    String answer = mSecurityAnswer.getText().toString();
                    User newUser = new User(email, password, question, answer);
                }
            }
        });

        /*Assign Spinner Values*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSecuritySpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
