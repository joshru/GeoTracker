package com.tcss450.moneyteam.geotracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.Authenticator;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;

/**
 * The forgot password fragment for resetting user password with secret question check.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class ForgotPasswordDialog extends DialogFragment {

    /** The new password edit text field*/
    private EditText mNewPassText;

    /** The edit text field for repeating new password*/
    private EditText mNewPassRepeatText;

    /** The user's secret question text view*/
    private TextView mDialogUserQuestion;

    /** The secret question answer edit text field*/
    private EditText mDialogSecurityAnswer;

    /** The button for submitting answer and new password*/
    private Button mDialogSubmitButton;

    /** Boolean for checking if the password reset was successful*/
    private boolean mResetSuccessful;

    /**
     * Creates and returns a new forgot password fragment
     * @return the forgot password fragment
     */
    public static ForgotPasswordDialog newInstance() {
        ForgotPasswordDialog fragment = new ForgotPasswordDialog();
        return fragment;
    }

    /**
     * Creates a new forgot password fragment and assigns all relevant listeners
     * @param savedInstanceState the saved instance state
     * @return alert dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mResetSuccessful = false;
        final String question;
        final String correctAnswer;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog;

        //INFLATE THIS FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_forgot_password_dialog, null);

        //REFERENCES TO VIEWS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mNewPassText = (EditText) v.findViewById((R.id.dialog_security_newpass));
        mNewPassRepeatText = (EditText) v.findViewById(R.id.dialog_security_repeatpass);
        mDialogUserQuestion = (TextView) v.findViewById(R.id.dialog_security_user_question);
        mDialogSecurityAnswer = (EditText) v.findViewById(R.id.dialog_security_answer);
        mDialogSubmitButton = (Button) v.findViewById(R.id.dialog_submit_button);

        //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        SharedPreferences myPreferences = getActivity()
                .getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor myPrefEditor = myPreferences.edit();
        //GET RESOURCES FROM SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        question = myPreferences
                .getString(getString(R.string.saved_question_key),
                        getString(R.string.default_restore_key));
        correctAnswer = myPreferences
                .getString(getString(R.string.saved_question_answer_key),
                        getString(R.string.default_restore_key));

        //DIALOG SETTINGS/ONCLICK LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mDialogUserQuestion.setText(question);
        mDialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * TODO
             * @param v
             */
            @Override
            public void onClick(View v) {
                //SECURITY QUESTION ANSWERED CORRECTLY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if (mDialogSecurityAnswer.getText().toString().equals(correctAnswer)) {
                    mNewPassText.setVisibility(View.VISIBLE);
                    mNewPassRepeatText.setVisibility(View.VISIBLE);
                    mDialogSubmitButton.setVisibility(View.INVISIBLE);
                    Poptart.display(getActivity(), getString(R.string.toast_security_correct),
                            Toast.LENGTH_SHORT);
                } else {
                    Poptart.display(getActivity(), getString(R.string.toast_security_incorrect),
                            Toast.LENGTH_LONG);
                }
            }
        });

        //SETS THE VIEW OF THE ALERT DIALOG BOX~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        builder.setView(v)
                //SETS ACTION FOR POSITIVE BUTTON~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                .setPositiveButton(R.string.reset_confirm_button, new DialogInterface.OnClickListener() {
                    /**
                     * TODO
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //Intentionally left blank, manually overriden later
                    }
                })
                //SETS ACTION FOR NEGATIVE BUTTON~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                .setNegativeButton(R.string.reset_cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setCancelable(false);
                        ForgotPasswordDialog.this.getDialog().cancel();
                        Poptart.display(getActivity(), getString(R.string.reset_cancel_button),
                                Toast.LENGTH_LONG);
                        //dialog.cancel();
                    }
                });
        //BUILD THE ALERT DIALOG~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        dialog = builder.create();
        dialog.show();

        //GET THE PROPER BUTTON~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            /**
             * TODO
             * @param v
             */
            @Override
            public void onClick(View v) {
                //CHECKS THAT NEW PASSWORD IS FORMATTED PROPERLY AND matches REPEAT~
                if (Authenticator.passFormatCheck(mNewPassText.getText().toString())
                        && mNewPassRepeatText
                        .getText().toString().equals(mNewPassText.getText().toString())) {
                    myPrefEditor.putString(getString(R.string.saved_pass_key),
                            Authenticator.generateHash(mNewPassText.getText().toString()));
                    myPrefEditor.apply();

                    mResetSuccessful = true;

                } else {
                    Poptart.display(getActivity(), getString(R.string.toast_security_password_failed),
                            Toast.LENGTH_LONG);
                }
                if (mResetSuccessful) {
                    dialog.dismiss();
                    Poptart.display(getActivity(), getString(R.string.toast_security_password_changed),
                            Toast.LENGTH_LONG);
                }
            }
        });

      return dialog;
    }
}
