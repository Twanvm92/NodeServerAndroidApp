package twanvm.movieapp.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import twanvm.movieapp.Constants;
import twanvm.movieapp.Presentation.LoginActivity;
import twanvm.movieapp.Presentation.Utilities;
import twanvm.movieapp.R;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.FilmMaker;

public class FilmAPIRequest {

    private Context context;
    public final String TAG = this.getClass().getSimpleName();

    // De aanroepende class implementeert deze interface.
    private FilmAPIRequest.FilmAPIListener filmListener;
    private FilmAPIRequest.LoginListener logListener;
    private FilmAPIRequest.RegisterListener regListener;

    public FilmAPIRequest(Context context, FilmAPIListener filmListener) {
        this.context = context;
        this.filmListener = filmListener;
    }

    public FilmAPIRequest(Context context, LoginListener logListener) {
        this.context = context;
        this.logListener = logListener;
    }

    public FilmAPIRequest(Context context, RegisterListener regListener) {
        this.context = context;
        this.regListener = regListener;
    }

    public void HandleLogin(String username, String password) {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Log.i(TAG, "handleLogin - body = " + body);

        try {
            JSONObject jsonBody = new JSONObject(body);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, Constants.URL_LOGIN, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Utilities.displayMessage(context, context.getString(R.string.login_succes));
                            try {
                                String token = response.getString("token");
                                int customer_id = response.getInt("customer_id");
                                SharedPreferences sharedPref = context.getSharedPreferences(
                                        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(context.getString(R.string.saved_token), token);
                                editor.putInt("saved_userID", customer_id);
                                editor.commit();

                                if(logListener != null) {
                                    logListener.isLoggedIn(true);
                                } else if (regListener != null) {
                                    regListener.isLoggedIn(true);
                                }
                            } catch (JSONException e) {
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

    public void handleGetFilms(int count, int offset) {

        Log.i(TAG, "handleGetFilms");
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.URL_FILMS + "?count=" + count + "&offset=" + offset,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Film> result = FilmMaker.makeFilmList(response);
                        filmListener.onFilmsAvailable(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        filmListener.handleResponseError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleyRequestQueue.getInstance(context).addToRequestQueue(jsArrayRequest);
    }

    public void handleGetRentedFilms(int customerID) {

        Log.i(TAG, "handleGetFilms");
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String token = sharedPref.getString("saved_token", "");
        if(token != null && !token.equals("")) {

            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    Constants.URL_RENTED_FILMS + customerID,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ArrayList<Film> result = FilmMaker.makeFilmList(response);
                            filmListener.onFilmsAvailable(result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            filmListener.handleResponseError(error);
                       }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Token", token);
                    return headers;
                }
            };

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(jsArrayRequest);
        }
    }

    public interface FilmAPIListener {
        // Callback function to return a fresh list of films
        void onFilmsAvailable(ArrayList<Film> films);

        // Callback function to handle a single added film.
        void onFilmAvailable(Film film);

        // Callback to handle serverside API errors
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
