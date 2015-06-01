package Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.BuildConfig;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.MainActivity;
import com.tcss450.moneyteam.geotracker.fragments.AccountFragment;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowSeekBar;
import org.w3c.dom.Text;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.util.FragmentTestUtil.startFragment;

/**
 * Robolectric test class for AccountFragment
 * @author Brandon Bell
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestAccountFragment {

    private AccountFragment mFragment;
    private MainActivity    mActivity;

    /*Fragment Components*/
    private ImageView     topADFrame;
    private TextView      acctDetailsTitle;
    private TextView      emailLabel;
    private TextView      userEmailLabel;
    private ImageView     emailUnderline;
    private TextView      locationTrackingLabel;
    private ToggleButton  trackingToggleButton;
    private TextView      trackingSliderLabel;
    private SeekBar       trackingSlider;
    private TextView      seekTimeLabel;
    private TextView      serverIntervalLabel;
    private Spinner       serverIntervalSpinner;
    private Button        changePasswordButton;

    private ShadowSeekBar shadowSeekBar;


    /**
     * Sets up the fragment and the activity needed for the tests
     */
    @Before
    public void setup() {
        mFragment = new AccountFragment();

        mActivity = Robolectric.buildActivity(MainActivity.class)
                .create().start().resume().visible().get();
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(mFragment, null);
        transaction.commit();
        fm.executePendingTransactions();
        ShadowApplication.runBackgroundTasks();
        getComponents();

    }


    /**
     * Free resources if there are any to free.
     * Hint: There's not.
     */
    @After
    public void tearDown() {

    }

    /**
     * Makes sure the fragment is created properly
     */
    @Test
    public void testFragmentExists() {
        assertNotNull(mActivity);
        assertNotNull(mFragment);
        assertTrue("Fragment should be visible.", mFragment.isAdded());
//        assertTrue("Fragment should be on screen", mFragment.isVisible()); redundant?
    }

    /**
     * Tests that all of the fragment's components aren't null
     */
    @Test
    public void testComponentsExist() {
        assertNotNull(topADFrame           );
        assertNotNull(acctDetailsTitle     );
        assertNotNull(emailLabel           );
        assertNotNull(userEmailLabel       );
        assertNotNull(emailUnderline       );
        assertNotNull(locationTrackingLabel);
        assertNotNull(trackingToggleButton );
        assertNotNull(trackingSliderLabel  );
        assertNotNull(trackingSlider       );
        assertNotNull(seekTimeLabel        );
        assertNotNull(serverIntervalLabel  );
        assertNotNull(serverIntervalSpinner);
        assertNotNull(changePasswordButton );

    }

    /**
     * Tests that all the components have the correct drawable resources.
     */
    @Test
    public void testComponentsHaveCorrectDrawables() {
        //Top Frame
        ShadowDrawable shadow = Shadows.shadowOf(topADFrame.getBackground());
        assertEquals(R.drawable.pip_title_hook, shadow.getCreatedFromResId());

        //email underline
        shadow = Shadows.shadowOf(emailUnderline.getBackground());
        assertEquals(R.drawable.pip_hud_flat_bar, shadow.getCreatedFromResId());

        //toggle button
        shadow = Shadows.shadowOf(trackingToggleButton.getBackground());
        if (trackingToggleButton.isChecked())
            assertEquals(R.drawable.edit_text_gradient, shadow.getCreatedFromResId());
        else assertEquals(R.drawable.edit_text_gradient_inverse, shadow.getCreatedFromResId());
        //seekbar
        shadow = Shadows.shadowOf(trackingSlider.getProgressDrawable());
        assertEquals(R.drawable.progress_draweable, shadow.getCreatedFromResId());

        //slider
        shadow = Shadows.shadowOf(serverIntervalSpinner.getBackground());
        assertEquals(R.drawable.pip_blank_background, shadow.getCreatedFromResId());

        //reset password button
        shadow = Shadows.shadowOf(changePasswordButton.getBackground());
        assertEquals(R.drawable.edit_text_gradient, shadow.getCreatedFromResId());
    }

    /**
     * Tests that changing the position of the seekbar actually changes
     * the tracking frequency.
     */
    @Test
    public void testTrackingChangesFrequency() {

        trackingSlider.setProgress(5);

        assertLogged(Log.DEBUG, AccountFragment.ACCOUNT_TEST_LOG,
                "Tracking frequency changed to: 5", null);
        assertEquals("Tracking slider should be updated",
                5, trackingSlider.getProgress());
    }

    /**
     * Tests that the reset password button opens the dialog fragment.
     */
    @Test
    public void testResetPasswordOpensDialog() {
        changePasswordButton.performClick();
        Fragment frag = mActivity.getFragmentManager().findFragmentByTag("forgotPW");

        assertNotNull(frag);
        assertTrue(frag instanceof DialogFragment);
        assertTrue(frag.isAdded());

    }

    /**
     * Tests that the tracking toggle actually starts tracking.
     * Note the use of logs to check if a certain event happened.
     */
    @Test
    public void testTogglingLocationTracking() {
        trackingToggleButton.performClick();

        assertTrue(trackingToggleButton.isChecked());

        assertLogged(Log.DEBUG, AccountFragment.ACCOUNT_TEST_LOG,
                "Location tracking toggled on.", null);


        //make sure button drawable changes
        ShadowDrawable shadow = Shadows.shadowOf(trackingToggleButton.getBackground());
        assertEquals(R.drawable.edit_text_gradient, shadow.getCreatedFromResId());


        trackingToggleButton.performClick();
        assertLogged(Log.DEBUG, AccountFragment.ACCOUNT_TEST_LOG,
                "Location tracking toggled off.", null);
        shadow = Shadows.shadowOf(trackingToggleButton.getBackground());
        assertEquals(R.drawable.edit_text_gradient_inverse, shadow.getCreatedFromResId());

    }

    /**
     * Tests that the server push interval actually gets changed by
     * selecting alternate spinner values.
     */
    @Test
    public void testIntervalSpinnerChangesInterval() {
        trackingToggleButton.performClick();

        serverIntervalSpinner.setSelection(1);
        assertEquals(serverIntervalSpinner.getSelectedItem(), "Every two hours");


        assertLogged(Log.DEBUG, MainActivity.MAINACTIVITY_TEST_TAG,
                "Push interval updated.", null);
        assertLogged(Log.DEBUG, MainActivity.MAINACTIVITY_TEST_TAG,
                "Spinner position changed.", null);


    }


    /**
     * Ties all of the fragment components to instance fields.
     */
    private void getComponents() {
        topADFrame             = (ImageView)mFragment.getActivity().findViewById(R.id.imageView6);
        acctDetailsTitle       = (TextView)mActivity.findViewById(R.id.textView12);
        emailLabel             = (TextView) mActivity.findViewById(R.id.textView15);
        userEmailLabel         = (TextView) mActivity.findViewById(R.id.f_account_email);
        emailUnderline         = (ImageView) mActivity.findViewById(R.id.f_email_line);
        locationTrackingLabel  = (TextView)mActivity.findViewById(R.id.textView16);
        trackingToggleButton   = (ToggleButton) mActivity.findViewById(R.id.toggleButton);
        trackingSliderLabel    = (TextView)mActivity.findViewById(R.id.textView18);
        trackingSlider         = (SeekBar)mActivity.findViewById(R.id.seekBar);
        seekTimeLabel          = (TextView)mActivity.findViewById(R.id.f_seek_time_label);
        serverIntervalLabel    = (TextView)mActivity.findViewById(R.id.textView9);
        serverIntervalSpinner  = (Spinner)mActivity.findViewById(R.id.server_spinner);
        changePasswordButton   = (Button)mActivity.findViewById(R.id.account_password_reset);
        shadowSeekBar          = Shadows.shadowOf(trackingSlider);

    }

    /**
     * Helper method that will pass if a log has been posted.
     * @param type Log constant (i.e Log.DEBUG, LOG.VERBOSE, etc.
     * @param tag  Tag that is associated with the log
     * @param msg  Log message
     * @param throwable Throwable argument, most of the time just pass in null.
     */
    private void assertLogged(int type, String tag, String msg, Throwable throwable) {
        boolean foundLog = false;
        for (ShadowLog.LogItem lastLog : ShadowLog.getLogsForTag(tag)) {
            if (lastLog.msg.equals(msg)) {
                assertEquals(type, lastLog.type);
                assertEquals(msg, lastLog.msg);
                assertEquals(tag, lastLog.tag);
                assertEquals(throwable, lastLog.throwable);
                foundLog = true;

            }
        }
        assertTrue(foundLog);
    }



}
