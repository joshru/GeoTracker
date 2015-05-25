package Activities;

import android.widget.EditText;

import com.tcss450.moneyteam.geotracker.BuildConfig;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Brandon on 5/24/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class TestLoginActivity {

    //TODO Things to test in login activity
    /*
    * Invalid email no password
    * valid email no password
    * valid email invalid password
    * valid email valid password
    * forgot password pops up dialog
    * register button opens new activity.
     *
     * Will potentially have to make the methods return a boolean or int to signal
     * different outcomes
     *
     * Can also test the toast text
     * ShadowToast.getLatest...
     */


    private LoginActivity mLoginActivity;

    @Before
    public void setup() {
        mLoginActivity = Robolectric.buildActivity(LoginActivity.class).get();
    }

    @Test
    public void testLogin() {
        EditText emailForm = (EditText)mLoginActivity.findViewById(R.id.email_text);

    }



}
