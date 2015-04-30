package com.tcss450.moneyteam.geotracker.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tcss450.moneyteam.geotracker.activities.RegisterActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Static class used to communicate with the webserver
 * Created by Brandon on 4/29/2015.
 */
public class WebServiceHelper {

    private static final String BASE_URL = "http://450.atwebpages.com/";

    private static final String RESULT_TAG = "result";
    private static final String ERROR_TAG = "error";

    private JSONArray mArray;
    protected JSONObject mJSONObject;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String mCallingMethod;


    public WebServiceHelper(Context context) {
        mContext = context;
        mArray = null;
        mJSONObject = null;
        mCallingMethod = null;
    }


    public void addUser(final String email, final String password,
                               final String question, final String answer) {

        //TODO check for and remove illegal characters using authenticator
        String query = BASE_URL + "adduser.php?" + "email=" + email + "&password=" + password
                + "&question=" + question.replace(" ", "%20").replace("?", "%3F")
                + "&answer=" + answer.replace(" ", "%20");

        String result ="debug string";

        Log.d("WEBQUERY", query);

        DownloadWebPageTask task = new DownloadWebPageTask();

        mCallingMethod = "addUser";
        task.execute(new String[] {query});

        //problem is here, this code needs to wait for the task to complete before executing

        /*if (mJSONObject != null) {
            try {
               // JSONObject object = mArray.getJSONObject(0);
                Log.d("RAWJSONSTRING", mJSONObject.toString());
                if (mJSONObject.getString(RESULT_TAG).equals("success")) {
                    Log.d("WEBSERVICE", "User successfully created");
                    result = "User successfully created";


                } else {
                    Log.d("ERRORSTRING", mJSONObject.getString(ERROR_TAG));
                    result = mJSONObject.getString(ERROR_TAG);
                }

                //Poptart.display(context, result, Toast.LENGTH_SHORT);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            mArray = null;
        }

        return result;*/
    }

    /**
     * Method to be called after thread has finished creating the user.
     * In general, I will be splitting each task into a pre and post execute method.
     * @return
     */
    private void addUserPostExecute() {
        String result = "debug string";

        if (mJSONObject != null) {
            try {
                // JSONObject object = mArray.getJSONObject(0);
                Log.d("RAWJSONSTRING", mJSONObject.toString());
                if (mJSONObject.getString(RESULT_TAG).equals("success")) {
                    Log.d("WEBSERVICE", "User successfully created");
                    //result = "User successfully created";
                    result = mJSONObject.getString("message");

                } else {
                    Log.d("ERRORSTRING", mJSONObject.getString(ERROR_TAG));
                    result = mJSONObject.getString(ERROR_TAG);
                }

                Poptart.display(mContext, result, Toast.LENGTH_LONG);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            mArray = null;
        }
        //return result;
    }

    //not sure if making it static is kosher, we'll see.
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext, "Wait", "Parsing Server...");
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
            mProgressDialog.dismiss();
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
                default:
                    break;

            }
            mCallingMethod = null;

        }
    }
}