package com.tcss450.moneyteam.geotracker.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * class used to communicate with the webserver
 * Created by Brandon on 4/29/2015.
 * @author Brandon Bell
 * @author Josh Rueschenberg
 * @author Alexander Cherry
 */
public class WebServiceHelper {

    /** Base url for all the queries*/
    private static final String BASE_URL = "http://450.atwebpages.com/";
    /**Tag used to get the result status from the JSON object*/
    private static final String RESULT_TAG = "result";
    /**Tag used to obtain the error message from the JSON object*/
    private static final String ERROR_TAG = "error";
    /**tag use to get the userid from the ?*/
    private static final String ID_TAG = "userid";
    /**Tag used to get whether or not a query was successful*/
    private static final String SUCCESS_TAG = "success";
    /**Tag used with JSON object to get the message*/
    private static final String MESSAGE_TAG = "message";
    /**Used to retrieve failure message*/
    private static final String FAIL_TAG = "fail";

   // private JSONArray mArray;
    protected JSONObject mJSONObject;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String mCallingMethod;
    private DownloadWebPageTask mDownloadTask;

    /**
     * Constructs a new instance of WebserviceHelper
     * @param context for the asnyctask to affect.
     */
    public WebServiceHelper(Context context) {
        mContext = context;
        mDownloadTask = new DownloadWebPageTask();
        mJSONObject = null;
        mCallingMethod = null;
    }

    //******************************************************
    //Query executors below here
    //******************************************************

    /**
     * Adds a new user to the webservice
     * @param email of the user
     * @param password hashed password of the user
     * @param question security question
     * @param answer security answer
     */
    public void addUser(final String email, final String password,
                               final String question, final String answer) {
        String query = BASE_URL + "adduser.php?" + "email=" + email + "&password=" + password
                + "&question=" + question.replace(" ", "%20").replace("?", "%3F")
                + "&answer=" + answer.replace(" ", "%20");

        Log.d("WEBQUERY", query);

        mCallingMethod = "addUser";
        mDownloadTask.execute(query);

    }

    /**
     * Logs in a user
     * @param email to log in with
     * @param password to log in with
     */
    public void loginUser(final String email, final String password) {

        mCallingMethod = "loginUser";

        String query = BASE_URL + "login.php?" + "email=" + email + "&password=" + password;
        mDownloadTask.execute(query);

    }

    /**
     * Resets the user's password
     * @param email of the account to reset the password of.
     */
    public void resetPassword(final String email) {
        mCallingMethod = "resetPassword";
        String query = BASE_URL + "reset.php?email=" + email;
        mDownloadTask.execute(query);

    }

    /**
     * Queries the server for the TOS
     */
    public void getAgreement() {
        mCallingMethod = "getAgreement";
        String query = BASE_URL + "agreement.php";
        mDownloadTask.execute(query);
    }

    /**
     * Used by the Location database to log points to the webservice.
     * @param cursor containing a reference to all rows of the database.
     */
    public void logPoint(Cursor cursor) {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);
        Log.d("CURSOR RECEIVED", "" + cursor.getDouble(0)
        + "Timestamp: " + cursor.getInt(5) + " UserID: " + cursor.getString(4));
        //Obtain user id from shared preferences
        String uid = prefs.getString(mContext.getString(R.string.saved_user_id_key),
                mContext.getString(R.string.default_restore_key));

        mCallingMethod = "logPoint";
        //build query
        String query = BASE_URL
                + "logAdd.php?lat=" + cursor.getDouble(0)
                + "&lon=" + cursor.getDouble(1)
                + "&speed=" + cursor.getDouble(2)
                + "&heading=" + cursor.getDouble(3)
                + "&source=" + uid //her example uid
                + "&timestamp=" + cursor.getInt(5);
        Log.d("LOGPOINTURL", query);
        mDownloadTask.execute(query);
        Log.d("EXECUTELOG", "Point log executed, doesn't mean it worked");

    }

    /**
     * Gets a range of points between from two specified dates.
     * @param startDate first date to query between
     * @param endDate second date to query between
     */
    public void getRange(Date startDate, Date endDate) {

        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);

        String uid = prefs.getString(mContext.getString(R.string.saved_user_id_key),
                mContext.getString(R.string.default_restore_key));
        mCallingMethod = "getRange";
       /* Log.d("GETTINGRANGE", "Initiated range query");

        Log.d("QTIME", "START DATE: " + startDate.toString());

        Log.d("QTIME", "\nSTART TIME: " + (startDate.getTime() / 1000L));

        Log.d("QTIME", "\nEND DATE: " + endDate.toString());

        Log.d("QTIME", "\nEND TIME: " + (endDate.getTime() / 1000L));*/

        String query = BASE_URL + "view.php?uid=" + uid
                + "&start=" + startDate.getTime() / 1000L
                + "&end=" + endDate.getTime() / 1000L;

        mDownloadTask.execute(query);
    }

    //****************************************************************
    //Post execute methods below here; These contain most of the logic
    //****************************************************************

    /**
     * Method to be called after thread has finished creating the user.
     * In general, I will be splitting each task into a pre and post execute method.
     *
     */
    private void addUserPostExecute() {
        String result;
        boolean success;

        if (mJSONObject != null) {
            try {
                Log.d("RAWJSONSTRING", mJSONObject.toString());

                if (mJSONObject.getString(RESULT_TAG).equals(SUCCESS_TAG)) {
                    Log.d("WEBSERVICE", "User successfully created");
                    success = true;
                    result = mJSONObject.getString(MESSAGE_TAG);

                } else {
                    Log.d("ERRORSTRING", mJSONObject.getString(ERROR_TAG));
                    success = false;
                    result = mJSONObject.getString(ERROR_TAG);
                }

                //Poptart.display(mContext, result, Toast.LENGTH_LONG);

                EventBus.getDefault().postSticky(new WebServiceEvent(result, success));
                Log.d("EVENT", "Event posted");

            } catch (JSONException e) {
                e.printStackTrace();

            }

            //mArray = null;
        }
    }

    /**
     * Method to be called after the login query has been processed and returned from the server.
     */
    private void loginUserPostExecute() {
        String result = "debug string";
        boolean success = false;
        if (mJSONObject != null) {
            try {
                Log.d("RAWJSONSTRING", mJSONObject.toString());

                if (mJSONObject.getString(RESULT_TAG).equals(SUCCESS_TAG)) {

                    mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_key),
                            Context.MODE_PRIVATE)
                            .edit()
                            .putString(mContext.getString(R.string.saved_user_id_key),
                            mJSONObject.getString(ID_TAG))
                            .apply();

                    result = mJSONObject.getString(ID_TAG);
                    success = true;
                    Log.d("LOGINSUCCESS", "login successful");
                }
                else if (mJSONObject.getString(RESULT_TAG).equals(FAIL_TAG)) {
                    result = mJSONObject.getString(ERROR_TAG);
                }
            } catch (JSONException e) {
                Log.e("LOGINFAILED", "Login failed");
                e.printStackTrace();
            }

        }
        //Send off event to listeners.
        EventBus.getDefault().postSticky(new WebServiceEvent(result, success));

        Log.d("LOGINEVENT", "Event posted.");
        //test change

    }
    /**Handles the post execute behavior of resetting the password.*/
    private void resetPasswordPostExecute() {
        String result;
        boolean success = false;


        if (mJSONObject != null) {
            try {
                Log.d("RAWJSONSTRING", mJSONObject.toString());

                String jsonResult = mJSONObject.getString(RESULT_TAG);

                if (jsonResult.equals(SUCCESS_TAG)) {
                    result = mJSONObject.getString(MESSAGE_TAG);
                    success  = true;
                } else {
                    result = mJSONObject.getString(ERROR_TAG);
                }



                //Send off event
                EventBus.getDefault().postSticky(new WebServiceEvent(result, success));

            } catch (JSONException e) {
                Log.e("ResetFailed", "Exception thrown from reset password web service");
                e.printStackTrace();
            }

            Log.d("RESETPOSTED", "Posted reset result.");
        }
    }

    /**
     * Handles the post execute behavior for getting the agreement from the webservice.
     * Sends off the result to register activity via EventBus
     */
    private void getAgreementPostExecute() {

        if (mJSONObject != null) {
            try {
                String jsonResult = mJSONObject.getString("agreement");
                EventBus.getDefault().postSticky(new AgreementEvent(jsonResult));
                Log.d("AGREEMENTSENT", jsonResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles post execute behavior for logging points.
     */
    private void logPointPostExecute() {
        //nothing to display to the user directly. All return data is for debugging.
        if (mJSONObject != null) {
            try {
                String jsonResult = mJSONObject.getString(RESULT_TAG);

                if (jsonResult.equals(SUCCESS_TAG)) {
                    Log.d("PLOGSUCCESS", "Logged point successfully");
                } else {
                    Log.e("PLOGFAIL", mJSONObject.getString("error"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Handles the post execute bhavior for getting a range of locations.
     */
    private void getRangePostExecute() {
        boolean success = false;
        String eventMessage;
        if (mJSONObject != null) {
            try {
                String result = mJSONObject.getString(RESULT_TAG);
                ArrayList<Location> locArr = new ArrayList<>();

                if (result.equals(SUCCESS_TAG)) {

                    JSONArray jsonArray = mJSONObject.getJSONArray("points"); // actually gets locations
                    Log.d("RETRIEVED", "Points retrieved from webservice");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject currObject = jsonArray.getJSONObject(i);
                        //Create temporary location
                        Location loc = new Location("temploc");
                        //add current location to array
                        loc.setLatitude(currObject.getDouble("lat"));
                        loc.setLongitude(currObject.getDouble("lon"));
                        loc.setSpeed(currObject.getLong("speed"));
                        loc.setBearing(currObject.getLong("heading"));
                        loc.setTime(currObject.getLong("time"));

                        locArr.add(loc);

                    }//endfor
                    eventMessage = "Retrieved Locations successfully.";
                    success = true;

                } else {
                    Log.d("NOPOINTS", "No points to retrieve");
                    eventMessage = mJSONObject.getString(ERROR_TAG);
                }
                //Send off locations to the activity
                EventBus.getDefault().postSticky(new LocationEvent(eventMessage, locArr, success));
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * Event class used to send locations to the tracking fragment.
     */
    public class LocationEvent {
        /**Message for this event*/
        public final String mEventMessage;
        /**Collection of Locations queried from the getRange method*/
        public final ArrayList<Location> mLocations;
        /**Whether or not this event was successful*/
        public final boolean mSuccess;

        /**
         * Public constructor
         * @param mEventMessage message to send to subscribers
         * @param theArr of Locations
         * @param theSucc whether or not the query was successful
         */
        public LocationEvent(String mEventMessage, ArrayList<Location> theArr, boolean theSucc) {
            this.mEventMessage = mEventMessage;
            this.mLocations = theArr;
            this.mSuccess = theSucc;
        }
    }

    /**
     * Event used to send the agreement obtained through the getAgreement method.
     */
    public class AgreementEvent {
        /**Html formatted string containing our agreement string*/
       public final String theAgreement;

        /**
         * Public constructor
         * @param theString containing the agreement
         */
       public AgreementEvent(final String theString) {
           theAgreement = theString;
       }

    }
    /**
     * Event used to notify activities that the webservice completed.
     * Sends the message returned by the query who sends the event.
     */
    public class WebServiceEvent {
        /** Message to be displayed on the receiver's end.*/
        public final String message;
        /** Whether or not the query reached success*/
        public final boolean success;

        /**
         * Public constructor
         * @param message to send to subscribers
         * @param success whether or not the query was successful
         */
        public WebServiceEvent(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

    }

    /**
     * Private Asynctask used to process all queries to the webservice.
     * Mostly boilerplate code for retrieving a JSON string from the webservice.
     */
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!mCallingMethod.equals("logPoint") && !mCallingMethod.equals("getRange")) {
                mProgressDialog = ProgressDialog.show(mContext, "Wait", "Parsing Server...");
            }

        }

        @Override
        protected String doInBackground(String... urls) {

            String response = "";

            for (String url : urls) {
                Log.d("URLLOOP", url);
                DefaultHttpClient defClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                try {
                    HttpResponse execute = defClient.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;

                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                    Log.d("RAWRESPONSE", response);
                } catch (Exception e) {
                    Log.d("DOINBACK", "Failed to download web page.");
                    e.printStackTrace();
                }
            }
            Log.d("RESPONSERETURNED", response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!mCallingMethod.equals("logPoint") && !mCallingMethod.equals("getRange")) {
                mProgressDialog.dismiss();//push
            }

            if (result != null) {
                try {
                    mJSONObject = new JSONObject(result);
                    Log.d("JSONOBJECT", "JSON object created.");
                    //Where the magic happens
                    postExecuteHandler();
                } catch (Exception e) {
                    Log.e("JSON-E", "JSONEXCEPTION thrown");
                    e.printStackTrace();
                }
            }
        }
        /**
         * Tests for which method created a query and calls the post execute method accordingly.
         */
        private void postExecuteHandler() {
            switch(mCallingMethod) {
                case "addUser":
                    addUserPostExecute();
                    break;
                case "loginUser":
                    loginUserPostExecute();
                    break;
                case "resetPassword":
                    resetPasswordPostExecute();
                    break;
                case "getAgreement":
                    getAgreementPostExecute();
                    break;
                case "logPoint":
                    logPointPostExecute();
                    break;
                case "getRange":
                    getRangePostExecute();
                    break;
                default:
                    break;
            }
            mCallingMethod = null; //free string memory and avoid potential accidental function calls.
        }
    }
}