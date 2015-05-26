package Activities;

import android.widget.Button;
import android.widget.EditText;

import com.tcss450.moneyteam.geotracker.BuildConfig;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Created by Brandon on 5/24/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class TestLoginActivity {

    //TODO Things to test in login activity
    /*
    * Invalid email no password                      *done*
    * valid email no password                        *done*
    * valid email invalid password
    * valid email valid password
    * forgot password pops up dialog
    * register button opens new activity.
    * Rotating screen doesn't invalidate text fields
     *
     * Will potentially have to make the methods return a boolean or int to signal
     * different outcomes
     *
     * Can also test the toast text
     * ShadowToast.getLatest...
     */

    private EditText mEmailForm;
    private EditText mPasswordForm;
    private Button mLoginButton;

    private LoginActivity mLoginActivity;

    @Before
    public void setup() {

        mLoginActivity = Robolectric.buildActivity(LoginActivity.class)
                .create().start().resume().get(); //start and resume may not be necessary
        mEmailForm = (EditText)mLoginActivity.findViewById(R.id.email_text);
        mPasswordForm = (EditText) mLoginActivity.findViewById(R.id.passphrase_text);
        mLoginButton = (Button) mLoginActivity.findViewById(R.id.login_button);

    }
    @After
    public void tearDown() {
        mEmailForm.setText("");
        mPasswordForm.setText("");
    }

    @Test
    public void testLoginBadEmail() {
        assertNotNull(mLoginButton);
        assertNotNull(mEmailForm);
        mEmailForm.setText("badEmail");

        mLoginButton.performClick();

        //assertTrue(ShadowToast.showedToast("Invalid e-mail format."));
        assertTrue(ShadowToast.showedCustomToast("Invalid e-mail format.", R.id.custom_toast_text));

    }
    @Test
    public void testLoginGoodEmailNoPassword() throws InterruptedException {
        assertNotNull(mLoginButton);
        assertNotNull(mEmailForm);

        mEmailForm.setText("email@email.com");
        mLoginButton.performClick();
        assertTrue(ShadowToast.showedCustomToast("Invalid passphrase format.", R.id.custom_toast_text));

        //assertTrue(ShadowToast.showedToast("Invalid passphrase format."));
    }



}
