package twanvm.movieapp.Presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;

public class FilmDetailActivity extends AppCompatActivity {
    private TextView movieTitle;
    private TextView movieYearTitle;
    private TextView movieYearTxt;
    private TextView movieLengthTitle;
    private TextView movieLengthTxt;
    private TextView movieRatingTitle;
    private TextView movieRatingTxt;
    private TextView movieDescrTitle;
    private TextView movieDescrTxt;
    private TextView movieExemplTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);


    }

    public void getMovieDetails() {
//        FilmAPIRequest mDetailedRequest = new FilmAPIRequest()
    }
}
