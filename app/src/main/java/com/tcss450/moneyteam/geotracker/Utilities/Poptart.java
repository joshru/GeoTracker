package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.R;

/**
 * Created by Josh on 4/23/2015.
 */
public final class Poptart {

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
     *
     * @param theContext
     * @param theMessage
     * @param theSeconds
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
