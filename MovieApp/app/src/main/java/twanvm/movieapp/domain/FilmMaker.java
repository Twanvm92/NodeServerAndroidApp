package twanvm.movieapp.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maikel on 14-6-2017.
 */

public class FilmMaker {

    public static final String FILM_ID = "film_id";
    public static final String FILM_TITLE = "title";
    public static final String FILM_DESCRIPTION = "description";
    public static final String FILM_RELEASE_YEAR = "release_year";
    public static final String FILM_RENTAL_RATE = "rental_rate";
    public static final String FILM_LENGTH = "length";
    public static final String FILM_RATING = "rating";

    public static ArrayList<Film> makeFilmList(JSONArray response){

        ArrayList<Film> filmList = new ArrayList<>();

        try{
//            JSONArray jsonArray = response.getJSONArray(CITY_RESULT);

            for(int i = 0; i < response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);

                Film film = new Film(
                        jsonObject.getInt(FILM_ID),
                        jsonObject.getString(FILM_TITLE),
                        jsonObject.getString(FILM_DESCRIPTION),
                        jsonObject.getInt(FILM_RELEASE_YEAR),
                        jsonObject.getDouble(FILM_RENTAL_RATE),
                        jsonObject.getInt(FILM_LENGTH),
                        jsonObject.getString(FILM_RATING)
                );
                filmList.add(film);
            }
        } catch( JSONException ex) {
            Log.e("CityMapper", "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        return filmList;
    }
}
