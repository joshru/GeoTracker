package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.tcss450.moneyteam.geotracker.R;

/**
 * Custom implementation of a Toast that allows for custom views and durations.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public final class Poptart {
    /**
     * Displays the Poptart to the screen.
     * @param theContext to display the message on top of
     * @param theMessage to display on the context
     * @param theDuration Will be a toast constant
     */
    public static void display(final Context theContext, final String theMessage,
                               final int theDuration){

        LayoutInflater myInflater = (LayoutInflater)
                theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myLayout = myInflater.inflate(R.layout.toast_custom, null);

        TextView tText = (TextView) myLayout.findViewById(R.id.custom_toast_text);
        tText.setText(theMessage);

        Toast custom = new Toast(theContext);
        custom.setDuration(theDuration);
        custom.setView(myLayout);
        custom.show();

    }

    /**
     * Displays a poptart for a given duration --B
     * @param theContext to display the poptart in
     * @param theMessage the text to be output
     * @param theSeconds time in seconds to display the message.
     */
    public static void displayCustomDuration(final Context theContext, final String theMessage,
                                             final int theSeconds) {
        LayoutInflater myInflater = (LayoutInflater)
                theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myLayout = myInflater.inflate(R.layout.toast_custom, null);

        TextView tText = (TextView) myLayout.findViewById(R.id.custom_toast_text);
        tText.setText(theMessage);

        final Toast custom = new Toast(theContext);
        custom.setDuration(Toast.LENGTH_SHORT);
        custom.setView(myLayout);
       // custom.show();
        int timeInMillis = (theSeconds - 1) * 1000;

        new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                custom.show();
            }

            @Override
            public void onFinish() {
                custom.show();
            }
        }.start();
    }
}
