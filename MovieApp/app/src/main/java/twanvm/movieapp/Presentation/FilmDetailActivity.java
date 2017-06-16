package twanvm.movieapp.Presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.adapter.InventoryFilmAdapter;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.InventoryFilm;

public class FilmDetailActivity extends AppCompatActivity implements FilmAPIRequest.FilmAPIListener {
    private ArrayAdapter invenFilmAdap;
    private ArrayList<Film> invenFilms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        Bundle extras = getIntent().getExtras();
        Film film = null;
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


        invenFilms = new ArrayList<>();
        ListView invenFilmLV = (ListView) findViewById(R.id.activityFilmDetail_lv_inventory);
        TextView emptyText = (TextView) findViewById(android.R.id.empty);
        invenFilmLV.setEmptyView(emptyText);
        invenFilmAdap = new InventoryFilmAdapter(this, invenFilms);
        invenFilmLV.setAdapter(invenFilmAdap);


    }

    public void getMovieDetails(int filmID) {
        FilmAPIRequest mDetailedRequest = new FilmAPIRequest(this, this);
        mDetailedRequest.handleGetInventoryFilms(filmID);
    }

    @Override
    public void onFilmsAvailable(ArrayList<Film> films) {
        for (Film f : films) {
            invenFilms.add(f);
        }

//        setDataInTV(films.get(0));


        invenFilmAdap.notifyDataSetChanged();
    }

    @Override
    public void onFilmAvailable(Film film) {
    }

    @Override
    public void handleResponseError(VolleyError error) {

    }

    public void setDataInTV(Film film) {
        TextView movieTitle = (TextView) findViewById(R.id.activityFilmDetail_tv_movieTitle);
        TextView movieYearTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_releaseText);
        TextView movieLengthTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_lengthText);
        TextView movieRatingTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_ratingText);
        TextView movieDescrTxt = (TextView) findViewById(R.id.activityFilmDetail_tv_descriptionText);

        String year = Integer.toString(film.getRelease_year());
        String length = Integer.toString(film.getLength());

        movieTitle.setText(film.getTitle());
        movieYearTxt.setText(year);
        movieLengthTxt.setText(length);
        movieRatingTxt.setText(film.getRating());
        movieDescrTxt.setText(film.getDescription());

    }
}
