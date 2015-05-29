package Activities;

import android.os.AsyncTask;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tcss450.moneyteam.geotracker.BuildConfig;
import com.tcss450.moneyteam.geotracker.R;
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
import org.robolectric.shadows.ShadowAsyncTask;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Brandon on 5/28/2015.
 */

//TODO create tests for RegisterActivity
    /*
    * No fields entered   ---------------------
    * Input invalid email ---------------------
    * valid email invalid pass -----------------
    * valid email valid pass
    * valid email valid pass invalid repeat
    * valid email-repeat invalid question response
    * Everything valid but checkbox
    * progress bar updates
     */


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class testRegisterActivity {

    RegisterActivity mRegisterActivity;
    TextView mEmailLabel;
    EditText mEmailForm;

    TextView mPassLabel;
    EditText mPassForm;

    TextView mRepeatPassLabel;
    EditText mRepeatPassForm;

    TextView mSecurityQuestionLabel;
    Spinner  mSecurityQuestionSpinner;

    TextView mSecurityAnswerLabel;
    EditText mSecurityAnswerForm;

    TextView mTermsOfService;

    CheckBox mTOSCheckbox;

    Button mRegisterButton;

    @Before
    public void setup() throws Exception {
        ActivityController controller = Robolectric.buildActivity(RegisterActivity.class).create();




        ShadowApplication.runBackgroundTasks();
        mRegisterActivity = (RegisterActivity) controller.start().resume().visible().get();

        mEmailLabel = (TextView) mRegisterActivity.findViewById(R.id.textView3);
        mEmailForm = (EditText)  mRegisterActivity.findViewById(R.id.register_email);

        mPassLabel = (TextView)  mRegisterActivity.findViewById(R.id.textView5);
        mPassForm  = (EditText)  mRegisterActivity.findViewById(R.id.register_password);

        mRepeatPassLabel = (TextView) mRegisterActivity.findViewById(R.id.textView6);
        mRepeatPassForm = (EditText) mRegisterActivity.findViewById(R.id.register_repeat_password);

        mSecurityQuestionLabel = (TextView) mRegisterActivity.findViewById(R.id.textView7);
        mSecurityQuestionSpinner = (Spinner) mRegisterActivity.findViewById(R.id.register_security_spinner);

        mSecurityAnswerLabel = (TextView) mRegisterActivity.findViewById(R.id.textView8);
        mSecurityAnswerForm = (EditText) mRegisterActivity.findViewById(R.id.register_security_answer);

        mTermsOfService = (TextView) mRegisterActivity.findViewById(R.id.tos_text_view);

        mTOSCheckbox = (CheckBox) mRegisterActivity.findViewById(R.id.register_checkbox);

        mRegisterButton = (Button) mRegisterActivity.findViewById(R.id.register_register_button);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testComponentsExist() {
        assertNotNull(mRegisterActivity);

        assertNotNull(mEmailForm);
        assertNotNull(mEmailLabel);
        assertNotNull(mPassLabel);
        assertNotNull(mPassForm);
        assertNotNull(mRepeatPassLabel);
        assertNotNull(mRepeatPassForm);
        assertNotNull(mSecurityQuestionLabel);
        assertNotNull(mSecurityQuestionSpinner);
        assertNotNull(mSecurityAnswerLabel);
        assertNotNull(mSecurityAnswerForm);
        assertNotNull(mTermsOfService);
        assertNotNull(mTOSCheckbox);
        assertNotNull(mRegisterButton);
    }

    @Test
    public void testLoginNoCredentials() {
        mRegisterButton.performClick();

        assertTrue("Should display invalid email toast.",
                ShadowToast.showedCustomToast("Please input a valid email.", R.id.custom_toast_text));

    }
    @Test
    public void testLoginInvalidEmail() {
        mEmailForm.setText("invalidemail.com");
        mRegisterButton.performClick();

        assertTrue("Should display invalid email toast.",
                ShadowToast.showedCustomToast("Please input a valid email.", R.id.custom_toast_text));


    }
    @Test
    public void testValidEmailInvalidPassword() {
        mEmailForm.setText("email@email.com");
        mPassForm.setText("inval");

        mRegisterButton.performClick();

        assertTrue("Should display invalid pass format toast",
                ShadowToast.showedCustomToast("Invalid password format.", R.id.custom_toast_text));
    }
    @Test
    public void testInvalidRepeat() {
        mEmailForm.setText("email@email.com");
        mPassForm.setText("validpass1");

        mRepeatPassForm.setText("notvalidrepeat1");

        mRegisterButton.performClick();

        assertTrue("Should display no match toast",
                ShadowToast.showedCustomToast("Passwords do not match.",
                        R.id.custom_toast_text));
    }

    @Test
    public void testInvalidQuestionResponse() {
        mEmailForm.setText("email@email.com");
        mPassForm.setText("validpass1");

        mRepeatPassForm.setText("validpass1");

        mRegisterButton.performClick();

        assertTrue("Should display input answer toast",
                ShadowToast.showedCustomToast("Please input a security answer.",
                        R.id.custom_toast_text));

    }

    @Test
    public void testTOSNotAccepted() {
        mEmailForm.setText("email@email.com");

        mPassForm.setText("validpass1");

        mRepeatPassForm.setText("validpass1");
        mSecurityAnswerForm.setText("answer");

        mRegisterButton.performClick();

        assertTrue("Should display accept terms toast",
                ShadowToast.showedCustomToast("Please accept the terms of service.",
                        R.id.custom_toast_text));
    }

    /**
     * Pain in the patootie
     */
   /* @Test
    public void testTOSHasText() {
        //ShadowApplication.runBackgroundTasks();

        String terms = mTermsOfService.getText().toString();
        assertTrue(terms.length() > 0);
    }*/

    @Test
    public void testProgressbarUpdates() {
        ImageView imageView = (ImageView) mRegisterActivity.findViewById(R.id.register_progress);
        ShadowDrawable baseShadow = Shadows.shadowOf(imageView.getDrawable());
        assertEquals(R.drawable.pip_progress_0, baseShadow.getCreatedFromResId());

        mEmailForm.setText("email@email.com");
        mEmailForm.onEditorAction(EditorInfo.IME_ACTION_NEXT);
        imageView = (ImageView) mRegisterActivity.findViewById(R.id.register_progress);

        baseShadow = Shadows.shadowOf(imageView.getBackground());
        assertEquals(R.drawable.pip_progress_1, baseShadow.getCreatedFromResId());
        mPassForm.setText("validpass1");
        mPassForm.onEditorAction(EditorInfo.IME_ACTION_DONE);
        baseShadow = Shadows.shadowOf(imageView.getBackground());
        assertEquals(R.drawable.pip_progress_2, baseShadow.getCreatedFromResId());

        mRepeatPassForm.setText("validpass1");
        mRepeatPassForm.onEditorAction(EditorInfo.IME_ACTION_DONE);
        baseShadow = Shadows.shadowOf(imageView.getBackground());
        assertEquals(R.drawable.pip_progress_3, baseShadow.getCreatedFromResId());



        mSecurityAnswerForm.setText("answer");
        mSecurityAnswerForm.onEditorAction(EditorInfo.IME_ACTION_DONE);
        baseShadow = Shadows.shadowOf(imageView.getBackground());
        assertEquals(R.drawable.pip_progress_4, baseShadow.getCreatedFromResId());

        mTOSCheckbox.performClick();
        mTOSCheckbox.onEditorAction(EditorInfo.IME_ACTION_DONE);
        baseShadow = Shadows.shadowOf(imageView.getBackground());
        assertEquals(R.drawable.pip_progress_5, baseShadow.getCreatedFromResId());


    }


}
