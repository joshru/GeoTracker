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
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;

import org.w3c.dom.Text;

import de.greenrobot.event.EventBus;

/**
 * The forgot password fragment for resetting user password with secret question check.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class ForgotPasswordDialog extends DialogFragment {

    private TextView mDialogEmailLabel;

    /**
     * Creates and returns a new forgot password fragment
     * @return the forgot password fragment
     */
    public static ForgotPasswordDialog newInstance() {
        ForgotPasswordDialog fragment = new ForgotPasswordDialog();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    public void onEventMainThread(WebServiceHelper.WebServiceEvent event) {
        if (event.success) {
            Poptart.displayCustomDuration(getActivity().getApplicationContext(), event.message, 4);
        }
    }


    /**
     * Creates a new forgot password fragment and assigns all relevant listeners
     * @param savedInstanceState the saved instance state
     * @return alert dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //FIELD INSTANTIATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
       // mResetSuccessful = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog;

        //INFLATE THIS FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        final View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_forgot_password_dialog, null);

        //SETS THE VIEW OF THE ALERT DIALOG BOX~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        builder.setView(v)
                //SETS ACTION FOR POSITIVE BUTTON~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                .setPositiveButton(R.string.reset_confirm_button, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebServiceHelper webServiceHelper = new WebServiceHelper(getActivity());
                        EditText email = (EditText) v.findViewById(R.id.dialog_email_input);
                        webServiceHelper.resetPassword(email.getText().toString());                    }
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
      return dialog;
    }
}