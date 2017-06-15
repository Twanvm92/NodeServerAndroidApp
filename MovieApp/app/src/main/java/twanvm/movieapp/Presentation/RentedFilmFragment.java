package twanvm.movieapp.Presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.Serializable;
import java.util.ArrayList;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.adapter.FilmAdapter;
import twanvm.movieapp.domain.Film;

import static android.view.View.VISIBLE;

public class RentedFilmFragment extends Fragment implements FilmAPIRequest.FilmAPIListener {

    public final String TAG = this.getClass().getSimpleName();
    private ListView listViewFilms;
    private ArrayAdapter filmAdapter;
    private ArrayList<Film> films = new ArrayList<>();
    private TextView notLoggedIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (tokenAvailable()) {
            Log.i(TAG, "Token found");
            int user = userAvailable();
            if (user != 0) {
                Log.i(TAG, "UserID found - getting films rented by user");
                getRentedFilms(user);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rented_film, container, false);
        listViewFilms = (ListView) view.findViewById(R.id.fragment_rented_film_ListView);
        filmAdapter = new FilmAdapter(getContext(), films);
        listViewFilms.setAdapter(filmAdapter);
        // detail scherm nog uitwerken
        listViewFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), FilmDetailActivity.class);
                Film film = films.get(position);
                i.putExtra("Film", (Serializable) film);
                startActivity(i);
            }
        });

        notLoggedIn = (TextView) view.findViewById(R.id.fragment_rented_film_notLoggedIn);
        if (userAvailable() == 0){
            notLoggedIn.setVisibility(VISIBLE);
        } else {
            notLoggedIn.setVisibility(View.GONE);
        }
        return view;
    }

    private boolean tokenAvailable() {
        boolean result = false;

        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("saved_token", "");
        if (token != null && !token.equals("")) {
            result = true;
        }
        return result;
    }

    private int userAvailable() {
        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int userID = sharedPref.getInt("saved_userID", 0);
        if (userID != 0) {
            return userID;
        }
        return 0;
    }

    @Override
    public void onFilmsAvailable(ArrayList<Film> filmArrayList) {

        Log.i(TAG, "We have " + filmArrayList.size() + " films in our list");

        films.clear();
        for(int i = 0; i < filmArrayList.size(); i++) {
            films.add(filmArrayList.get(i));
        }
        filmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilmAvailable(Film film) {
        films.add(film);
        filmAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleResponseError(VolleyError error) {
        notLoggedIn.setVisibility(VISIBLE);
        Log.e(TAG, error.toString());
    }

    private void getRentedFilms(int customerID){
        FilmAPIRequest request = new FilmAPIRequest(getContext(), this);
        request.handleGetRentedFilms(customerID);
    }
}
