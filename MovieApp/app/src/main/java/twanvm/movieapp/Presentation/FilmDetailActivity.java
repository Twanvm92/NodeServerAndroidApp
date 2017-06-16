package twanvm.movieapp.Presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.adapter.FilmRentedAdapter;
import twanvm.movieapp.adapter.InventoryFilmAdapter;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.InventoryFilm;
import twanvm.movieapp.domain.RentedFilm;

public class FilmDetailActivity extends AppCompatActivity implements FilmAPIRequest.FilmAPIListener,
        InventoryFilmAdapter.InvenFilmAdapterListener {
    private ArrayAdapter invenFilmAdap;
    private ArrayList<Film> invenFilms = new ArrayList<>();
    private Film film;
    public final String TAG = this.getClass().getSimpleName();
    private TextView emptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        Bundle extras = getIntent().getExtras();
        film = null;
        if (extras != null) {
            film = (Film) extras.getSerializable("film");
            getMovieDetails(film.getId());
        }

        TextView movieTitle = (TextView) findViewById(R.id.activityFilmDetail_tv_movieTitle);
        TextView movieYearTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_releaseText);
        TextView movieLengthTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_lengthText);
        TextView movieRatingTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_ratingText);
        TextView movieDescrTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_descriptionText);

        String year = Integer.toString(film.getRelease_year());
        String length = Integer.toString(film.getLength());

        if(film != null) {
            movieTitle.setText(film.getTitle());
            movieYearTxt.setText(year);
            movieLengthTxt.setText(length);
            movieRatingTxt.setText(film.getRating());
            movieDescrTxt.setText(film.getDescription());
        }



        ListView invenFilmLV = (ListView) findViewById(R.id.activityFilmDetail_lv_inventory);
        emptyText = (TextView) findViewById(R.id.emptyText);
        invenFilmAdap = new InventoryFilmAdapter(this, invenFilms, this);
        invenFilmLV.setAdapter(invenFilmAdap);
        invenFilmLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), FilmDetailActivity.class);
//                RentedFilm rentedFilm = rentedFilms.get(position);
                startActivity(i);
            }
        });


    }

    public void getMovieDetails(int filmID) {
        FilmAPIRequest mDetailedRequest = new FilmAPIRequest(this, this);
        mDetailedRequest.handleGetInventoryFilms(filmID);
    }

    @Override
    public void handleLoginNeeded(boolean loginNeeded) {

    }

    @Override
    public void isFilmReturned(boolean filmReturned) {

    }

    @Override
    public void onFilmsAvailable(ArrayList<Film> films) {
        Log.i(TAG, "We have " + films.size() + " inventory films in our list");
        invenFilms.clear();

        for (Film f : films) {
            invenFilms.add(f);
        }
        Log.i(TAG, "We have " + invenFilms.size() + " inventory films in our actual list");

        invenFilmAdap.notifyDataSetChanged();

        if (invenFilmAdap == null) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRentedFilmsAvailable(ArrayList<RentedFilm> rentedFilms) {

    }

    @Override
    public void handleResponseError(VolleyError error) {

    }

    @Override
    public void isFilmRented(boolean filmRented) {
        if (filmRented) {
            getMovieDetails(film.getId());
        }
    }

    @Override
    public void isFilmRentedAdapter(boolean filmReturned) {
        if (filmReturned) {
            getMovieDetails(film.getId());
        }
    }

    @Override
    public void handleLoginNeededAdapter(boolean loginNeeded) {
        if (loginNeeded) {
            SharedPreferences sharedPref = getSharedPreferences(
                    getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("saved_token");
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Token expired, please login again", Toast.LENGTH_SHORT).show();
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
