package Activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tcss450.moneyteam.geotracker.BuildConfig;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.LoginActivity;
import com.tcss450.moneyteam.geotracker.activities.MainActivity;
import com.tcss450.moneyteam.geotracker.activities.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Robolectric test class for LoginActivity
 * @author Brandon Bell
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21) //emulate sdk isn't necessary
public class TestLoginActivity {                        //useful for testing a specific version
                                                        //of android
    //TODO Things to test in login activity
    /*
    * Invalid email no password                      *done*
    * valid email no password                        *done*
    * valid email invalid password                   *done*
    * valid email valid password                     *done*
    * forgot password pops up dialog                 *done*
    * register button opens new activity.            *done*
    * Rotating screen doesn't invalidate text fields
    */

    /**
     * Components
     */
    private EditText mEmailForm;
    private EditText mPasswordForm;
    private Button mLoginButton;

    private LoginActivity mLoginActivity;

    /**
     * Setup the activity and commonly used components.
     */
    @Before
    public void setup() {


        mLoginActivity = Robolectric.buildActivity(LoginActivity.class)
                .create().start().resume().visible().get();
        mEmailForm = (EditText)mLoginActivity.findViewById(R.id.email_text);
        mPasswordForm = (EditText) mLoginActivity.findViewById(R.id.passphrase_text);
        mLoginButton = (Button) mLoginActivity.findViewById(R.id.login_button);

    }

    /**
     * Free resources.
     */
    @After
    public void tearDown() {

    }

    /**
     * Sanity check: make sure everything exists.
     */
    @Test
    public void testActivityHasComponents() {
        /*Didn't feel like making instance fields*/
        TextView registerTextView = (TextView) mLoginActivity.findViewById(R.id.register_label);
        TextView forgotPasswordView = (TextView) mLoginActivity.findViewById(R.id.login_forgot_password_label);


        assertNotNull(mLoginActivity);
        assertNotNull(mLoginButton);
        assertNotNull(mEmailForm);
        assertNotNull(mPasswordForm);

        assertNotNull(registerTextView);
        assertNotNull(forgotPasswordView);
    }

    /**
     * Test logging in with an invalid email.
     */
    @Test
    public void testLoginBadEmail() {

        mEmailForm.setText("badEmail");

        mLoginButton.performClick();

        assertTrue(ShadowToast.showedCustomToast("Invalid e-mail format.", R.id.custom_toast_text));

    }

    /**
     * Test logging in with a bad password(empty)
     */
    @Test
    public void testLoginGoodEmailNoPassword() {


        mEmailForm.setText("email@email.com");
        mLoginButton.performClick();
        assertTrue(ShadowToast.showedCustomToast("Invalid passphrase format.", R.id.custom_toast_text));

    }

    /**
     * Test logging in with no credentials.
     */
    @Test
    public void testLoginNoCredentials() {
        mLoginButton.performClick();
        assertTrue(ShadowToast.showedCustomToast("Invalid e-mail format.", R.id.custom_toast_text));

    }

    /**
     * Test logging in with valid credentials
     */
    @Test
    public void testLoginValidCredentials() {
        mEmailForm.setText("anemail@uw.edu"); //You thought I'd let you steal my identity, didn't you?
        mPasswordForm.setText("password1");

        mLoginButton.performClick();
        ShadowApplication.runBackgroundTasks();//Tells Robolectric to wait for background
                                               //threads to complete before continuing
                                               //useful for waiting on web service requests.
        Intent intent = Shadows.shadowOf(mLoginActivity).peekNextStartedActivity();
        assertEquals("Should start Main Activity",
                MainActivity.class.getCanonicalName(),
                intent.getComponent().getClassName());

    }

    /**
     * Test that the register button opens the register menu.
     */
    @Test
    public void testRegisterOpensActivity() {
        TextView registerLabel = (TextView) mLoginActivity.findViewById(R.id.register_label);

        registerLabel.performClick();
        Intent intent = Shadows.shadowOf(mLoginActivity).peekNextStartedActivity();

        assertEquals("Should start Register Activity",
                RegisterActivity.class.getCanonicalName(),
                intent.getComponent().getClassName());
    }

    /**
     * Tests that the forgot password dialog opens when the textview is clicked.
     */
    @Test
    public void testDialogFragmentOpens() {
        TextView forgotPasswordLabel = (TextView) mLoginActivity
                .findViewById(R.id.login_forgot_password_label);
        forgotPasswordLabel.performClick();
        Fragment frag = mLoginActivity.getFragmentManager().findFragmentByTag("forgotPW");

        assertNotNull(frag);
        assertTrue(frag instanceof DialogFragment);
        assertTrue(frag.isAdded());


    }



}
