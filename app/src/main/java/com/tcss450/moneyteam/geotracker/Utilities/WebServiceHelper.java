package com.tcss450.moneyteam.geotracker.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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

import de.greenrobot.event.EventBus;

/**
 * Static class used to communicate with the webserver
 * Created by Brandon on 4/29/2015.
 */
public class WebServiceHelper {

    private static final String BASE_URL = "http://450.atwebpages.com/";

    private static final String RESULT_TAG = "result";
    private static final String ERROR_TAG = "error";
    private static final String ID_TAG = "userid";

    private JSONArray mArray;
    protected JSONObject mJSONObject;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String mCallingMethod;
    private DownloadWebPageTask mDownloadTask;


    public WebServiceHelper(Context context) {
        mContext = context;
        mDownloadTask = new DownloadWebPageTask();
        mArray = null;
        mJSONObject = null;
        mCallingMethod = null;
    }

    //******************************************************
    //Query executors below here
    //******************************************************
    public void addUser(final String email, final String password,
                               final String question, final String answer) {

        //TODO check for and remove illegal characters using authenticator
        String query = BASE_URL + "adduser.php?" + "email=" + email + "&password=" + password
                + "&question=" + question.replace(" ", "%20").replace("?", "%3F")
                + "&answer=" + answer.replace(" ", "%20");

       // String result ="debug string";

        Log.d("WEBQUERY", query);

        mCallingMethod = "addUser";
        mDownloadTask.execute(new String[] {query});

        //problem is here, this code needs to wait for the task to complete before executing


    }

    public void loginUser(final String email, final String password) {

        mCallingMethod = "loginUser";

        String query = BASE_URL + "login.php?" + "email=" + email + "&password=" + password;
        mDownloadTask.execute(new String[] {query});

    }
    public void resetPassword(final String email) {
        mCallingMethod = "resetPassword";
        String query = BASE_URL + "reset.php?email=" + email;
        mDownloadTask.execute(new String[] {query});

    }

    public void getAgreement() {
        mCallingMethod = "getAgreement";
        String query = BASE_URL + "agreement.php";
        mDownloadTask.execute(new String[] {query});
    }

    public void logPoint(Cursor cursor) {

        Log.d("CURSOR RECEIVED", "" + cursor.getDouble(0)
        + "Timestamp: " + cursor.getInt(5) + " UserID: " + cursor.getString(4));


        //TODO open up the database, select all rows, iterate through and log every point
        mCallingMethod = "logPoint";
        String query = BASE_URL
                + "logAdd.php?lat=" + cursor.getDouble(0)
                + "&lon=" + cursor.getDouble(1)
                + "&speed=" + cursor.getDouble(2)
                + "&heading=" + cursor.getDouble(3)
                + "&source=" + "0cd11d57f8f6bb8368a36f5a7d12e19b2228dc62" //her example uid
                + "&timestamp=" + cursor.getInt(5);
        Log.d("LOGPOINTURL", query);
        mDownloadTask.execute(new String[] {query});
        Log.d("EXECUTELOG", "Point log executed, doesn't mean it worked");

    }

//comment test



    //**********************************************************
    //Post execute methods below here
    //**********************************************************

    /**
     * Method to be called after thread has finished creating the user.
     * In general, I will be splitting each task into a pre and post execute method.
     *
     */
    private void addUserPostExecute() {
        String result = "debug string";
        boolean success;

        if (mJSONObject != null) {
            try {
                // JSONObject object = mArray.getJSONObject(0);
                Log.d("RAWJSONSTRING", mJSONObject.toString());
                if (mJSONObject.getString(RESULT_TAG).equals("success")) {
                    Log.d("WEBSERVICE", "User successfully created");
                    success = true;
                    result = mJSONObject.getString("message");

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

            mArray = null;
        }
    }

    private void loginUserPostExecute() {
        String result = "debug string";
        boolean success = false;
        if (mJSONObject != null) {
            try {
                Log.d("RAWJSONSTRING", mJSONObject.toString());

                if (mJSONObject.getString(RESULT_TAG).equals("success")) {

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
                else if (mJSONObject.getString(RESULT_TAG).equals("fail")) {
                    result = mJSONObject.getString("error"); //TODO create constant
                }
            } catch (JSONException e) {
                Log.e("LOGINFAILED", "Login failed");
                e.printStackTrace();
            }

        }
            //Poptart.display(mContext, result, 2);
        EventBus.getDefault().postSticky(new WebServiceEvent(result, success));

        Log.d("LOGINEVENT", "Event posted.");
        //test change

    }

    private void resetPasswordPostExecute() {
        String result = "debug string";
        boolean success = false;


        if (mJSONObject != null) {
            try {
                Log.d("RAWJSONSTRING", mJSONObject.toString());

                String jsonResult = mJSONObject.getString(RESULT_TAG);

                if (jsonResult.equals("success")) {
                    result = mJSONObject.getString("message");
                    success  = true;
                } else {
                    result = mJSONObject.getString("error");
                }

                Log.d("RESETRESULT", "reset success");
                EventBus.getDefault().postSticky(new WebServiceEvent(result, success));

            } catch (JSONException e) {
                Log.e("ResetFailed", "Exception thrown from reset password web service");
                e.printStackTrace();
            }

            Log.d("RESETPOSTED", "Posted reset result.");
        }


    }
    private void getAgreementPostExecute() {
      //  boolean success = true;

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

    private void logPointPostExecute() {
        //nothing to display to the user directly. All return data is for debugging.
        if (mJSONObject != null) {
            try {
                String jsonResult = mJSONObject.getString(RESULT_TAG);

                if (jsonResult.equals("success")) {
                    Log.d("PLOGSUCCESS", "Logged point successfully");
                } else {
                    Log.e("PLOGFAIL", mJSONObject.getString("error"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public class AgreementEvent {
       public final String theAgreement;
       public AgreementEvent(final String theString) {
           theAgreement = theString;
       }

    }

    /**
     * Event used to notify activities that the webservice completed.
     */
    public class WebServiceEvent {
        /** Message to be displayed on the receiver's end.*/
        public final String message;
        /** Whether or not the query reached success*/
        public final boolean success;

        public WebServiceEvent(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

    }

    //not sure if making it static is kosher, we'll see.
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mCallingMethod.equals("logPoint")) {
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
                    String s = "";

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
            if (!mCallingMethod.equals("logPoint")) {
                mProgressDialog.dismiss();
            }
            String queryResult = result; //redundant, can probably remove

            if (queryResult != null) {
                try {
                   // mArray = new JSONArray(result);
                    mJSONObject = new JSONObject(result);
                    Log.d("JSONOBJECT", "JSON object created.");
                   // JSONObject object = arr.getJSONObject()


                    postExecuteHandler();
                } catch (Exception e) {
                    Log.e("JSON-E", "JSONEXCEPTION thrown");
                    e.printStackTrace();
                }
            }
            //super.onPostExecute(s);
        }

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
                default:

                    break;

            }
            mCallingMethod = null;

        }
    }
}