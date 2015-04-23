package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
}
