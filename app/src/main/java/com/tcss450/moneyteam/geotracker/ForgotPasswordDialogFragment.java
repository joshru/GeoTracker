package com.tcss450.moneyteam.geotracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForgotPasswordDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgotPasswordDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mNewPassText;
    private EditText mNewPassRepeatText;
    private View mFragmentView;
    private OnFragmentInteractionListener mListener;
    private TextView mDialogUserQuestion;
    private EditText mDialogSecurityAnswer;
    private Button mDialogSubmitButton;
    private Activity mActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordDialogFragment newInstance(String param1, String param2) {
        ForgotPasswordDialogFragment fragment = new ForgotPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ForgotPasswordDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mNewPassText = (EditText) mActivity.findViewById((R.id.dialog_security_newpass));
        mNewPassRepeatText = (EditText) mActivity.findViewById(R.id.dialog_security_repeatpass);
        mDialogUserQuestion = (TextView) mActivity.findViewById(R.id.dialog_security_user_question);
        mDialogSecurityAnswer = (EditText) mActivity.findViewById(R.id.dialog_security_answer);
        mDialogSubmitButton = (Button) mActivity.findViewById(R.id.dialog_submit_button);//return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();

        //Obtain security question and answer
        SharedPreferences myPreferences = mActivity
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
       /* mNewPassText = (EditText) mFragmentView.findViewById(R.id.dialog_security_newpass);
        mNewPassRepeatText = (EditText) mFragmentView.findViewById(R.id.dialog_security_repeatpass);
        mDialogUserQuestion = (TextView) mFragmentView.findViewById(R.id.dialog_security_user_question);
        mDialogSecurityAnswer = (EditText) mFragmentView.findViewById(R.id.dialog_security_answer);
        mDialogSubmitButton = (Button) mFragmentView.findViewById(R.id.dialog_submit_button);
*/
        mDialogUserQuestion.setText(question);

        mDialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDialogSecurityAnswer.getText().toString().equals(correctAnswer)) {
                    mNewPassText.setVisibility(View.VISIBLE);
                    mNewPassRepeatText.setVisibility(View.VISIBLE);
                    mDialogSubmitButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(mActivity, getString(R.string.toast_security_correct),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, getString(R.string.toast_security_incorrect),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        mFragmentView = inflater.inflate(R.layout.fragment_forgot_password_dialog, null);
        builder.setView(mFragmentView)
            .setPositiveButton(R.string.reset_confirm_button, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Authenticator.passFormatCheck(mNewPassText.getText().toString())
                        && mNewPassRepeatText
                            .getText().toString().equals(mNewPassText.getText().toString())) {
                        myPrefEditor.putString(getString(R.string.saved_pass_key),
                                Authenticator.generateHash(mNewPassText.getText().toString()));
                        Toast.makeText(mActivity, getString(R.string.toast_security_password_changed),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mActivity, getString(R.string.toast_security_password_failed),
                                Toast.LENGTH_LONG).show();
                    }
                }
            })
        .setNegativeButton(R.string.reset_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ForgotPasswordDialogFragment.this.getDialog().cancel();
                Toast.makeText(mActivity, getString(R.string.reset_cancel_button),
                        Toast.LENGTH_LONG).show();
                //dialog.cancel();
            }
        });


        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_forgot_password_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
