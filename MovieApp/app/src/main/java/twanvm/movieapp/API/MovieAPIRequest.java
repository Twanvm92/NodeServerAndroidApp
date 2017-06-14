package twanvm.movieapp.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import twanvm.movieapp.Constants;
import twanvm.movieapp.Presentation.Utilities;
import twanvm.movieapp.R;

/**
 * Created by twanv on 13-6-2017.
 */

public class MovieAPIRequest {

    private Context context;
    public final String TAG = this.getClass().getSimpleName();

    // De aanroepende class implementeert deze interface.
    private MovieAPIRequest.MovieAPIListener listener;
    private MovieAPIRequest.LoginListener logListener;
    private MovieAPIRequest.RegisterListener regListener;

    public MovieAPIRequest(Context context, MovieAPIListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public MovieAPIRequest(Context context, LoginListener logListener) {
        this.context = context;
        this.logListener = logListener;
    }

    public MovieAPIRequest(Context context, RegisterListener regListener) {
        this.context = context;
        this.regListener = regListener;
    }

    public void HandleLogin(String username, String password) {
        //
        // Maak een JSON object met username en password. Dit object sturen we mee
        // als request body (zoals je ook met Postman hebt gedaan)
        //
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Log.i(TAG, "handleLogin - body = " + body);

        try {
            JSONObject jsonBody = new JSONObject(body);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, Constants.URL_LOGIN, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Succesvol response - dat betekent dat we een geldig token hebben.
                            // txtLoginErrorMsg.setText("Response: " + response.toString());
                            Utilities.displayMessage(context, context.getString(R.string.login_succes));

                            // We hebben nu het token. We kiezen er hier voor om
                            // het token in SharedPreferences op te slaan. Op die manier
                            // is het token tussen app-stop en -herstart beschikbaar -
                            // totdat het token expired.
                            try {
                                String token = response.getString("token");
                                SharedPreferences sharedPref = context.getSharedPreferences(
                                        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(context.getString(R.string.saved_token), token);
                                editor.commit();

                                if(logListener != null) {
                                    logListener.isLoggedIn(true);
                                } else if (regListener != null) {
                                    regListener.isLoggedIn(true);
                                }



                            } catch (JSONException e) {
                                // e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(logListener != null) {
                                logListener.handleResponseError(error);
                                logListener.isLoggedIn(false);
                            } else if (regListener != null) {
                                regListener.handleResponseError(error);
                                regListener.isLoggedIn(false);
                            }

                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1500, // SOCKET_TIMEOUT_MS,
                    2, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
             e.printStackTrace();
        }
        return;
    }

    public void HandleRegistration(final String username, final String password) {
        //
        // Maak een JSON object met username en password. Dit object sturen we mee
        // als request body (zoals je ook met Postman hebt gedaan)
        //
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Log.i(TAG, "handleLogin - body = " + body);

        try {
            JSONObject jsonBody = new JSONObject(body);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, Constants.URL_REGISTER, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Utilities.displayMessage(context,"Registration succeeded!");

                            regListener.isRegistered(true, username, password);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            regListener.handleResponseError(error);
                            regListener.isLoggedIn(false);
                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1500, // SOCKET_TIMEOUT_MS,
                    2, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public interface MovieAPIListener {

        void handleResponseError(VolleyError error);
    }

    public interface LoginListener {

        void isLoggedIn(boolean loggedIn);

        void handleResponseError(VolleyError error);
    }

    public interface RegisterListener {
        void isRegistered(boolean registered, String username, String password);

        void isLoggedIn(boolean loggedIn);

        void handleResponseError(VolleyError error);
    }

}
