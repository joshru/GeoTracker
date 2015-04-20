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

/**
 * Created by Brandon on 4/16/2015.
 */
public class ForgotPasswordDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText mNewPassText;
    private EditText mNewPassRepeatText;
    private TextView mDialogUserQuestion;
    private EditText mDialogSecurityAnswer;
    private Button mDialogSubmitButton;
    private boolean mResetSuccessful;

    public static ForgotPasswordDialog newInstance() {
        ForgotPasswordDialog fragment = new ForgotPasswordDialog();
        //Bundle args = new Bundle();
        ////args.putString(ARG_PARAM1, param1);
        ////args.putString(ARG_PARAM2, param2);
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mResetSuccessful = false;
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_forgot_password_dialog, null);

        mNewPassText = (EditText) v.findViewById((R.id.dialog_security_newpass));
        mNewPassRepeatText = (EditText) v.findViewById(R.id.dialog_security_repeatpass);
        mDialogUserQuestion = (TextView) v.findViewById(R.id.dialog_security_user_question);
        mDialogSecurityAnswer = (EditText) v.findViewById(R.id.dialog_security_answer);
        mDialogSubmitButton = (Button) v.findViewById(R.id.dialog_submit_button);//return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      //  LayoutInflater inflater = mActivity.getLayoutInflater();

        //Obtain security question and answer
        SharedPreferences myPreferences = getActivity()
                .getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor myPrefEditor = myPreferences.edit();
        final String question = myPreferences
                .getString(getString(R.string.saved_question_key),
                        getString(R.string.default_restore_key));
        final String correctAnswer = myPreferences
                .getString(getString(R.string.saved_question_answer_key),
                        getString(R.string.default_restore_key));



        /* USE the .setVisibility(View.VISIBLE) to make the password text visible once the user
        enters the correct answer.

*/
        mDialogUserQuestion.setText(question);

        mDialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDialogSecurityAnswer.getText().toString().equals(correctAnswer)) {
                    mNewPassText.setVisibility(View.VISIBLE);
                    mNewPassRepeatText.setVisibility(View.VISIBLE);
                    mDialogSubmitButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), getString(R.string.toast_security_correct),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_security_incorrect),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

      //  mFragmentView = inflater.inflate(R.layout.fragment_forgot_password_dialog, null);

        builder.setView(v)
                //.setCancelable(false)
                .setPositiveButton(R.string.reset_confirm_button, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //blank, manually overriden later
                    }
                })
                .setNegativeButton(R.string.reset_cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setCancelable(false);
                        ForgotPasswordDialog.this.getDialog().cancel();
                        Toast.makeText(getActivity(), getString(R.string.reset_cancel_button),
                                Toast.LENGTH_LONG).show();
                        //dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Authenticator.passFormatCheck(mNewPassText.getText().toString())
                        && mNewPassRepeatText
                        .getText().toString().equals(mNewPassText.getText().toString())) {
                    myPrefEditor.putString(getString(R.string.saved_pass_key),
                            Authenticator.generateHash(mNewPassText.getText().toString()));
                    myPrefEditor.apply();


                    mResetSuccessful = true;

                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_security_password_failed),
                            Toast.LENGTH_LONG).show();
                }
                if (mResetSuccessful) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.toast_security_password_changed),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

      return dialog;
    }
}
