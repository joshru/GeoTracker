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
    public void testActivityHasComponents() {
        TextView registerTextView = (TextView) mLoginActivity.findViewById(R.id.register_label);
        TextView forgotPasswordView = (TextView) mLoginActivity.findViewById(R.id.login_forgot_password_label);


        assertNotNull(mLoginActivity);
        assertNotNull(mLoginButton);
        assertNotNull(mEmailForm);
        assertNotNull(mPasswordForm);

        assertNotNull(registerTextView);
        assertNotNull(forgotPasswordView);
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


        mEmailForm.setText("email@email.com");
        mLoginButton.performClick();
        assertTrue(ShadowToast.showedCustomToast("Invalid passphrase format.", R.id.custom_toast_text));

        //assertTrue(ShadowToast.showedToast("Invalid passphrase format."));
    }

    @Test
    public void testLoginNoCredentials() {
        mLoginButton.performClick();
        assertTrue(ShadowToast.showedCustomToast("Invalid e-mail format.", R.id.custom_toast_text));

    }

    @Test
    public void testLoginValidCredentials() {
        mEmailForm.setText("brandb94@uw.edu");
        mPasswordForm.setText("password1");


        ShadowApplication.runBackgroundTasks();//plswork
        Intent intent = Shadows.shadowOf(mLoginActivity).peekNextStartedActivity();
        //assertTrue("Toast should display", ShadowToast.showedCustomToast("") );
        assertEquals("Should start Main Activity",
                MainActivity.class.getCanonicalName(),
                intent.getComponent().getClassName());

    }
    @Test
    public void testRegisterOpensActivity() {
        TextView registerLabel = (TextView) mLoginActivity.findViewById(R.id.register_label);

        registerLabel.performClick();
        Intent intent = Shadows.shadowOf(mLoginActivity).peekNextStartedActivity();

        assertEquals("Should start Register Activity",
                RegisterActivity.class.getCanonicalName(),
                intent.getComponent().getClassName());
    }
    //TODO create test class for the dialog.
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
